package fi.paivola.mapserver;

import fi.paivola.mapserver.core.Event;
import fi.paivola.mapserver.core.ExtensionModel;
import fi.paivola.mapserver.core.GameManager;
import fi.paivola.mapserver.core.GameThread;
import fi.paivola.mapserver.core.Model;
import fi.paivola.mapserver.core.SettingsParser;
import fi.paivola.mapserver.core.WSServer;
import fi.paivola.mapserver.core.setting.*;
import fi.paivola.mapserver.utils.LatLng;
import fi.paivola.mapserver.core.setting.SettingMaster;
import fi.paivola.mapserver.models.PopCenter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import static java.lang.Integer.parseInt;
import java.net.UnknownHostException;
import java.util.logging.LogManager;
import org.json.simple.parser.ParseException;

public class App {

    static final boolean profilingRun = false;

    public static void main(String[] args) throws UnknownHostException, IOException, ParseException, InterruptedException {

        SettingsParser.parse();

        if (profilingRun) { // For profiling

            LogManager.getLogManager().reset();

            for (int i = 0; i < 1000; i++) {
                runTest();
            }

        } else {

            WSServer ws = new WSServer(parseInt(SettingsParser.settings.get("websocket_port").toString()));
            ws.start();

            BufferedReader sysin = new BufferedReader(new InputStreamReader(System.in));
            printHelp();
            mainloop:
            while (true) {
                String in = sysin.readLine();
                switch (in) {
                    case "q":
                    case "quit":
                    case "e":
                    case "exit":
                        ws.stop();
                        break mainloop;
                    case "t":
                    case "test":
                        ws.stop();
                        runTest();
                        break mainloop;
                    case "h":
                    case "help":
                        printHelp();
                        break;
                    default:
                        System.out.println("Unknown command (" + in + ")");
                        printHelp();
                        break;
                }
            }
        }
    }

    static void printHelp() {
        System.out.println("q|e|quit|exit   - Quits the program\n"
                + "t|test          - Run the test function\n"
                + "h|help          - Display this help");
    }

    /**
     * This function can be used for testing your own models. Please modify
     * this!
     */
    static void runTest() {

        // How many ticks? Each one is a week.
        GameThread one = new GameThread((int) Math.floor(52.177457 * 10));
        GameManager gm = one.game;
        
        gm.createModel("Weather");

        SettingMaster sm = one.game.getDefaultSM("Field");
        sm.settings.get("content").setValue("maize");
        Model m = gm.createModel("Field");
        // Create and add
        Model town1 = gm.createModel("PopCenter");
        Model town2 = gm.createModel("PopCenter");
        Model town3 = gm.createModel("PopCenter");
        Model town4 = gm.createModel("PopCenter");
        Model town5 = gm.createModel("PopCenter");
        
        Model[] towns = new Model[]{town1,town2,town3,town4,town5};
        Model road12 = gm.createModel("Road");
        Model road23 = gm.createModel("Road");
        Model road34 = gm.createModel("Road");
        Model road45 = gm.createModel("Road");
        Model road51 = gm.createModel("Road");
        gm.linkModelsWith(town1, town2, road12);
        gm.linkModelsWith(town2, town3, road23);
        gm.linkModelsWith(town3, town4, road34);
        gm.linkModelsWith(town4, town5, road45);
        gm.linkModelsWith(town5, town1, road51);
        
        for(int i = 0; i < 5; i++){
            for(int j = i+1; j<5;j++){
                gm.linkModelsWith(towns[i], towns[j], gm.createModel("PhoneLine"));
            }
        }
        
        ((PopCenter)town1).DebugFoodSource = true;

        Model mg = gm.createModel("exampleGlobal");

        // This is how you change a "setting" from the code.
        SettingMaster sm2 = gm.getDefaultSM("exampleGlobal");
        sm2.settings.get("cats").setValue("2");
        mg.onActualUpdateSettings(sm2);

        int size = 30;
        
        Model[] points = new Model[size];
        Model[] conns = new Model[size];

        for (int i = 0; i < size; i++) {
            points[i] = gm.createModel("examplePoint");
            conns[i] = gm.createModel("exampleConnection");
        }

        for (int i = 0; i < size; i++) {
            if (i > 0) {
                gm.linkModelsWith(points[i - 1], points[i], conns[i - 1]);
            }
        }
        gm.linkModelsWith(points[size - 1], points[0], conns[size - 1]);

        Model mp0 = gm.createModel("Power plant");
        Model mc0 = gm.createModel("Power connection");
        Model mu0 = gm.createModel("Power user");
        Model mc1 = gm.createModel("Power connection");

        Model mn0 = gm.createModel("Power node");

        mn0.setLatLng(1, 0);
        mp0.setLatLng(1, 3);

        Model mc2 = gm.createModel("Power connection");
        Model mp1 = gm.createModel("Solar plant");

        Model mu1 = gm.createModel("Power user");
        Model mc3 = gm.createModel("Power connection");

        
        gm.linkModelsWith(mu0, mn0, mc0);
        gm.linkModelsWith(mp0, mn0, mc1);
        gm.linkModelsWith(mp1, mn0, mc2);
        gm.linkModelsWith(mu1, mn0, mc3);

        // Create and add
        Model l1 = gm.createModel("Lake");
        Model l2 = gm.createModel("Lake");
        Model r1 = gm.createModel("River");
        Model r2 = gm.createModel("River");
        Model l3 = gm.createModel("Lake");
        Model r3 = gm.createModel("River");
        Model s1 = gm.createModel("Sea");

        gm.linkModelsWith(l1, l3, r1);
        gm.linkModelsWith(l2, l3, r2);
        gm.linkModelsWith(l3, s1, r3);

        if (!profilingRun) {
            gm.printOnDone = 2;
        }

        // Start the gamethread
        one.start();
    }
}
