/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.paivola.mapserver.models;

import fi.paivola.mapserver.core.ConnectionModel;
import fi.paivola.mapserver.core.DataFrame;
import fi.paivola.mapserver.core.Event;
import fi.paivola.mapserver.core.GameManager;
import fi.paivola.mapserver.core.setting.SettingMaster;

/**
 *
 * @author kivi
 */
public class RoadModel extends ConnectionModel {

    private static double BASE_SPEED = 80; //km/h
    private static double TICK_TIME = 168; //h
    private static double TRIP_REST = 12; //h

    private static double[] TT_MOD = new double[]{1, 0.75, 0.8, 1};
    private static double[] RT_MOD = new double[]{1, 0.8, 1};
    private static double[] A_MOD = new double[]{55, 10, 0.3, 0.05};
    private static double RAIN_MOD = -0.1;

    private int transport_type, road_type;

    public RoadModel(int id, SettingMaster sm) {
        super(id, sm);
    }

    public RoadModel() {
        super();
    }

    @Override
    public void onTick(DataFrame last, DataFrame current) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onEvent(Event e, DataFrame current) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onRegisteration(GameManager gm, SettingMaster sm) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onGenerateDefaults(DataFrame df) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private double calcSpeed(boolean raining) {
        double road = RT_MOD[road_type];
        if (raining) {
            road += RoadModel.RAIN_MOD;
        }
        return BASE_SPEED * (road + TT_MOD[transport_type]);
    }

    private double calcTime(double len, double speed) {
        return len / speed;
    }

    private double calcTrips(double time) {
        return TICK_TIME / (time + TRIP_REST);
    }

    private double calcMaxStuff(double trips) {
        return trips * A_MOD[transport_type];
    }

    private boolean possible() {
        if (road_type == 1 && transport_type == 0) {
            return false;
        }
        if (road_type == 2 && transport_type != 3) {
            return false;
        }
        return true;
    }

}
