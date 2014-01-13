package fi.paivola.population;

import fi.paivola.mapserver.core.Event;
import fi.paivola.mapserver.core.DataFrame;
import fi.paivola.mapserver.core.PointModel;
import fi.paivola.mapserver.core.GameManager;
import fi.paivola.population.FlowingDistribution;
import fi.paivola.mapserver.core.setting.SettingMaster;

/**
 *
 * @author hegza
 */
public class PopulationCenter extends PointModel {
    private FlowingDistribution populationByAge;
    
    public void PopulationCenter() {
        populationByAge = new FlowingDistribution( Constants.NUM_AGE_GROUPS );
    }

    @Override
    public void onTick(DataFrame last, DataFrame current) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onEvent(Event e, DataFrame current) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onRegisteration(GameManager gm, SettingMaster sm) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onGenerateDefaults(DataFrame df) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}