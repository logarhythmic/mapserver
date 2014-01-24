package fi.paivola.water;

import au.com.bytecode.opencsv.CSVWriter;
import fi.paivola.mapserver.utils.Color;
import fi.paivola.mapserver.core.DataFrame;
import fi.paivola.mapserver.core.Event;
import fi.paivola.mapserver.core.GameManager;
import fi.paivola.mapserver.core.GlobalModel;
import fi.paivola.mapserver.core.setting.SettingInt;
import fi.paivola.mapserver.core.setting.SettingMaster;
import fi.paivola.mapserver.core.Model;
import fi.paivola.mapserver.utils.RangeInt;

/**
 *
 * @author Esa
 */
public class Sea extends GlobalModel
{
    public Sea(int id){
        super(id);
        this.type = "Sea";
    }
    
    @Override
    public void onUpdateSettings(SettingMaster sm){
        
    }
    
    @Override
    public void onGenerateDefaults(DataFrame df){
        
    }
    
    @Override
    public void onRegisteration(GameManager gm, SettingMaster sm){
        
    }
    
    @Override
    public void onEvent(Event e, DataFrame df){
        
    }
    
    @Override
    public void onTick(DataFrame last, DataFrame current){
        
    }
}
