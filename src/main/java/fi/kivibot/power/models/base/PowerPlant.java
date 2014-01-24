package fi.kivibot.power.models.base;

import fi.kivibot.power.misc.EU;
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

    protected double energy = 0;
    protected double costs = 0;

    public PowerPlant(int id) {
        super(id);
        this.name = "Power plant";
    }

    @Override
    public void onTick(DataFrame last, DataFrame current) {
        EU eu = new EU(energy * 168);
        this.saveString("production", eu.toString());
        EU.saveEU(current, this, eu);
    }

    @Override
    public void onEvent(Event e, DataFrame current) {
    }

    protected double calcCost(double kwh) {
        return costs * kwh;
    }

    @Override
    public void onRegisteration(GameManager gm, SettingMaster sm) {
        sm.setIcon(Icon.CAPE);
        sm.color = new Color(0, 0, 0);
    }

    @Override
    public void onGenerateDefaults(DataFrame df) {
        EU eu = new EU(energy);
        this.saveString("production", eu.toString());
        EU.saveEU(df, this, eu);
    }

    @Override
    public void onUpdateSettings(SettingMaster sm) {
    }

}
