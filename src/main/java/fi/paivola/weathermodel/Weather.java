package fi.paivola.weathermodel;

import fi.paivola.mapserver.core.DataFrame;
import fi.paivola.mapserver.core.Event;
import fi.paivola.mapserver.core.GameManager;
import fi.paivola.mapserver.core.GlobalModel;
import fi.paivola.mapserver.core.setting.*;
import fi.paivola.mapserver.utils.RangeDouble;
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
    	current.saveGlobalData("wind", getWind(current.getDate()));
    }

    @Override
    public void onEvent(Event e, DataFrame current) {

    }

    @Override
    public void onRegisteration(GameManager gm, SettingMaster sm) {
        sm.name = "Weather";
        sm.settings.put("sade", new SettingDouble("sade", 0.0,
                    new RangeDouble(0, Double.MAX_VALUE)));
        sm.settings.put("dLämpö", new SettingDouble("dLämpö", 0.0,
                    new RangeDouble(Double.MIN_VALUE, Double.MAX_VALUE)));
    }

    @Override
    public void onGenerateDefaults(DataFrame df) {
    	df.saveGlobalData("rain", getRain(df.getDate()));
    	df.saveGlobalData("temperature", getTemperature(df.getDate()));
    	df.saveGlobalData("sunlight", getSunlight(df.getDate()));
    	df.saveGlobalData("wind", getWind(df.getDate()));
    }

    public void onUpdateSettings(SettingMaster sm) {
        Rain.rain = Double.parseDouble(sm.settings.get("sade").getValue());
        Temperature.dtemp = Double.parseDouble(sm.settings.get("dLämpö").getValue());
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

    public static double getWind(Calendar date) {
        return Wind.getWind(date);
    }
}
