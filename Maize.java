/**
 * @version 0.1
 * @author Jaakko Hannikainen
 * Class for maize.
 */

public class Maize extends Crop {
    public Maize() {
        this.super();
        this.waterMinimum = 1.01;
        this.waterOptimal = 2.07;
        this.waterMaximum = 3.01;
        this.temperatureMinimum = 20;
        this.temperatureOptimal = 30;
        this.temperatureMaximum = 40;
        this.sunlightMinimum = 42;
        this.sunlightOptimal = 45;
        this.sunlightMaximum = 70;
   }
}
