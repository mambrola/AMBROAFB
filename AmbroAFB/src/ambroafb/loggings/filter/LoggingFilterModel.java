/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.loggings.filter;

import ambroafb.clients.Client;
import ambroafb.general.DateConverter;
import ambroafb.general.interfaces.DateTimeFilterModel;
import java.time.LocalDate;

/**
 *
 * @author dato
 */
public class LoggingFilterModel extends DateTimeFilterModel {

    private static final String PREF_BIGGER_DATE_KEY = "logging/filter/dateBigger";
    private static final String PREF_LESS_DATE_KEY = "logging/filter/dateLess";
    private static final String PREF_CLIENT_KEY = "logging/filter/client_id";
    
    public LoggingFilterModel(){
        
    }

    public void setFromDate(LocalDate date) {
        saveIntoPref(PREF_BIGGER_DATE_KEY, (date == null) ? "" : date.toString());
    }

    public void setToDate(LocalDate date) {
        saveIntoPref(PREF_LESS_DATE_KEY, (date == null) ? "" : date.toString());
    }
    
    public void setSelectedClient(Client client) {
        if (client != null){
            saveIntoPref(PREF_CLIENT_KEY, client.getRecId());
        }
    }

    
    /**
     * @return The method return date object that user selected on datePicker.
     */
    @Override
    public LocalDate getFromDate(){
        String date = getStringFromPref(PREF_BIGGER_DATE_KEY);
        return DateConverter.getInstance().parseDate(date);
    }
    
    /**
     * @return The method return date object that user selected on datePicker.
     */
    @Override
    public LocalDate getToDate(){
        String date = getStringFromPref(PREF_LESS_DATE_KEY);
        return DateConverter.getInstance().parseDate(date);
    }
    
    public int getSelectedClientId(){
        return getIntFromPref(PREF_CLIENT_KEY);
    }

    public boolean isSelectedConcreteClient() {
        return getSelectedClientId() > 0;
    }
}
