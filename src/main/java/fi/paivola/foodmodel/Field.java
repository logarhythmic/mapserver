package fi.paivola.foodmodel;

import fi.paivola.mapserver.core.*;
import fi.paivola.mapserver.utils.*;
import fi.paivola.mapserver.core.setting.*;
/**
 * @author Jaakko Hannikainen
 */
public class Field extends PointModel {

    private Edible content;

    /**
     * Main constructor for the food model
     * 
     * @param id ID of the field
     * @param sm
     */
    public Field(int id, SettingMaster sm) {
        super(id, sm);
        this.sm = sm;
        this.sm.settings.put("area", new SettingDouble("area", 1.0,
                new RangeDouble(0, Integer.MAX_VALUE)));
        this.sm.settings.put("content", new SettingString("content", "empty"));
    }

    @Override
    public void onTick(DataFrame last, DataFrame current) {
        this.saveDouble("foodAmount", this.content.onTick(last));
    }

    @Override
    public void onEvent(Event e, DataFrame current) {
        switch(e.name) {
            default:
                break;
        }
    }

    @Override
    public void onRegisteration(GameManager gm, SettingMaster sm) {

    }

    @Override
    public void onGenerateDefaults(DataFrame df) {
        content = new Empty(sm.settings);
    }
}


