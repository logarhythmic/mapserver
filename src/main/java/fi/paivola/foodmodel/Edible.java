package fi.paivola.foodmodel;

import fi.paivola.mapserver.core.DataFrame;
import fi.paivola.mapserver.core.setting.*;
import java.util.Map;

/**
 * @author Jaakko Hannikainen
 * Base class for edible things.
 */

public abstract class Edible {

    SettingMaster sm;
    public Edible(SettingMaster sm) {
        this.sm = sm;
    }
    
    abstract double onTick(DataFrame last);
}
