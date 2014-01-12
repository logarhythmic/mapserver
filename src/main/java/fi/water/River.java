
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
    float waterAmount;
    int width = 10;         // meters
    double length = 300;    // kilometers
    int depth = 4;          // meters
    int angle = 5;
    float waterSpeed;
    float runoff;
    int height = 0;
    
    public River (int id){
        super(id);
        this.maxConnections = 2;
        this.type = "River";
        this.passthrough = false;
    }
    
    @Override
    public void onTick(DataFrame last, DataFrame current) 
    {
       float C = (float)(1/0.035*Math.pow(waterAmount/width/1000/length, 1/6));
       waterSpeed = (float)(C*Math.sqrt(waterAmount/width/1000/length*angle));
       runoff = waterSpeed*width*depth;
       if(waterAmount - runoff < 0)
       {
           Event e = new Event("Runoff", Event.Type.DOUBLE, ""+waterAmount);
           this.addEventTo(this.connections.get(0), current, e);
           waterAmount = 0;
       }
       else{
           waterAmount -= runoff;
           Event e = new Event("Runoff", Event.Type.DOUBLE, ""+runoff);
           this.addEventTo(this.connections.get(0), current, e);
       }
       //System.out.println("River "+waterAmount+" Runoff "+runoff*1000000000);
    }

    @Override
    public void onEvent(Event e, DataFrame current) 
    {
        if(e.sender == this.connections.get(0) && e.name.contains("Overflow")){
            waterAmount += e.getDouble();
        }
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
    public void addEvent(Event e, Model m) 
    {
        super.addEvent(e, m);
    }
    
    @Override
    public void onUpdateSettings(SettingMaster sm){
        height = Integer.parseInt(sm.settings.get("height").getValue());
        this.saveInt("height",height);
        //System.out.print("height "+height);
    }
}
