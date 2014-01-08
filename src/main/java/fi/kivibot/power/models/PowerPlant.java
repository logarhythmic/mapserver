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

    private double energy = 10;

    public PowerPlant(int id, SettingMaster sm) {
        super(id, sm);
    }

    public PowerPlant() {
        super();
    }

    @Override
    public void onTick(DataFrame last, DataFrame current) {
        this.addEventTo(this, current, new Event("energy-req", "double", "0"));
    }

    @Override
    public void onEvent(Event e) {
        System.out.println(e.name);
        switch (e.name) {
            case "energy-req":
                System.out.println("energy request from " + e.sender);
                double am = e.getDouble();
                double es = energy;
                if (energy >= am) {
                    es = am;
                }
                energy -= es;
                Event ev = new Event("energy-get", "double", "" + es);
                ev.frame = e.frame+1;
                e.sender.addEvent(e, this);
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
