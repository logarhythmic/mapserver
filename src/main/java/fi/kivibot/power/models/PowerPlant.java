package fi.kivibot.power.models;

import fi.paivola.mapserver.core.DataFrame;
import fi.paivola.mapserver.core.Event;
import fi.paivola.mapserver.core.GameManager;
import fi.paivola.mapserver.core.PointModel;
import fi.paivola.mapserver.core.setting.SettingMaster;
import fi.paivola.mapserver.utils.Color;
import fi.paivola.mapserver.utils.Icon;

/**
 *
 * @author kivi
 */
public class PowerPlant extends PointModel {

    private double energy = 70;

    public PowerPlant(int id) {
        super(id);
        this.name = "Power plant";
    }

    @Override
    public void onTick(DataFrame last, DataFrame current) {
        EU eu = new EU(energy);
        this.saveString("production", eu.toString());
        EU.saveEU(current, this, eu);
    }

    @Override
    public void onEvent(Event e, DataFrame current) {

    }

    @Override
    public void onRegisteration(GameManager gm, SettingMaster sm) {
        sm.setIcon(Icon.CAPE);
        sm.color = new Color(0, 0, 0);
    }

    @Override
    public void onGenerateDefaults(DataFrame df) {
        EU eu = new EU(energy);
        EU.saveEU(df, this, eu);
    }

    @Override
    public void onUpdateSettings(SettingMaster sm) {
    }

}
