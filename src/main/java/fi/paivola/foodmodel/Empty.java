package fi.paivola.foodmodel;

import fi.paivola.mapserver.core.DataFrame;
import fi.paivola.mapserver.core.Event;
import fi.paivola.mapserver.core.setting.*;
import fi.paivola.mapserver.utils.Supplies;
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

    Supplies harvest(double max) {
        return new Supplies(0,0);
    }
}
