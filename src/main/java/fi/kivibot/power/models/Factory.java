/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package fi.kivibot.power.models;

import fi.kivibot.power.misc.PowerCounter;
import fi.kivibot.power.models.base.PowerUser;
import fi.paivola.mapserver.core.DataFrame;

/**
 *
 * @author Nicklas Ahlskog
 */
public class Factory extends PowerUser {

    private double profit_base = 20_000;
    private double profit_gained = 0;

    public Factory(int id) {
        super(id);
    }

    @Override
    public void onTick(DataFrame last, DataFrame current) {
        super.onTick(last, current);
        double profit = this.profit_base * this.getDouble("power");
        profit_gained += profit;
        this.saveDouble("profit_this_week", profit);
        PowerCounter.addCosts(current, -profit);
    }

    @Override
    public void onGenerateDefaults(DataFrame df) {
        this.saveDouble("profit_this_week", 0);
        this.saveDouble("usage", 168 * 40);
    }

}
