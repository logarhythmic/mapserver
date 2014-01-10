package fi.paivola.foodmodel;

import fi.paivola.mapserver.core.setting.*;
import java.util.Map;

/**
 * @author Cliona Shakespeare, Jaakko Hannikainen
 * Class for sorghum.
 */

public class Sorghum extends Crop {
    public Sorghum(SettingMaster sm) {
        super(sm);
        this.setWaterMinimum(1.33); // water is for sorghum, all else isn't
        this.setWaterOptimal(2.66);
        this.setWaterMaximum(3.99);
        this.setTemperatureMinimum(19);
        this.setTemperatureOptimal(22);
        this.setTemperatureMaximum(25);
        this.setSunlightMinimum(42);
        this.setSunlightOptimal(45);
        this.setSunlightMaximum(70);
        this.setPHMinimum(6);
        this.setPHOptimal(6.5);
        this.setPHMaximum(7);
    }
}
