
package fi.water;

import fi.paivola.mapserver.utils.Color;
import fi.paivola.mapserver.core.DataFrame;
import fi.paivola.mapserver.core.Event;
import fi.paivola.mapserver.core.GameManager;
import fi.paivola.mapserver.core.ConnectionModel;
import fi.paivola.mapserver.core.Model;
import fi.paivola.mapserver.core.setting.SettingInt;
import fi.paivola.mapserver.core.setting.SettingList;
import fi.paivola.mapserver.core.setting.SettingMaster;
import fi.paivola.mapserver.utils.Icon;
import fi.paivola.mapserver.utils.RangeInt;

/**
 *
 * @author Esa
 */
public class River extends ConnectionModel
{
    int height = 0;
    
    public River (int id){
        super(id);
        this.maxConnections = 2;
        this.type = "River";
        this.passthrough = false;
    }
    
    @Override
    public void onTick(DataFrame last, DataFrame current)   {
    }

    @Override
    public void onEvent(Event e, DataFrame current) {
    }

    @Override
    public void onRegisteration(GameManager gm, SettingMaster sm) {
        sm.color = new Color(0,0,255);
        sm.name = "River";
        sm.settings.put("height", new SettingInt("How high is this object?", 1, new RangeInt(1,5)));
    }

    @Override
    public void onGenerateDefaults(DataFrame df){
        
    }
    
    @Override
    public void onUpdateSettings(SettingMaster sm){
        height = Integer.parseInt(sm.settings.get("height").getValue());
        this.saveInt("height",height);
    }
}
