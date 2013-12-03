package fi.paivola.foodmodel;

import fi.paivola.mapserver.core.*;
/**
 *
 * @version 0.1
 * @author Jaakko Hannikainen
 */
public class Field extends PointModel {

    private Edible content;

    /**
     * Main constructor for the food model
     * 
     * @param id ID of the field
     */
    public Field(int id) {
        super(id);
        this.settings.add(new Setting("area", "double", "1"));
        this.settings.add(new Setting("content", "string", "empty"));
    }

    @Override
    public void onTick(DataFrame last, DataFrame current) {
        this.saveDouble("foodAmount", this.content.onTick(last));
    }

    @Override
    public void onEvent(Event e) {
        switch(e.name) {
            default:
                break;
        }
    }

    @Override
    public void onRegisteration(GameManager gm) {

    }

    @Override
    public void onGenerateDefaults(DataFrame df) {
        content = new Empty(settings);
    }

}


