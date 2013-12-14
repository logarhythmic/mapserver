package fi.paivola.foodmodel;

import fi.paivola.mapserver.core.DataFrame;
import fi.paivola.mapserver.core.setting.*;
import java.util.List;

/**
 *
 * @author jgke
 */
public class Empty extends Edible {

    public Empty(List<Setting> settings) {
        super(settings);
    }

    @Override
    double onTick(DataFrame last) {
        return 0;
    }
    
}
