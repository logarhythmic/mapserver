/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.paivola.developmentaid;
import java.util.Random;
import fi.paivola.mapserver.core.DataFrame;
import fi.paivola.mapserver.core.Event;
import fi.paivola.mapserver.core.GameManager;
import fi.paivola.mapserver.core.GlobalModel;
import fi.paivola.mapserver.core.setting.SettingMaster;

/**
 *
 * @author Shaqqy
 */
public class Oil extends GlobalModel{
    public Oil(int id){
        super(id);
    }

    @Override
    public void onTick(DataFrame last, DataFrame current) {
        current.saveGlobalData("Oil Price", Oljy.oljy(current.index));
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
        df.saveGlobalData("Oil Price", 60);
         //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onUpdateSettings(SettingMaster sm) {
         //To change body of generated methods, choose Tools | Templates.
    }
    private static class Oljy{
       public static Object oljy(int index){
        Random random=new Random();
        float öljy=60; 
        for (int i=0; i<index;i++){    
            int öljykriisi=random.nextInt(520);
            int nousulasku=random.nextInt(2);
            double lol=random.nextDouble();
            
    
            if (öljykriisi == 0){
                nousulasku=3;
                }
            if (nousulasku==0){
                öljy += (lol*4);
                }
            else if(nousulasku==1){
                öljy += (lol*(-4));
                }
            else{
                öljy += (lol*6);
                }

        }
    System.out.println(öljy);
    return öljy;   
    
    }
    }
    
}
