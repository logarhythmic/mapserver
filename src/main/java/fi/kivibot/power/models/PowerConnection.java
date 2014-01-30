/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package fi.kivibot.power.models;

import fi.paivola.mapserver.core.ConnectionModel;
import fi.paivola.mapserver.core.DataFrame;
import fi.paivola.mapserver.core.Event;
import fi.paivola.mapserver.core.GameManager;
import fi.paivola.mapserver.core.Model;
import fi.paivola.mapserver.core.setting.SettingMaster;
import fi.paivola.mapserver.utils.LatLng;

/**
 *
 * @author Nicklas Ahlskog
 */
public class PowerConnection extends ConnectionModel {

    private double ala, alo, bla, blo, le;
    private double olb;

    public PowerConnection(int id) {
        super(id);
        this.passthrough = true;
        this.name = "Power connection";
        this.maxConnections = 2;

        this.saveDouble("losspkm", 0.000035);
    }

    @Override
    public void onTick(DataFrame last, DataFrame current) {
        olb = this.getDouble("losspkm");
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

    @Override
    public void onUpdateSettings(SettingMaster sm) {
    }

    public double lenC() {
        LatLng a = connections.get(0).getLatLng();
        LatLng b = connections.get(1).getLatLng();
        if (ala != a.latitude || alo != a.longitude || bla != b.latitude || blo != b.longitude) {
            ala = a.latitude;
            alo = a.longitude;
            bla = b.latitude;
            blo = b.longitude;
            le = a.distanceTo(b);
        }
        return le;
    }

    public double lossC() {
        double d = lenC();
        return d * olb;
    }

}
