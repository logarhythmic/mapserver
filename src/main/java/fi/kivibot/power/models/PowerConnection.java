package fi.kivibot.power.models;

import fi.paivola.mapserver.core.ConnectionModel;
import fi.paivola.mapserver.core.DataFrame;
import fi.paivola.mapserver.core.Event;
import fi.paivola.mapserver.core.GameManager;
import fi.paivola.mapserver.core.Model;
import fi.paivola.mapserver.core.setting.SettingMaster;

/**
 *
 * @author kivi
 */
public class PowerConnection extends ConnectionModel {

    public PowerConnection(int id) {
        super(id);
        this.passthrough = true;
        this.name = "Power connection";
        this.maxConnections = 2;
        
        this.saveDouble("losspkm", 0.000035);
    }

    @Override
    public void onTick(DataFrame last, DataFrame current) {
    }

    @Override
    public void onEvent(Event e, DataFrame current) {
    }

    @Override
    public void onRegisteration(GameManager gm, SettingMaster sm) {
    }

    @Override
    public void onGenerateDefaults(DataFrame df) {
    }

    @Override
    public void onUpdateSettings(SettingMaster sm) {

    }
}
