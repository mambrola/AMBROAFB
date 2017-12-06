/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general.interfaces;

import ambroafb.general.GeneralConfig;

/**
 *
 * @author dato
 */
public abstract class FilterModel {
    
    protected static final String DATE_BIGGER = "1970-01-01";
    protected static final String DATE_LESS = "9999-01-01";
    
    protected static final String EXTRA_TIME_BIGGER = "00:00:00";
    protected static final String EXTRA_TIME_LESS = "23:59:59";
    
    private boolean modelCanceled;
    
    public boolean isCanceled(){
        return modelCanceled;
    }
    
    public void changeModelAsEmpty() {
        modelCanceled = true;
    }
    
    /**
     * The method saves String value into preference on specific key.
     * Note: key and value must not be null.
     * @param key
     * @param value 
     */
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
     * @return If preferences do not contains key then 0, otherwise - appropriate value from preferences.
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
