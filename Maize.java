package fi.paivola.foodmodel;

import fi.paivola.mapserver.core.Setting;
import java.util.List;

/**
 * @version 0.1
 * @author Jaakko Hannikainen
 * Class for maize.
 */

public class Maize extends Crop {
    public Maize(List<Setting> settings) {
        super(settings);
        this.setWaterMinimum(1.01);
        this.setWaterOptimal(2.07);
        this.setWaterMaximum(3.01);
        this.setTemperatureMinimum(20);
        this.setTemperatureOptimal(30);
        this.setTemperatureMaximum(40);
        this.setSunlightMinimum(42);
        this.setSunlightOptimal(45);
        this.setSunlightMaximum(70);
   }
}
