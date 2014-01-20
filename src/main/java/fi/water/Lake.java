package fi.water;

/*
 * @author Esa Niemi
 */

import au.com.bytecode.opencsv.CSVWriter;
import fi.paivola.mapserver.utils.Color;
import fi.paivola.mapserver.core.Model;
import fi.paivola.mapserver.core.DataFrame;
import fi.paivola.mapserver.core.Event;
import fi.paivola.mapserver.core.GameManager;
import fi.paivola.mapserver.core.PointModel;
import fi.paivola.mapserver.core.setting.SettingInt;
import fi.paivola.mapserver.core.setting.SettingString;
import fi.paivola.mapserver.core.setting.SettingMaster;
import fi.paivola.mapserver.utils.Icon;
import fi.paivola.mapserver.utils.RangeInt;
import java.util.*;
import java.io.*;
import java.io.FileWriter;

public class Lake extends PointModel
{
    // General stuff
    int height = 0;             // used to check the direction of river runoff
    CSVWriter writer;
    
    //PET variables
    float evapotranspiration;               // mm/day
    float airHumidity;                      // mb
    float temperature = 30;                 // 'C
    float k = 1;                            // Hamon coefficient
    float daytimeLength = 1;                // x/12h
    
    //Lake dimensions and water amount (Näsijärvi)
    float surfaceArea = 256.12f;            // km^2
    float depth = 14.1f;                    // m
    float waterAmount = 3.48f;              // km^3
    
    public Lake(int id)
    {
        super(id);
    }
    
     @Override
    public void onTick(DataFrame last, DataFrame current)
    {
        // make some difference to the temperature
        
        temperature = new Random().nextInt(35-28)+28;
        
        // calculate the air humidity and the evapotranspiration for it
        airHumidity = (float)(6.108f*Math.exp((17.27f*temperature)/(temperature+273.3)));
        evapotranspiration =  k*0.165f*216.7f*daytimeLength*(airHumidity/(temperature+273.3f))*7/1000000*surfaceArea;
        
        // substract the evaporation from our current water reserves and check that we won't end up having negative amounts of water
        if(waterAmount - evapotranspiration< 0)
        {
            waterAmount = 0;
        }
        else
        {
            waterAmount -= evapotranspiration;
        }
        
        // write the data to a .csv - file
        String[] entries = (waterAmount+"#"+evapotranspiration+"#"+temperature).split("#");
        
        for(int i = 0; i < entries.length; i++)
        {
            entries[i] = entries[i].trim();
        }
        
        if(writer != null)
        {
            writer.writeNext(entries);
        }
    }

    @Override
    public void onEvent(Event e, DataFrame current) 
    {
        
    }

    @Override
    public void onRegisteration(GameManager gm, SettingMaster sm) 
    {
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
        
        if(writer == null)
        {
            try 
            {
                writer = new CSVWriter(new FileWriter(this.name+".csv"), ',');
            } 
            catch (IOException e) 
            {
                System.out.println("Error with the writer");
            }
        }
    }
}
