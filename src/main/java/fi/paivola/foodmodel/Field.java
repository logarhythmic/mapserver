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
     */
    public Field() {
        super();
    }

    public Field(int id, SettingMaster sm) {
        super(id, sm);
        if(sm.settings.containsKey("content")) {
            switch(sm.settings.get("content").getValue()) {
                case "empty":
                    content = new Empty(sm);
                    break;
                case "wheat":
                    content = new Wheat(sm);
                    break;
                default:
                    content = new Empty(sm);
                    break;
            }
        }
    }
    
    @Override
    public void onTick(DataFrame last, DataFrame current) {
        double d = this.content.onTick(last);
        this.saveDouble("foodAmount", d);
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
        sm.settings.put("area", new SettingDouble("area", 1.0,
                new RangeDouble(0, Integer.MAX_VALUE)));
        if(!sm.settings.containsKey("content"))
            sm.settings.put("content", new SettingString("content", "empty"));
    }

    @Override
    public void onGenerateDefaults(DataFrame df) {
        if(content == null)
            content = new Empty(sm);
    }
}


