/**
 *
 * @version 0.1
 * @author Jaakko Hannikainen
 */
public class Field extends PointModel {

    private double waterIndex;
    private double temperatureIndex;
    private double sunshineIndex;
    private double nutrientIndex;
    private double area;

    private final double maxYield = 1;

    /**
     * Main constructor for the food model
     * 
     * @param id ID of the field
     */
    public Food(int id) {
        super(id);
        this.settings.add(new Setting("area", "double", "1", setArea));

        this.settings.add(new Setting("water minimum (cm/week)", "double", "0.5", setArea));
        this.settings.add(new Setting("water maximum (cm/week)", "double", "1.5", setArea));
        this.settings.add(new Setting("minimum average temperature (C)", "double", "1", setArea));
        this.settings.add(new Setting("maximum average temperature (C)", "double", "1", setArea));
        this.settings.add(new Setting("minimum sunlight (hours / day)", "double", "1", setArea));
        this.settings.add(new Setting("maximum sunlight (hours / day)", "double", "1", setArea));
        this.settings.add(new Setting("minimum nutrient content", "double", "1", setArea));
        this.settings.add(new Setting("maximum nutrient content", "double", "1", setArea));

    }

    public double getWaterIndex() {
        return this.waterIndex;
    }

    private void setWaterIndex(double waterIndex) {
        this.waterIndex = waterIndex;
    }

    public double getTemperatureIndex() {
        return this.temperatureIndex;
    }

    private void setTemperatureIndex(double temperatureIndex) {
        this.TemperatureIndex = temperatureIndex;
    }

    public double getSunshineIndex() {
        return this.sunshineIndex;
    }

    private void setSunshineIndex(double sunshineIndex) {
        this.sunshineIndex = sunshineIndex;
    }

    public double getNutrientIndex() {
        return this.nutrientIndex;
    }

    private void setNutrientIndex(double nutrientIndex) {
        this.nutrientIndex = nutrientIndex;
    }

    public double getAreaIndex() {
        return this.areaIndex;
    }

    public void setAreaIndex(double areaIndex) {
        this.areaIndex = areaIndex;
    }

    public double getMaxYield() {
        return this.maxYield;
    }

    @Override
    public void onTick(DataFrame last, DataFrame current) {
        this.saveDouble("foodAmount", this.getWaterIndex()
                * this.getTemperatureIndex() * this.getSunshineIndex()
                * this.getNutrientIndex() * this.getArea()
                * this.getMaxYield());
    }

    @Override
    public void onEvent(Event e) {
        switch(e.name) {
            default:
                break;
        }
    }

    @Override
    public void onRegisteration(GameManager gm) {

    }

    @Override
    public void onGenerateDefaults() {
        this.setWaterIndex(0.5);
        this.setTemperatureIndex(0.5);
        this.setSunshineIndex(0.5);
        this.setNutrientIndex(0.5);
        this.setArea(1);
    }
}


