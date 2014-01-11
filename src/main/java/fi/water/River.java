
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

/**
 *
 * @author Esa
 */
public class River extends ConnectionModel
{
    float waterAmount;
    int width = 10;
    double length = 3000;
    int depth = 4;
    int angle = 5;
    float waterSpeed;
    float runoff;
    
    public River (int id, SettingMaster sm){
        super(id, sm);
        this.passtrough = false;
        this.maxConnections = 2;
        this.type = "River";
        waterAmount = (float)(width*length*depth);
    }
    public River(){
        super();
        this.passtrough = false;
        this.maxConnections = 2;
        this.type = "River";
    }
    @Override
    public void onTick(DataFrame last, DataFrame current) 
    {
       waterSpeed = (float)(0.035*Math.sqrt(waterAmount/width/length*angle));
       runoff = waterSpeed*width*depth;
       if(waterAmount - runoff < 0)
       {
           Event e = new Event("Runoff", "double", ""+waterAmount);
           this.addEventTo(this.connections.get(1), current, e);
           waterAmount = 0;
       }
       else{
           waterAmount -= runoff;
           Event e = new Event("Runoff", "double", ""+runoff);
           this.addEventTo(this.connections.get(1), current, e);
       }
       System.out.println("River "+waterAmount+" Runoff "+runoff);
    }

    @Override
    public void onEvent(Event e, DataFrame current) 
    {
        if(e.sender == this.connections.get(0) && e.name == "Overflow"){
            waterAmount += e.getDouble();
        }
    }

    @Override
    public void onRegisteration(GameManager gm, SettingMaster sm) {
    }

    @Override
    public void onGenerateDefaults(DataFrame df){
        
    }
    
    public void addEventToAllExceptOne(Event e, Model m) {
        for (Model i : this.connections) {
            if (i != m) {
                i.addEvent(e, m);
            }
        }
    }

    @Override
    public void addEvent(Event e, Model m) 
    {
        if (this.passtrough) {
            this.addEventToAllExceptOne(e, m);
        } else {
            super.addEvent(e, m);
        }
    }
}
