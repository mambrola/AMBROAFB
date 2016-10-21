/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.licenses.filter;

import ambroafb.general.FilterModel;
import ambroafb.licenses.helper.LicenseStatus;
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

    private static final String clientPrefKey = "licenses/filter/client_id";
    private static final String productPrefKey = "licenses/filter/product_id";
    private static final String statusesPrefKey = "licenses/filter/status_ids";
    private static final String extraDaysPrefKey = "licenses/filter/extraDays";

    private ObservableList<LicenseStatus> checkedStatuses;
    
    private final int indeterminate = 2;
    private final int isSelectedExtraDays = 1;
    private final int notSelectedExtraDays = 0;
    
    public LicenseFilterModel() {
        checkedStatuses = FXCollections.observableArrayList();
    }

//    public void setSelectedClient(Client client) {
//        if (client != null) {
//            saveIntoPref(clientPrefKey, client.getRecId());
//        }
//    }
    
    public void setSelectedClientId(int clientId) {
        saveIntoPref(clientPrefKey, clientId);
    }

//    public void setSelectedProduct(Product product) {
//        if (product != null) {
//            saveIntoPref(productPrefKey, product.getRecId());
//        }
//    }
    
    public void setSelectedProductId(int productId) {
        saveIntoPref(productPrefKey, productId);
    }

    public void setSelectedStatuses(ObservableList<LicenseStatus> statuses) {
        if (statuses != null) {
            checkedStatuses = statuses;
        }
    }
    
    public void setSelectedStatusIndexes(List<Integer> indexes){
        if (indexes != null){
            saveIntoPref(statusesPrefKey, indexes.toString());
        }
    }

    public void onlyExtraDays(boolean selected){
        saveIntoPref(extraDaysPrefKey, (selected) ? isSelectedExtraDays : notSelectedExtraDays);
    }
    
    public void withAndWithoutExtraDays(boolean isIndeterminate){
        saveIntoPref(extraDaysPrefKey, indeterminate);
    }

//    /**
//     * The method provides to get a client object which is selected from filter.
//     * @return  If client does not select from filter - null;
//     *          If selected is ALL - client object which DB id is 0 and name is "ALL";
//     *          If selected concrete client - an appropriate client object.
//     */
//    public Client getSelectedClient() {
//        Client client = null;
//        int clientId = getIntFromPref(clientPrefKey);
//        if (clientId == 0) {
//            client = ClientComboBox.clientALL;
//        }
//        else if (clientId > 0) {
//            client = Client.getOneFromDB(clientId);
//        }
//        return client;
//    }

    public int getSelectedClientId() {
        return getIntFromPref(clientPrefKey);
    }
    
//    /**
//     * The method provides to get a product object which is selected from filter.
//     * @return  If product does not select from filter - null;
//     *          If selected is ALL - product object which DB id is 0 and name is "ALL";
//     *          If selected concrete product - an appropriate product object.
//     */
//    public Product getSelectedProduct() {
//        Product product = null;
//        int productId = getIntFromPref(productPrefKey);
//        if (productId == 0){
//            product = ProductComboBox.productALL;
//        }
//        else if (productId > 0) {
//            product = Product.getOneFromDB(productId);
//        }
//        return product;
//    }
    
    public int getSelectedProductId() {
        return getIntFromPref(productPrefKey);
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
            String statusIds = getStringFromPref(statusesPrefKey);
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
        return getIntFromPref(extraDaysPrefKey) == isSelectedExtraDays;
    }
    
    public boolean withAndWithoutExtraDays(){
        return getIntFromPref(extraDaysPrefKey) == indeterminate;
    }
}
