/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package fi.kivibot.power.misc;

/**
 *
 * @author Nicklas Ahlskog
 */
public class GPPInfo {

    public final double build_cost, run_cost, build_speed, base_production;

    public GPPInfo(double bc, double rc, double bs, double bp) {
        this.build_cost = bc;
        this.run_cost = rc;
        this.build_speed = bs;
        this.base_production = bp;
    }

}
