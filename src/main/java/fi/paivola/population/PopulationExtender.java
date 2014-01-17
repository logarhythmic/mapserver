package fi.paivola.population;

import fi.paivola.mapserver.core.Event;
import fi.paivola.mapserver.core.DataFrame;
import fi.paivola.mapserver.core.ExtensionModel;
import fi.paivola.mapserver.core.PointModel;
import fi.paivola.mapserver.core.GameManager;
import fi.paivola.mapserver.core.setting.SettingDouble;
import fi.paivola.population.FlowingDistribution;
import fi.paivola.mapserver.core.setting.SettingMaster;
import fi.paivola.mapserver.utils.Color;
import fi.paivola.mapserver.utils.Icon;
import fi.paivola.mapserver.utils.RangeDouble;

/**
 *
 * @author hegza
 */
public class PopulationExtender extends ExtensionModel {
    public PopulationExtender(int id) {
        super(id);
    }

    @Override
    public void onTick(DataFrame last, DataFrame current) {
    }

    @Override
    public void onEvent(Event e, DataFrame current) {
    }

    @Override
    public void onRegisteration(GameManager gm, SettingMaster sm) {
        sm.name = "populationExtender";
        sm.exts = "populationPoint"; // what are we extending?
    }

    @Override
    public void onGenerateDefaults(DataFrame df) {
    }

    @Override
    public void onUpdateSettings(SettingMaster sm) {
    }

    
}