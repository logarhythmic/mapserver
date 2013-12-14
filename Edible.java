package fi.paivola.foodmodel;

import fi.paivola.mapserver.core.DataFrame;
import fi.paivola.mapserver.core.setting.*;
import java.util.Map;

/**
 * @version 0.1
 * @author Jaakko Hannikainen
 * Base class for edible things.
 */

public abstract class Edible {

    Map<String, Setting> settings;
    public Edible(Map<String, Setting> settings) {
        this.settings = settings;
    }
    
    abstract double onTick(DataFrame last);
}
