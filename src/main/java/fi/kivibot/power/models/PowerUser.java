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

/**
 *
 * @author kivi
 */
public class PowerUser extends PointModel {

    private double usage = 0.3;
    private boolean online = true;

    private double pogo = 0;
    
    public PowerUser(int id, SettingMaster sm) {
        super(id, sm);
    }

    public PowerUser() {
        super();
    }

    @Override
    public void onTick(DataFrame last, DataFrame current) {
        Event e = new Event("energy-req", "double", "" + (usage));
        for (Model m : this.connections) {
            if (m.type.equals("Power connection")) {
                this.addEventTo(m, last, e);
            }
        }
        System.out.println(">>"+pogo/usage);
        pogo = 0;
    }

    @Override
    public void onEvent(Event e, DataFrame current) {
        switch (e.name) {
            case "energy-get":
                pogo += e.getDouble();
                break;
        }
    }

    @Override
    public void onRegisteration(GameManager gm, SettingMaster sm) {
    }

    @Override
    public void onGenerateDefaults(DataFrame df) {
    }

}
