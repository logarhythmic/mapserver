package fi.paivola.foodmodel;

import fi.paivola.mapserver.core.DataFrame;
import fi.paivola.mapserver.core.Setting;
import java.util.List;

/**
 * @version 0.1
 * @author Jaakko Hannikainen
 * Base class for edible things.
 */

public abstract class Edible {

    List<Setting> settings;
    public Edible(List<Setting> settings) {
        this.settings = settings;
    }
    
    abstract double onTick(DataFrame last);
}
