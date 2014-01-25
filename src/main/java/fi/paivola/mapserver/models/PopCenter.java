
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
    
    public boolean DebugFoodSource;
    private int requestsReceivedThisTick = 0;
    private int requestsServedThisTick = 0;
    
    ArrayList<Event> outgoing;
    ArrayList<PopCenter> otherTowns;
    
    public PopCenter(int id){
        super(id);
    }
    
    @Override
    public void onTick(DataFrame last, DataFrame current) {
        if(countFood() < 1000){
            requestSuppliesFromAll(new Supplies(0, Math.min(50, 1000 - countFood())), current);
            requestSuppliesFromAll(new Supplies(1, Math.min(50, 1000 - countFood())), current);
        }
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
        this.saveDouble("availableFood", this.countFood());
        //this.saveDouble("Items in storage", this.currentStorageCapacity);
        //this.saveDouble("Storage fullness", this.currentStorageCapacity / this.maxStorageCapacity);
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
        if(outOfMilk && outOfGrain){
            Event starvation = new Event("outOfFood", Event.Type.DOUBLE, toEat-availableMilk - availableGrain);
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
        Supplies sent = Take(((Supplies)e.value).id, ((Supplies)e.value).amount);
        return sendSupplies(sent, (PopCenter) e.sender, d);
    }
    
    private RoadModel[] getRouteTo(PointModel target, Supplies s){
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
    public Supplies sendSupplies(Supplies s, PopCenter target, DataFrame d){
        RoadModel[] route = getRouteTo(target, s);
        if (route == null || s.amount == 0)
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
        return new Supplies(s.id, s.amount - destroyed.amount - delivered.amount);
    }
    
    
    @Override
    public void onRegisteration(GameManager gm, SettingMaster sm) {
        sm.setIcon(Icon.TOWN);
        sm.color = new Color(255, 128, 64);
        sm.name = "PopCenter";
        sm.settings.put("maxCap", new SettingDouble("The volume of the storage unit of this model", 100000, new RangeDouble(1, 1000000000)));
        sm.settings.put("ratRavenousness", new SettingDouble("How much of stored food will be eaten by rats in a week", 0.023, new RangeDouble(0, 1)));
    }
    
    @Override
    public void onGenerateDefaults(DataFrame df) {
        storage = new ArrayList<>();
        outgoing = new ArrayList<>();
        otherTowns = new ArrayList<>();
        findOthers();
        if (DebugFoodSource){
            while(Store(new Supplies(0,1000)) == 0){}
        }
        this.saveDouble("availableFood", this.countFood());
        //this.saveDouble("Items in storage", this.currentStorageCapacity);
        //this.saveDouble("Storage fullness", this.currentStorageCapacity / this.maxStorageCapacity);
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
        this.STORAGE_RAT_RAVENOUSNESS = Double.parseDouble(sm.settings.get("ratRavenousness").getValue());
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
     * @param in what to store
     * @return if the storage filled up, this is how much was left of the item
     */
    public double Store(Supplies in){
        if (in == null)
            return 0;
        Supplies found = findSupplies(in.id);
        if (found == null){
            storage.add(new Supplies(in.id, in.amount+currentStorageCapacity>maxStorageCapacity?maxStorageCapacity-currentStorageCapacity:in.amount));
            currentStorageCapacity += in.amount+currentStorageCapacity>maxStorageCapacity?maxStorageCapacity-currentStorageCapacity:in.amount;
        } else {
            found.amount = (found.amount+(in.amount+currentStorageCapacity>maxStorageCapacity?maxStorageCapacity-currentStorageCapacity:in.amount));
            currentStorageCapacity += (in.amount+currentStorageCapacity>maxStorageCapacity?maxStorageCapacity-currentStorageCapacity:in.amount);
        }
        double overflow = currentStorageCapacity+in.amount-maxStorageCapacity;
        overflow = overflow<0?0:overflow;
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
        ArrayList<Supplies> scopy = new ArrayList<>(storage);
        for (int i = scopy.size()-1; i >= 0; i--){
            if (scopy.get(i) != null ){
                if (scopy.get(i).amount == 0){
                    storage.remove(scopy.get(i));
                }
                currentStorageCapacity += scopy.get(i).amount;
            }
        }
        
        if (currentStorageCapacity > maxStorageCapacity){
//            throw new UnsupportedOperationException( "Wrong usage of PopCenter" );
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
