package fi.paivola.water;

import au.com.bytecode.opencsv.CSVWriter;
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
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 *
 * @author Esa
 */
public class River extends ConnectionModel {

    // CSVWriter writer;
    // General (dimensions and stuff
    float waterAmount = 0;              //  m^3
    float width = 50;                   //  m
    float length = 50000;               //  m
    float floodDepth = 10;
    float slope = 90;
    
    public River(int id) {
        super(id);
        this.maxConnections = 2;
        this.type = "River";
        this.passthrough = false;
    }

    @Override
    public void onTick(DataFrame last, DataFrame current) {
        float flow = 0;
        if (waterAmount > 0) {
            float currentDepth = waterAmount/(width*length);
            float C = (float) (1 / 0.035f * Math.pow(currentDepth, 1 / 6));
            float flowSpeed = (float) (C * Math.sqrt(currentDepth * slope));
            flow = width * currentDepth * flowSpeed;
            List<Model> lakes = new ArrayList<Model>();
            for (Model m : this.connections) {
                if ((m.type.equals("Lake")||m.type.equals("Sea")) && m.id > this.id) {
                    lakes.add(m);
                }
            }
            for (Model m : lakes) {
                Event e = new Event("River flow", Event.Type.STRING, "" + flow / lakes.size());
                this.addEventTo(m, last, e);
            }
            if (waterAmount - flow < 0) {
                waterAmount = 0;
            } else {
                waterAmount -= flow;
            }
            Event e;
            if(waterAmount > width*length*floodDepth)
            {
                Boolean flood = true;
                e = new Event("Flood",Event.Type.OBJECT, flood);
                this.addEventToAll(current, e);
            }
        }
        /*
        String[] entries = (waterAmount/1000000000 + "#" + flow/1000000000).split("#");
        for (int i = 0; i < entries.length; i++) {
            entries[i] = entries[i].trim();
        }
        if (writer != null) {
            writer.writeNext(entries);
        }*/
        this.saveData("waterAmount",waterAmount);
        //this.saveData("flooding",flood);
    }

    @Override
    public void onEvent(Event e, DataFrame current) {
        if (e.name.equals("Flow") && e.sender.id < this.id) {
            waterAmount += Float.parseFloat(e.getString());
        }
    }

    @Override
    public void onRegisteration(GameManager gm, SettingMaster sm) {
        sm.color = new Color(0, 0, 255);
        sm.name = "River";
    }

    @Override
    public void onGenerateDefaults(DataFrame df) {

    }

    @Override
    public void onUpdateSettings(SettingMaster sm) {
        /*if (writer == null) {
            try {
                writer = new CSVWriter(new FileWriter(this.id + ".csv"), ',');
            } catch (IOException e) {
                System.out.println(e.toString());
            }
        }*/
    }
}
