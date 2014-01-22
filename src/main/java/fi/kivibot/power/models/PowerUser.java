/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.kivibot.power.models;

import fi.paivola.mapserver.core.ConnectionModel;
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

    public PowerUser(int id) {
        super(id);
        this.name = "Power user";
        this.saveDouble("usage", 10);
    }

    @Override
    public void onTick(DataFrame last, DataFrame current) {
        System.out.println(this.findSources().get(0).getDist());
    }

    @Override
    public void onEvent(Event e, DataFrame current) {
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

    /**
     * Threadsafe
     *
     * @param l
     */
    private synchronized void getPower(List<PowerPlant> l) {
    }

    private List<PowerSourceInfo> findSources() {
        List<PowerSourceInfo> sources = new LinkedList<>();
        List<Model> visited = new LinkedList<>();
        Queue<PowerSourceInfo> next = new LinkedList<>();

        for (Model m : this.connections) {
            if (m.name.equals("Power connection")) {
                Model mo = getOther((ConnectionModel) m, this);
                next.add(new PowerSourceInfo(mo, this.distanceTo(mo)));
            }
        }
        PowerSourceInfo psi;
        while ((psi = next.poll()) != null) {
            Model m = psi.getPowerPlant();
            if (!visited.contains(m)) {
                visited.add(m);
                if (m.name.equals("Power plant")) {
                    sources.add(psi);
                } else if(m.name.equals("Power node")){
                    for (Model m2 : m.connections) {
                        if (m2.name.equals("Power connection")) {
                            Model mo = getOther((ConnectionModel) m2, m);
                            next.add(new PowerSourceInfo(mo, psi.getDist() + m.distanceTo(mo)));
                        }
                    }
                }
            }
        }

        return sources;
    }

    private Model getOther(ConnectionModel cm, Model not) {
        for (Model m : cm.connections) {
            if (!m.equals(not)) {
                return m;
            }
        }
        return null;
    }

}
