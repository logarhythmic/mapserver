package fi.paivola.foodmodel;

import fi.paivola.mapserver.core.DataFrame;
import fi.paivola.mapserver.core.Event;
import fi.paivola.mapserver.core.setting.*;
import fi.paivola.mapserver.utils.Supplies;
import java.util.Map;

/**
 * @author Jaakko Hannikainen
 * Base class for edible things.
 */

public abstract class Edible {

    String name;
    private double area;
    public Edible(String name) { }
    
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
    
    /**
     * @return the area
     */
    public double getArea() {
        return area;
    }

    /**
     * @param area the area to set
     */
    public void setArea(double area) {
        this.area = area;
    }
    
    abstract void handleEvent(Event e, DataFrame current);
    abstract double onTick(DataFrame last, DataFrame current);
    abstract Supplies harvest(double max);

}
