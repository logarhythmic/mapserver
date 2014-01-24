package fi.kivibot.power.misc;

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
