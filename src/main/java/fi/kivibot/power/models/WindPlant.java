package fi.kivibot.power.models;

import fi.kivibot.power.misc.EU;
import fi.kivibot.power.models.base.PowerPlant;
import fi.paivola.mapserver.core.DataFrame;

/**
 *
 * @author kivi
 */
public class WindPlant extends PowerPlant {

    public WindPlant(int id) {
        super(id);
        this.energy = 100_000;
    }

    @Override
    public void onTick(DataFrame last, DataFrame current) {
        Object windstr = last.getGlobalData("wind");
        if (windstr == null) {
            EU eu = new EU(energy * 168.0 / 2.0);
            this.saveString("production", eu.toString());
            EU.saveEU(current, this, eu);
        } else {
            EU eu = new EU(wind2eu((Double) windstr));
            this.saveString("production", eu.toString());
            EU.saveEU(current, this, eu);
        }
    }
    
    public double wind2eu(double d){
        return energy*d;
    }
    
}
