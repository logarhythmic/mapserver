/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.paivola.developmentaid;
import fi.paivola.mapserver.core.DataFrame;
import fi.paivola.mapserver.core.Event;
import fi.paivola.mapserver.core.GameManager;
import fi.paivola.mapserver.core.GlobalModel;
import fi.paivola.mapserver.core.setting.SettingMaster;
/**
 *
 * @author Shaqqy
 */
public class DevelopmentAid extends GlobalModel{
public DevelopmentAid(int id){
    super (id);
    
    public double getAid(int week){
        return (double) Math.round(kehitysapu(i)); // i viikon numero
    }
}
    @Override
    public void onTick(DataFrame last, DataFrame current) {
         //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onEvent(Event e, DataFrame current) {
        //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onRegisteration(GameManager gm, SettingMaster sm) {
        //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onGenerateDefaults(DataFrame df) {
         //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onUpdateSettings(SettingMaster sm) {
         //To change body of generated methods, choose Tools | Templates.
    }
    
}
