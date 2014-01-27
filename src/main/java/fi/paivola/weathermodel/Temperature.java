package fi.paivola.weathermodel;

import java.util.Calendar;
import java.util.Random;

/**
 * Temperature class.
 * @author Jaakko Hannikainen
 */
public class Temperature {
    public static final double[] tempMax = {
        30, 30, 31, 32,  31, 30, 29, 29,  29, 30, 31, 31 };
    public static final double[] tempMin = {
        23, 23, 25, 26,  25, 24, 23, 23,  23, 24, 24, 23 };
    public static double dtemp = 0;

    // XXX: quick dumb implementation
    private Temperature() {}

    public static double getTemperature(Calendar date) {
        int month = date.get(Calendar.MONTH);
        return tempMin[month] + (tempMax[month] - tempMin[month]) / 2 - 1 +
            (int)(Math.random()*3) + dtemp;
    }
}
