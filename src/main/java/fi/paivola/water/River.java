package fi.paivola.water;

import au.com.bytecode.opencsv.CSVWriter;
import fi.paivola.mapserver.utils.Color;
import fi.paivola.mapserver.core.DataFrame;
import fi.paivola.mapserver.core.Event;
import fi.paivola.mapserver.core.GameManager;
import fi.paivola.mapserver.core.ConnectionModel;
import fi.paivola.mapserver.core.Model;
import fi.paivola.mapserver.core.setting.SettingDouble;
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

    CSVWriter writer;
    // General (dimensions and stuff
    double waterAmount = 0;              //  m^3
    double width = 100;                   //  m
    double length = 1000000;             //  m
    double floodDepth = 10;
    double flowDepth = 0.5;
    double slope = 45;
    int order = 0;
    
    public River(int id) {
        super(id);
        this.maxConnections = 2;
        this.type = "River";
        this.passthrough = false;
    }

    @Override
    public void onTick(DataFrame last, DataFrame current) {
        double flow = 0;
        Boolean flood = false;
        if (waterAmount > width*length*flowDepth) {
            double currentDepth = waterAmount/(width*length);
            double C = (double) (1 / 0.035f * Math.pow(currentDepth, 1 / 6));
            double flowSpeed = (double) (C * Math.sqrt(currentDepth * slope));
            flow = width * currentDepth * flowSpeed;
            List<Model> lakes = new ArrayList<Model>();
            for (Model m : this.connections) {
                if ((m.type.equals("Lake")||m.type.equals("Sea")) && m.getInt("order") > this.order) {
                    lakes.add(m);
                }
            }
            for (Model m : lakes) {
                Event e = new Event("Flow", Event.Type.STRING, "" + flow);
                this.addEventTo(m, current, e);
            }
            if (waterAmount - flow < 0) {
                waterAmount = 0;
            } else {
                waterAmount -= flow;
            }
            Event e;
            if(waterAmount > width*length*floodDepth)
            {
                flood = true;
                e = new Event("Flood",Event.Type.OBJECT, flood);
                this.addEventToAll(current, e);
            }
        }
        String[] entries = (waterAmount/1000000000 + "#" + flow/1000000+"#"+(flood?1:0)).split("#");
        for (int i = 0; i < entries.length; i++) {
            entries[i] = entries[i].trim();
        }
        if (writer != null) {
            writer.writeNext(entries);
        }
    }

    @Override
    public void onEvent(Event e, DataFrame current) {
        if (e.name.equals("Flow") && e.sender.getInt("order") < this.order) {
            waterAmount += Float.parseFloat(e.getString());
        }
    }

    @Override
    public void onRegisteration(GameManager gm, SettingMaster sm) {
        sm.settings.put("order", new SettingInt("Position in the hydrodynamic chain", 0, new RangeInt(0, 100)));
        sm.color = new Color(0, 0, 255);
        sm.name = "River";
    }

    @Override
    public void onGenerateDefaults(DataFrame df) {

    }

    @Override
    public void onUpdateSettings(SettingMaster sm) {
        if(Integer.parseInt(sm.settings.get("order").getValue()) != this.order)
        {
            this.order = Integer.parseInt(sm.settings.get("order").getValue());
            this.saveInt("order", order);
        }
        if (writer == null) {
            try {
                writer = new CSVWriter(new FileWriter(this.id + ".csv"), ',');
            } catch (IOException e) {
                System.out.println(e.toString());
            }
        }
    }
}
