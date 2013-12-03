package fi.paivola.foodmodel;

import fi.paivola.mapserver.core.DataFrame;
import fi.paivola.mapserver.core.Setting;
import java.util.List;
/**
 * @version 0.1
 * @author Jaakko Hannikainen
 * Generic class for field products, eg. maize.
 */

public abstract class Crop extends Edible {

    private String cropName; 
    private double waterMinimum;
    private double waterOptimal;
    private double waterMaximum;
    private double temperatureMinimum;
    private double temperatureOptimal;
    private double temperatureMaximum;
    private double sunlightMinimum;
    private double sunlightOptimal;
    private double sunlightMaximum;
    private double nutrientMinimum;
    private double nutrientOptimal;
    private double nutrientMaximum;
    
    public Crop(List<Setting> settings) {
        super(settings);
        this.settings.add(new Setting("water minimum (cm/week)", "double", "0.5"));
        this.settings.add(new Setting("water optimum (cm/week)", "double", "1"));
        this.settings.add(new Setting("water maximum (cm/week)", "double", "1.5"));
        this.settings.add(new Setting("minimum average temperature (C)", "double", "1"));
        this.settings.add(new Setting("optimal average temperature (C)", "double", "1"));
        this.settings.add(new Setting("maximum average temperature (C)", "double", "1"));
        this.settings.add(new Setting("minimum sunlight (hours / day)", "double", "1"));
        this.settings.add(new Setting("optimal sunlight (hours / day)", "double", "1"));
        this.settings.add(new Setting("maximum sunlight (hours / day)", "double", "1"));
        this.settings.add(new Setting("minimum nutrient content", "double", "1"));
        this.settings.add(new Setting("optimal nutrient content", "double", "1"));
        this.settings.add(new Setting("maximum nutrient content", "double", "1"));
    }

    public double onTick(DataFrame last) {
        return this.getWaterIndex(last) * this.getTemperatureIndex(last)
                * this.getSunshineIndex(last) * this.getNutrientIndex(last)
                * this.getArea() * this.getMaxYield();
    }
    
    private double getWaterIndex(DataFrame last) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    private double getTemperatureIndex(DataFrame last) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private double getSunshineIndex(DataFrame last) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private double getNutrientIndex(DataFrame last) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private double getArea() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private double getMaxYield() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    
    /**
     * @return the cropName
     */
    public String getCropName() {
        return cropName;
    }

    /**
     * @param cropName the cropName to set
     */
    public void setCropName(String cropName) {
        this.cropName = cropName;
    }
    
    /**
     * @return the waterMinimum
     */
    public double getWaterMinimum() {
        return waterMinimum;
    }

    /**
     * @param waterMinimum the waterMinimum to set
     */
    public void setWaterMinimum(double waterMinimum) {
        this.waterMinimum = waterMinimum;
    }

    /**
     * @return the waterOptimal
     */
    public double getWaterOptimal() {
        return waterOptimal;
    }

    /**
     * @param waterOptimal the waterOptimal to set
     */
    public void setWaterOptimal(double waterOptimal) {
        this.waterOptimal = waterOptimal;
    }

    /**
     * @return the waterMaximum
     */
    public double getWaterMaximum() {
        return waterMaximum;
    }

    /**
     * @param waterMaximum the waterMaximum to set
     */
    public void setWaterMaximum(double waterMaximum) {
        this.waterMaximum = waterMaximum;
    }

    /**
     * @return the temperatureMinimum
     */
    public double getTemperatureMinimum() {
        return temperatureMinimum;
    }

    /**
     * @param temperatureMinimum the temperatureMinimum to set
     */
    public void setTemperatureMinimum(double temperatureMinimum) {
        this.temperatureMinimum = temperatureMinimum;
    }

    /**
     * @return the temperatureOptimal
     */
    public double getTemperatureOptimal() {
        return temperatureOptimal;
    }

    /**
     * @param temperatureOptimal the temperatureOptimal to set
     */
    public void setTemperatureOptimal(double temperatureOptimal) {
        this.temperatureOptimal = temperatureOptimal;
    }

    /**
     * @return the temperatureMaximum
     */
    public double getTemperatureMaximum() {
        return temperatureMaximum;
    }

    /**
     * @param temperatureMaximum the temperatureMaximum to set
     */
    public void setTemperatureMaximum(double temperatureMaximum) {
        this.temperatureMaximum = temperatureMaximum;
    }

    /**
     * @return the sunlightMinimum
     */
    public double getSunlightMinimum() {
        return sunlightMinimum;
    }

    /**
     * @param sunlightMinimum the sunlightMinimum to set
     */
    public void setSunlightMinimum(double sunlightMinimum) {
        this.sunlightMinimum = sunlightMinimum;
    }

    /**
     * @return the sunlightOptimal
     */
    public double getSunlightOptimal() {
        return sunlightOptimal;
    }

    /**
     * @param sunlightOptimal the sunlightOptimal to set
     */
    public void setSunlightOptimal(double sunlightOptimal) {
        this.sunlightOptimal = sunlightOptimal;
    }

    /**
     * @return the sunlightMaximum
     */
    public double getSunlightMaximum() {
        return sunlightMaximum;
    }

    /**
     * @param sunlightMaximum the sunlightMaximum to set
     */
    public void setSunlightMaximum(double sunlightMaximum) {
        this.sunlightMaximum = sunlightMaximum;
    }

    /**
     * @return the nutrientMinimum
     */
    public double getNutrientMinimum() {
        return nutrientMinimum;
    }

    /**
     * @param nutrientMinimum the nutrientMinimum to set
     */
    public void setNutrientMinimum(double nutrientMinimum) {
        this.nutrientMinimum = nutrientMinimum;
    }

    /**
     * @return the nutrientOptimal
     */
    public double getNutrientOptimal() {
        return nutrientOptimal;
    }

    /**
     * @param nutrientOptimal the nutrientOptimal to set
     */
    public void setNutrientOptimal(double nutrientOptimal) {
        this.nutrientOptimal = nutrientOptimal;
    }

    /**
     * @return the nutrientMaximum
     */
    public double getNutrientMaximum() {
        return nutrientMaximum;
    }

    /**
     * @param nutrientMaximum the nutrientMaximum to set
     */
    public void setNutrientMaximum(double nutrientMaximum) {
        this.nutrientMaximum = nutrientMaximum;
    }

}
