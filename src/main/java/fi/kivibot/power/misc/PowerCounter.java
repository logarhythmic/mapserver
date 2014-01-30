/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package fi.kivibot.power.misc;

import fi.paivola.mapserver.core.DataFrame;

/**
 *
 * @author Nicklas Ahlskog
 */
public class PowerCounter {

    private static double costs = 0;
    private static double profit = 0;

    public static synchronized void addCosts(DataFrame df, double amount) {
        String pc1 = (String) df.getGlobalData("power-cur-costs");
        double b1 = pc1 == null ? 0 : Double.parseDouble(pc1);
        b1 += amount;
        String pc2 = (String) df.getGlobalData("power-cur-profit");
        double b2 = pc2 == null ? 0 : Double.parseDouble(pc2);
        b2 += amount;
        if (amount > 0) {
            costs += amount;
        } else {
            profit -= amount;
        }
        df.saveGlobalData("power-costs", costs);
        df.saveGlobalData("power-profit", profit);
        df.saveGlobalData("power-net", profit + costs);
        df.saveGlobalData("power-cur-costs", b1);
        df.saveGlobalData("power-cur-profit", b2);
        df.saveGlobalData("power-cur-net", b1 + b2);
    }

}
