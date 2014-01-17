package fi.paivola.population;

import fi.paivola.mapserver.core.Event;
import fi.paivola.mapserver.core.DataFrame;
import fi.paivola.mapserver.core.PointModel;
import fi.paivola.mapserver.core.GameManager;
import fi.paivola.population.FlowingDistribution;
import fi.paivola.mapserver.core.setting.SettingMaster;
import fi.paivola.mapserver.utils.Color;
import fi.paivola.mapserver.utils.Icon;

/**
 *
 * @author hegza
 */
public class PopulationPoint extends PointModel {
    private FlowingDistribution populationByAge;
    
    public PopulationPoint(int id) {
        super(id);
        populationByAge = new FlowingDistribution( Constants.NUM_AGE_GROUPS );
        populationByAge.setAnnualFlowPc(0.2);
    }

    @Override
    public void onTick(DataFrame last, DataFrame current) {
        populationByAge.step( 1 );
        System.out.println( "AGES_0TO4: " + (int)populationByAge.getQuantities()[0] );
        System.out.println( "AGES_5TO9: " + (int)populationByAge.getQuantities()[1] );
        System.out.println( "AGES_10TO14: " + (int)populationByAge.getQuantities()[2] );
    }

    @Override
    public void onEvent(Event e, DataFrame current) {
    }

    @Override
    public void onRegisteration(GameManager gm, SettingMaster sm) {
        sm.setIcon(Icon.CAPE); // ToDo: Have some effect.
        sm.color = new Color(255, 204, 255); // What color is displayed in client.
        sm.allowedNames.add("exampleConnection"); // The things trying to get connected to this need satisfy atleast one of these tags.
        sm.name = "populationPoint";
    }

    @Override
    public void onGenerateDefaults(DataFrame df) {
//        this.saveData("populationByAge", populationByAge.getQuantities());
    }

    @Override
    public void onUpdateSettings(SettingMaster sm) {
   }
}
