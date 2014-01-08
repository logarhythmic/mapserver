package fi.paivola.mapserver;

import fi.paivola.mapserver.core.GameManager;
import fi.paivola.mapserver.core.GameThread;
import fi.paivola.mapserver.core.Model;
import fi.paivola.mapserver.core.SettingsParser;
import fi.paivola.mapserver.core.WSServer;
import fi.paivola.mapserver.core.setting.SettingMaster;
import fi.paivola.mapserver.utils.LatLng;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import static java.lang.Integer.parseInt;
import java.net.UnknownHostException;
import org.json.simple.parser.ParseException;

public class App {

    public static void main(String[] args) throws UnknownHostException, IOException, ParseException, InterruptedException {
        /*
        SettingsParser sp = new SettingsParser();
        
        WSServer ws = new WSServer(parseInt(SettingsParser.settings.get("websocket_port").toString()));
        ws.start();
        
        
        BufferedReader sysin = new BufferedReader( new InputStreamReader( System.in ) );
        while(true) {
            String in = sysin.readLine();
            if(in.equals("q")) {
                ws.stop();
                break;
            }
        }
        
        */
        GameThread one = new GameThread(100);
        GameManager gm = one.game;
        
        //Model mg = gm.createModel("asdGlobal",null);
        //gm.addModel(mg, "asdGlobal");

        Model m1 = gm.createModel("Power plant",new SettingMaster());
        gm.addModel(m1, "Power plant");
        
        Model m2 = gm.createModel("Power connection",new SettingMaster());
        gm.addModel(m2, "Power connection");
        
        Model m3 = gm.createModel("Power user",new SettingMaster());
        gm.addModel(m3, "Power user");
        
        gm.linkModels(m3, m2);
        gm.linkModels(m2, m1);
        
        
        /*
        Model m2 = gm.createModel("asdConnection",null);
        gm.addModel(m2, "asdConnection");
        Model m3 = gm.createModel("asd",null);
        gm.addModel(m3, "asd");
        Model m4 = gm.createModel("asdConnection",null);
        gm.addModel(m4, "asdConnection");
        Model m5 = gm.createModel("asd",null);
        gm.addModel(m5, "asd");
        Model m6 = gm.createModel("asdConnection",null);
        gm.addModel(m6, "asdConnection");

        gm.linkModels(m1, m2);
        gm.linkModels(m2, m3);
        gm.linkModels(m3, m4);
        gm.linkModels(m4, m5);
        gm.linkModels(m5, m6);
        gm.linkModels(m6, m1);*/
        
        one.start();
    }
}
