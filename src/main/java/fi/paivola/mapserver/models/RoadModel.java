/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.paivola.mapserver.models;

import fi.paivola.mapserver.utils.Supplies;
import fi.paivola.mapserver.core.ConnectionModel;
import fi.paivola.mapserver.core.DataFrame;
import fi.paivola.mapserver.core.Event;
import fi.paivola.mapserver.core.GameManager;
import fi.paivola.mapserver.core.GlobalModel;
import fi.paivola.mapserver.core.setting.SettingDouble;
import fi.paivola.mapserver.core.setting.SettingInt;
import fi.paivola.mapserver.core.setting.SettingMaster;
import fi.paivola.mapserver.utils.Color;
import fi.paivola.mapserver.utils.RangeDouble;
import fi.paivola.mapserver.utils.RangeInt;
import java.util.ArrayList;

/**
 *
 * @author kivi, fixed by ln
 */
public class RoadModel extends ConnectionModel {

    private double BASE_SPEED = 80; //km/h
    static private double TICK_TIME = 168; //h
    private double TRIP_REST = 12; //h

    static private double[] TT_MOD = new double[]{1, 0.75, 0.8, 1};
    static private double[] RT_MOD = new double[]{1, 0.8, 1};
    static private double[] A_MOD = new double[]{55, 10, 0.3, 0.05};
    private double RAIN_MOD = -0.1;
    
    private double roadLength;

    private int transport_type, road_type;
    
    private double deliveredThisTick;
    public double remainingCapacityThisTick;
    private double stealage = 0; // get from crime team
    public ArrayList<Supplies> stolenGoods;
    
    boolean roadBlocked;
    double rain;
    
    public RoadModel(int id) {
        super(id);
    }

    @Override
    public void onTickStart(DataFrame last, DataFrame current){
        roadBlocked = false;
        rain = (double) current.getGlobalData("rain");
        super.onTickStart(last, current);
        remainingCapacityThisTick = calcMaxStuff(calcTrips(calcTime(calcSpeed())));
    }
    
    @Override
    public void onTick(DataFrame last, DataFrame current) {
        this.saveDouble("Supplies delivered through this road", deliveredThisTick);
        deliveredThisTick = 0;
    }

    @Override
    public void onEvent(Event e, DataFrame current) {
        if (e.name.equals("Flood") && (boolean)e.value == true){
            roadBlocked = true;
        }
    }

    @Override
    public void onRegisteration(GameManager gm, SettingMaster sm) {
        sm.name = "roadModel";
        sm.color = new Color(0,0,0);
        sm.settings.put("roadLength", new SettingDouble("This is how long the road is in km", 10, new RangeDouble(0.001, 3000)));
        sm.settings.put("baseSpeed", new SettingDouble("This is the base speed of vehicles on this road, km/h", 80, new RangeDouble(1, 1000)));
        sm.settings.put("tripRest", new SettingDouble("This is how long drivers rest after deliveries, hours", 12, new RangeDouble(0, 48)));
        sm.settings.put("rainMod", new SettingDouble("This is how much deliveries are slowed down by rain", 0.1, new RangeDouble(0, 1)));
        sm.settings.put("transportType", new SettingInt("Type of transportation vehicles available on this road, 0:truck, 1:small truck, 2:pickup, 3:donkey", 1, new RangeInt(0,3)));
        sm.settings.put("roadType", new SettingInt("Type of this road, 0:paved, 1:unpaved, 2:footpath", 1, new RangeInt(0,2)));
    }

    @Override
    public void onGenerateDefaults(DataFrame df) {
        stolenGoods = new ArrayList<>();
    }

    private double calcSpeed() {
        double road = RT_MOD[road_type];
        road += this.RAIN_MOD * rain / 280;
        return BASE_SPEED * (road + TT_MOD[transport_type])/2;
    }

    private double calcTime(double speed) {
        return roadLength / speed;
    }

    private double calcTrips(double time) {
        return TICK_TIME / (time + TRIP_REST);
    }

    private double calcMaxStuff(double trips) {
        return possible()?trips * A_MOD[transport_type]:0;
    }

    private boolean possible() {
        if (road_type == 1 && transport_type == 0) {
            return false;
        }
        if (road_type == 2 && transport_type != 3) {
            return false;
        }
        return !roadBlocked;
    }
    
    public Supplies[] calcDelivery(Supplies sent){
        Supplies lost = new Supplies(sent.id, sent.amount * stealage);
        stolenGoods.add(lost);
        Supplies delivered = new Supplies(sent.id, Math.min(sent.amount - lost.amount, remainingCapacityThisTick));
        remainingCapacityThisTick -= delivered.amount;
        deliveredThisTick += delivered.amount;
        return new Supplies[] {lost, delivered};
    }

    @Override
    public void onUpdateSettings(SettingMaster sm) {
        BASE_SPEED = Double.parseDouble(sm.settings.get("baseSpeed").getValue());
        roadLength = Double.parseDouble(sm.settings.get("roadLength").getValue());
        TRIP_REST = Double.parseDouble(sm.settings.get("tripRest").getValue());
        RAIN_MOD = Double.parseDouble(sm.settings.get("rainMod").getValue());
        transport_type = Integer.parseInt(sm.settings.get("transportType").getValue());
        road_type = Integer.parseInt(sm.settings.get("roadType").getValue());
    }
}
