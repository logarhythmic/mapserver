/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.kivibot.power.models;

import fi.paivola.mapserver.core.Model;

/**
 *
 * @author kivi
 */
public class PowerSourceInfo {

    private Model pp;
    private double dist;

    public PowerSourceInfo(Model p, double d) {
        pp = p;
        dist = d;
    }

    public Model getPowerPlant() {
        return pp;
    }

    public double getDist() {
        return dist;
    }

}
