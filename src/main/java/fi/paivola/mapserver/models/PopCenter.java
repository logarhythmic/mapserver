
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
import java.util.ArrayList;

/**
 *
 * @author Allan Palmu <allan.palmu@gmail.com>
 */
public class PopCenter extends PointModel {

    public boolean DebugFoodSource;
    private int requestsReceivedThisTick = 0;
    private int requestsServedThisTick = 0;
    
    ArrayList<Event> outgoing;
    public TownStorage storehouse;
    ArrayList<PopCenter> otherTowns;
    
    public PopCenter(int id){
        super(id);
    }
    
    @Override
    public void onTick(DataFrame last, DataFrame current) {
        if(storehouse.countFood() < 3000){
                requestSuppliesFromAll(new Supplies(0, Math.min(50, 3000 - storehouse.countFood())), current);
        }
        for (Event e : outgoing){
            Supplies retrieved = answerToRequest(e, current);
            storehouse.Store(retrieved);
        }
        outgoing.clear();
        this.saveInt("Shipments sent", requestsServedThisTick);
        requestsServedThisTick = 0;
        this.saveInt("Shipments requested", requestsReceivedThisTick);
        requestsReceivedThisTick = 0;
    }

    @Override
    public void onEvent(Event e, DataFrame d) {
        if (e.name.equals("request_supplies") && e.type == Event.Type.OBJECT && e.value.getClass() == Supplies.class && e.sender != this){
            requestsReceivedThisTick ++;
            outgoing.add(e);
        }
        
        if (e.name.equals("receive_supplies") && e.type == Event.Type.OBJECT && e.value.getClass() == Supplies.class && e.sender != this){
            Supplies received = (Supplies) e.value;
            storehouse.Store(received);
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
            if (p.storehouse.currentCapacity > 0)
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
        Supplies sent = storehouse.Take(((Supplies)e.value).id, ((Supplies)e.value).amount);
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
    }
    
    @Override
    public void onGenerateDefaults(DataFrame df) {
        outgoing = new ArrayList<>();
        otherTowns = new ArrayList<>();
        findOthers();
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
        //idk lol
    }
    
}
