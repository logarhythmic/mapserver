/*
 * The MIT License
 *
 * Copyright 2013 Allan Palmu <allan.palmu@gmail.com>.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package fi.paivola.mapserver.models;
import fi.paivola.mapserver.core.DataFrame;
import fi.paivola.mapserver.core.Event;
import java.util.ArrayList;
import fi.paivola.mapserver.core.ExtensionModel;
import fi.paivola.mapserver.core.GameManager;
import fi.paivola.mapserver.core.setting.SettingDouble;
import fi.paivola.mapserver.core.setting.SettingMaster;
import fi.paivola.mapserver.utils.RangeDouble;

/**
 * Defines a storage unit for a single town.
 * @author Allan Palmu <allan.palmu@gmail.com>
 */
public class TownStorage extends ExtensionModel {
    ArrayList<Supplies> storage;
    double maxCapacity;
    double currentCapacity;
    
    /**
     * Creates a new storage unit
     */
    public TownStorage(){
        super(5);
        this.maxCapacity = 0;
        storage = new ArrayList<>();
    }
    
    /**
     * @return the maximum capacity of the storage unit
     */
    public double getMax(){
        return maxCapacity;
    }
    
    /**
     * @param in the maximum capacity will be set to this
     */
    public void setMax(double in){
        maxCapacity = in;
    }
    
    Supplies findSupplies(int id){
        for (Supplies s : storage){
            if (s.id == id)
                return s;
        }
        return null;
    }
    
    /**
     * Stores the given supplies item into the storage
     * @param in what to store
     * @return if the storage filled up, this is how much was left of the item
     */
    public double Store(Supplies in){
        Supplies found = findSupplies(in.id);
        if (found == null){
            storage.add(new Supplies(in.id, in.getAmount()+currentCapacity>maxCapacity?maxCapacity-currentCapacity:in.getAmount()));
        } else {
            found.setAmount(found.getAmount()+(in.getAmount()+currentCapacity>maxCapacity?maxCapacity-currentCapacity:in.getAmount()));
        }
        double overflow = currentCapacity+in.getAmount()-maxCapacity;
        overflow = overflow<0?0:overflow;
        Update();
        return overflow;
    }
    
    /**
     * @param id the identifier for the type of supplies
     * @return how much of that we have in stock
     */
    public double QuerySupplies(int id){
        Supplies found = findSupplies(id);
        if (found == null)
            return 0;
        else return found.getAmount();
    }
    
    /**
     * @return how much stuff is in stock
     */
    public double QueryCapacity(){
        return currentCapacity;
    }
    
    public double QuerySpace(){
        return maxCapacity - currentCapacity;
    }
    
    /**
     * Takes a supply item out of the storage
     * @param id which supplies
     * @param amount how much
     * @return the created Supplies item
     */
    public Supplies Take(int id, double amount){
        Supplies found = findSupplies(id);
        if (found == null){
            return new Supplies(id, 0);
        } else {
            double taken = Math.min(amount, found.getAmount());
            found.setAmount(found.getAmount() - taken);
            return new Supplies(id, taken);
        }
    }
    
    void Update(){
        currentCapacity = 0;
        for (Supplies s:storage){
            if (s.getAmount() == 0){
                storage.remove(s);
            }
            currentCapacity += s.getAmount();
        }
        if (currentCapacity > maxCapacity){
            System.out.println("Storage had more goods than could fit inside. "
                              +"This should not happen. "
                              +"Please use the Store method to store items.");
        }
    }

    @Override
    public void onEvent(Event e, DataFrame d) {
        //idk
    }

    @Override
    public void onGenerateDefaults(DataFrame df) {
        this.saveDouble("Items in storage", this.currentCapacity);
        this.saveDouble("Storage fullness", this.currentCapacity / this.maxCapacity);
    }

    @Override
    public void onRegisteration(GameManager gm, SettingMaster sm) {
        sm.name = "townStorage";
        sm.exts = "popCenter";
        sm.settings.put("maxCap", new SettingDouble("The volume of the storage unit of this model", 10000, new RangeDouble(1, 1000000000)));
    }

    @Override
    public void onTick(DataFrame last, DataFrame current) {
        for (Supplies s : storage){
            s.amount*=s.edible?0.7:1;  //our highly advanced rat algorithm
        }
    }

    @Override
    public void onUpdateSettings(SettingMaster sm) {
        this.maxCapacity = Double.parseDouble(sm.settings.get("maxCap").getValue());
    }
}
