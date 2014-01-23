
package fi.paivola.mapserver.models;

import fi.paivola.mapserver.utils.Color;
import fi.paivola.mapserver.core.DataFrame;
import fi.paivola.mapserver.core.Event;
import fi.paivola.mapserver.core.GameManager;
import fi.paivola.mapserver.core.PointModel;
import fi.paivola.mapserver.core.setting.*;
import fi.paivola.mapserver.utils.Icon;

/**
 *
 * @author Allan Palmu <allan.palmu@gmail.com>
 */
public class PopCenter extends PointModel {

    public PopCenter(String name, int id){
        super(id);
    }
    
    @Override
    public void onTick(DataFrame last, DataFrame current) {
        
    }

    @Override
    public void onEvent(Event e, DataFrame d) {
        if (e.name.equals("request_supplies") && e.type == Event.Type.OBJECT && e.value.getClass() == Supplies.class){
            Supplies retrieved = answerToRequest(e, d);
            ((TownStorage)(this.extensions.get("storehouse"))).Store(retrieved);
        }
        
        if (e.name.equals("receive_supplies") && e.type == Event.Type.OBJECT && e.value.getClass() == Supplies.class){
           Supplies received = (Supplies) e.value;
           ((TownStorage)(this.extensions.get("storehouse"))).Store(received);
        }
    }
    
    /**
     * Sends a request for supplies to the target PopCenter
     * @param s requested supplies
     * @param target who to bug
     * @param d the dataframe for the event
     */
    public void requestSupplies(Supplies s, PopCenter target, DataFrame d){
        Event request = new Event("request_supplies", Event.Type.OBJECT, s);
        request.sender = this;
        target.answerToRequest(request, d);
    }
    
    /**
     * Processes a supplies request event
     * @param e the event
     * @param d the dataframe for this event
     * @return Supplies sent back from delivery due to failure to deliver
     */
    public Supplies answerToRequest(Event e, DataFrame d){
        Supplies sent = (Supplies) e.value;
        return sendSupplies(sent, (PopCenter) e.sender, d);
    }
    
    private RoadModel[] getRouteTo(PopCenter target){
        return null;
    }
    
    /**
     * Sends supplies from this PopCenter to another. The Transport model is queried for information on how the delivery went. (todo)
     * @param s Supplies to send
     * @param target PopCenter to send supplies to
     * @param d the dataframe for the event
     * @return Supplies sent back from delivery due to failure to deliver
     */
    public Supplies sendSupplies(Supplies s, PopCenter target, DataFrame d){
        Supplies[] destroyed_and_delivered = new Supplies[]{new Supplies(s.id, 0), new Supplies(s.id, s.amount)}; //TODO: query supplies successfully transported during transit from transport
        Supplies destroyed = destroyed_and_delivered[0];
        Supplies delivered = destroyed_and_delivered[1];
        Event e = new Event("receive_supplies", Event.Type.OBJECT, delivered);
        e.sender = this;
        addEventTo(target, d, e);
        return new Supplies(s.id, s.amount - destroyed.amount);
    }
    
    
    @Override
    public void onRegisteration(GameManager gm, SettingMaster sm) {
        sm.setIcon(Icon.TOWN);
        sm.color = new Color(255, 128, 64);
        this.addExtension("storehouse", new TownStorage(1000));
    }

    @Override
    public void onGenerateDefaults(DataFrame df) {
        //idk lol
    }

    @Override
    public void onUpdateSettings(SettingMaster sm) {
        //idk lol
    }
    
}
