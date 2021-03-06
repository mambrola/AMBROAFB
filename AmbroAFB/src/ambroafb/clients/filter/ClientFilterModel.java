/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.clients.filter;

import ambroafb.clients.helper.ClientStatus;
import ambroafb.countries.Country;
import ambroafb.countries.CountryComboBox;
import ambroafb.general.DateConverter;
import ambroafb.general.interfaces.DateTimeFilterModel;
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
public class ClientFilterModel extends DateTimeFilterModel {
    
    private static final String PREF_FROM_DATE_KEY = "clients/filter/from_date";
    private static final String PREF_TO_DATE_KEY = "clients/filter/to_date";
    private static final String PREF_TO_COUNTRY_KEY = "clients/filter/country";
    private static final String PREF_SELECTED_STATUSES_KEY = "clients/filter/statuses_indexes";
    private static final String PREF_JURIDICAL_KEY = "clients/filter/juridical";
    private static final String PREF_JURIDICAL_INDETERMINATE_KEY = "clients/filter/juridical_indeterminate";
    private static final String PREF_REZIDENT_KEY = "clients/filter/rezident";
    private static final String PREF_REZIDENT_INDETERMINATE_KEY = "clients/filter/rezident_indeterminate";
    private static final String PREF_TYPE_KEY = "clients/filter/type";
    private static final String PREF_TYPE_INDETERMINATE_KEY = "clients/filter/type_indeterminate";
    
    
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

    public void setSelectedCountry(Country value) {
        if (value != null){
            saveIntoPref(PREF_TO_COUNTRY_KEY, value.getCode());
        }
    }

    public void setRezidentSelected(boolean selected) {
        saveIntoPref(PREF_REZIDENT_KEY, selected);
    }

    public void setRezidentIndeterminate(boolean indeterminate) {
        saveIntoPref(PREF_REZIDENT_INDETERMINATE_KEY, indeterminate);
    }

    public void setTypeIndeterminate(boolean indeterminate) {
        saveIntoPref(PREF_TYPE_INDETERMINATE_KEY, indeterminate);
    }

    public void setTypeSelected(boolean selected) {
        saveIntoPref(PREF_TYPE_KEY, selected);
    }

    public void setSelectedStatusesIndexes(ObservableList<Integer> checkedIndexes) {
        saveIntoPref(PREF_SELECTED_STATUSES_KEY, (checkedIndexes == null || checkedIndexes.isEmpty()) ? "[]" : checkedIndexes.toString());
    }
    
    public void setSelectedStatuses(ObservableList<ClientStatus> checkedItems){
        selectedStatuses = checkedItems;
    }
    
    
    @Override
    public LocalDate getFromDate(){
        String date = getStringFromPref(PREF_FROM_DATE_KEY);
        return DateConverter.getInstance().parseDate(date);
    }
    
    @Override
    public LocalDate getToDate(){
        String date = getStringFromPref(PREF_TO_DATE_KEY);
        return DateConverter.getInstance().parseDate(date);
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

    public boolean isTypeIndeterminate() {
        return getBooleanFromPref(PREF_TYPE_INDETERMINATE_KEY);
    }

    public boolean isTypeSelected() {
        return getBooleanFromPref(PREF_TYPE_KEY);
    }
    
    public String getSelectedCountryCode(){
        return getStringFromPref(PREF_TO_COUNTRY_KEY);
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
        String selectedCountryCode = getSelectedCountryCode();
        return selectedCountryCode != null && !selectedCountryCode.equals(CountryComboBox.categoryALL);
    }
    
    public boolean hasSelectedStatuses(){
        return (selectedStatuses != null && !selectedStatuses.isEmpty());
    }
}
