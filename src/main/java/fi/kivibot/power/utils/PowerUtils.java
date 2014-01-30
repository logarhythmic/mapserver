/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package fi.kivibot.power.utils;

import fi.kivibot.power.models.Factory;
import fi.kivibot.power.models.GenericPowerPlant;
import fi.kivibot.power.models.PowerConnection;
import fi.kivibot.power.models.PowerNode;
import fi.paivola.mapserver.core.GameManager;
import fi.paivola.mapserver.core.Model;
import fi.paivola.mapserver.core.setting.SettingMaster;
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

        for (int i = 0; i < 1000; i++) {
            Model pn = gm.createModel("Power node");
            pn.setLatLng(Math.random() * 2.0 - 1.0, Math.random() * 2.0 - 1.0);
            nodes.add((PowerNode) pn);
        }

        for (int i = 0; i < nodes.size() - 1; i++) {
            PowerConnection pc = (PowerConnection) gm.createModel("Power connection");
            gm.linkModelsWith(nodes.get(i), nodes.get(i + 1), pc);
        }

        for (int i = 0; i < 15; i++) {
            Factory f = (Factory) gm.createModel("Factory");
            PowerConnection pc = (PowerConnection) gm.createModel("Power connection");
            gm.linkModelsWith(nodes.get((int) (1000.0 * Math.random())), f, pc);
        }

        for (int i = 0; i < 50; i++) {
            GenericPowerPlant f = (GenericPowerPlant) gm.createModel("GenericPowerPlant");
            SettingMaster sm = gm.getDefaultSM("GenericPowerPlant");
            f.onActualUpdateSettings(sm);
            PowerConnection pc = (PowerConnection) gm.createModel("Power connection");
            gm.linkModelsWith(nodes.get((int) (1000.0 * Math.random())), f, pc);
        }

    }

}
