/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.kivibot.power.models;

import fi.paivola.mapserver.core.ConnectionModel;
import fi.paivola.mapserver.core.DataFrame;
import fi.paivola.mapserver.core.Event;
import fi.paivola.mapserver.core.GameManager;
import fi.paivola.mapserver.core.setting.SettingMaster;

/**
 *
 * @author kivi
 */
public class PowerConnection extends ConnectionModel{

    public PowerConnection(int id, SettingMaster sm){
        super(id,sm);
        this.passtrough = true;
        this.type = "Power connection";
        this.maxConnections = Integer.MAX_VALUE;
    }
    
    public PowerConnection(){
        super();
        this.passtrough = true;
        this.type = "Power connection";
        this.maxConnections = Integer.MAX_VALUE;
    }
    
    @Override
    public void onTick(DataFrame last, DataFrame current) {
    }

    @Override
    public void onEvent(Event e, DataFrame current) {
    }

    @Override
    public void onRegisteration(GameManager gm, SettingMaster sm) {
    }

    @Override
    public void onGenerateDefaults(DataFrame df) {
    }
    
}
