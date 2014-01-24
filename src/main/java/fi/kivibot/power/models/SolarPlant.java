package fi.kivibot.power.models;

import fi.kivibot.power.models.base.PowerPlant;
import fi.kivibot.power.misc.EU;
import fi.paivola.mapserver.core.DataFrame;

/**
 *
 * @author kivi
 */
public class SolarPlant extends PowerPlant {

    public SolarPlant(int id) {
        super(id);
        this.energy = 150_000;
        this.costs = 0.48;
    }

    @Override
    public void onTick(DataFrame last, DataFrame current) {
        Object sunstr = last.getGlobalData("sunlight");
        if (sunstr == null) {
            EU eu = new EU(energy * 168.0 / 2.0);
            this.saveString("production", eu.toString());
            EU.saveEU(current, this, eu);
        } else {
            EU eu = new EU(sun2eu((Double) sunstr));
            this.saveString("production", eu.toString());
            EU.saveEU(current, this, eu);
        }
    }

    /**
     *
     * @param convert sun light into power
     * @return
     */
    private double sun2eu(double s) {
        return energy * s;
    }

}
