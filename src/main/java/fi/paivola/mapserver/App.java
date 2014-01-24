package fi.paivola.mapserver;

import fi.paivola.mapserver.core.GameManager;
import fi.paivola.mapserver.core.GameThread;
import fi.paivola.mapserver.core.Model;
import fi.paivola.mapserver.core.SettingsParser;
import fi.paivola.mapserver.core.WSServer;
import fi.paivola.mapserver.core.setting.*;
import fi.paivola.mapserver.utils.LatLng;
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
        GameThread one = new GameThread((int) Math.floor(52.177457 * 10)); // ten years
        GameManager gm = one.game;
        
        gm.createModel("Weather");

        SettingMaster sm = one.game.getDefaultSM("Field");
        sm.settings.get("content").setValue("maize");
        Model m = gm.createModel("Field");
        // Create and add

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
        Model mp1 = gm.createModel("Power plant");

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
