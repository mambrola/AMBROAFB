/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.licenses.filter;

import ambroafb.clients.Client;
import ambroafb.general.interfaces.FilterModel;
import ambroafb.licenses.helper.LicenseStatus;
import ambroafb.products.Product;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author dato
 */
public class LicenseFilterModel extends FilterModel {

    private static final String PREF_STATUS_KEY = "licenses/filter/status_ids";
    private static final String PREF_EXTRA_DAYS_KEY = "licenses/filter/extraDays";

    private ObservableList<LicenseStatus> checkedStatuses;
    private Client selectedClient;
    private Product selectedProduct;
    
    private final int indeterminate = 2;
    private final int isSelectedExtraDays = 1;
    private final int notSelectedExtraDays = 0;
    
    public LicenseFilterModel() {
        checkedStatuses = FXCollections.observableArrayList();
    }

    public void setSelectedClient(Client selectedItem) {
        selectedClient = selectedItem;
    }

    public void setSelectedProduct(Product selectedItem){
        selectedProduct = selectedItem;
    }

    public void setSelectedStatuses(ObservableList<LicenseStatus> statuses) {
        if (statuses != null) {
            checkedStatuses = statuses;
        }
    }
    
    public void setSelectedStatusIndexes(List<Integer> indexes){
        if (indexes != null){
            saveIntoPref(PREF_STATUS_KEY, indexes.toString());
        }
    }

    public void onlyExtraDays(boolean selected){
        saveIntoPref(PREF_EXTRA_DAYS_KEY, (selected) ? isSelectedExtraDays : notSelectedExtraDays);
    }
    
    public void withAndWithoutExtraDays(boolean isIndeterminate){
        saveIntoPref(PREF_EXTRA_DAYS_KEY, indeterminate);
    }

    public Client getSelectedClient(){
        return selectedClient;
    }
    
    public Product getSelectedProduct(){
        return selectedProduct;
    }

    /**
     * @return Selected status objects list from filter.
     * Note: If no license status is selected from filter, then list will be empty.
     */
    public ObservableList<LicenseStatus> getSelectedStatuses(){
        return checkedStatuses;
    }
    
    /**
     * @return The selected status indexes from filter comboBox.
     */
    public List<Integer> getSelectedStatusIndexes() {
        try {
            String statusIds = getStringFromPref(PREF_STATUS_KEY);
            if (statusIds != null) {
                ObjectMapper mapper = new ObjectMapper();
                return mapper.readValue(statusIds, new TypeReference<ArrayList<Integer>>(){});
            }
        } catch (IOException ex) {
            Logger.getLogger(LicenseFilterModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<>();
    }

    public boolean onlyExtraDays(){
        return getIntFromPref(PREF_EXTRA_DAYS_KEY) == isSelectedExtraDays;
    }
    
    public boolean withAndWithoutExtraDays(){
        return getIntFromPref(PREF_EXTRA_DAYS_KEY) == indeterminate;
    }
    
    public boolean isSelectedConcreteClient(){
        return selectedClient != null && selectedClient.getRecId() > 0;
    }
    
    public boolean isSelectedConcreteProduct(){
        return selectedProduct != null && selectedProduct.getRecId() > 0;
    }
}
