package fi.paivola.population;

import au.com.bytecode.opencsv.CSVReader;
import fi.paivola.mapserver.core.Event;
import fi.paivola.mapserver.core.DataFrame;
import fi.paivola.mapserver.core.ExtensionModel;
import fi.paivola.mapserver.core.GameManager;
import fi.paivola.mapserver.core.setting.SettingMaster;
import fi.paivola.mapserver.models.ExampleGlobal;
import java.io.IOException;
import java.io.InputStreamReader;
import static java.lang.Double.parseDouble;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hegza
 */
public class PopulationExtender extends ExtensionModel {
    private MortalityModel          mortalityModel;
    private PopulationDistribution  populationByAge;
    
    public PopulationExtender(int id) {
        super(id);
        this.mortalityModel = new MortalityModel();
        
        // Parse initial age-structure from file
        double[] ageGroups = new double[Constants.NUM_AGE_GROUPS];
        parseInitialAgeStructure(ageGroups, "populationByAge_2010.csv");
        this.populationByAge = new PopulationDistribution(ageGroups, mortalityModel);
        
        // 20% of people age 5 years annually 
        this.populationByAge.setAnnualFlowPc(0.2);
        this.populationByAge.setBirthsPc(0.047492154); // from births_population.ods
        // check that the data is conformant
        assert(this.populationByAge.getQuantities().length == Constants.NUM_AGE_GROUPS);
    }

    @Override
    public void onTick(DataFrame last, DataFrame current) {
        populationByAge.step( 1 );
        double[] quantities = populationByAge.getQuantities();
        for (int i = 0; i != quantities.length; ++i) {
            this.saveData("populationByAge" + i, quantities[i]);
        }
        /*
        System.out.println( "AGES_0TO4:\t" + (long)populationByAge.getQuantities()[0] );
        System.out.println( "AGES_5TO9:\t" + (long)populationByAge.getQuantities()[1] );
        System.out.println( "AGES_10TO14:\t" + (long)populationByAge.getQuantities()[2] );
        System.out.println( "Total:\t\t" + (long)populationByAge.total() );
        System.out.println( "Mode:\t"
                + "**\t" + populationByAge.mode() );
        */
    }

    @Override
    public void onEvent(Event e, DataFrame current) {
    }

    @Override
    public void onRegisteration(GameManager gm, SettingMaster sm) {
        sm.name = "populationExtender";
        sm.exts = "PopCenter";
    }

    @Override
    public void onGenerateDefaults(DataFrame df) {
        double[] quantities = populationByAge.getQuantities();
        for (int i = 0; i != quantities.length; ++i) {
            this.saveData("populationByAge" + i, quantities[i]);
        }
    }

    @Override
    public void onUpdateSettings(SettingMaster sm) {
    }
    
    private void parseInitialAgeStructure(double[] ageGroups, String filename) {
        try {
            CSVReader reader = new CSVReader(new InputStreamReader(PopulationExtender.class.getClassLoader().getResourceAsStream(filename)));
            String[] nextLine;
            int line = 0;
            while ((nextLine = reader.readNext()) != null) {
                if (line > 0) {
                    for (int i = 0; i != Constants.NUM_AGE_GROUPS; ++i) {
                        ageGroups[i] = parseDouble(nextLine[i]);
                    }
                }
                line++;
            }
        } catch (IOException ex) {
            Logger.getLogger(ExampleGlobal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
