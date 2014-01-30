/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package fi.kivibot.power.misc;

import fi.paivola.mapserver.core.DataFrame;
import fi.paivola.mapserver.core.Model;
import java.util.HashMap;

/**
 *
 * @author Nicklas Ahlskog
 */
public class EU {

    private static final HashMap<DataFrame, HashMap<Model, EU>> hm = new HashMap<>();
    private double asd;

    public EU(double e) {
        asd = e;
    }

    public void set(double d) {
        asd = d;
    }

    public double get() {
        return asd;
    }

    @Override
    public String toString() {
        return "" + asd;
    }

    public static EU getEU(DataFrame df, Model m) {
        HashMap<Model, EU> h = hm.get(df);
        if (h != null) {
            return h.get(m);
        }
        return null;
    }

    public synchronized static void saveEU(DataFrame df, Model m, EU eu) {
        HashMap<Model, EU> h = hm.get(df);
        if (h == null) {
            h = new HashMap<>();
            hm.put(df, h);
        }
        h.put(m, eu);
    }

}
