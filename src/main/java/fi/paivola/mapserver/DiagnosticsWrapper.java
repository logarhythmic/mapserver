package fi.paivola.mapserver;

import fi.paivola.mapserver.core.GameThread;
import java.util.ArrayList;

/**
 * Wrapper-singleton for making simulation-/model-diagnostics easier to use.
 * @author hegza
 */
public class DiagnosticsWrapper {
    private static DiagnosticsWrapper   instance = null;
    private int                         tbi = -1; // thread being inspected
    private ArrayList<GameThread>       gameThreads = new ArrayList<GameThread>(); // gamethreads with diagnostics

    protected DiagnosticsWrapper() {
    }
    
    public static DiagnosticsWrapper getInstance() {
        if(instance == null) {
            instance = new DiagnosticsWrapper();
        }
        return instance;
    }
    
    public void addGameThread(GameThread gameThread) {
        gameThreads.add(gameThread);
        if (tbi == -1) {
            tbi = gameThreads.size()-1;
        }
    }
    
    public void printDiagnostics() {
        if (usable()) {
            System.out.println("DiagnosticsWrapper:OUTPUT");
            System.out.println("DiagnosticsWrapper:END");
        } else {
            System.out.println("DiagnosticsWrapper:NOT_USABLE");
            System.out.println("DiagnosticsWrapper is unable to print debug-data");
        }
    }
    
    private boolean usable() {
        return (tbi != -1) && (gameThreads.size() != 0);
    }
}
