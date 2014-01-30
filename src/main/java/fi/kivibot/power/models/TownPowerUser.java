/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package fi.kivibot.power.models;

import fi.kivibot.power.misc.PowerNetworkAccessTool;
import fi.paivola.mapserver.core.DataFrame;
import fi.paivola.mapserver.core.Event;
import fi.paivola.mapserver.core.ExtensionModel;
import fi.paivola.mapserver.core.GameManager;
import fi.paivola.mapserver.core.setting.SettingMaster;

/**
 *
 * @author Nicklas Ahlskog
 */
public class TownPowerUser extends ExtensionModel {

    private double need_per_human;
    private double base_need;

    public TownPowerUser(int id) {
        super(id);
    }

    @Override
    public void onTick(DataFrame last, DataFrame current) {
        double need = this.base_need + this.getPop() * this.need_per_human;
        double p = PowerNetworkAccessTool.doPowerCalculations(need, last, this);
        this.saveData("power/need", p);
    }

    private int getPop() {
        String s = (String) this.getData("totalPopulation");
        return s == null ? 0 : Integer.parseInt(s);
    }

    @Override
    public void onEvent(Event e, DataFrame current) {
    }

    @Override
    public void onRegisteration(GameManager gm, SettingMaster sm) {
        sm.exts = "PopCenter";
    }

    @Override
    public void onGenerateDefaults(DataFrame df) {
    }

    @Override
    public void onUpdateSettings(SettingMaster sm) {
    }

}
