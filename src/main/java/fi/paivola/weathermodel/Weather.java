package fi.paivola.weathermodel;

import fi.paivola.mapserver.core.DataFrame;
import fi.paivola.mapserver.core.Event;
import fi.paivola.mapserver.core.GameManager;
import fi.paivola.mapserver.core.GlobalModel;
import fi.paivola.mapserver.core.setting.SettingMaster;
import java.util.Calendar;

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
    	current.saveGlobalData("rain", getRain(current.getDate()));
    	current.saveGlobalData("temperature", getTemperature(current.getDate()));
    	current.saveGlobalData("sunlight", getSunlight(current.getDate()));
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
    	df.saveGlobalData("rain", getRain(df.getDate()));
    	df.saveGlobalData("temperature", getTemperature(df.getDate()));
    	df.saveGlobalData("sunlight", getSunlight(df.getDate()));
    }

    public void onUpdateSettings(SettingMaster sm) {
    }

    public static double getRain(Calendar date) {
        return Rain.getRain(date);
    }

    public static double getTemperature(Calendar date) {
        return Temperature.getTemperature(date);
    }

    public static double getSunlight(Calendar date) {
        return Sunlight.getSunlight(date);
    }
}