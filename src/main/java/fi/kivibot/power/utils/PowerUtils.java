/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.kivibot.power.utils;

import fi.kivibot.power.models.Factory;
import fi.kivibot.power.models.PowerConnection;
import fi.kivibot.power.models.PowerNode;
import fi.paivola.mapserver.core.GameManager;
import fi.paivola.mapserver.core.Model;
import fi.paivola.mapserver.utils.LatLng;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author kivi
 */
public class PowerUtils {

    public static void createNetwork(GameManager gm) {
        List<PowerNode> nodes = new ArrayList<>();

        for (int i = 0; i < 500; i++) {
            Model pn = gm.createModel("Power node");
            pn.setLatLng(Math.random() * 2.0 - 1.0, Math.random() * 2.0 - 1.0);
            nodes.add((PowerNode) pn);
        }
        for (int i = 0; i < nodes.size() - 1; i++) {
            PowerConnection pc = (PowerConnection) gm.createModel("Power connection");
            gm.linkModelsWith(nodes.get(i), nodes.get(i + 1), pc);
        }

        for (int i = 0; i < 50; i++) {
            Factory f = (Factory) gm.createModel("Factory");
            PowerConnection pc = (PowerConnection) gm.createModel("Power connection");
            gm.linkModelsWith(nodes.get((int) (500.0 * Math.random())), f, pc);
        }

    
    }

}
