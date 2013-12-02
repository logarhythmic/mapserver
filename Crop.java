package fi.paivola.foodmodel;

/**
 * @version 0.1
 * @author Jaakko Hannikainen
 * Generic class for field products, eg. maize.
 */

public abstract class Crop extends Edible {

    double waterMinimum;
    double waterOptimal;
    double waterMaximum;
    double temperatureMinimum;
    double temperatureOptimal;
    double temperatureMaximum;
    double sunlightMinimum;
    double sunlightOptimal;
    double sunlightMaximum;
    double nutrientMinimum;
    double nutrientOptimal;
    double nutrientMaximum;

    public Crop() {
        super();
         
    }
}
