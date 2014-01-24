package fi.kivibot.power.utils;

import fi.paivola.mapserver.core.GameManager;
import fi.paivola.mapserver.core.Model;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author kivi
 */
public class CSVDumper {

    public static final String csv_separator = ";";
    public static final String decimal_separator = ",";

    private class asd {

        private Model m;
        private String s;
        private String o;

        public asd(Model mo, String st) {
            m = mo;
            s = st;
            o = m.id + ",\t" + s + ",\t";
        }

        @Override
        public String toString() {
            return o;
        }

    }

    private final List<asd> lines = new LinkedList<>();
    private PrintWriter out = null;

    public CSVDumper() {
        try {
            out = new PrintWriter(new FileWriter(new File("dump.csv")));
        } catch (IOException ex) {
            Logger.getLogger(CSVDumper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * @param m The model to find
     * @param prop The name of the property such as "production"
     */
    public void add(Model m, String prop) {
        lines.add(new asd(m, prop));
    }

    /**
     * Saves the data from <b>gm</b> to dump.csv
     *
     * @param gm Current GameManager
     */
    public void save(GameManager gm) {
        this.saveHead();
        Map<String, String[]> map = gm.getData();

        for (int i = 0; i < map.size(); i++) {
            saveLine(map.get(i + ""));
        }
        out.flush();
    }

    private void saveHead() {
        List<String> data = new LinkedList<>();
        for (asd a : lines) {
            String s = a.toString();
            data.add(s.split(",\t")[0] + "-" + s.split(",\t")[1]);
        }
        write(data.toArray());
    }

    private void write(Object[] ss) {
        for (Object s : ss) {
            out.print((s + csv_separator).replace(".", decimal_separator));
        }
        out.println();
    }

    private void saveLine(String[] ss) {
        List<String> data = new LinkedList<>();
        for (String s : ss) {
            for (asd a : lines) {
                String f = a.toString();
                if (s.contains(f)) {
                    data.add(s.split(",\t")[2]);
                }
            }
        }
        write(data.toArray());
    }

}
