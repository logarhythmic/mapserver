package fi.water;

/*
 * @author Esa
 */

import fi.paivola.mapserver.utils.Color;
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
    private int radius = 500;           // meters
    private int depth = 10;             // meters
    private int k = 1;                  // unitless
    private float waterAmount = 0;     
    private float temp = 27;         
    private float time = 12;         
    private float PET;          
    private float es;        
    private int drainageRadius = 100;
    private float runoffCoeff = 0.5f;
    private float rainfall = 20; // Temp variable until we get the actual rain data
    
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
    }
    
    public Lake(){
        super();
    }
     @Override
    public void onTick(DataFrame last, DataFrame current) 
    {
        waterAmount = (float)(Math.PI*Math.pow(radius,2));
        System.out.print("Before rain: "+waterAmount);
        waterAmount += (rainfall * Math.PI * Math.pow(drainageRadius, 2))/1000;
        System.out.println("After rain: "+waterAmount);
        es = (float)(6.108*Math.exp(17.27*temp/(temp+237.3)));
        PET = (float)(k*0.165*216.7*time*(es/(temp+273.3)));
        waterAmount -= PET;
        System.out.println("PET: "+PET+" After evapotranspiration "+waterAmount);
    }

    @Override
    public void onEvent(Event e, DataFrame current) {
    switch(e.name){
        // Event handling here
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
