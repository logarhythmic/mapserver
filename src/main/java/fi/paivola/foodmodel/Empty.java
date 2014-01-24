package fi.paivola.foodmodel;

import fi.paivola.mapserver.core.DataFrame;
import fi.paivola.mapserver.core.Event;
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
    double onTick(DataFrame last, DataFrame current) {
        return 0;
    }
    
    @Override
    void handleEvent(Event e, DataFrame current) {

    }

    double harvest(double max) {
        return 0;
    }
}
