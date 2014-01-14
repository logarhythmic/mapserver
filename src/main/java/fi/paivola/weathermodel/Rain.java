package fi.paivola.weathermodel;

/**
 * Rain class.
 * @author Jaakko Hannikainen
 */
public class Rain {
    public static final double[] rainAvrg = {
        1, 0, 9, 58,  56, 82, 58, 40,  23, 27, 36, 9};
    public static final double[] rainProb = {
        0, 0, 3, 17,  23, 43, 42, 32,  17, 13, 13, 6};

    // XXX: quick dumb implementation
    private Rain() {}

    public static double getRain(int week) {
        if(week < 0 || week > 51)
            throw new IllegalArgumentException("week must be positive");
        week%=52;
        week/=4;
        return Math.random() * 100 < rainProb[week] ? (rainAvrg[week]:0;
    }
}
