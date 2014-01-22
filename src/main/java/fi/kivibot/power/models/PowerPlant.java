/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.kivibot.power.models;

import fi.paivola.mapserver.core.DataFrame;
import fi.paivola.mapserver.core.Event;
import fi.paivola.mapserver.core.GameManager;
import fi.paivola.mapserver.core.Model;
import fi.paivola.mapserver.core.PointModel;
import fi.paivola.mapserver.core.setting.SettingMaster;
import fi.paivola.mapserver.utils.Color;
import fi.paivola.mapserver.utils.Icon;

/**
 *
 * @author kivi
 */
public class PowerPlant extends PointModel {

    private double energy = 0;

    public PowerPlant(int id) {
        super(id);
        this.name = "Power plant";
    }

    @Override
    public void onTick(DataFrame last, DataFrame current) {
        this.energy = 0.25;
    }

    @Override
    public void onEvent(Event e, DataFrame current) {
        switch (e.name) {
            case "energy-req":
                Object[] dat = (Object[]) e.value;
                Object[] amo = (Object[]) dat[0];
                double am = ((Double) amo[0]).doubleValue();
                double es = energy;
                if (energy >= am) {
                    es = am;
                }
                energy -= es;
                amo[0] = Double.valueOf(am - es);
                Event ev = new Event("energy-get", Event.Type.OBJECT, "" + es);
                this.addEventTo(e.sender, current, ev);
                break;
            default:
                break;
        }
    }

    @Override
    public void onRegisteration(GameManager gm, SettingMaster sm) {
        sm.setIcon(Icon.CAPE);
        sm.color = new Color(0, 0, 0);
    }

    @Override
    public void onGenerateDefaults(DataFrame df) {
    }

    @Override
    public void onUpdateSettings(SettingMaster sm) {
    }

}
