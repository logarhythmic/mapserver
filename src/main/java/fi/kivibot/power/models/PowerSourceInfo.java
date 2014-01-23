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
    private double dist, loss;

    public PowerSourceInfo(Model p, double d, double l) {
        pp = p;
        dist = d;
        loss = l;
    }

    public Model getPowerPlant() {
        return pp;
    }

    public double getDist() {
        return dist;
    }

    public double getLoss() {
        return loss;
    }

}
