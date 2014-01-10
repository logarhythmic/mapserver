package fi.paivola.foodmodel;

import fi.paivola.mapserver.core.setting.*;
import java.util.Map;

/**
 * @author Jaakko Hannikainen, Cliona Shakespeare
 * Class for maize.
 */

public class Maize extends Crop {
    public Maize(SettingMaster sm) {
        super(sm, "Maize", 1.01, 2.07, 3.01,
                            20, 30, 40,
                            42, 45, 70,
                            5.5, 6.25, 7,
                            18);
    }
}
