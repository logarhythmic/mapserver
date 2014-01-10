/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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

    private double energy = 0;

    public PowerPlant(int id, SettingMaster sm) {
        super(id, sm);
    }

    public PowerPlant() {
        super();
    }

    @Override
    public void onTick(DataFrame last, DataFrame current) {
        this.energy = 0.16;
    }

    @Override
    public void onEvent(Event e, DataFrame current) {
        switch (e.name) {
            case "energy-req":
                double am = e.getDouble();
                double es = energy;
                if (energy >= am) {
                    es = am;
                }
                energy -= es;
                e.value = ""+(am-es);
                Event ev = new Event("energy-get", "double", "" + es);
                this.addEventTo(e.sender, current, ev);
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

}
