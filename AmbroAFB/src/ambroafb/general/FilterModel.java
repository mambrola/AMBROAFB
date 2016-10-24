/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general;

/**
 *
 * @author dato
 */
public abstract class FilterModel {
    
    private boolean modelIsEmpty;
    
    public boolean isEmpty(){
        return modelIsEmpty;
    }
    
    public void changeModelAsEmpty() {
        modelIsEmpty = true;
    }
    
    public void saveIntoPref(String key, String value){
        GeneralConfig.prefs.put(key, value);
    }
    
    public void saveIntoPref(String key, int value){
        GeneralConfig.prefs.putInt(key, value);
    }
    
    public void saveIntoPref(String key, double value){
        GeneralConfig.prefs.putDouble(key, value);
    }
    
    public void saveIntoPref(String key, boolean value){
        GeneralConfig.prefs.putBoolean(key, value);
    }
    
    
    public String getStringFromPref(String key){
        return GeneralConfig.prefs.get(key, null);
    }
    
    /**
     * @param key Preference key
     * @return If preferences do not contains key then -1, otherwise - appropriate value from preferences.
     */
    public int getIntFromPref(String key){
        return GeneralConfig.prefs.getInt(key, 0);
    }
    
    /**
     * @param key Preference key
     * @return -1 if preferences do not contains key, otherwise - appropriate value from preferences.
     */
    public double getDoubleFromPref(String key){
        return GeneralConfig.prefs.getDouble(key, -1);
    }
    
    /**
     * @param key Preference key
     * @return False if preferences do not contains key, otherwise - appropriate value from preferences.
     */
    public boolean getBooleanFromPref(String key){
        return GeneralConfig.prefs.getBoolean(key, false);
    }
}
