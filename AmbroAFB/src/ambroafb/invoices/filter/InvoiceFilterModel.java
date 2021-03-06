/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.invoices.filter;

import ambroafb.clients.Client;
import ambroafb.clients.filter.ClientFilterModel;
import ambroafb.general.DateConverter;
import ambroafb.general.interfaces.FilterModel;
import ambroafb.invoices.helper.InvoiceReissuing;
import ambroafb.invoices.helper.InvoiceStatusClarify;
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
    
    private ObservableList<InvoiceReissuing> selectedReissuings;
    private ObservableList<InvoiceStatusClarify> selectedClarifies;
    
    private static final String PREF_START_DATE_FROM_KEY = "invoices/filter/start_date_from";
    private static final String PREF_START_DATE_TO_KEY = "invoices/filter/start_date_to";
    private static final String PREF_END_DATE_FROM_KEY = "invoices/filter/end_date_from";
    private static final String PREF_END_DATE_TO_KEY = "invoices/filter/end_date_to";
    private static final String PREF_CHECKED_REISSUING_INDEXES_KEY = "invoices/filter/checked_invoice_reissuing_indexes";
    private static final String PREF_CHECKED_CLARIFY_INDEXES_KEY = "invoices/filter/checked_invoice_clarifies_indexes";
    private static final String PREF_CLIENT_KEY = "invoices/filter/client_id";
    
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
        if (client != null){
            saveIntoPref(PREF_CLIENT_KEY, client.getRecId());
        }
    }
    
    public void setCheckedReissuingsIndexes(ObservableList<Integer> checkedIndexes){
        saveIntoPref(PREF_CHECKED_REISSUING_INDEXES_KEY, checkedIndexes.toString());
    }
    
    public void setCheckedReissuings(ObservableList<InvoiceReissuing> reissuings){
        selectedReissuings = reissuings;
    }
    
    public void setCheckedClarifiesIndexes(ObservableList<Integer> checkedIndexes){
        saveIntoPref(PREF_CHECKED_CLARIFY_INDEXES_KEY, checkedIndexes.toString());
    }

    public void setCheckedClarifies(ObservableList<InvoiceStatusClarify> clarifies) {
        this.selectedClarifies = clarifies;
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
    
    public int getSelectedClientId(){
        return getIntFromPref(PREF_CLIENT_KEY);
    }
    
    public ArrayList<Integer> getCheckedReissuingsIndexes(){
        String indexes = getStringFromPref(PREF_CHECKED_REISSUING_INDEXES_KEY);
        return getListFor(indexes);
    }
    
    public ObservableList<InvoiceReissuing> getCheckedReissuings(){
        return selectedReissuings;
    }
    
    public ArrayList<Integer> getCheckedClarifiesIndexes(){
        String indexes = getStringFromPref(PREF_CHECKED_CLARIFY_INDEXES_KEY);
        return getListFor(indexes);
    }
    
    private ArrayList<Integer> getListFor(String indexes){
        ObjectMapper mapper = new ObjectMapper();
        try {
            if (indexes != null){ // for the first case, if pref not include appropriate key
                return mapper.readValue(indexes, new TypeReference<ArrayList<Integer>>(){});
            }
        } catch (IOException ex) {
            Logger.getLogger(ClientFilterModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<>();
    }
    
    public ObservableList<InvoiceStatusClarify> getCheckedClarifies(){
        return selectedClarifies;
    }

    public boolean isSelectedConcreteClient() {
        return getSelectedClientId() > 0;
    }
    
    public boolean hasSelectedReissuings(){
        return (selectedReissuings != null && !selectedReissuings.isEmpty());
    }
    
    public boolean hasSelectedClarifies(){
        return (selectedClarifies != null && !selectedClarifies.isEmpty());
    }
}
