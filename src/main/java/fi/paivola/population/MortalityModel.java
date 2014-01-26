package fi.paivola.population;

import fi.paivola.population.Constants;

/**
 * Sub-model for mortality. Automatically initializes values that were pre-
 * computed by Henri Lunnikivi (from mortality_population.ods).
 * @author Henri Lunnikivi (hegza@paivola.fi)
 */
public class MortalityModel  {
    private double[]    baseMortalityRatesPc; // for each age-group per *five* years
    private double[]    mortalityIncreaseByFamine;
    private double      foodShortage;
    
    public MortalityModel() {
        setContentFromtHardcodedData();
    }

    public void setContentFromtHardcodedData() {
        baseMortalityRatesPc = new double[Constants.NUM_AGE_GROUPS];
        baseMortalityRatesPc[Constants.AGES_0TO4] = 0.177355079;
        baseMortalityRatesPc[Constants.AGES_5TO9] = 0.023141826;
        baseMortalityRatesPc[Constants.AGES_10TO14] = 0.018364581;
        baseMortalityRatesPc[Constants.AGES_15TO19] = 0.019372052;
        baseMortalityRatesPc[Constants.AGES_20TO24] = 0.025083533;
        baseMortalityRatesPc[Constants.AGES_25TO29] = 0.030008579;
        baseMortalityRatesPc[Constants.AGES_30TO34] = 0.03533238;
        baseMortalityRatesPc[Constants.AGES_35TO39] = 0.042289071;
        baseMortalityRatesPc[Constants.AGES_40TO44] = 0.048480605;
        baseMortalityRatesPc[Constants.AGES_45TO49] = 0.054892079;
        baseMortalityRatesPc[Constants.AGES_50TO54] = 0.067414339;
        baseMortalityRatesPc[Constants.AGES_55TO59] = 0.089477594;
        baseMortalityRatesPc[Constants.AGES_60TO64] = 0.130027819;
        baseMortalityRatesPc[Constants.AGES_65TO69] = 0.196021405;
        baseMortalityRatesPc[Constants.AGES_70TO74] = 0.30355144;
        baseMortalityRatesPc[Constants.AGES_75TO79] = 0.477421394;
        baseMortalityRatesPc[Constants.AGES_80TO84] = 0.708366385;
        baseMortalityRatesPc[Constants.AGES_85TO89] = 1.032560043;
        baseMortalityRatesPc[Constants.AGES_90TO94] = 1.418019143;
        baseMortalityRatesPc[Constants.AGES_95TO99] = 1.940559441;
        baseMortalityRatesPc[Constants.AGES_OVER_100] = 1.940559441;
        mortalityIncreaseByFamine = new double[Constants.NUM_AGE_GROUPS];
        mortalityIncreaseByFamine[Constants.AGES_0TO4] = 0.95;
        mortalityIncreaseByFamine[Constants.AGES_5TO9] = 0.7;
        mortalityIncreaseByFamine[Constants.AGES_10TO14] = 0.5;
        mortalityIncreaseByFamine[Constants.AGES_15TO19] = 0.5;
        mortalityIncreaseByFamine[Constants.AGES_20TO24] = 0.5;
        mortalityIncreaseByFamine[Constants.AGES_25TO29] = 0.5;
        mortalityIncreaseByFamine[Constants.AGES_30TO34] = 0.5;
        mortalityIncreaseByFamine[Constants.AGES_35TO39] = 0.5;
        mortalityIncreaseByFamine[Constants.AGES_40TO44] = 0.5;
        mortalityIncreaseByFamine[Constants.AGES_45TO49] = 0.5;
        mortalityIncreaseByFamine[Constants.AGES_50TO54] = 0.5;
        mortalityIncreaseByFamine[Constants.AGES_55TO59] = 0.6;
        mortalityIncreaseByFamine[Constants.AGES_60TO64] = 0.7;
        mortalityIncreaseByFamine[Constants.AGES_65TO69] = 0.7;
        mortalityIncreaseByFamine[Constants.AGES_70TO74] = 0.8;
        mortalityIncreaseByFamine[Constants.AGES_75TO79] = 0.8;
        mortalityIncreaseByFamine[Constants.AGES_80TO84] = 0.9;
        mortalityIncreaseByFamine[Constants.AGES_85TO89] = 0.95;
        mortalityIncreaseByFamine[Constants.AGES_90TO94] = 0.95;
        mortalityIncreaseByFamine[Constants.AGES_95TO99] = 0.95;
        mortalityIncreaseByFamine[Constants.AGES_OVER_100] = 0.95;
    }
    
    public double getAnnualMortalityRate(int ageGroup) {
        return baseMortalityRatesPc[ageGroup] * 0.2 + getFamineStress(ageGroup);
    }
    
    public double getFamineStress(int ageGroup) {
        return mortalityIncreaseByFamine[ageGroup] * foodShortage;
    }
    
    /**
     * @param shortagePerPerson aka. severity
     */
    public void setFoodShortage(double shortagePerPopulation) {
        if (shortagePerPopulation >= 0 && shortagePerPopulation <= 1) {
            foodShortage = shortagePerPopulation;
        } else {
            System.out.println("Illegal value for food shortage: "+shortagePerPopulation);
        }
    }
}
