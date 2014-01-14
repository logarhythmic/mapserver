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
    public Field(int id) {
        super(id);
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
        content = new Empty();
        content.setArea(1);
    }

    @Override
    public void onUpdateSettings(SettingMaster sm) {
        if(sm.settings.containsKey("content")) {
            switch(sm.settings.get("content").getValue().toLowerCase()) {
                case "empty":
                    content = new Empty();
                    break;
                case "wheat":
                    content = new Wheat();
                    break;
                case "maize":
                    content = new Maize();
                    break;
                case "sorghum":
                    content = new Sorghum();
                    break;
                default:
                    // Should this fail or not?
                    content = new Empty();
                    break;
            }
        }
        this.content.setArea(Double.parseDouble(sm.settings.get("area").getValue()));
    }
}


