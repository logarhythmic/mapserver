package fi.paivola.mapserver;

import fi.paivola.mapserver.core.GameThread;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Wrapper-singleton for making simulation-/model-diagnostics easier to use.
 * @author hegza
 */
public class DiagnosticsWrapper {
    private static DiagnosticsWrapper   instance = null;
    private int                         tbi = -1; // thread being inspected
    private ArrayList<GameThread>       gameThreads = new ArrayList<GameThread>(); // gamethreads with diagnostics
    private HashMap<String, Double>     watchValues = new HashMap<String, Double>() {};
    private boolean                     debugOutput = false;
    
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
    
    public void createWatchDouble(String key, Double initialValue) {
        watchValues.put(key, initialValue);
    }
    
    public void setWatchDouble(String key, Double value) {
        if (watchValues.get(key) != value) watchValues.put(key, value);
    }
    
    public Double getWatchDouble(String key) {
        return watchValues.get(key);
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
    
    public void println(String line) {
        if (debugOutput) System.out.println(line);
    }
    
    public void setDebugOutput(boolean on) {
        debugOutput = on;
    }
            
    private boolean usable() {
        return (tbi != -1) && (gameThreads.size() != 0);
    }
}
