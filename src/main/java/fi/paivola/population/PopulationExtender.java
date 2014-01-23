package fi.paivola.population;

import fi.paivola.mapserver.core.Event;
import fi.paivola.mapserver.core.DataFrame;
import fi.paivola.mapserver.core.ExtensionModel;
import fi.paivola.mapserver.core.GameManager;
import fi.paivola.mapserver.core.setting.SettingMaster;

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
        this.populationByAge = new PopulationDistribution( 
                ".\\src\\main\\resources\\populationByAge_2010.csv", 1, mortalityModel);
        // 20% of people age 5 years annually
        this.populationByAge.setAnnualFlowPc(0.2);
        this.populationByAge.setBirthsPc(0.047492154); // from births_population.ods
        // check that the data is conformant
        assert(this.populationByAge.getQuantities().length == Constants.NUM_AGE_GROUPS);
    }

    @Override
    public void onTick(DataFrame last, DataFrame current) {
        populationByAge.step( 1 );
        current.saveData(this, "populationByAge", populationByAge.getQuantities().toString());
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
        sm.exts = "examplePoint";
    }

    @Override
    public void onGenerateDefaults(DataFrame df) {
//        this.saveData("populationByAge", populationByAge.getQuantities());
    }

    @Override
    public void onUpdateSettings(SettingMaster sm) {
    }
}
