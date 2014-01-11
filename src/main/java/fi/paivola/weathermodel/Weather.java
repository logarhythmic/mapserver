package fi.paivola.weathermodel;

import fi.paivola.mapserver.core.DataFrame;
import fi.paivola.mapserver.core.Event;
import fi.paivola.mapserver.core.GameManager;
import fi.paivola.mapserver.core.GlobalModel;
import fi.paivola.mapserver.core.setting.SettingMaster;
import static java.lang.Integer.parseInt;

/**
 * Weather class.
 * @author Jaakko Hannikainen
 */
public class Weather extends GlobalModel {

    public Weather(int id, SettingMaster sm) {
        super(id, sm);
    }
    
    public Weather() {
        super();
    }

    @Override
    public void onTick(DataFrame last, DataFrame current) {
        //current.saveGlobalData("asdness", ""+(parseInt(last.getGlobalData("asdness"))+ 1));
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

}
