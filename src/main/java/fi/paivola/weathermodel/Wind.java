package fi.paivola.weathermodel;

import java.util.Calendar;

/**
 * Sunlight class.
 * @author Jaakko Hannikainen
 */
public class Wind {
    // XXX: quick dumb implementation
    private Wind() {}

    public static double getWind(Calendar date) {
//        System.out.println("asd");
        return 8 + Math.random() * 3;
    }
}
