
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
    
    ArrayList<Supplies> storage;
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
        if(countFood() < 300){
            requestSuppliesFromAll(new Supplies(0, Math.min(50, 3000 - countFood())), current);
        }
        for (Event e : outgoing){
            Supplies retrieved = answerToRequest(e, current);
            Store(retrieved);
        }
        outgoing.clear();
        this.saveInt("Shipments sent", requestsServedThisTick);
        requestsServedThisTick = 0;
        this.saveInt("Shipments requested", requestsReceivedThisTick);
        requestsReceivedThisTick = 0;
        ArrayList<Supplies> scopy = new ArrayList<>(storage);
        for (int i = 0; i < scopy.size(); i++){
            if (scopy.get(i) != null && scopy.get(i).amount > 0 && scopy.get(i).edible && storage.size() > 0)
                storage.get(i).amount*=(1 - STORAGE_RAT_RAVENOUSNESS);  //our highly advanced rat algorithm
        }
        UpdateStorage();
        this.saveDouble("Food in storage", this.countFood());
        //this.saveDouble("Items in storage", this.currentStorageCapacity);
        //this.saveDouble("Storage fullness", this.currentStorageCapacity / this.maxStorageCapacity);
    }

    @Override
    public void onEvent(Event e, DataFrame d) {
        if (e.name.equals("request_supplies") && e.type == Event.Type.OBJECT && e.value.getClass() == Supplies.class && e.sender != this){
            requestsReceivedThisTick ++;
            outgoing.add(e);
        }
        
        if (e.name.equals("receive_supplies") && e.type == Event.Type.OBJECT && e.value.getClass() == Supplies.class && e.sender != this){
            Supplies received = (Supplies) e.value;
            Store(received);
        }
    }
    
    /**
     * Sends a request for supplies to the target PopCenter
     * @param s requested supplies
     * @param target who to bug
     * @param d the dataframe for the event
     */
    public void requestSuppliesFrom(Supplies s, PopCenter target, DataFrame d){
        Event request = new Event("request_supplies", Event.Type.OBJECT, s);
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
        Event e = new Event("receive_supplies", Event.Type.OBJECT, delivered);
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
        sm.settings.put("maxCap", new SettingDouble("The volume of the storage unit of this model", 10000, new RangeDouble(1, 1000000000)));
        sm.settings.put("ratRavenousness", new SettingDouble("How much of stored food will be eaten by rats in a week", 0, new RangeDouble(0, 1)));
    }
    
    @Override
    public void onGenerateDefaults(DataFrame df) {
        System.out.println("OGD called on village "+this.id+" at "+df.index);
        storage = new ArrayList<>();
        outgoing = new ArrayList<>();
        otherTowns = new ArrayList<>();
        findOthers();
        if (DebugFoodSource){
            while(Store(new Supplies(0,1000)) == 0){}
        }
        this.saveDouble("Food in storage", this.countFood());
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
