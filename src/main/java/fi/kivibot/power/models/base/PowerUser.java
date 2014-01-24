package fi.kivibot.power.models.base;

import fi.kivibot.power.misc.EU;
import fi.kivibot.power.misc.PowerSourceInfo;
import fi.paivola.mapserver.core.ConnectionModel;
import fi.paivola.mapserver.core.DataFrame;
import fi.paivola.mapserver.core.Event;
import fi.paivola.mapserver.core.GameManager;
import fi.paivola.mapserver.core.Model;
import fi.paivola.mapserver.core.PointModel;
import fi.paivola.mapserver.core.setting.SettingMaster;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 *
 * @author kivi
 */
public abstract class PowerUser extends PointModel {

    public PowerUser(int id) {
        super(id);
        this.name = "Power user";
        this.saveDouble("usage", 5);
    }

    @Override
    public void onTick(DataFrame last, DataFrame current) {
        getPower(this.findSources(), last, this);
    }

    @Override
    public void onEvent(Event e, DataFrame current) {
    }

    @Override
    public void onRegisteration(GameManager gm, SettingMaster sm) {
    }

    @Override
    public void onGenerateDefaults(DataFrame df) {
        this.saveDouble("power", 0);
    }

    @Override
    public void onUpdateSettings(SettingMaster sm) {
    }

    /**
     * Threadsafe
     *
     * @param l
     */
    private static synchronized void getPower(List<PowerSourceInfo> l, DataFrame df, Model th) {
        double ce = 0;
        l = sort_loss(l);

        for (PowerSourceInfo psi : l) {
            if (ce >= th.getDouble("usage")) {
                break;
            }
            double a = th.getDouble("usage") - ce;
            double loss = psi.getLoss();
            double req = a / (1 - loss);

            Model m = psi.getPowerPlant();

            EU eu = EU.getEU(df, m);

            double cpm = eu.get();

            double sub = Math.min(cpm, req);

            eu.set(cpm - sub);
            ce += sub * (1.0 - loss);

        }
        th.saveDouble("power", ce / th.getDouble("usage"));
    }

    private static List<PowerSourceInfo> sort_loss(List<PowerSourceInfo> l) {
        Collections.sort(l, new Comparator() {

            @Override
            public int compare(Object o1, Object o2) {
                double a = ((PowerSourceInfo) o1).getLoss();
                double b = ((PowerSourceInfo) o2).getLoss();
                return a == b ? 0 : a < b ? 1 : -1;
            }
        });

        return l;
    }

    private List<PowerSourceInfo> findSources() {
        List<PowerSourceInfo> sources = new LinkedList<>();
        List<Model> visited = new LinkedList<>();
        Queue<PowerSourceInfo> next = new LinkedList<>();

        for (Model m : this.connections) {
            if (m.name.equals("Power connection")) {
                Model mo = getOther((ConnectionModel) m, this);
                double d = this.distanceTo(mo);
                next.add(new PowerSourceInfo(mo, d, d * m.getDouble("losspkm")));
            }
        }
        PowerSourceInfo psi;
        while ((psi = next.poll()) != null) {
            Model m = psi.getPowerPlant();
            if (!visited.contains(m)) {
                visited.add(m);
                if (m.name.equals("Power plant")) {
                    sources.add(psi);
                } else if (m.name.equals("Power node")) {
                    for (Model m2 : m.connections) {
                        if (m2.name.equals("Power connection")) {
                            Model mo = getOther((ConnectionModel) m2, m);
                            double d = m.distanceTo(mo);
                            next.add(new PowerSourceInfo(mo, psi.getDist() + d, psi.getLoss() + (d * m2.getDouble("losspkm"))));
                        }
                    }
                }
            }
        }

        return sources;
    }

    private Model getOther(ConnectionModel cm, Model not) {
        for (Model m : cm.connections) {
            if (!m.equals(not)) {
                return m;
            }
        }
        return null;
    }
    
}
