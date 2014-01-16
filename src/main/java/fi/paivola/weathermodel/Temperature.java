package fi.paivola.weathermodel;

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

    // XXX: quick dumb implementation
    private Temperature() {}

    public static double getTemperature(int week) {
        if(week < 0)
            throw new IllegalArgumentException("week must be positive");
        week%=52;
        week/=52*12;
        return tempMin[week] + (tempMax[week] - tempMin[week]) / 2;
    }
}
