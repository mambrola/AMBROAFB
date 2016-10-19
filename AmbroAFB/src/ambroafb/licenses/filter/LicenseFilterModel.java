/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.licenses.filter;

import ambroafb.clients.Client;
import ambroafb.clients.ClientComboBox;
import ambroafb.general.FilterModel;
import ambroafb.licenses.License;
import ambroafb.licenses.helper.LicenseStatus;
import ambroafb.products.Product;
import ambroafb.products.ProductComboBox;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author dato
 */
public class LicenseFilterModel extends FilterModel {

    private final ObjectProperty<Client> clientProp;

    private static final String clientPrefKey = "licenses/filter/client_id";
    private static final String productPrefKey = "licenses/filter/product_id";
    private static final String statusesPrefKey = "licenses/filter/status_ids";
    private static final String extraDaysPrefKey = "licenses/filter/extraDays";

    public LicenseFilterModel() {
        clientProp = new SimpleObjectProperty<>();
    }

    public ObjectProperty<Client> clientProperty() {
        return clientProp;
    }

    public void setClient(Client client) {
        if (client != null) {
            saveIntoPref(clientPrefKey, client.getRecId());
        }
    }

    public void setProduct(Product product) {
        if (product != null) {
            saveIntoPref(productPrefKey, product.getRecId());
        }
    }

    public void setStatus(ObservableList<LicenseStatus> statuses) {
        if (statuses != null) {
            ArrayList<Integer> statusIDs = (ArrayList<Integer>) statuses.stream().map((LicenseStatus status) -> status.getLicenseStatusId())
                    .collect(Collectors.toList());
            saveIntoPref(statusesPrefKey, statusIDs.toString());
        }
    }

    public void setExtraDays(boolean extraDays) {
        saveIntoPref(extraDaysPrefKey, extraDays);
    }

    public Client getClient() {
        Client client = null;
        int clientId = getIntFromPref(clientPrefKey);
        if (clientId == 0) {
            client = ClientComboBox.clientALL;
        }
        else if (clientId > 0) {
            client = Client.getOneFromDB(clientId);
        }
        return client;
    }

    public Product getProduct() {
        Product product = null;
        int productId = getIntFromPref(productPrefKey);
        if (productId == 0){
            product = ProductComboBox.productALL;
        }
        else if (productId > 0) {
            product = Product.getOneFromDB(productId);
        }
        return product;
    }

    public ObservableList<LicenseStatus> getStatuses() {
        ObservableList<LicenseStatus> statuses = FXCollections.observableArrayList();
        try {
            String statusIds = getStringFromPref(statusesPrefKey);
            if (statusIds != null) {
                ObjectMapper mapper = new ObjectMapper();
                ArrayList<Integer> ids = mapper.readValue(statusIds, new TypeReference<ArrayList<Integer>>(){});
                ids.stream().forEach((statusId) -> {
                    statuses.add(License.getLicenseStatusFromDB(statusId));
                });
            }
        } catch (IOException ex) {
            Logger.getLogger(LicenseFilterModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return statuses;
    }

    public boolean areExtraDays() {
        return getBooleanFromPref(extraDaysPrefKey);
    }
}
