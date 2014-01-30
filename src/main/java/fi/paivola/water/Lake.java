package fi.paivola.water;

/*
 * @author Esa Niemi
 */
//import au.com.bytecode.opencsv.CSVWriter;
import fi.paivola.mapserver.utils.Color;
import fi.paivola.mapserver.core.DataFrame;
import fi.paivola.mapserver.core.Event;
import fi.paivola.mapserver.core.GameManager;
import fi.paivola.mapserver.core.PointModel;
import fi.paivola.mapserver.core.setting.SettingInt;
import fi.paivola.mapserver.core.setting.SettingMaster;
import fi.paivola.mapserver.core.Model;
import fi.paivola.mapserver.core.setting.SettingDouble;
import fi.paivola.mapserver.utils.RangeDouble;
import fi.paivola.mapserver.utils.RangeInt;

import java.io.*;
import java.util.*;
import java.io.FileWriter;

public class Lake extends PointModel {

    // General stuff
    //CSVWriter writer = null;
    int order = 0;

    // PET variables
    double evapotranspiration;               // mm/day
    double airHumidity;                      // mb
    double temperature = 30;                 // 'C
    double k = 1;                            // Hamon coefficient
    double daytimeLength = 1;                // x/12h

    // Lake dimensions and water amount 
    double surfaceArea = 256120000.0;       // m^2
    double depth = 14.1f;                   // m       flood water level
    double waterAmount = 0;                 // m^3

    double startAmount = 0.9;               // percent of flood amount in the beginning
    double flowAmount = 0.91;               // percent of flood amount before starts to flow

    // Flows
    double C;                                // Chezy variable  

    // Rain
    double basinArea = 7642000000.0;           // m^2
    double rainfall = 0;                      // m^3
    double terrainCoefficient = 0.5f;         // unitless, how much of the rainfall ends up to the lake

    // Weather
    public Lake(int id) {
        super(id);
        this.type = "Lake";
    }

    @Override
    public void onTick(DataFrame last, DataFrame current) {
        // make some difference to the temperature
        temperature = Double.parseDouble(last.getGlobalString("temperature"));
        daytimeLength = Double.parseDouble(last.getGlobalString("sunlight"));

        // calculate the air humidity and the evapotranspiration for it
        airHumidity = (double) (6.108f * Math.exp((17.27f * temperature) / (temperature + 273.3)));
        evapotranspiration = k * 0.165f * 216.7f * daytimeLength * (airHumidity / (temperature + 273.3f)) * 7 / 1000000 * surfaceArea;

        // substract the evaporation from our current water reserves and check that we won't end up having negative amounts of water
        if (waterAmount - evapotranspiration < 0) {
            waterAmount = 0;
        } else {
            waterAmount -= evapotranspiration;
        }

        String[] entries;

        rainfall = Double.parseDouble(last.getGlobalString("rain")) / 1000;
        double actualRainfall = basinArea * rainfall * terrainCoefficient;
        waterAmount += actualRainfall;

        double flow = 0;

        //if the water level is over the boundaries of the lake, count the overflowing amount water
        if (waterAmount > depth * flowAmount * surfaceArea) {
            // the depth of the overflowing water
            double currentDepth = (waterAmount - surfaceArea * depth * flowAmount) / surfaceArea;
            // calculate the chezy variable used in the chezy flow speed equation
            C = (double) ((1 / 0.03f) * Math.pow(currentDepth, 1 / 6));
            // calculate the speed of the overflowing watermass
            double speed = (double) (C * Math.sqrt(0.05 * currentDepth));
            // calculate the amount of water that is going to leave
            flow = speed * (double) Math.sqrt(surfaceArea) * currentDepth;
            // substract the flow from the water in the lake if we have any rivers connected to this lake

            if (this.connections.size() > 0) {
                List<Model> rivers = new ArrayList<Model>();

                for (Model m : this.connections) {
                    if ((m.type == "River") && m.getInt("order") > this.order) {
                        rivers.add(m);
                    }
                }
                if (rivers.size() > 0) {
                    if (waterAmount - flow < 0) {
                        waterAmount = 0;
                    } else {
                        waterAmount -= flow;
                    }

                    for (Model m : rivers) {
                        Event e = new Event("Flow", Event.Type.STRING, "" + (flow / rivers.size()));
                        this.addEventTo(m, current, e);
                    }
                }
            }
        }

        // write the data to a .csv - file -- wateramount, actual rainfall and flow in km^3
        Event e;
        Boolean flood = false;
        if (waterAmount > surfaceArea * depth) {
            flood = true;
            e = new Event("Flood", Event.Type.OBJECT, flood);
            this.addEventToAll(current, e);
        }
        
         entries = (waterAmount/1000000000 + "#" + evapotranspiration/1000000 + "#" + temperature + "#" + actualRainfall/basinArea+ "#" + flow/1000000000+"#"+(flood?1:0)).split("#");
        
         /*for (int i = 0; i < entries.length; i++) {
         entries[i] = entries[i].trim();
         }

         if (writer != null) {
         writer.writeNext(entries);
         }*/
    }

