
package fi.paivola.mapserver.models;

import fi.paivola.mapserver.utils.Supplies;
import fi.paivola.mapserver.utils.Color;
import fi.paivola.mapserver.core.DataFrame;
import fi.paivola.mapserver.core.Event;
import fi.paivola.mapserver.core.GameManager;
import fi.paivola.mapserver.core.Model;
import fi.paivola.mapserver.core.PointModel;
import fi.paivola.mapserver.core.setting.*;
import fi.paivola.mapserver.utils.Icon;
import fi.paivola.mapserver.utils.RangeDouble;
import fi.paivola.mapserver.utils.RangeInt;
import java.util.ArrayList;

/**
 *
 * @author Allan Palmu <allan.palmu@gmail.com>
 */
public class PopCenter extends PointModel {

    double STORAGE_RAT_RAVENOUSNESS;
    
    private ArrayList<Supplies> storage;
    double maxStorageCapacity;
    double currentStorageCapacity;
    
    private double initialFood;
    
    private int requestsReceivedThisTick = 0;
    private int requestsServedThisTick = 0;
    
    ArrayList<Event> outgoing;
    ArrayList<PopCenter> otherTowns;
    
    private int vehicles;
    private int vehiclesUsed;
    
    private double foodNeededLastTick;
    
    private boolean isPort;
    private double allTimeStored;
    
    public PopCenter(int id){
        super(id);
    }
    
    @Override
    public void onTick(DataFrame last, DataFrame current) {
        if (isPort){
            if (last != null && last.getGlobalData("devAid") != null){
                double aid = (double)last.getGlobalData("devAid");
                aid *= 20;
                Store(new Supplies(0,aid/2));
                Store(new Supplies(1,aid/2));
            }
        }
        if(countFood() < foodNeededLastTick*3+50000){
            requestSuppliesFromAll(new Supplies(0, Math.min(50, foodNeededLastTick*3+50000 - countFood())), current);
            requestSuppliesFromAll(new Supplies(1, Math.min(50, foodNeededLastTick*3+50000 - countFood())), current);
        }
        vehiclesUsed = 0;
        for (Event e : outgoing){
            Supplies retrieved = answerToRequest(e, current);
            Store(retrieved);
        }
        outgoing.clear();
        this.saveInt("sentShipments", requestsServedThisTick);
        requestsServedThisTick = 0;
        this.saveInt("shipmentRequestsReceived", requestsReceivedThisTick);
        requestsReceivedThisTick = 0;
        ArrayList<Supplies> scopy = new ArrayList<>(storage);
        for (int i = 0; i < scopy.size(); i++){
            if (scopy.get(i) != null && scopy.get(i).amount > 0 && scopy.get(i).edible && storage.size() > 0)
                storage.get(i).amount*=(1 - STORAGE_RAT_RAVENOUSNESS);  //our highly advanced rat algorithm
        }
        UpdateStorage();
        this.saveDouble("allTimeFood", this.allTimeStored);
        this.saveDouble("availableFood", this.countFood());
    }

    @Override
    public void onEvent(Event e, DataFrame d) {
        if (e.sender == this)
            return;
        switch (e.name){
            case "requestSupplies":
                requestsReceivedThisTick ++;
                outgoing.add(e);
                break;
            case "harvested":
            case "receiveSupplies":
                Supplies received = (Supplies) e.value;
                Store(received);
                break;
            case "cropReady":
                if (QuerySpace() >= (double)e.value)
                    addEventTo(e.sender, d, new Event("gather", Event.Type.DOUBLE, QuerySpace()));
                break;
            case "consumeFood":
                eatFood(e, d);
                break;
        }
    }
    
    void eatFood(Event e, DataFrame current){
        double toEat = (double) e.value;
        foodNeededLastTick = toEat;
        double availableMilk = 0;
        if (findSupplies(0) != null)
            availableMilk = findSupplies(0).amount;
        double availableGrain = 0;
        if (findSupplies(1) != null)
            availableGrain = findSupplies(1).amount;
        boolean outOfMilk = availableMilk < toEat/2;
        boolean outOfGrain = availableGrain < toEat/2;
        outOfMilk = outOfGrain?toEat-availableGrain>availableMilk:outOfMilk;
        outOfGrain = outOfMilk?toEat-availableMilk>availableGrain:outOfGrain;
        //System.out.print(this.id + ": Needed food = "+(long)toEat+". Available foods: "+(long)availableGrain+" grain, "+(long)availableMilk+" milk.");
        if(outOfMilk && outOfGrain){
            Event starvation = new Event("outOfFood", Event.Type.DOUBLE, (toEat - availableMilk - availableGrain) / toEat);
            //System.out.print(" Town "+this.id+" is starving!");
            Take(0, availableMilk);
            Take(1, availableGrain);
            starvation.sender = this;
            addEventTo(this, current, starvation);
        } else if (outOfMilk){
            Take(0,availableMilk);
            Take(1, toEat - availableMilk);
        } else if (outOfGrain){
            Take(1,availableGrain);
            Take(0, toEat - availableGrain);
        } else {
            Take(0,toEat/2);
            Take(1,toEat/2);
        }
        //System.out.println("");
    }
    
