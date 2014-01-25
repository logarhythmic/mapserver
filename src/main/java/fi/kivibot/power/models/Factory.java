/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.kivibot.power.models;

import fi.kivibot.power.models.base.PowerUser;
import fi.paivola.mapserver.core.DataFrame;

/**
 *
 * @author kivi
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
    }

    @Override
    public void onGenerateDefaults(DataFrame df) {
        this.saveDouble("profit_this_week", 0);
        this.saveDouble("usage", 168 * 40);
    }

}
