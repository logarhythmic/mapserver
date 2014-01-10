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
    String name;
    public Edible(SettingMaster sm, String name) {
        this.sm = sm;
    }
    
    /**
     * @return the name
     */
    public final String getCropName() {
        return this.name;
    }

    /**
     * @param name the name to set
     */
    public final void setCropName(String name) {
        this.name = name;
    }
    
    abstract double onTick(DataFrame last);
}