    /**
     * Sends a request for supplies to the target PopCenter
     * @param s requested supplies
     * @param target who to bug
     * @param d the dataframe for the event
     */
    public void requestSuppliesFrom(Supplies s, PopCenter target, DataFrame d){
        Event request = new Event("requestSupplies", Event.Type.OBJECT, s);
        request.sender = this;
        this.addEventTo(target, d, request);
    }
    
    public void requestSuppliesFromAll(Supplies s, DataFrame d){
        for(PopCenter p : otherTowns){
            if (p.currentStorageCapacity > 0)
                requestSuppliesFrom(s, p, d);
        }
    }
    
    /**
     * Processes a supplies request event
     * @param e the event
     * @param d the dataframe for this event
     * @return Supplies sent back from delivery due to failure to deliver
     */
    public Supplies answerToRequest(Event e, DataFrame d){
        Supplies s = new Supplies(((Supplies)e.value).id, 0);
        while(true){
            RoadModel[] route = getRouteTo((PopCenter)e.sender, s);
            if (route == null)
                break;
            double shipmentSize = Double.MAX_VALUE;
            for(RoadModel r : route){
                if (r.getVehicleCap() < shipmentSize)
                    shipmentSize = r.getVehicleCap();
            }
            Supplies sent = Take(((Supplies)e.value).id, shipmentSize);
            Supplies back = sendSupplies(sent, route, (PopCenter) e.sender, d);
            s.amount += back.amount;
            if (back.amount == shipmentSize || sent.amount == 0)
                break;
        }
        return s;
    }
    
