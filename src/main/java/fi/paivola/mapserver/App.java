package fi.paivola.mapserver;

import fi.kivibot.power.utils.CSVDumper;
import fi.paivola.mapserver.core.GameManager;
import fi.paivola.mapserver.core.GameThread;
import fi.paivola.mapserver.core.Model;
import fi.paivola.mapserver.core.SettingsParser;
import fi.paivola.mapserver.core.WSServer;
import fi.paivola.mapserver.core.setting.SettingMaster;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import static java.lang.Integer.parseInt;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
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
        GameThread one = new GameThread((int) Math.floor(52.177457 * 3));
        GameManager gm = one.game;

        Model mp0 = gm.createModel("Solar plant");
        Model mc0 = gm.createModel("Power connection");
        Model mu0 = gm.createModel("Power user");
        Model mc1 = gm.createModel("Power connection");

        Model mn0 = gm.createModel("Power node");

        mn0.setLatLng(1, 0);

        mp0.setLatLng(1, 3);

        Model mc2 = gm.createModel("Power connection");
        Model mp1 = gm.createModel("Aggregate");

        Model mu1 = gm.createModel("Power user");
        Model mc3 = gm.createModel("Power connection");

        gm.linkModelsWith(mu0, mn0, mc0);
        gm.linkModelsWith(mp0, mn0, mc1);
        gm.linkModelsWith(mp1, mn0, mc2);
        gm.linkModelsWith(mu1, mn0, mc3);

        if (!profilingRun) {
            gm.printOnDone = 2;
        }

        // Start the gamethread
        one.start();
        
        while(!gm.isReady()){
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        CSVDumper d = new CSVDumper();
        d.add(mp0, "production");
        d.add(mu0, "power");
        d.save(gm);
    }
}
