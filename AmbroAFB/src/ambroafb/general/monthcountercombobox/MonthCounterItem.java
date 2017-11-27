/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general.monthcountercombobox;

import ambroafb.general.GeneralConfig;
import ambroafb.general.Utils;

/**
 *
 * @author dato
 */
public class MonthCounterItem {
    
    private String monthCount;
    private String dayCount;
    
    public final int DAYS_IN_MONTH = 30;
    public final int MONTH_IN_YEAR = 12;
    
    
    /**
     * @param counter The counter means month counter. 
     * For example: 
     *          counter = 2     -> 2 months
     *          counter = 3.5   -> 3 months and 15 days
     *          counter = 3.25  -> 3 months and 7 days
     *          counter = 12.25 -> 12 months and 7 days, 1 year and 7 days
     */
    public MonthCounterItem(String counter){
        proccess(counter);
    }
    
    /**
     * The value of month will be zero.
     */
    public MonthCounterItem(){
        proccess("0");
    }
    
    public int getMonthCount(){
        return Utils.getIntValueFor(monthCount);
    }
    
    public int getDayCount(){
        double days = Utils.getDoubleValueFor(dayCount);
        if (days == -1) // error in converting
            days = 0;
        return (int)(DAYS_IN_MONTH * days);
    }
    
    public void setMonth(String counter){
        proccess(counter);
    }
    
    private void proccess(String counter){
        if (counter == null) return;
        int pointIndex = counter.indexOf(".");
        if (pointIndex != -1){
            monthCount = counter.substring(0, pointIndex);
            dayCount = "0" + counter.substring(pointIndex); // for ex: 0.25
        }
        else {
            monthCount = counter;
        }
    }
    
    @Override
    public boolean equals(Object other){
        if (other == null) return false;
        MonthCounterItem otherMonthCounter = (MonthCounterItem) other;
        return this.getMonthCount() == otherMonthCounter.getMonthCount() && this.getDayCount() == otherMonthCounter.getDayCount();
    }
        
    @Override
    public String toString(){
        int month = getMonthCount();
        int year = month / MONTH_IN_YEAR;
        int modMonth = month % MONTH_IN_YEAR;
        
        String result = "";
        if (month != 0) {
            if (modMonth == 0) {
                result = year + " " + GeneralConfig.getInstance().getTitleFor("year");
            } else {
                result = month + " " + GeneralConfig.getInstance().getTitleFor("month");
            }
        }
        return result;
    }
}
