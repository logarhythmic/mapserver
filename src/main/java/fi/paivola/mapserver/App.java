package fi.paivola.mapserver;

import fi.paivola.mapserver.core.Event;
import fi.paivola.mapserver.core.ExtensionModel;
import fi.paivola.mapserver.core.GameManager;
import fi.paivola.mapserver.core.GameThread;
import fi.paivola.mapserver.core.Model;
import fi.paivola.mapserver.core.SettingsParser;
import fi.paivola.mapserver.core.TestcaseRunner;
import fi.paivola.mapserver.core.WSServer;
import fi.paivola.mapserver.core.setting.*;
import fi.paivola.mapserver.utils.LatLng;
import fi.paivola.mapserver.core.setting.SettingMaster;
import fi.paivola.mapserver.models.ExampleGlobal;
import fi.paivola.mapserver.utils.CSVDumper;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import static java.lang.Integer.parseInt;
import java.net.UnknownHostException;
import java.util.logging.LogManager;
import org.json.simple.parser.ParseException;

public class App {

    static final boolean profilingRun = false;
    static DiagnosticsWrapper dw; // for the wrapping of stupid debug stuff

    public static void main(String[] args) throws UnknownHostException, IOException, ParseException, InterruptedException, Exception {
        InputStream stream = null;
        if (args.length > 0) {
            File file = new File(args[0]);
            stream = new FileInputStream(file);
            TestcaseRunner tr = new TestcaseRunner(stream);
            return;
        } else {
            stream = App.class.getClassLoader().getResourceAsStream("default_testcase.csv");
        }

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
                    case "f":
                        ws.stop();
                        TestcaseRunner tr = new TestcaseRunner(stream);
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
                + "f               - Run the TestcaseRunner\n"
                + "h|help          - Display this help");
    }

    /**
     * This function can be used for testing your own models. Please modify
     * this!
     */ 
    static void runTest() {
        dw = DiagnosticsWrapper.getInstance();

        // How many ticks? Each one is a week.
        int simulationDurationTicks = (int) Math.floor(Constants.WEEKS_IN_YEAR * 20);
        // print debug-info on all parameters moving between models
        boolean printFrameData = true;
        GameThread one = new GameThread(simulationDurationTicks, printFrameData);
        GameManager gm = one.game;
        
        dw.addGameThread(one); // for debugging purposes

        // globalit
        gm.createModel("Weather");

        // ruoka
        SettingMaster sm = one.game.getDefaultSM("Field");
        sm.settings.get("content").setValue("maize");

        // kaupungit
        Model Town1 = gm.createModel("PopCenter");
        Model Town2 = gm.createModel("PopCenter");
        Model Road1 = gm.createModel("Road");
        gm.linkModelsWith(Town1, Town2, Road1);

        // ruoka x kaupungit
        
        for(int i = 0; i < 500; i++){
            gm.linkModelsWith(gm.createModel("Field"), Town1, gm.createModel("GenericConnection"));
        }

        // water
        Model l1 = gm.createModel("Lake");
        sm = gm.getDefaultSM("Lake");
        sm.settings.get("order").setValue("1");
        sm.settings.get("k").setValue("1");
        sm.settings.get("surfaceArea").setValue("256120000f");
        sm.settings.get("depth").setValue("14.1");
        sm.settings.get("startAmount").setValue("0.9");
        sm.settings.get("flowAmount").setValue("0.91");
        sm.settings.get("basinArea").setValue("7642000000f");
        sm.settings.get("terrainCoefficient").setValue("0.5f");
        l1.onActualUpdateSettings(sm);
        
        Model l2 = gm.createModel("Lake");
        sm = gm.getDefaultSM("Lake");
        sm.settings.get("order").setValue("1");
        sm.settings.get("k").setValue("1");
        sm.settings.get("surfaceArea").setValue("256120000f");
        sm.settings.get("depth").setValue("14.1");
        sm.settings.get("startAmount").setValue("0.9");
        sm.settings.get("flowAmount").setValue("0.91");
        sm.settings.get("basinArea").setValue("7642000000f");
        sm.settings.get("terrainCoefficient").setValue("0.5f");
        l2.onActualUpdateSettings(sm);
        
        Model r1 = gm.createModel("River");
        sm = gm.getDefaultSM("River");
        sm.settings.get("order").setValue("2");
        sm.settings.get("width").setValue("100");
        sm.settings.get("length").setValue("100000");
        sm.settings.get("startDepth").setValue("0");
        sm.settings.get("floodDepth").setValue("10");;
        sm.settings.get("flowDepth").setValue("0.5");
        r1.onActualUpdateSettings(sm);
        
        Model r2 = gm.createModel("River");
        sm = gm.getDefaultSM("River");
        sm.settings.get("order").setValue("2");
        sm.settings.get("width").setValue("100");
        sm.settings.get("length").setValue("100000");
        sm.settings.get("startDepth").setValue("0");
        sm.settings.get("floodDepth").setValue("10");;
        sm.settings.get("flowDepth").setValue("0.5");
        r2.onActualUpdateSettings(sm);
        
        Model l3 = gm.createModel("Lake");
        sm = gm.getDefaultSM("Lake");
        sm.settings.get("order").setValue("3");
        sm.settings.get("k").setValue("1");
        sm.settings.get("surfaceArea").setValue("256120000f");
        sm.settings.get("depth").setValue("14.1");
        sm.settings.get("startAmount").setValue("0.9");
        sm.settings.get("flowAmount").setValue("0.91");
        sm.settings.get("basinArea").setValue("7642000000f");
        sm.settings.get("terrainCoefficient").setValue("0.5f");
        l3.onActualUpdateSettings(sm);
        
        Model r3 = gm.createModel("River");
        sm = gm.getDefaultSM("River");
        sm.settings.get("order").setValue("4");
        sm.settings.get("width").setValue("100");
        sm.settings.get("length").setValue("100000");
        sm.settings.get("startDepth").setValue("0");
        sm.settings.get("floodDepth").setValue("10");;
        sm.settings.get("flowDepth").setValue("0.5");
        r3.onActualUpdateSettings(sm);
        
        Model s1 = gm.createModel("Sea");
        sm = gm.getDefaultSM("Sea");
        sm.settings.get("order").setValue("5");
        s1.onActualUpdateSettings(sm);
        
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
