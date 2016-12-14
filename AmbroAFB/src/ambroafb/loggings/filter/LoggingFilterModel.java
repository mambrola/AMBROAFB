/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.loggings.filter;

import ambroafb.clients.Client;
import ambroafb.general.DateConverter;
import ambroafb.general.FilterModel;
import java.time.LocalDate;

/**
 *
 * @author dato
 */
public class LoggingFilterModel extends FilterModel {

    private static final String DATE_BIGGER = "1970-01-01";
    private static final String DATE_LESS = "9999-01-01";
    
    private static final String PREF_BIGGER_DATE_KEY = "logging/filter/dateBigger";
    private static final String PREF_LESS_DATE_KEY = "logging/filter/dateLess";
    private static final String PREF_CLIENT_KEY = "logging/filter/selected_client_index";
    
    private Client selectedClient;
    
    public LoggingFilterModel(){
        
    }

    public void setFromDate(LocalDate date) {
        saveIntoPref(PREF_BIGGER_DATE_KEY, (date == null) ? "" : date.toString());
    }

    public void setToDate(LocalDate date) {
        saveIntoPref(PREF_LESS_DATE_KEY, (date == null) ? "" : date.toString());
    }
    
    public void setSelectedClient(Client client) {
        selectedClient = client;
    }

    public void setSelectedClientIndex(int selectedIndex) {
        saveIntoPref(PREF_CLIENT_KEY, selectedIndex);
    }
    
    
    /**
     * @return The method return date object that user selected on datePicker.
     */
    public LocalDate getFromDate(){
        String date = getStringFromPref(PREF_BIGGER_DATE_KEY);
        return DateConverter.getInstance().parseDate(date);
    }
    
    /**
     * If user select to nothing in datePicker, then must return specified Object which is min date;
     * @return 
     */
    public LocalDate getFromDateForDB(){
        LocalDate fromDate = getFromDate();
        if (fromDate == null){
            fromDate = DateConverter.getInstance().parseDate(DATE_BIGGER);
        }
        System.out.println("from date: " + fromDate);
        return fromDate;
    }
    
    /**
     * @return The method return date object that user selected on datePicker.
     */
    public LocalDate getToDate(){
        String date = getStringFromPref(PREF_LESS_DATE_KEY);
        return DateConverter.getInstance().parseDate(date);
    }
    
    /**
     * If user select to nothing in datePicker, then must return specified Object which is max date;
     * @return 
     */
    public LocalDate getToDateForDB(){
        LocalDate toDate = getToDate();
        if (toDate == null){
            toDate = DateConverter.getInstance().parseDate(DATE_LESS);
        }
        System.out.println("toDate: " + toDate);
        return toDate;
    }
    
    public int getSelectedClientIndex(){
        return getIntFromPref(PREF_CLIENT_KEY);
    }
    
    public Client getSelectedClient(){
        return selectedClient;
    }

    public boolean isSelectedConcreteClient() {
        return selectedClient != null && selectedClient.getRecId() > 0;
    }
}
