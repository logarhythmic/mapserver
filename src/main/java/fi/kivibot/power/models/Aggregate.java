package fi.kivibot.power.models;

import fi.kivibot.power.misc.EU;
import fi.paivola.mapserver.core.DataFrame;

/**
 *
 * @author kivi
 */
public class Aggregate extends PowerPlant {

    public Aggregate(int id) {
        super(id);
        this.energy = 2.3;
        this.costs = 0.87 / energy;
    }

    @Override
    public void onTick(DataFrame last, DataFrame current) {
        Object fcost = last.getGlobalData("cost of fuel here");
        if (fcost == null) {
            super.onTick(last, current);
        } else {
            EU eu = new EU(168 * (Double) fcost);
            this.saveString("production", eu.toString());
            EU.saveEU(current, this, eu);
        }
    }

}
