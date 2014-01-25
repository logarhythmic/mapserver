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
import java.util.Random;
/**
 *
 * @author Shaqqy
 */
public class DevelopmentAid extends GlobalModel{
public DevelopmentAid(int id){
    super (id);

}
    @Override
    public void onTick(DataFrame last, DataFrame current) {
         current.saveGlobalData("Kehitysapu", Kehitysapu.KehitysAvunLaskenta(current.index,662000000));
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
         df.saveGlobalData("Kehitysapu", 662000000);
    }

    @Override
    public void onUpdateSettings(SettingMaster sm) {
         //To change body of generated methods, choose Tools | Templates.
    }

    private static class Kehitysapu {

            public static Object KehitysAvunLaskenta(int index,double alkuApu ){
                Random random=new Random();
                
		double kehitysapu = alkuApu;
		double xx = 0;
		double x = 0.5;
		int onnettomuudet = 0;
		double inflaatio = 2.0;
		int konflikti = 2;
		int kesto = 0;
		double ilmastonmuutos = 0.01/52;
		double nalanhata = 0.001/52;
		double z = 0.9/52;
		double bktkasvukerroin = 0.00001913/52;
		double bktlaskukerroin = 0.00001838/52;
		
		for(int i=0;i<index;i++) {
			double randomi = random.nextDouble();			
			if ((konflikti == 1) && (z > randomi)){
				kehitysapu = kehitysapu + ((kehitysapu*2)/52);
				onnettomuudet = onnettomuudet + 1;
				konflikti = 2;
				z = 0.6/52;
				kesto = kesto + 1;
				}
			else if (konflikti == 2){
				if (randomi > z){
					konflikti = 1;
					kehitysapu = kehitysapu-(kehitysapu / (2*52));
					z = 0.1/52;
					}
				}
			if (x >= randomi){
				kehitysapu = kehitysapu + ((kehitysapu*bktkasvukerroin)/52);
				xx = x;
				x = x - 0.1;
				}	
			else if (x < randomi){
				kehitysapu = kehitysapu - (kehitysapu*bktlaskukerroin)/52;
				xx = x;
				x = x + 0.1;
				}	
			if (inflaatio > 2.0){				
				inflaatio = inflaatio * randomi;
				kehitysapu = kehitysapu - (kehitysapu*0.01*inflaatio)/52;
				}	
			else if (inflaatio <= 2.0){
				inflaatio = inflaatio + randomi;
				}
			else if(true){
				nalanhata = nalanhata + ilmastonmuutos;
				if (nalanhata > randomi){
					kehitysapu = kehitysapu + ((kehitysapu*0.2)/52);
					}
				}
			//else if(1==0){
//				bktkasvukerroin = bktkasvukerroin - taloudenmuutoskerroin;
//				bktlaskukerroin = bktlaskukerroin - taloudenmuutoskerrroin;
//				taloudenmuutoskerroin = Math.pow(taloudenmuutoskerroin, (1+taloudenmuutoskerroin));
		
                
		}
                System.out.println(kehitysapu);
                return (kehitysapu);
    }
    }
}
    
