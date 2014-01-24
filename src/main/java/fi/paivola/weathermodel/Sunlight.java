package fi.paivola.weathermodel;

import java.util.Calendar;

/**
 * Sunlight class.
 * @author Jaakko Hannikainen
 */
public class Sunlight {
    public static final double[] sunAvg = {
        8.6, 8.9, 9.1, 8.6, 8.8, 7.3, 7.3, 8.2, 8.8, 8.6, 8.7, 8.3 };

    // XXX: quick dumb implementation
    private Sunlight() {}

    public static double getSunlight(Calendar date) {
        int month = date.get(Calendar.MONTH);
        return sunAvg[month] * 7;
    }
}
