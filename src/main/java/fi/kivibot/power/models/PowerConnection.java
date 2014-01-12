/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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

    public PowerConnection(int id, SettingMaster sm) {
        super(id, sm);
        this.passthrough = true;
        this.name = "Power connection";
        this.maxConnections = 2;
    }

    public PowerConnection() {
        super();
        this.passthrough = true;
        this.name = "Power connection";
        this.maxConnections = 2;
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

    /**
     * EVIL CODE - DO NOT COPY
     *
     * @param e
     * @param m
     */
    @Override
    public void addEvent(Event e, Model m) {

        switch (e.name) {
            case "energy-req":
                Object[] os = (Object[]) e.value;
                Object[] val = (Object[]) os[0];
                Double l = (Double) os[1];
                double len = l.doubleValue() + this.connections.get(0).distanceTo(this.connections.get(1));
                l = Double.valueOf(len);
                Object out = new Object[]{val, l};
                Event oe = e;
                e = new Event(oe.name, oe.type, oe.value);
                e.frame = oe.frame;
                e.sender = oe.sender;
                e.value = out;
                break;
        }

        super.addEvent(e, m);
    }
}
