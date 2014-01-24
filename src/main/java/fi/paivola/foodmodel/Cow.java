package fi.paivola.foodmodel;

import fi.paivola.mapserver.core.DataFrame;
import fi.paivola.mapserver.core.Event;
import fi.paivola.mapserver.core.setting.*;
import java.util.Map;

/**
 * @author jgke
 */
public class Cow extends Edible {

    double cowAmount = 1;
    double foodAmount = 1;
    double storedMilk = 0;

    public Cow() {
        super("cow");
    }

    @Override
    double onTick(DataFrame last, DataFrame current) {
        double d = foodAmount;
        double cows = (int)cowAmount;
        if(foodAmount > 11.7 * cows) {
            foodAmount -= 11.7 * cows;
            storedMilk += (0.3375 * 11.7 + 8.325) * 7 * cows;
        }
        else {
            foodAmount = 0;
            storedMilk += (0.3375 * (d/cows) + 8.325) * 7 * cows;
        }
        foodAmount += 15 * getArea() * 7;
        if(cowAmount < 10 * getArea())
            cowAmount *= 1.05;

        return storedMilk;
    }
    
    @Override
    void handleEvent(Event e, DataFrame current) {

    }

    double harvest(double max) {
        double d = storedMilk;
        if(d < max) {
            storedMilk = 0;
            return d;
        }
        storedMilk -= max;
        return max;
    }
}
