package fi.paivola.population;

import com.sun.corba.se.impl.orbutil.closure.Constant;
import fi.paivola.population.CsvUtil;
import fi.paivola.population.Constants;

/**
 * Array-like data-structure for populations with some additional
 * functionality. Births = constant fraction of total population.
 * @author Henri Lunnikivi (hegza@paivola.fi)
 */
public class PopulationDistribution  {
    /**
     * distribution-array
     */
    private double[]    quantities;
    private double[]    deaths;
    private double      birthsPc; // constant fraction of total population
    private MortalityModel  mortalityModel; // for each age-group
    private double      annualFlowPc; // 0..1
    
    /**
     * Initializes PopulationDistribution from hard-coded debug-data
     * @param amQuantities Amount of debug values to be initialized
     */
    public PopulationDistribution(int amQuantities, 
            MortalityModel mortalityModel) {
        this.annualFlowPc = 0.2; // default is 20% (5yr age-groups)
        this.birthsPc = 0.05; // default
        this.mortalityModel = mortalityModel;
        setContentFromDebug(amQuantities);
    }
    
    /*
     * Initializes PopulationDistribution quantities from double[]
     */
    public PopulationDistribution(double[] quantities, MortalityModel mortalityModel) {
        this.annualFlowPc = 0.2;
        this.birthsPc = 0.05; // default
        this.mortalityModel = mortalityModel;
        this.quantities = quantities;
        this.deaths = new double[Constants.NUM_AGE_GROUPS];
    }

    /**
     * Initializes PopulationDistribution from a .csv-file
     * @param csvFilename
     */
    public PopulationDistribution(String csvFilename, int rowNumber, 
            MortalityModel mortalityModel) {
        this.mortalityModel = mortalityModel;
        setContentFromCsv(csvFilename, rowNumber);
    }
        
    public void setAnnualFlowPc(double annualFlowPc) {
        assert(annualFlowPc >= 0.0);
        assert(annualFlowPc <= 1.0);
        this.annualFlowPc = annualFlowPc;
    }
    
    /**
     * Sets the birth-rate give in fraction of the total population.
     * @param birthsPerPopulation Constant fraction of total population to be
     * added to the first quantity.
     */
    public void setBirthsPc(double birthsPerPopulation) {
        this.birthsPc = birthsPerPopulation;
    }
    
    /**
     * Causes quantities to flow forwards in the arrays.
     * @param dt Time-step in weeks
     */
    public void step(int dt) {
        double[] newDeaths = new double[ this.quantities.length ];
        double[] newQuantities = new double[ this.quantities.length ];
        final int lastIndex = this.quantities.length - 1;
        
        double dtYears = dt / Constants.WEEKS_IN_YEAR;
        for (int i = 0; i != this.quantities.length; ++i)
        {
            newDeaths[i] = quantities[i] * dtYears * mortalityModel.getAnnualMortalityRate( i );
        }
        // don't use influx for the first one
        newQuantities[0] = 
                quantities[0] // previous
                - (quantities[0] - deaths[0]) * annualFlowPc * dtYears // age-flow (out)
                + birthsPc * (total()-totalDeaths()) * dtYears // new people
                - newDeaths[0]; // new deaths
        // calculate influx and outflux for each entry after the first
        // ignore the last category (which is 100+)
        for (int i = 1; i != lastIndex; ++i) {
            newQuantities[i] = 
                    quantities[i] // previous
                    + (quantities[i-1] - deaths[i-1]) * annualFlowPc * dtYears // age-flow (in)
                    - (quantities[i] - deaths[i]) * annualFlowPc * dtYears // age-flow (out)
                    - newDeaths[i]; // new deaths
        }
        // 100+ don't lose members to a newer category
        newQuantities[lastIndex] = 
                quantities[lastIndex] // previous
                + (quantities[lastIndex-1] - deaths[lastIndex-1]) * annualFlowPc * dtYears // age-flow (in)
                - newDeaths[lastIndex]; // new deaths
        this.quantities = newQuantities;
        this.deaths = newDeaths;
        
        // TODO: fixme, less than 0 populations should not happen
        for (double d : quantities) {
            if (d < 0) d = 0;
        }
        
        // do random sanitychecks
//        for (double d : quantities) {
//            if (d < 0) throw new java.lang.IllegalStateException("age-group-population was negative");
//        }
    }
    
    public double[] getQuantities() {
        return this.quantities;
    }
    
    public double getQuantity(int index) {
        return this.quantities[index];
    }
    
    public double total()
    {
        double total = 0;
        for ( double q : this.quantities )
        {
            total += q;
        }
        return total;
    }
    
    public double totalDeaths() {
        double total = 0;
        for ( double q : this.deaths )
        {
            total += q;
        }
        return total;
    }
            
    
    /**
     * Find out the distribution-mode
     * @return Group with the largest quantity
     */
    public int mode()
    {
        int mode = 0;
        double largest = 0;
        for ( int i = 0; i != this.quantities.length; ++i )
        {
            if (this.quantities[i] > largest)
            {
                largest = this.quantities[i];
                mode = i;
            }
        }
        return mode;
    }

    private void setContentFromCsv(String filename, int rowNumber) {
        this.quantities = CsvUtil.readDoubles(filename, rowNumber);
        this.deaths = new double[Constants.NUM_AGE_GROUPS];
        for (int i = 0; i != this.deaths.length; ++i) {
            this.deaths[i] = 0;
        }
    }
    
    private void setContentFromDebug(int amQuantities) {
        this.quantities = new double[amQuantities];
        for (int i = 0; i != amQuantities; ++i)
        {
            this.quantities[i] = 10 * (amQuantities-i);
        }
        this.deaths = new double[Constants.NUM_AGE_GROUPS];
        for (int i = 0; i != this.deaths.length; ++i) {
            this.deaths[i] = 0;
        }
     }
}
