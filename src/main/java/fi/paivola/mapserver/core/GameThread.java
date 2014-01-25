package fi.paivola.mapserver.core;

import fi.paivola.mapserver.DiagnosticsWrapper;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author juhani
 */
public class GameThread extends Thread {

    public GameManager game;
    private final static Logger log = Logger.getLogger("mapserver");
    public Thread thread;
    public static int threads = 0;

    public GameThread(int ticks, boolean printFrameData) {
        log.log(Level.INFO, "GameThread init @{0}", Thread.currentThread().getName());
        this.game = new GameManager(ticks, printFrameData);
        this.thread = new Thread(this, "thread#" + (threads++));
    }
    
    @Override
    public void start() {
        this.thread.start();
    }

    @Override
    public void run() {
        log.log(Level.INFO, "GameThread starting run @{0}", Thread.currentThread().getName());
        long startTime = System.nanoTime();
        this.game.stepTrough();
        DiagnosticsWrapper.getInstance().printDiagnostics();
        long endTime = System.nanoTime();
        log.log(Level.INFO, "GameThread ended run @{0} in {1} seconds", new String[]{Thread.currentThread().getName(), "" + (endTime - startTime) / 1000000000.0});
    }
}
