package fi.paivola.population;

import fi.paivola.population.CsvUtil;
import fi.paivola.population.Constants;

/**
 * Array-like data-structure with some additional
 * functionality.
 * @author Henri Lunnikivi (hegza@paivola.fi)
 */
public class FlowingDistribution  {
    /**
     * distribution-array
     */
    private double[]    quantities;
    private double      annualFlowPc; // 0..1
    
    /**
     * Initializes MutableDistribution from hard-coded debug-data
     * @param amQuantities Amount of debug values to be initialized
     */
    public FlowingDistribution(int amQuantities) {
        setContentFromDebug(amQuantities);
    }

    /**
     * Initializes MutableDistribution from a .csv-file
     * @param csvFilename
     */
    public FlowingDistribution(String csvFilename) {
        setContentFromCsv(csvFilename);
    }
        
    public void setAnnualFlowPc(double annualFlowPc) {
        assert(annualFlowPc >= 0.0);
        assert(annualFlowPc <= 1.0);
        this.annualFlowPc = annualFlowPc;
    }
    
    /**
     * Causes quantities to flow forwards in the arrays.
     * @param dt Time-step in weeks
     */
    public void step(int dt) {
        double[] newQuantities = new double[ this.quantities.length ];
        for (int i = 0; i != this.quantities.length; ++i) {
            final double dtYears = dt / Constants.WEEKS_IN_YEAR;
            if (i == 0) {
                newQuantities[0] = this.quantities[i] -
                    this.quantities[i] * annualFlowPc * dtYears;
                continue;
            }
            newQuantities[i] = this.quantities[i] + (this.quantities[i-1]
                    - this.quantities[i]) * annualFlowPc * dtYears;
        }
        this.quantities = newQuantities;
    }
    
    public double[] getQuantities() {
        return this.quantities;
    }

    private void setContentFromCsv(String filename) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    private void setContentFromDebug(int amQuantities) {
        this.quantities = new double[amQuantities];
        for (int i = 0; i != amQuantities; ++i)
        {
            this.quantities[i] = 10 * (amQuantities-i);
        }
    }
}
