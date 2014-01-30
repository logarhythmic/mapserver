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
import fi.paivola.mapserver.core.setting.SettingDouble;
import fi.paivola.mapserver.core.setting.SettingInt;
import fi.paivola.mapserver.core.setting.SettingMaster;
import fi.paivola.mapserver.utils.Color;
import fi.paivola.mapserver.utils.RangeDouble;
import fi.paivola.mapserver.utils.RangeInt;
import java.util.Random;

/**
 *
 * @author kivi, fixed by ln
 */
public class RoadModel extends ConnectionModel {

    private double BASE_SPEED = 80; //km/h
    private static final double TICK_TIME = 168; //h
    private double TRIP_REST = 1; //h

    private static final double[] TT_MOD = new double[]{1, 0.75, 0.8, 1};
    private static final double[] VEHICLE_LENGTH_MOD = new double[]{30, 15, 5, 1};
    private static final double[] RT_MOD = new double[]{1, 0.8, 1};
    private static final double[] A_MOD = new double[]{55000, 10000, 300, 50};
    private double RAIN_MOD = -0.1;
    
    public double roadLength; // km
    
    private int transport_type, road_type;
    
    private double deliveredThisTick;
    public double remainingCapacityThisTick;
    private double stealChance = 0;
    
    boolean roadBlocked;
    double rain;
    
    double stolenThisTick;
    
    Random random;
    
    public RoadModel(int id) {
        super(id);
        random = new Random();
    }

    @Override
    public void onTickStart(DataFrame last, DataFrame current){
        roadBlocked = false;
        this.maxConnections = 20;
        if (current.getGlobalData("rain")!= null)
            rain = (double) current.getGlobalData("rain");
        super.onTickStart(last, current);
        remainingCapacityThisTick = calcMaxStuff(calcTrips(calcTime(calcSpeed())));
    }
    
    public double getVehicleCap(){
        return A_MOD[transport_type];
    }
    
    @Override
    public void onTick(DataFrame last, DataFrame current) {
        this.saveDouble("Supplies delivered through this road", deliveredThisTick);
        this.saveDouble("Supplies stolen on this road", stolenThisTick);
        deliveredThisTick = 0;
        stolenThisTick = 0;
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
        sm.settings.put("tripRest", new SettingDouble("This is how long drivers rest after deliveries, hours", 1, new RangeDouble(0, 48)));
        sm.settings.put("rainMod", new SettingDouble("This is how much deliveries are slowed down by rain", 0.1, new RangeDouble(0, 1)));
        sm.settings.put("transportType", new SettingInt("Type of transportation vehicles available on this road, 0:truck, 1:small truck, 2:pickup, 3:donkey", 1, new RangeInt(0,3)));
        sm.settings.put("roadType", new SettingInt("Type of this road, 0:paved, 1:unpaved, 2:footpath", 1, new RangeInt(0,2)));
        sm.settings.put("theftChance", new SettingDouble("Chance of a delivery being hijacked by thieves and lost", 0.1, new RangeDouble(0,1)));
    }

    @Override
    public void onGenerateDefaults(DataFrame df) {
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
        return (TICK_TIME / (time + TRIP_REST)) * roadLength / VEHICLE_LENGTH_MOD[transport_type];
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
        Supplies lost = new Supplies(sent.id, random.nextDouble()<stealChance?sent.amount:0);
        stolenThisTick += lost.amount;
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
        stealChance = Double.parseDouble(sm.settings.get("theftChance").getValue());
    }
}
