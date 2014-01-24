package fi.paivola.mapserver;

import fi.paivola.mapserver.core.Event;
import fi.paivola.mapserver.core.ExtensionModel;
import fi.paivola.mapserver.core.GameManager;
import fi.paivola.mapserver.core.GameThread;
import fi.paivola.mapserver.core.Model;
import fi.paivola.mapserver.core.SettingsParser;
import fi.paivola.mapserver.core.WSServer;
import fi.paivola.mapserver.core.setting.SettingMaster;
import fi.paivola.mapserver.models.PopCenter;
import fi.paivola.mapserver.models.TownStorage;
import fi.paivola.mapserver.utils.Supplies;
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
        GameThread one = new GameThread((int) Math.floor(52.177457 * 20));
        GameManager gm = one.game;

        SettingMaster sm = gm.getDefaultSM("exampleGlobal");
        
        // Create and add
        Model town1 = gm.createModel("PopCenter");
        ExtensionModel s1 = (ExtensionModel) gm.createModel("TownStorage");
        town1.addExtension("storehouse", s1);
        Model town2 = gm.createModel("PopCenter");
        ExtensionModel s2 = (ExtensionModel) gm.createModel("TownStorage");
        town2.addExtension("storehouse", s2);
        Model town3 = gm.createModel("PopCenter");
        ExtensionModel s3 = (ExtensionModel) gm.createModel("TownStorage");
        town3.addExtension("storehouse", s3);
        Model town4 = gm.createModel("PopCenter");
        ExtensionModel s4 = (ExtensionModel) gm.createModel("TownStorage");
        town4.addExtension("storehouse", s4);
        Model town5 = gm.createModel("PopCenter");
        ExtensionModel s5 = (ExtensionModel) gm.createModel("TownStorage");
        town5.addExtension("storehouse", s5);
        
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
        
        ((PopCenter)town1).DebugFoodSource = true;
        
        // Print final data in the end?
        if (!profilingRun) {
            gm.printOnDone = 2;
        }

        // Start the gamethread
        one.start();
    }
}
