package fi.paivola.foodmodel;

import fi.paivola.mapserver.core.*;
import fi.paivola.mapserver.utils.*;
import fi.paivola.mapserver.core.setting.*;
/**
 * @author Jaakko Hannikainen
 */
public class Field extends PointModel {

    private Edible content;
    private String contentString;
    private double area;

    /**
     * Main constructor for the food model
     */
    public Field(int id) {
        super(id);
    }
    
    @Override
    public void onTick(DataFrame last, DataFrame current) {
        double d = this.content.onTick(last, current);
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
        sm.settings.put("content", new SettingString("content", "empty"));
    }

    @Override
    public void onGenerateDefaults(DataFrame df) {
        setContents(contentString);
        content.setArea(this.area);
    }

    @Override
    public void onUpdateSettings(SettingMaster sm) {
        setContents(sm.settings.get("content").getValue().toLowerCase());
        this.area = Double.parseDouble(sm.settings.get("area").getValue());
        this.content.setArea(this.area);
    }

    public void setContents(String newContent) {
        this.contentString = newContent;
        switch(newContent) {
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
                throw new UnsupportedOperationException();
        }
    }
}


