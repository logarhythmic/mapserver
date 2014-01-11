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

public class Lake extends PointModel
{
    private int area = 6500;               // meters
    private float depth = 0.03f;             // meters, everything over this is going to flow to the rivers. If there are none, the lake is going to overflow 
    private float k = 1.2f;                  // unitless
    private float waterAmount = 0;     
    private float temp = 28;         
    private float time = 12;         
    private float PET;                  // mm/d
    private float es;        
    private long drainageArea = 130000;
    private float rainfall = 0.0002f;        // Temp variable until we get the actual rain data
    
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
    
    public Lake(int id, SettingMaster sm){
        super(id, sm);
        waterAmount = (float)(area*depth);
        es = (float)(6.108*Math.exp(17.27*temp/(temp+237.3)));
    }
    
    public Lake(){
        super();
    }
     @Override
    public void onTick(DataFrame last, DataFrame current) 
    {
        waterAmount += drainageArea*(rainfall/1000);
        PET = (float)(k*0.165*216.7*time*(es/(temp+273.3)));
        waterAmount -= PET;
        
        if(waterAmount > (float)(area*depth)) {
            float of = (float)(0.035*Math.sqrt(waterAmount/area));
            waterAmount -= of;
            System.out.print("Overflow "+of);
            Event e = new Event("Overflow", "double", ""+of);
            for(Model m : this.connections){
                this.addEventTo(m, current, e);
            }
        }
        System.out.println(" Lake "+waterAmount);
    }

    @Override
    public void onEvent(Event e, DataFrame current) {
    switch(e.name){
        case "Runoff":
            waterAmount += e.getDouble();
            break;
    }
    }

    @Override
    public void onRegisteration(GameManager gm, SettingMaster sm) {
        sm.color = new Color(0,0,255);
        sm.name = "Lake";
    }

    @Override
    public void onGenerateDefaults(DataFrame df) {
    }
    
    
}
