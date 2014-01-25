package fi.paivola.mapserver;

import fi.paivola.mapserver.core.GameManager;
import java.util.ArrayList;
import java.util.HashMap;

public class TestCase {
    public class ConnectionMeta {
        public String left;
        public String right;
        public String modelType;
    }
    private HashMap<String, String>     models;
    private ArrayList<ConnectionMeta>   connections;
    
    public TestCase() {
        models = new HashMap<String, String>();
        connections = new ArrayList<ConnectionMeta>();
    }
    
    /**
     * @param modelType Check modelType from settings.csv
     */
    public void addModel(String identifier, String modelType) {
        models.put(identifier, modelType);
    }
    
    /**
     * Connects two models with a connection of the specified type
     * @param connectionType Check from settings.csv
     * @param modelA 
     * @param modelB 
     */
    public void addConnection(String connectionType, String modelA, String modelB) {
        ConnectionMeta conn = new ConnectionMeta();
        conn.modelType = connectionType;
        conn.left = modelA;
        conn.right = modelB;
        connections.add(conn);
    }
    
    /**
     * Uses the GameManager to create the test-case.
     * @param gameManager
     * @return success
     */
    public boolean generateTestCase(GameManager gameManager) {
        // TODO: iterate through models and connections and use gameManager to
        // create context for simulation.
        return false;
    }
}
