/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package fi.kivibot.power.misc;

import fi.paivola.mapserver.core.Model;

/**
 *
 * @author Nicklas Ahlskog
 */
public class PowerSourceInfo {

    private final Model pp;
    private final double dist, loss;

    public PowerSourceInfo(Model p, double d, double l) {
        pp = p;
        dist = d;
        loss = l;
    }

    public Model getPowerPlant() {
        return pp;
    }

    public double getDist() {
        return dist;
    }

    public double getLoss() {
        return loss;
    }

}
