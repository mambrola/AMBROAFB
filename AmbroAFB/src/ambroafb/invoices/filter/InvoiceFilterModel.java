/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.invoices.filter;

import ambroafb.clients.Client;
import ambroafb.clients.filter.ClientFilterModel;
import ambroafb.general.DateConverter;
import ambroafb.general.FilterModel;
import ambroafb.invoices.helper.InvoiceReissuing;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;

/**
 *
 * @author dato
 */
public class InvoiceFilterModel extends FilterModel {
    
    private Client selectedClient;
    private ObservableList<InvoiceReissuing> selectedReissuings;
    
    private static final String PREF_START_DATE_FROM_KEY = "invoices/filter/start_date_from";
    private static final String PREF_START_DATE_TO_KEY = "invoices/filter/start_date_to";
    private static final String PREF_END_DATE_FROM_KEY = "invoices/filter/end_date_from";
    private static final String PREF_END_DATE_TO_KEY = "invoices/filter/end_date_to";
    private static final String PREF_SELECTED_CLIENT_INDEX_KEY = "invoices/filter/selected_client_index";
    private static final String PREF_CHECKED_REISSUING_INDEXES_KEY = "invoices/filter/checked_invoice_reissuing_indexes";
    
    public static final String DATE_BIGGER = "1970-01-01";
    public static final String DATE_LESS = "9999-01-01";
    
    public InvoiceFilterModel(){
        
    }

    public void setStartDateFrom(LocalDate date) {
        saveIntoPref(PREF_START_DATE_FROM_KEY, (date == null) ? "" : date.toString());
    }

    public void setStartDateTo(LocalDate date) {
        saveIntoPref(PREF_START_DATE_TO_KEY, (date == null) ? "" : date.toString());
    }
    
    public void setEndDateFrom(LocalDate date){
        saveIntoPref(PREF_END_DATE_FROM_KEY, (date == null) ? "" : date.toString());
    }
    
    public void setEndDateTo(LocalDate date){
        saveIntoPref(PREF_END_DATE_TO_KEY, (date == null) ? "" : date.toString());
    }
    
    public void setSelectedClient(Client client){
        selectedClient = client;
    }
    
    public void setSelectedClientIndex(int index){
        saveIntoPref(PREF_SELECTED_CLIENT_INDEX_KEY, index);
    }
    
    public void setCheckedReissuingsIndexes(ObservableList<Integer> checkedIndexes){
        saveIntoPref(PREF_CHECKED_REISSUING_INDEXES_KEY, (checkedIndexes == null || checkedIndexes.isEmpty()) ? "[]" : checkedIndexes.toString());
    }
    
    public void setCheckedReissuings(ObservableList<InvoiceReissuing> reissuings){
        selectedReissuings = reissuings;
    }

    
    // If date from pref is empty, datepicker value will empty.
    public LocalDate getStartDate(boolean isFromDate){
        String date = getStringFromPref((isFromDate) ? PREF_START_DATE_FROM_KEY : PREF_START_DATE_TO_KEY);
        return DateConverter.getInstance().parseDate(date);
    }
    
    // If date from pref is empty, datebase will give default localDate min value.
    public LocalDate getStartDateForDB(boolean isFromDate){
        LocalDate date = getStartDate(isFromDate);
        if (date == null){
            date = DateConverter.getInstance().parseDate((isFromDate) ? DATE_BIGGER : DATE_LESS);
        }
        return date;
    }
    
    public LocalDate getEndDate(boolean isFromDate){
        String date = getStringFromPref((isFromDate) ? PREF_END_DATE_FROM_KEY : PREF_END_DATE_TO_KEY);
        return DateConverter.getInstance().parseDate(date);
    }
    
    public LocalDate getEndDateForDB(boolean isFromDate){
        LocalDate date = getEndDate(isFromDate);
        if (date == null){
            date = DateConverter.getInstance().parseDate((isFromDate) ? DATE_BIGGER : DATE_LESS);
        }
        return date;
    }
    
    public int getSelectedClientIndex(){
        return getIntFromPref(PREF_SELECTED_CLIENT_INDEX_KEY);
    }
    
    public Client getSelectedClient(){
        return selectedClient;
    }
    
    public ArrayList<Integer> getCheckedReissuingsIndexes(){
        ObjectMapper mapper = new ObjectMapper();
        try {
            String indexes = getStringFromPref(PREF_CHECKED_REISSUING_INDEXES_KEY);
            if (indexes != null){ // for the first case, if pref not include appropriate key
                return mapper.readValue(indexes, new TypeReference<ArrayList<Integer>>(){});
            }
        } catch (IOException ex) {
            Logger.getLogger(ClientFilterModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<>();
    }
    
    public ObservableList<InvoiceReissuing> getCheckedReissuings(){
        return selectedReissuings;
    }

    public boolean isSelectedConcreteClient() {
        return getIntFromPref(PREF_SELECTED_CLIENT_INDEX_KEY) > 0;
    }
    
    public boolean hasSelectedReissuings(){
        return (selectedReissuings != null && !selectedReissuings.isEmpty());
    }
}
