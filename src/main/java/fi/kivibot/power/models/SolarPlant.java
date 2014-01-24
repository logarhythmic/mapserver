package fi.kivibot.power.models;

import fi.paivola.mapserver.core.DataFrame;

/**
 *
 * @author kivi
 */
public class SolarPlant extends PowerPlant {

    public SolarPlant(int id) {
        super(id);
    }

    @Override
    public void onTick(DataFrame last, DataFrame current) {
        Object sunstr = last.getGlobalData("sunlight");
        if (sunstr == null) {
            super.onTick(last, current);
        } else {
            EU eu = new EU((Double) sunstr);
            this.saveString("production", eu.toString());
            EU.saveEU(current, this, eu);
        }
    }

}
