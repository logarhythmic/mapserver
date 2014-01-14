package fi.paivola.weathermodel;

import fi.paivola.mapserver.core.DataFrame;
import fi.paivola.mapserver.core.Event;
import fi.paivola.mapserver.core.GameManager;
import fi.paivola.mapserver.core.GlobalModel;
import fi.paivola.mapserver.core.setting.SettingMaster;

/**
 * Weather class.
 * @author Jaakko Hannikainen
 */
public class Weather extends GlobalModel {

    public Weather(int id) {
        super(id);
    }

    @Override
    public void onTick(DataFrame last, DataFrame current) {
    	current.saveGlobalData("rain", getRain(current.index));
    	current.saveGlobalData("temperature", getTemperature(current.index));
    }

    @Override
    public void onEvent(Event e, DataFrame current) {

    }

    @Override
    public void onRegisteration(GameManager gm, SettingMaster sm) {
        sm.name = "Weather";
    }

    @Override
    public void onGenerateDefaults(DataFrame df) {
    }

    public void onUpdateSettings(SettingMaster sm) {
    }

    public static double getRain(int week) {
        return Rain.getRain(week);
    }

    public static double getTemperature(int week) {
        return Temperature.getTemperature(week);
    }

    public static double getSunlight(int week) {
        return Sunlight.getSunlight(week);
    }
}
