
package fi.paivola.mapserver.models;

import fi.paivola.mapserver.utils.Color;
import fi.paivola.mapserver.core.DataFrame;
import fi.paivola.mapserver.core.Event;
import fi.paivola.mapserver.core.GameManager;
import fi.paivola.mapserver.core.Model;
import fi.paivola.mapserver.core.PointModel;
import fi.paivola.mapserver.core.setting.*;
import fi.paivola.mapserver.utils.Icon;

/**
 *
 * @author Allan Palmu <allan.palmu@gmail.com>
 */
public class PopCenter extends PointModel {

    public PopCenter(String name, int id, SettingMaster sm){
        super(id, sm);
    }
    
    @Override
    public void onTick(DataFrame last, DataFrame current) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onEvent(Event e) {
        if (e.name.equals("request_supplies")){
            Supplies retrieved = answerToRequest(e);
            ((TownStorage)(this.extensions.get("storehouse"))).Store(retrieved);
        }
        
        if (e.name.equals("receive_supplies")){
           Supplies received = new Supplies(Integer.parseInt(e.type), Integer.parseInt(e.value));
           ((TownStorage)(this.extensions.get("storehouse"))).Store(received);
        }
    }
    
    /**
     * Sends a request for supplies to the target PopCenter
     * @param s requested supplies
     * @param target who to bug
     */
    public void requestSupplies(Supplies s, PopCenter target){
        target.answerToRequest(new Event("request_supplies", s.id + "", s.amount + ""));
    }
    
    /**
     * Processes a supplies request event
     * @param e the event
     * @return Supplies sent back from delivery due to failure to deliver
     */
    public Supplies answerToRequest(Event e){
        Supplies sent = ((TownStorage)(this.extensions.get("storehouse"))).Take(Integer.parseInt(e.type), Integer.parseInt(e.value));
        return sendSupplies(sent, (PopCenter)(e.sender));
    }
    
    /**
     * Sends supplies from this PopCenter to another. The Transport model is queried for information on how the delivery went. (todo)
     * @param s Supplies to send
     * @param target PopCenter to send supplies to
     * @return Supplies sent back from delivery due to failure to deliver
     */
    public Supplies sendSupplies(Supplies s, PopCenter target){
        Supplies[] destroyed_and_delivered = new Supplies[]{new Supplies(s.id, 0), new Supplies(s.id, s.amount)}; //TODO: query supplies successfully transported during transit from transport
        Supplies destroyed = destroyed_and_delivered[0];
        Supplies delivered = destroyed_and_delivered[1];
        target.addEvent(new Event("receive_supplies", delivered.id + "", delivered.amount + ""), this);
        return new Supplies(s.id, s.amount - destroyed.amount);
    }
    
    
    @Override
    public void onRegisteration(GameManager gm, SettingMaster sm) {
        sm.setIcon(Icon.TOWN);
        sm.color = new Color(255, 128, 64);
        this.addExtension("storehouse", new TownStorage(1000, sm));
    }

    @Override
    public void onGenerateDefaults(DataFrame df) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