    private RoadModel[] getRouteTo(PopCenter target, Supplies s){
        ArrayList<RoadModel[]> routes = new ArrayList<>();
        ArrayList<RoadModel> primary = new ArrayList<>();
        for (Model m : this.connections){
            if (m.getClass().equals(RoadModel.class)) {
                RoadModel mr = (RoadModel)m;
                if(mr.remainingCapacityThisTick > 0){
                    primary.add(mr);
                }
            }
        }
        for (RoadModel mr : primary){
            for(Model t : mr.connections){
                if (t.getClass().equals(PopCenter.class)){
                    if (t.id == target.id){
                        routes.add(new RoadModel[] {mr});
                    }
                    else if (t.id != this.id){
                        for(Model mm : t.connections){
                            if (mm.getClass().equals(RoadModel.class)) {
                                RoadModel mmr = (RoadModel)mm;
                                if(mmr.remainingCapacityThisTick > 0){
                                    for (Model tt : mmr.connections){
                                        if (tt.id == target.id){
                                            routes.add(new RoadModel[] {mr, mmr});
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if (routes.isEmpty()){
            return null;
        }
        if (routes.size() == 1)
            return routes.get(0);
        RoadModel[] bestRoute = routes.get(0);
        double highestLowestCapacity = 0;
        for (RoadModel r : bestRoute){
            highestLowestCapacity = Math.min(highestLowestCapacity, r.remainingCapacityThisTick);
        }
        double maybeHighestLowestCap;
        for (RoadModel[] r : routes){
            maybeHighestLowestCap = r[0].remainingCapacityThisTick;
            for (RoadModel ro : bestRoute){
                maybeHighestLowestCap = Math.min(maybeHighestLowestCap, ro.remainingCapacityThisTick);
            }
            if (maybeHighestLowestCap > highestLowestCapacity){
                highestLowestCapacity = maybeHighestLowestCap;
                bestRoute = r;
            }
        }
        return bestRoute;
    }
    
    /**
     * Sends supplies from this PopCenter to another. The Transport model is queried for information on how the delivery went.
     * @param s Supplies to send
     * @param target PopCenter to send supplies to
     * @param d the dataframe for the event
     * @return Supplies sent back from delivery due to failure to deliver
     */
    public Supplies sendSupplies(Supplies s, RoadModel[] route, PopCenter target, DataFrame d){
        if (route == null || s.amount == 0 || vehiclesUsed >= vehicles )
            return s;
        Supplies destroyed = new Supplies(s.id, 0);
        Supplies delivered = new Supplies(s.id, s.amount);
        for (RoadModel r : route){
            Supplies[] destroyed_and_delivered = r.calcDelivery(delivered);
            destroyed.amount += destroyed_and_delivered[0].amount;
            delivered = destroyed_and_delivered[1];
        }
        Event e = new Event("receiveSupplies", Event.Type.OBJECT, delivered);
        e.sender = this;
        addEventTo(target, d, e);
        requestsServedThisTick ++;
        vehiclesUsed ++;
        return new Supplies(s.id, s.amount - destroyed.amount - delivered.amount);
    }
    
    
    @Override
    public void onRegisteration(GameManager gm, SettingMaster sm) {
        sm.setIcon(Icon.TOWN);
        sm.color = new Color(255, 128, 64);
        sm.name = "PopCenter";
        sm.settings.put("maxCap", new SettingDouble("The volume of the storage unit of this model", Integer.MAX_VALUE, new RangeDouble(1, Double.MAX_VALUE)));
        sm.settings.put("spoilingRate", new SettingDouble("How much of stored food will get eaten by rats and/or rot in a week", 0.023, new RangeDouble(0, 1)));
        sm.settings.put("vehicles", new SettingInt("How many vehicles this model has available for sending supplies", 1, new RangeInt(0, 10000)));
        sm.settings.put("initialFood", new SettingDouble("How much food this model has in store initially", 10000, new RangeDouble(0,Double.MAX_VALUE)));
        sm.settings.put("isPort", new SettingBoolean("Does this town receive support packages from abroad?", false));
    }
    
    @Override
    public void onGenerateDefaults(DataFrame df) {
        storage = new ArrayList<>();
        outgoing = new ArrayList<>();
        otherTowns = new ArrayList<>();
        findOthers();
        if (initialFood > 0){
            Store(new Supplies(0,initialFood));
        }
        this.saveDouble("allTimeFood", this.allTimeStored);
        this.saveDouble("availableFood", this.countFood());
    }
    
    void findOthers(){
        ArrayList<Model> connectedModels = new ArrayList<>();
        connectedModels.add(this);
        while(true){
            int amount = connectedModels.size();
            ArrayList<Model> newConns = new ArrayList<>();
            for(Model m : connectedModels){
                for(Model mm: m.connections){
                    if (!connectedModels.contains(mm) && !newConns.contains(mm)){
                        newConns.add(mm);
                    }
                }
            }
            connectedModels.addAll(newConns);
           if (connectedModels.size() != amount)
                continue;
            break;
        }
        connectedModels.remove(this);
        for (Model m : connectedModels){
            if (m.getClass().equals(this.getClass()))
                otherTowns.add((PopCenter)m);
        }
    }

    @Override
    public void onUpdateSettings(SettingMaster sm) {
        this.maxStorageCapacity = Double.parseDouble(sm.settings.get("maxCap").getValue());
        this.STORAGE_RAT_RAVENOUSNESS = Double.parseDouble(sm.settings.get("spoilingRate").getValue());
        this.vehicles = Integer.parseInt(sm.settings.get("vehicles").getValue());
        this.initialFood = Double.parseDouble(sm.settings.get("initialFood").getValue());
        this.isPort = Boolean.parseBoolean(sm.settings.get("isPort").getValue());
    }
    
    Supplies findSupplies(int id){
        if (storage.isEmpty())
            return null;
        for (Supplies s : storage){
            if (s.id == id)
                return s;
        }
        return null;
    }
    
    /**
     * Stores the given supplies item into the storage
     * @param inS what to store
     * @return if the storage filled up, this is how much was left of the item
     */
    public double Store(Supplies inS){
        if (inS == null)
            return 0;
        Supplies in = new Supplies(inS.id, inS.amount);
        double overflow = currentStorageCapacity+in.amount-maxStorageCapacity;
        Supplies found = findSupplies(in.id);
        if (found == null){
            in.amount = (in.amount+currentStorageCapacity>maxStorageCapacity)?maxStorageCapacity-currentStorageCapacity:in.amount;
            storage.add(in);
        } else {
            in.amount = (in.amount+currentStorageCapacity>maxStorageCapacity)?maxStorageCapacity-currentStorageCapacity:in.amount;
            found.amount += in.amount;
        }
        overflow = overflow<0?0:overflow;
        currentStorageCapacity += in.amount;
        allTimeStored += in.amount;
        return overflow;
    }
    
    
    
    /**
     * @param id the identifier for the type of supplies
     * @return how much of that we have in stock
     */
    public double QuerySupplies(int id){
        Supplies found = findSupplies(id);
        if (found == null)
            return 0;
        else return found.amount;
    }
    
    /**
     * @return how much stuff is in stock
     */
    public double QueryCapacity(){
        return currentStorageCapacity;
    }
    
    public double QuerySpace(){
        return maxStorageCapacity - currentStorageCapacity;
    }
    
    /**
     * Takes a supply item out of the storage
     * @param id which supplies
     * @param amount how much
     * @return the created Supplies item
     */
    public Supplies Take(int id, double amount){
        if (amount < 0)
            return null;
        Supplies found = findSupplies(id);
        if (found == null){
            return new Supplies(id, 0);
        } else {
            double taken = Math.min(amount, found.amount);
            found.amount = (found.amount - taken);
            return new Supplies(id, taken);
        }
    }
    
    void UpdateStorage(){
        currentStorageCapacity = 0;
        
        for (int i = storage.size()-1; i >= 0; i--){
            if (storage.get(i) != null ){
                if (storage.get(i).amount == 0){
                    storage.remove(storage.get(i));
                }
            }
        }
        
        for(Supplies s : storage){
            currentStorageCapacity += s.amount;
        }
        
        if (currentStorageCapacity > maxStorageCapacity){
            System.out.println("Storage had more goods than could fit inside. "
                              +"This should not happen. "
                              +"Please use the Store method to store items.");
        }
    }

    public double countFood(){
        double ret = 0;
        for (Supplies s: storage){
            ret += s.edible?s.amount:0;
        }
        
        return ret;
    }
}
