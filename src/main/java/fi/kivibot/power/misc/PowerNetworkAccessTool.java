/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package fi.kivibot.power.misc;

import fi.kivibot.power.models.PowerConnection;
import fi.paivola.mapserver.core.ConnectionModel;
import fi.paivola.mapserver.core.DataFrame;
import fi.paivola.mapserver.core.Model;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

/**
 *
 * @author Nicklas Ahlskog
 */
public class PowerNetworkAccessTool {

    public static double doPowerCalculations(double usage, DataFrame df, Model base) {
        List<PowerSourceInfo> l = sort_loss(findSources(base));
        return getPower(usage, df, l) / usage;
    }

    private static synchronized double getPower(double usage, DataFrame df, List<PowerSourceInfo> l) {
        double ce = 0;

        for (PowerSourceInfo psi : l) {
            if (ce >= usage) {
                break;
            }
            double a = usage - ce;
            double loss = psi.getLoss();
            double req = a / (1 - loss);

            Model m = psi.getPowerPlant();
            EU eu = EU.getEU(df, m);

            double cpm = eu.get();

            double sub = Math.min(cpm, req);

            eu.set(cpm - sub);
            ce += sub * (1.0 - loss);

        }

        return ce;
    }

    private static List<PowerSourceInfo> sort_loss(List<PowerSourceInfo> l) {
        Collections.sort(l, new Comparator() {

            @Override
            public int compare(Object o1, Object o2) {
                double a = ((PowerSourceInfo) o1).getLoss();
                double b = ((PowerSourceInfo) o2).getLoss();
                return a == b ? 0 : a < b ? 1 : -1;
            }
        });

        return l;
    }

    private static List<PowerSourceInfo> findSources(Model base) {
        List<PowerSourceInfo> sources = new LinkedList<>();
        Set<Model> visited = new HashSet<>();
        Queue<PowerSourceInfo> next = new LinkedList<>();

        for (Model m : base.connections) {
            if (m.name.equals("Power connection")) {
                Model mo = getOther((ConnectionModel) m, base);
                double d = ((PowerConnection) m).lenC();
                next.add(new PowerSourceInfo(mo, d, d * m.getDouble("losspkm")));
            }
        }
        PowerSourceInfo psi;
        while ((psi = next.poll()) != null) {
            Model m = psi.getPowerPlant();
            if (!visited.contains(m)) {
                visited.add(m);
                if (m.name.equals("Power plant")) {
                    sources.add(psi);
                } else if (m.name.equals("Power node")) {
                    for (Model m2 : m.connections) {
                        if (m2.name.equals("Power connection")) {
                            Model mo = getOther((ConnectionModel) m2, m);
                            double d = ((PowerConnection) m2).lenC();
                            double l = ((PowerConnection) m2).lossC();
                            next.add(new PowerSourceInfo(mo, psi.getDist() + d, psi.getLoss() + l));
                        }
                    }
                }
            }
        }

        return sources;
    }

    private static Model getOther(ConnectionModel cm, Model not) {
        for (Model m : cm.connections) {
            if (!m.equals(not)) {
                return m;
            }
        }
        return null;
    }
}
