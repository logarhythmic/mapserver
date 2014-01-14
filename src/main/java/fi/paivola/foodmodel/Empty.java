package fi.paivola.foodmodel;

import fi.paivola.mapserver.core.DataFrame;
import fi.paivola.mapserver.core.setting.*;
import java.util.Map;

/**
 * @author jgke
 */
public class Empty extends Edible {

    public Empty() {
        super("Empty");
    }

    @Override
    double onTick(DataFrame last) {
        return 0;
    }
    
}
