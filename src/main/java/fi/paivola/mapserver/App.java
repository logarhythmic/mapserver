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
        GameThread one = new GameThread((int) Math.floor(52.177457 * 1));
        GameManager gm = one.game;

        // globalit
        gm.createModel("Weather");

        // ruoka
        SettingMaster sm = one.game.getDefaultSM("Field");
        sm.settings.get("content").setValue("maize");
        Model Field1 = gm.createModel("Field");

        // kaupungit
        Model Town1 = gm.createModel("PopCenter");
        Model Town2 = gm.createModel("PopCenter");
        Model Road1 = gm.createModel("Road");
        gm.linkModelsWith(Town1, Town2, Road1);
        
        // ruoka x kaupungit
        Model Road2 = gm.createModel("Road");
        gm.linkModelsWith(Field1, Town1, Road2);

        // power
        Model PP1 = gm.createModel("Power plant");
        Model PC1 = gm.createModel("Power connection");
        Model PU1 = gm.createModel("Power user");
        PP1.setLatLng(1, 0);
        PU1.setLatLng(1, 3);
        gm.linkModelsWith(PP1, PU1, PC1);

        // water
        Model Lake1 = gm.createModel("Lake");
        Model Lake2 = gm.createModel("Lake");
        Model River1 = gm.createModel("River");
        Model River2 = gm.createModel("River");
        Model Sea1 = gm.createModel("Sea");
        gm.linkModelsWith(Lake1, Lake2, River1);
        gm.linkModelsWith(Lake2, Sea1, River2);

        if (!profilingRun) {
            gm.printOnDone = 2;
        }

        // Start the gamethread
        one.start();
    }
}
