/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.kivibot.power.models;

import fi.paivola.mapserver.core.DataFrame;
import fi.paivola.mapserver.core.Event;
import fi.paivola.mapserver.core.GameManager;
import fi.paivola.mapserver.core.PointModel;
import fi.paivola.mapserver.core.setting.SettingMaster;
import fi.paivola.mapserver.utils.Color;
import fi.paivola.mapserver.utils.Icon;

/**
 *
 * @author kivi
 */
public class PowerPlant extends PointModel {

    @Override
    public void onTick(DataFrame last, DataFrame current) {
    }

    @Override
    public void onEvent(Event e) {
    }

    @Override
    public void onRegisteration(GameManager gm, SettingMaster sm) {
        sm.setIcon(Icon.CAPE);
        sm.color = new Color(0, 0, 0);
    }

    @Override
    public void onGenerateDefaults(DataFrame df) {
    }

}
