/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package fi.kivibot.power.models;

import fi.kivibot.power.misc.EU;
import fi.kivibot.power.misc.GPPInfo;
import fi.kivibot.power.misc.PowerCounter;
import fi.paivola.mapserver.core.DataFrame;
import fi.paivola.mapserver.core.Event;
import fi.paivola.mapserver.core.GameManager;
import fi.paivola.mapserver.core.PointModel;
import fi.paivola.mapserver.core.setting.SettingInt;
import fi.paivola.mapserver.core.setting.SettingMaster;
import fi.paivola.mapserver.utils.RangeInt;

/**
 *
 * @author Nicklas Ahlskog
 */
public class GenericPowerPlant extends PointModel {

    private static final int TYPE_NUCLEAR = 1;
    private static final int TYPE_COAL = 2;
    private static final int TYPE_WIND_LAND = 3;
    private static final int TYPE_WIND_SEA = 4;
    private static final int TYPE_SOLAR = 5;
    private static final int TYPE_WATER = 6;
    private static final int TYPE_WAVE = 7;
    private static final int TYPE_NATURAL_GAS = 8;
    private static final int TYPE_FUEL = 9;
    private static final int TYPE_GEOTHERMAL = 10;
    private static final int TYPE_BIOMASS = 11;
    private static final int TYPE_DEFAULT = 0;

    //GPPINFO bc rc bs bp
    private static final GPPInfo info[][] = new GPPInfo[][]{
        //DEFAULT
        new GPPInfo[]{new GPPInfo(1, 1, 0.1, 1), new GPPInfo(1, 1, 0.1, 1)},
        //NUCLEAR
        new GPPInfo[]{new GPPInfo(1, 1, 0.1, 1), new GPPInfo(1, 1, 0.1, 1)},
        //COAL
        new GPPInfo[]{new GPPInfo(1, 1, 0.1, 1), new GPPInfo(1, 1, 0.1, 1)},
        //WIND_LAND
        new GPPInfo[]{new GPPInfo(1, 1, 0.1, 1), new GPPInfo(1, 1, 0.1, 1)},
        //WIND_SEA
        new GPPInfo[]{new GPPInfo(1, 1, 0.1, 1), new GPPInfo(1, 1, 0.1, 1)},
        //SOLAR
        new GPPInfo[]{new GPPInfo(1, 1, 0.1, 1), new GPPInfo(1, 1, 0.1, 1)},
        //WATER
        new GPPInfo[]{new GPPInfo(1, 1, 0.1, 1), new GPPInfo(1, 1, 0.1, 1)},
        //WAVE
        new GPPInfo[]{new GPPInfo(1, 1, 0.1, 1), new GPPInfo(1, 1, 0.1, 1)},
        //NATURAL_GAS
        new GPPInfo[]{new GPPInfo(1, 1, 0.1, 1), new GPPInfo(1, 1, 0.1, 1)},
        //FUEL
        new GPPInfo[]{new GPPInfo(1, 1, 0.1, 1), new GPPInfo(1, 1, 0.1, 1)},
        //GEOTHERMAL
        new GPPInfo[]{new GPPInfo(1, 1, 0.1, 1), new GPPInfo(1, 1, 0.1, 1)},
        //BIOMASS
        new GPPInfo[]{new GPPInfo(1, 1, 0.1, 1), new GPPInfo(1, 1, 0.1, 1)}};

    private double build_cost_per_week;
    private double run_cost_per_kwh;
    private double build_speed_week;
    private double build_percent;
    private double base_production;
    private int pp_type, size;
    private boolean disabled = false;

    public GenericPowerPlant(int id) {
        super(id);
        this.name = "Power plant";
    }

    @Override
    public void onTick(DataFrame last, DataFrame current) {
        if (this.build_percent < 1 && !disabled) {
            this.build_percent += this.build_speed_week;
            PowerCounter.addCosts(current, this.build_cost_per_week);
            saveEmptyEU(current);
        } else if (!disabled) {
            calcPower(last, current);
        }
        this.saveInt("disabled", disabled ? 1 : 0);
        disabled = false;
    }

    private void calcPower(DataFrame l, DataFrame c) {
        double d;
        switch (pp_type) {
            case TYPE_SOLAR:
                d = getGlobalValue(l, "sunlight", 8);
                break;
            case TYPE_WIND_LAND:
                d = getGlobalValue(l, "wind", 9);
                break;
            default:
                d = 1;
                break;
        }
        EU eu = new EU(d * this.base_production);
        this.saveData("production", eu);
        EU.saveEU(c, this, eu);
        PowerCounter.addCosts(c, eu.get() * this.run_cost_per_kwh);
    }

    private double getGlobalValue(DataFrame df, String s, double d) {
        String ln = (String) df.getGlobalData(s);
        return ln == null ? d : Double.parseDouble(ln);
    }

    private void saveEmptyEU(DataFrame df) {
        EU.saveEU(df, this, new EU(0));
    }

    @Override
    public void onEvent(Event e, DataFrame current) {
        if (e.name.equals("Flood") && (boolean) e.value == true) {
            disabled = true;
        }
    }

    @Override
    public void onRegisteration(GameManager gm, SettingMaster sm) {
        sm.settings.put("pp_type", new SettingInt("Type (see javadoc/code)", 0, new RangeInt(0, 11)));
        sm.settings.put("size", new SettingInt("Size (see javadoc/code)", 0, new RangeInt(0, 1)));
    }

    @Override
    public void onGenerateDefaults(DataFrame df) {
        this.build_cost_per_week = info[pp_type][size].build_cost;
        this.build_speed_week = info[pp_type][size].build_speed;
        this.run_cost_per_kwh = info[pp_type][size].run_cost;
        this.base_production = info[pp_type][size].base_production;
        this.saveEmptyEU(df);
    }

    @Override
    public void onUpdateSettings(SettingMaster sm) {
        pp_type = Integer.parseInt(sm.settings.get("pp_type").getValue());
        size = Integer.parseInt(sm.settings.get("size").getValue());
    }

}
