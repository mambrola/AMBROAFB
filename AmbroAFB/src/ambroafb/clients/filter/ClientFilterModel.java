/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.clients.filter;

import ambroafb.clients.helper.ClientStatus;
import ambroafb.countries.Country;
import ambroafb.general.DateConverter;
import ambroafb.general.FilterModel;
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
public class ClientFilterModel extends FilterModel {
    
    private static final String DEFAULT_DATE_FROM = "1970-01-01";
    private static final String DEFAULT_DATE_LESS = "9999-01-01";
    
    private static final String PREF_FROM_DATE_KEY = "clients/filter/from_date";
    private static final String PREF_TO_DATE_KEY = "clients/filter/to_date";
    private static final String PREF_JURIDICAL_KEY = "clients/filter/juridical";
    private static final String PREF_JURIDICAL_INDETERMINATE_KEY = "clients/filter/juridical_indeterminate";
    private static final String PREF_COUNTRY_KEY = "clients/filter/country/index";
    private static final String PREF_REZIDENT_KEY = "clients/filter/rezident";
    private static final String PREF_REZIDENT_INDETERMINATE_KEY = "clients/filter/rezident_indeterminate";
    private static final String PREF_SELECTED_STATUSES_KEY = "clients/filter/statuses_indexes";
    
    
    private Country selectedCountry;
    private ObservableList<ClientStatus> selectedStatuses;
    
    public ClientFilterModel(){
        
    }

    public void setFromDate(LocalDate date) {
        saveIntoPref(PREF_FROM_DATE_KEY, (date == null) ? "" : date.toString());
    }

    public void setToDate(LocalDate date) {
        saveIntoPref(PREF_TO_DATE_KEY, (date == null) ? "" : date.toString());
    }

    public void setJuridicalIndeterminate(boolean indeterminate) {
        saveIntoPref(PREF_JURIDICAL_INDETERMINATE_KEY, indeterminate);
    }

    public void setJuridicalSelected(boolean selected) {
        saveIntoPref(PREF_JURIDICAL_KEY, selected);
    }

    public void setSelectedCountryIndex(int selectedIndex) {
        saveIntoPref(PREF_COUNTRY_KEY, selectedIndex);
    }

    public void setSelectedCountry(Country value) {
        selectedCountry = value;
    }

    public void setRezidentSelected(boolean selected) {
        saveIntoPref(PREF_REZIDENT_KEY, selected);
    }

    public void setRezidentIndeterminate(boolean indeterminate) {
        saveIntoPref(PREF_REZIDENT_INDETERMINATE_KEY, indeterminate);
    }

    public void setSelectedStatusesIndexes(ObservableList<Integer> checkedIndexes) {
        saveIntoPref(PREF_SELECTED_STATUSES_KEY, (checkedIndexes == null || checkedIndexes.isEmpty()) ? "[]" : checkedIndexes.toString());
    }
    
    public void setSelectedStatuses(ObservableList<ClientStatus> checkedItems){
        selectedStatuses = checkedItems;
    }
    
    
    public LocalDate getFromDate(){
        String date = getStringFromPref(PREF_FROM_DATE_KEY);
        return DateConverter.getInstance().parseDate(date);
    }
    
    public LocalDate getFromDateForDB(){
        LocalDate date = getFromDate();
        if (date == null){
            date = DateConverter.getInstance().parseDate(DEFAULT_DATE_FROM);
        }
        return date;
    }
    
    public LocalDate getToDate(){
        String date = getStringFromPref(PREF_TO_DATE_KEY);
        return DateConverter.getInstance().parseDate(date);
    }
    
    public LocalDate getToDateForDB(){
        LocalDate date = getToDate();
        if (date == null){
            date = DateConverter.getInstance().parseDate(DEFAULT_DATE_LESS);
        }
        return date;
    }
    
    public boolean isJuridicalIndeterminate(){
        return getBooleanFromPref(PREF_JURIDICAL_INDETERMINATE_KEY);
    }
    
    public boolean isJuridicalSelected(){
        return getBooleanFromPref(PREF_JURIDICAL_KEY);
    }
    
    public boolean isRezidentIndeterminate(){
        return getBooleanFromPref(PREF_REZIDENT_INDETERMINATE_KEY);
    }
    
    public boolean isRezidentSelected(){
        return getBooleanFromPref(PREF_REZIDENT_KEY);
    }
    
    public int getSelectedCountryIndex(){
        return getIntFromPref(PREF_COUNTRY_KEY);
    }
    
    public Country getSelectedCountry(){
        return selectedCountry;
    }
    
    public ArrayList<Integer> getSelectedStatusesIndexes(){
        ObjectMapper mapper = new ObjectMapper();
        try {
            String indexes = getStringFromPref(PREF_SELECTED_STATUSES_KEY);
            if (indexes != null){  // for the first case, if pref not include appropriate key
                return mapper.readValue(indexes, new TypeReference<ArrayList<Integer>>(){});
            }
        } catch (IOException ex) {
            Logger.getLogger(ClientFilterModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<>();
    }
    
    public ObservableList<ClientStatus> getSelectedStatuses(){
        return selectedStatuses;
    }

    public boolean isSelectedConcreteCountry() {
        return getIntFromPref(PREF_COUNTRY_KEY) > 0;
    }
    
    public boolean hasSelectedStatuses(){
        return (selectedStatuses != null && !selectedStatuses.isEmpty());
    }
}
