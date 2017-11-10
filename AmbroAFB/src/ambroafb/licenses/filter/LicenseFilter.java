/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.licenses.filter;

import ambroafb.clients.Client;
import ambroafb.clients.ClientComboBox;
import ambroafb.general.SceneUtils;
import ambroafb.general.interfaces.FilterModel;
import ambroafb.general.interfaces.Filterable;
import ambroafb.general.interfaces.UserInteractiveFilterStage;
import ambroafb.general.okay_cancel.FilterOkayCancelController;
import ambroafb.licenses.helper.LicenseStatus;
import ambroafb.licenses.helper.LicenseStatusCheckComboBox;
import ambroafb.products.Product;
import ambroafb.products.ProductComboBox;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.stage.Stage;

/**
 *
 * @author Dato
 */
public class LicenseFilter extends UserInteractiveFilterStage implements Filterable, Initializable {
    
    @FXML
    private ClientComboBox clients;
    @FXML
    private ProductComboBox products;
    @FXML
    private LicenseStatusCheckComboBox statuses;
    @FXML
    private CheckBox extraDays;
    @FXML
    private FilterOkayCancelController okayCancelController;
    
    private final LicenseFilterModel filterModel = new LicenseFilterModel();
    
    public LicenseFilter(Stage owner){
        super(owner, "licenses");
        
        Scene scene = SceneUtils.createScene("/ambroafb/licenses/filter/LicenseFilter.fxml", (LicenseFilter)this);
        this.setScene(scene);
    }

    @Override
    public FilterModel getResult() {
        showAndWait();
        return filterModel;
    }

    @Override
    public void setResult(boolean isOk) {
        if (!isOk) {
            filterModel.changeModelAsEmpty();
        }
        else {
            filterModel.setSelectedClient(clients.getValue());
            filterModel.setSelectedProduct(products.getValue());
            filterModel.setSelectedStatuses(statuses.getCheckModel().getCheckedItems());
            filterModel.setSelectedStatusIndexes(statuses.getCheckModel().getCheckedIndices());
            if (extraDays.isIndeterminate()){
                filterModel.withAndWithoutExtraDays(true);
            }
            else {
                filterModel.onlyExtraDays(extraDays.isSelected());
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Consumer<ObservableList<Client>> clientConsumer = (clientList) -> {
            Optional<Client> optClient = clientList.stream().filter((client) -> client.getRecId() == filterModel.getSelectedClientId()).findFirst();
            if (optClient.isPresent()){
                clients.valueProperty().set(optClient.get());
            }
        };
        clients.fillComboBoxOnlyClientsWithALL(clientConsumer);
        
        Consumer<ObservableList<Product>> productConsumer = (productList) -> {
            Optional<Product> optProduct = productList.stream().filter((product) -> product.getRecId() == filterModel.getSelectedProductId()).findFirst();
            if (optProduct.isPresent()){
                products.valueProperty().set(optProduct.get());
            }
        };
        products.fillComboBoxWithALL(productConsumer);
        
        Consumer<ObservableList<LicenseStatus>> selectSaveStatuses = (statusList) -> {
            filterModel.getSelectedStatusIndexes().stream().forEach((index) -> {
                statuses.getCheckModel().check(index);
            });
        };
        statuses.fillComboBox(selectSaveStatuses);
        
        // The order of these bellow command is importent.
        extraDays.setSelected(filterModel.onlyExtraDays());
        extraDays.setIndeterminate(filterModel.withAndWithoutExtraDays());
        
    }

    @Override
    protected FilterOkayCancelController getOkayCancelController() {
        return okayCancelController;
    }
    
}
