package fi.water;

/*
 * @author Esa
 */

import fi.paivola.mapserver.utils.Color;
import fi.paivola.mapserver.core.Model;
import fi.paivola.mapserver.core.DataFrame;
import fi.paivola.mapserver.core.Event;
import fi.paivola.mapserver.core.GameManager;
import fi.paivola.mapserver.core.PointModel;
import fi.paivola.mapserver.core.setting.SettingInt;
import fi.paivola.mapserver.core.setting.SettingList;
import fi.paivola.mapserver.core.setting.SettingMaster;
import fi.paivola.mapserver.utils.Icon;
import fi.paivola.mapserver.utils.RangeInt;
import java.util.*;

public class Lake extends PointModel
{
    private int area = 6500;                // Squarekilometers
    private float depth = 0.03f;            // kilometers, everything over this is going to flow to the rivers. If there are none, the lake is going to overflow 
    private float k = 1.2f;                 // unitless
    private float waterAmount = 0;          // Cubic kilometers
    private float temp = 28;                // Celcius
    private float time = 12;         
    private float PET;                      // mm/d
    private float es;        
    private long drainageArea = 130000;
    private float rainfall = 0.02f;        // Temp variable until we get the actual rain data
    int height = 0;
    
    /*
    public Lake(int id, SettingMaster sm, int r, int dr, int d, int k, int water, int roc, int rain){
        super(id, sm);
        this.radius = r;
        this.depth = d;
        this.k = k;
        this.waterAmount = water;
        this.drainageRadius = dr;   
        this.runoffCoeff = roc;
        this.rainfall = rain;
    }
    */
    
    public Lake(int id){
        super(id);
        waterAmount = (float)(area*depth);
        es = (float)(6.108*Math.exp(17.27*temp/(temp+237.3)));
    }
    
     @Override
    public void onTick(DataFrame last, DataFrame current){
        waterAmount += drainageArea*(rainfall/1000);
        PET = (float)(k*0.165*216.7*time*(es/(temp+273.3)));
        
        if(waterAmount-waterAmount*PET*7/1000 < 0){
            waterAmount = 0;
            //System.out.print("Drought ");
        }
        else{
            waterAmount -= PET*7/1000;
        }
        
        if(waterAmount > (float)(area*depth)) {
            float of = (float)(0.035*Math.sqrt(waterAmount/area));
            //System.out.print("Overflow "+of+" ");
            List<Model> downstream = new ArrayList<Model>(connections);
            for(Model m : this.connections){
                if(m.type != "River" && m.getInt("height") > this.height)
                    downstream.remove(m);
            }
            Event e = new Event("Overflow", Event.Type.DOUBLE, ""+of/downstream.size());
            for(Model m : downstream){
                this.addEventTo(m, current, e);
                System.out.println("Overflow  "+of/downstream.size());
            }
            waterAmount -= of;
        }
    }

    @Override
    public void onEvent(Event e, DataFrame current) {
    switch(e.name){
        case "Runoff":
            System.out.println("Runoff "+e.getDouble());
            waterAmount += e.getDouble();
            break;
    }
    }

    @Override
    public void onRegisteration(GameManager gm, SettingMaster sm) {
        sm.color = new Color(0,0,255);
        sm.name = "Lake";
        sm.settings.put("height", new SettingInt("How high is this object?", 1, new RangeInt(1,5)));
    }

    @Override
    public void onGenerateDefaults(DataFrame df) {
    }
    
    @Override
    public void onUpdateSettings(SettingMaster sm){
        height = Integer.parseInt(sm.settings.get("height").getValue());
        this.saveInt("height", height);
    }
}
