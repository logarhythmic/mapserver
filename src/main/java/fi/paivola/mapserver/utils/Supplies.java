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

package fi.paivola.mapserver.utils;

/**
 *
 * @author Allan Palmu <allan.palmu@gmail.com>
 */
public class Supplies {
    public double amount;
    public int id;
    public boolean edible;
    public boolean flammable;
    public boolean fuel;
    
    public Supplies(int id, double amount){
       this.id = id;
       switch(id){
           case 0: //uninflammable food
               edible = true;
               flammable = false;
               fuel = false;
               break;
           case 1: //inflammable food
               edible = true;
               flammable = true;
               fuel = false;
               break;
           case 2: //inflammable fuel
               edible = false;
               flammable = true;
               fuel = true;
               break;
           case 3: //inflammable other
               edible = false;
               flammable = true;
               fuel = false;
               break;
           default: //other
               edible = false;
               flammable = false;
               fuel = false;
               break;
       }
       this.amount = amount;
    }
}
