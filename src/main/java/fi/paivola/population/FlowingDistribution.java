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
    private Double[]    quantities;
    private Double      first;
    private Double      last;
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
        Double[] newQuantities = new Double[ this.quantities.length ];
        for (int i = 0; i != this.quantities.length; ++i) {
            if (i == 0) continue; // nothing flows to the first quantity
            final double dtYears = dt / Constants.WEEKS_IN_YEAR;
            newQuantities[i] = this.quantities[i] + (this.quantities[i-1]
                    - this.quantities[i]) * annualFlowPc * dtYears;
        }
        this.quantities = newQuantities;
        reassignPointers(); // not sure if actually necessary, dunno java
    }
    
    public Double getFirstQuantity()
    {
        return first;
    }
    
    public Double getLastQuantity()
    {
        return last;
    }
    
    public Double[] getQuantities() {
        return this.quantities;
    }

    private void setContentFromCsv(String filename) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    private void setContentFromDebug(int amQuantities) {
        this.quantities = new Double[amQuantities];
        for (int i = 0; i != amQuantities; ++i)
        {
            this.quantities[i] = new Double( 10 * (amQuantities-i) );
        }
        reassignPointers();
    }
    
    private void reassignPointers() {
        this.first = this.quantities[0];
        this.last = this.quantities[ this.quantities.length - 1 ];
    }
}
