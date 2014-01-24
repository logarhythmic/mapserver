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
 * @author Allan Palmu <allan.palmu@gmail.com>
 */
public class PhoneLine extends ConnectionModel {

    public PhoneLine(int id){
        super(id);
    }

    @Override
    public void onTick(DataFrame last, DataFrame current) {
        //nothing
    }

    @Override
    public void onEvent(Event e, DataFrame current) {
        //nothing
    }

    @Override
    public void onRegisteration(GameManager gm, SettingMaster sm) {
        //nothing
    }

    @Override
    public void onGenerateDefaults(DataFrame df) {
        //nothing
    }

    @Override
    public void onUpdateSettings(SettingMaster sm) {
        //nothing
    }
}