    @Override
    public void onEvent(Event e, DataFrame current) {
        if (e.sender.type == "River" && e.name.equals("Flow") && e.sender.getInt("order") < this.order) {
            this.saveDouble("before", waterAmount);
            waterAmount += Double.parseDouble(e.getString());
            this.saveDouble("after", waterAmount);
        }
    }

    @Override
    public void onRegisteration(GameManager gm, SettingMaster sm) {
        sm.settings.put("order", new SettingInt("Position in the hydrodynamic chain", 0, new RangeInt(0, 100)));
        sm.settings.put("k", new SettingDouble("Hamon coefficient", 1.0, new RangeDouble(0, 10)));
        sm.settings.put("surfaceArea", new SettingDouble("sufraceArea", 256120000.0, new RangeDouble(0, Double.MAX_VALUE)));
        sm.settings.put("depth", new SettingDouble("Flood depth", 14.1, new RangeDouble(0, Double.MAX_VALUE)));
        sm.settings.put("startAmount", new SettingDouble("Start water depth", 0.9, new RangeDouble(0.0, 1.0)));
        sm.settings.put("flowAmount", new SettingDouble("Flowing starts water depth", 0.91, new RangeDouble(0, 1)));
        sm.settings.put("basinArea", new SettingDouble("Drainage basin area", 7642000000.0, new RangeDouble(0, Double.MAX_VALUE)));
        sm.settings.put("terrainCoefficient", new SettingDouble("Terrain flow coefficient", 0.5, new RangeDouble(0, Double.MAX_VALUE)));
        sm.color = new Color(0, 0, 255);
        sm.name = "Lake";
        sm.type = "Lake";
    }

    @Override
    public void onGenerateDefaults(DataFrame df) {
    }

    @Override
    public void onUpdateSettings(SettingMaster sm) {
            this.order = Integer.parseInt(sm.settings.get("order").getValue());
            this.k = Double.parseDouble(sm.settings.get("k").getValue());
            this.surfaceArea = Double.parseDouble(sm.settings.get("surfaceArea").getValue());
            this.depth = Double.parseDouble(sm.settings.get("depth").getValue());
            this.startAmount = Double.parseDouble(sm.settings.get("startAmount").getValue());
            this.flowAmount = Double.parseDouble(sm.settings.get("flowAmount").getValue());
            this.basinArea = Double.parseDouble(sm.settings.get("basinArea").getValue());
            this.terrainCoefficient = Double.parseDouble(sm.settings.get("terrainCoefficient").getValue());   
            this.saveInt("order", order);
            this.saveDouble("k",k);
            this.saveDouble("surfaceArea",surfaceArea);
            this.saveDouble("depth",k);
            this.saveDouble("startAmount",k);
            this.saveDouble("flowAmount",k);
            this.saveDouble("basinArea",k);
            this.saveDouble("terrainCoefficient",k);
            
            waterAmount = surfaceArea * startAmount*depth;
        
         /*if (writer == null) {
         try {
         writer = new CSVWriter(new FileWriter(this.id + ".csv"), ',');
         } catch (IOException e) {
         System.out.println(e.toString());
         }
         }*/
    }
}
