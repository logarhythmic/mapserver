package fi.paivola.population;

import fi.paivola.population.Constants;

/**
 * Sub-model for mortality. Automatically initializes values that were pre-
 * computed by Henri Lunnikivi (from mortality_population.ods).
 * @author Henri Lunnikivi (hegza@paivola.fi)
 */
public class MortalityModel  {
    private double[] mortalityRatesPc; // for each age-group per *five* years
    public MortalityModel() {
        setContentFromDebug();
    }

    public void setContentFromDebug() {
        mortalityRatesPc = new double[Constants.NUM_AGE_GROUPS];
        mortalityRatesPc[Constants.AGES_0TO4] = 0.177355079;
        mortalityRatesPc[Constants.AGES_5TO9] = 0.023141826;
        mortalityRatesPc[Constants.AGES_10TO14] = 0.018364581;
        mortalityRatesPc[Constants.AGES_15TO19] = 0.019372052;
        mortalityRatesPc[Constants.AGES_20TO24] = 0.025083533;
        mortalityRatesPc[Constants.AGES_25TO29] = 0.030008579;
        mortalityRatesPc[Constants.AGES_30TO34] = 0.03533238;
        mortalityRatesPc[Constants.AGES_35TO39] = 0.042289071;
        mortalityRatesPc[Constants.AGES_40TO44] = 0.048480605;
        mortalityRatesPc[Constants.AGES_45TO49] = 0.054892079;
        mortalityRatesPc[Constants.AGES_50TO54] = 0.067414339;
        mortalityRatesPc[Constants.AGES_55TO59] = 0.089477594;
        mortalityRatesPc[Constants.AGES_60TO64] = 0.130027819;
        mortalityRatesPc[Constants.AGES_65TO69] = 0.196021405;
        mortalityRatesPc[Constants.AGES_70TO74] = 0.30355144;
        mortalityRatesPc[Constants.AGES_75TO79] = 0.477421394;
        mortalityRatesPc[Constants.AGES_80TO84] = 0.708366385;
        mortalityRatesPc[Constants.AGES_85TO89] = 1.032560043;
        mortalityRatesPc[Constants.AGES_90TO94] = 1.418019143;
        mortalityRatesPc[Constants.AGES_95TO99] = 1.940559441;
        mortalityRatesPc[Constants.AGES_OVER_100] = 1.940559441;
    }
    
    public double getAnnualMortalityRate(int ageGroup) {
        return mortalityRatesPc[ageGroup] * 0.2;
    }
}
