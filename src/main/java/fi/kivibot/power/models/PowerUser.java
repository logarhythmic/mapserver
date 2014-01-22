/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.kivibot.power.models;

import fi.paivola.mapserver.core.DataFrame;
import fi.paivola.mapserver.core.Event;
import fi.paivola.mapserver.core.GameManager;
import fi.paivola.mapserver.core.Model;
import fi.paivola.mapserver.core.PointModel;
import fi.paivola.mapserver.core.setting.SettingMaster;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 *
 * @author kivi
 */
public class PowerUser extends PointModel {

    private double usage = 0.3;
    private boolean online = true;

    private double pogo = 0;

    public PowerUser(int id) {
        super(id);
    }

    @Override
    public void onTick(DataFrame last, DataFrame current) {
        System.out.println(this.findSources().size());
    }

    @Override
    public void onEvent(Event e, DataFrame current) {
        switch (e.name) {
            case "energy-get":
                pogo += e.getDouble();
                break;
        }
    }

    @Override
    public void onRegisteration(GameManager gm, SettingMaster sm) {
    }

    @Override
    public void onGenerateDefaults(DataFrame df) {
    }

    @Override
    public void onUpdateSettings(SettingMaster sm) {
    }

    private List<PowerPlant> findSources() {
        List<PowerPlant> sources = new LinkedList<>();
        List<Model> visited = new LinkedList<>();
        Queue<Model> next = new LinkedList<>();

        for (Model m : this.connections) {
            if (m.name.equals("Power connection")) {
                next.add(m);
            }
        }
        Model m;
        while ((m = next.poll()) != null) {
            if (!visited.contains(m)) {
                visited.add(m);
                for (Model m2 : m.connections) {
                    if (m2.name.equals("Power connection")) {
                        next.add(m2);
                    } else if (m2 instanceof PowerPlant) {
                        sources.add((PowerPlant) m2);
                    }
                }
            }
        }

        return sources;
    }

}
