package fi.paivola.weathermodel;

/**
 * Sunlight class.
 * @author Jaakko Hannikainen
 */
public class Sunlight {
    public static final double[] sunAvg = {
        8.6, 8.9, 9.1, 8.6, 8.8, 7.3, 7.3, 8.2, 8.8, 8.6, 8.7, 8.3 };

    // XXX: quick dumb implementation
    private Sunlight() {}

    public static double getSunlight(int week) {
        if(week < 0)
            throw new IllegalArgumentException("week must be positive");
        week%=52;
        week/=52*12;
        return sunAvg[week];
    }
}
