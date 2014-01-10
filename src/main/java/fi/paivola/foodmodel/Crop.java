package fi.paivola.foodmodel;

import fi.paivola.mapserver.core.DataFrame;
import fi.paivola.mapserver.core.setting.*;
import fi.paivola.mapserver.utils.*;
import java.util.Map;
/**
 * @author Jaakko Hannikainen
 * Generic class for field products, eg. maize.
 */

public abstract class Crop extends Edible {

    private double waterMinimum;
    private double waterOptimal;
    private double waterMaximum;
    private double temperatureMinimum;
    private double temperatureOptimal;
    private double temperatureMaximum;
    private double sunlightMinimum;
    private double sunlightOptimal;
    private double sunlightMaximum;
    private double phMinimum;
    private double phOptimal;
    private double phMaximum;
    private int growTime;
    private int currentGrowTime;
    private double currentIndexMultiplier;
    private Distribution waterDistribution;
    private Distribution temperatureDistribution;
    private Distribution sunlightDistribution;
    private Distribution phDistribution;

    public Crop(SettingMaster sm, String name, double wmin, double wopt, double wmax,
            double tmin, double topt, double tmax, double smin, double sopt,
            double smax, double phmin, double phopt, double phmax, int gtime) {
        super(sm, name);
        
        RangeDouble r = new RangeDouble(0, Double.MAX_VALUE);
        RangeInt i = new RangeInt(0, Integer.MAX_VALUE);
        
        this.name = name;
        waterMinimum = wmin;
        waterOptimal = wopt;
        waterMaximum = wmax;
        temperatureMinimum = tmin;
        temperatureOptimal = topt;
        temperatureMaximum = tmax;
        sunlightMinimum = smin;
        sunlightOptimal = sopt;
        sunlightMaximum = smax;
        phMinimum = phmin;
        phOptimal = phopt;
        phMaximum = phmax;
        growTime = gtime;
        
        this.sm.settings.put("name", new SettingString("name", this.name));
        this.sm.settings.put("minwater", new SettingDouble("water minimum"
                + "(cm/week)", getWaterMinimum(), r));
        this.sm.settings.put("optwater", new SettingDouble("water optimum"
                + "(cm/week)", getWaterOptimal(), r));
        this.sm.settings.put("maxwater", new SettingDouble("water maximum"
                + "(cm/week)", getWaterMaximum(), r));
        this.sm.settings.put("mintemp", new SettingDouble("minimum average "
                + "temperature (C)", getTemperatureMinimum(), r));
        this.sm.settings.put("opttemp", new SettingDouble("optimal average "
                + "temperature (C)", getTemperatureOptimal(), r));
        this.sm.settings.put("maxtemp", new SettingDouble("maximum average "
                + "temperature (C)", getTemperatureMaximum(), r));
        this.sm.settings.put("minsun", new SettingDouble("minimum sunlight "
                + "(hours / day)", getSunlightMinimum(), r));
        this.sm.settings.put("optsun", new SettingDouble("optimal sunlight "
                + "(hours / day)", getSunlightOptimal(), r));
        this.sm.settings.put("maxsun", new SettingDouble("maximum sunlight "
                + "(hours / day)", getSunlightMaximum(), r));
        this.sm.settings.put("minph", new SettingDouble("minimum ph",
                    getPHMinimum(), r));
        this.sm.settings.put("optph", new SettingDouble("optimal ph", 
                    getPHOptimal(), r));
        this.sm.settings.put("maxph", new SettingDouble("maximum ph",
                    getPHMaximum(), r));
        this.sm.settings.put("growtime", new SettingInt("growing time",
                    getGrowTime(), i));

        this.waterDistribution = new Distribution(getWaterMinimum(),
                                                  getWaterOptimal(),
                                                  getWaterMaximum());
        this.temperatureDistribution = new Distribution(getTemperatureMinimum(),
                                                  getTemperatureOptimal(),
                                                  getTemperatureMaximum());
        this.sunlightDistribution = new Distribution(getSunlightMinimum(),
                                                  getSunlightOptimal(),
                                                  getSunlightMaximum());
        this.phDistribution = new Distribution(getPHMinimum(),
                                                  getPHOptimal(),
                                                  getPHMaximum());
    }

    @Override
    public double onTick(DataFrame last) {
        if(this.getCurrentGrowTime() == this.getGrowTime()) {
            return this.getCurrentIndexMultiplier() / this.getCurrentGrowTime()
                    * this.getArea() * this.getMaxYield();
        }
        this.setCurrentGrowTime(this.getCurrentGrowTime() + 1);
        this.setCurrentIndexMultiplier(this.getCurrentIndexMultiplier() +
                this.getWaterIndex(last) * this.getTemperatureIndex(last)
                    * this.getSunshineIndex(last) * this.getPHIndex(last));
        return 0;
    }
   
    // These need data from Weather... assuming random weather for now.
    private double getWaterIndex(DataFrame last) {
        return waterDistribution.random();
        //throw new UnsupportedOperationException("Not supported yet.");
    }
    
