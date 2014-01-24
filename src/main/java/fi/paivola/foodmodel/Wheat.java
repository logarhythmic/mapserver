package fi.paivola.foodmodel;

import fi.paivola.mapserver.core.setting.*;
import java.util.Map;

/**
 * @author Cliona Shakespeare, Jaakko Hannikainen
 * Class for maize.
 */

public class Wheat extends Crop {
    public Wheat() {
        super("Wheat", 1.09, 2.17, 3.23,
                           19, 22, 25,
                           42, 45, 70,
                           6, 6.5, 7,
                           15, 8);
    }
}
