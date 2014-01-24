package fi.paivola.water;

/*
 * @author Esa Niemi
 */
import au.com.bytecode.opencsv.CSVWriter;
import fi.paivola.mapserver.utils.Color;
import fi.paivola.mapserver.core.DataFrame;
import fi.paivola.mapserver.core.Event;
import fi.paivola.mapserver.core.GameManager;
import fi.paivola.mapserver.core.PointModel;
import fi.paivola.mapserver.core.setting.SettingInt;
import fi.paivola.mapserver.core.setting.SettingMaster;
import fi.paivola.mapserver.core.Model;
import fi.paivola.mapserver.utils.RangeInt;

import java.io.*;
import java.util.*;
import java.io.FileWriter;

public class Lake extends PointModel {
    //(Näsijärvi)

    // General stuff
    //CSVWriter writer = null;

    // PET variables
    float evapotranspiration;               // mm/day
    float airHumidity;                      // mb
    float temperature = 30;                 // 'C
    float k = 1;                            // Hamon coefficient
    float daytimeLength = 1;                // x/12h

    // Lake dimensions and water amount 
    float surfaceArea = 256120000f;         // m^2
    float depth = 14.1f;                    // m    TODO rename to fit its actual role as the depth of the lake "bowl"
    float floodDepth = 20;
    float waterAmount = 3480000000f;        // m^3

    // Flows
    float C;                                // Chezy variable

    // Rain
    float basinArea = 7642000000f;           // m^2
    float rainfall = 0;
    float terrainCoefficient = 0.5f;          // unitless, how much of the rainfall ends up to the lake

    // Weather
    public Lake(int id) {
        super(id);
        this.type = "Lake";
    }

    @Override
    public void onTick(DataFrame last, DataFrame current) {
        // make some difference to the temperature
        temperature = Float.parseFloat(last.getGlobalString("temperature"));
        daytimeLength = Float.parseFloat(last.getGlobalString("sunlight"));

        // calculate the air humidity and the evapotranspiration for it
        airHumidity = (float) (6.108f * Math.exp((17.27f * temperature) / (temperature + 273.3)));
        evapotranspiration = k * 0.165f * 216.7f * daytimeLength * (airHumidity / (temperature + 273.3f)) * 7 / 1000000 * surfaceArea;

        // substract the evaporation from our current water reserves and check that we won't end up having negative amounts of water
        if (waterAmount - evapotranspiration < 0) {
            waterAmount = 0;
        } else {
            waterAmount -= evapotranspiration;
        }

        String[] entries;

        rainfall = Float.parseFloat(last.getGlobalString("rain")) * 1000;
        float actualRainfall = basinArea * rainfall / 1000 * terrainCoefficient;
        waterAmount += actualRainfall;

        float flow = 0;

        //if the water level is over the boundaries of the lake, count the overflowing amount water
        if (waterAmount > depth * surfaceArea) {
            // the depth of the overflowing water
            float currentDepth = (waterAmount - surfaceArea * depth) / surfaceArea;
            // calculate the chezy variable used in the chezy flow speed equation
            C = (float) ((1 / 0.03f) * Math.pow(currentDepth, 1 / 6));
            // calculate the speed of the overflowing watermass
            float speed = (float) (C * Math.sqrt(180 * currentDepth));
            // calculate the amount of water that is going to leave
            flow = speed * (float) Math.sqrt(surfaceArea) * currentDepth;
            // substract the flow from the water in the lake if we have any rivers connected to this lake

            if (this.connections.size() > 0) {
                List<Model> rivers = new ArrayList<Model>();

                for (Model m : this.connections) {
                    if ((m.type == "River" || m.type == "Sea") && m.id > this.id) {
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

        // write the data to a .csv - file
        entries = (waterAmount / 1000000000 + "#" + evapotranspiration / 1000000000 + "#" + temperature + "#" + actualRainfall / 1000000000 + "#" + flow / 1000000000).split("#");
        /*
        for (int i = 0; i < entries.length; i++) {
            entries[i] = entries[i].trim();
        }

        if (writer != null) {
            writer.writeNext(entries);
        }*/
        Event e;
        if(waterAmount > surfaceArea*floodDepth)
        {
            Boolean flood = true;
            e = new Event("Flood",Event.Type.OBJECT, flood);
            this.addEventToAll(current, e);
        }
        this.saveData("waterAmount", waterAmount);
    }

    @Override
    public void onEvent(Event e, DataFrame current) {

    }

    @Override
    public void onRegisteration(GameManager gm, SettingMaster sm) {
        sm.color = new Color(0, 0, 255);
        sm.name = "Lake";
        sm.type = "Lake";
    }

    @Override
    public void onGenerateDefaults(DataFrame df) {

    }

    @Override
    public void onUpdateSettings(SettingMaster sm) {
       /* if (writer == null) {
            try {
                writer = new CSVWriter(new FileWriter(this.id + ".csv"), ',');
            } catch (IOException e) {
                System.out.println(e.toString());
            }
        }*/
    }
}