    private double getTemperatureIndex(DataFrame last) {
        return temperatureDistribution.random();
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    private double getSunshineIndex(DataFrame last) {
        return sunlightDistribution.random();
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    private double getPHIndex(DataFrame last) {
        return phDistribution.random();
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * @return the area
     */

    private double getArea() {
        return Double.parseDouble(this.sm.settings.get("area").getValue());
    }

    /* XXX: arbitary value */
    private double getMaxYield() {
        return 1;
    }

    /**
     * @return the waterMinimum
     */
    public final double getWaterMinimum() {
        return waterMinimum;
    }

    /**
     * @param waterMinimum the waterMinimum to set
     */
    public final void setWaterMinimum(double waterMinimum) {
        this.waterMinimum = waterMinimum;
    }

    /**
     * @return the waterOptimal
     */
    public final double getWaterOptimal() {
        return waterOptimal;
    }

    /**
     * @param waterOptimal the waterOptimal to set
     */
    public final void setWaterOptimal(double waterOptimal) {
        this.waterOptimal = waterOptimal;
    }

    /**
     * @return the waterMaximum
     */
    public final double getWaterMaximum() {
        return waterMaximum;
    }

    /**
     * @param waterMaximum the waterMaximum to set
     */
    public final void setWaterMaximum(double waterMaximum) {
        this.waterMaximum = waterMaximum;
    }

    /**
     * @return the temperatureMinimum
     */
    public final double getTemperatureMinimum() {
        return temperatureMinimum;
    }

    /**
     * @param temperatureMinimum the temperatureMinimum to set
     */
    public final void setTemperatureMinimum(double temperatureMinimum) {
        this.temperatureMinimum = temperatureMinimum;
    }

    /**
     * @return the temperatureOptimal
     */
    public final double getTemperatureOptimal() {
        return temperatureOptimal;
    }

    /**
     * @param temperatureOptimal the temperatureOptimal to set
     */
    public final void setTemperatureOptimal(double temperatureOptimal) {
        this.temperatureOptimal = temperatureOptimal;
    }

    /**
     * @return the temperatureMaximum
     */
    public final double getTemperatureMaximum() {
        return temperatureMaximum;
    }

    /**
     * @param temperatureMaximum the temperatureMaximum to set
     */
    public final void setTemperatureMaximum(double temperatureMaximum) {
        this.temperatureMaximum = temperatureMaximum;
    }

    /**
     * @return the sunlightMinimum
     */
    public final double getSunlightMinimum() {
        return sunlightMinimum;
    }

    /**
     * @param sunlightMinimum the sunlightMinimum to set
     */
    public final void setSunlightMinimum(double sunlightMinimum) {
        this.sunlightMinimum = sunlightMinimum;
    }

    /**
     * @return the sunlightOptimal
     */
    public final double getSunlightOptimal() {
        return sunlightOptimal;
    }

    /**
     * @param sunlightOptimal the sunlightOptimal to set
     */
    public final void setSunlightOptimal(double sunlightOptimal) {
        this.sunlightOptimal = sunlightOptimal;
    }

    /**
     * @return the sunlightMaximum
     */
    public final double getSunlightMaximum() {
        return sunlightMaximum;
    }

    /**
     * @param sunlightMaximum the sunlightMaximum to set
     */
    public final void setSunlightMaximum(double sunlightMaximum) {
        this.sunlightMaximum = sunlightMaximum;
    }

    /**
     * @return the phMinimum
     */
    public final double getPHMinimum() {
        return phMinimum;
    }

    /**
     * @param phMinimum the phMinimum to set
     */
    public final void setPHMinimum(double phMinimum) {
        this.phMinimum = phMinimum;
    }

    /**
     * @return the phOptimal
     */
    public final double getPHOptimal() {
        return phOptimal;
    }

    /**
     * @param phOptimal the phOptimal to set
     */
    public final void setPHOptimal(double phOptimal) {
        this.phOptimal = phOptimal;
    }

    /**
     * @return the phMaximum
     */
    public final double getPHMaximum() {
        return phMaximum;
    }

    /**
     * @param phMaximum the phMaximum to set
     */
    public final void setPHMaximum(double phMaximum) {
        this.phMaximum = phMaximum;
    }

    /**
     * @return the growTime
     */
    public final int getGrowTime() {
        return growTime;
    }

    /**
     * @param growTime the growTime (in weeks) to set
     */
    public final void setGrowTime(int growTime) {
        this.growTime = growTime;
    }

    /**
     * @return the currentGrowTime
     */
    public int getCurrentGrowTime() {
        return currentGrowTime;
    }

    /**
     * @param currentGrowTime the currentGrowTime to set
     */
    public void setCurrentGrowTime(int currentGrowTime) {
        this.currentGrowTime = currentGrowTime;
    }
    
    /**
     * @return the currentIndexMultiplier
     */
    public double getCurrentIndexMultiplier() {
        return currentIndexMultiplier;
    }

    /**
     * @param currentIndexMultiplier the currentIndexMultiplier to set
     */
    public void setCurrentIndexMultiplier(double currentIndexMultiplier) {
        this.currentIndexMultiplier = currentIndexMultiplier;
    }
}
