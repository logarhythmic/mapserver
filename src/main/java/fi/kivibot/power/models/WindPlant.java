package fi.kivibot.power.models;

import fi.kivibot.power.models.base.PowerPlant;

/**
 *
 * @author kivi
 */
public class WindPlant extends PowerPlant{

    public WindPlant(int id) {
        super(id);
        this.energy = 100_000;
    }
}
