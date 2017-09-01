/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.licenses.filter;

import ambroafb.clients.ClientComboBox;
import ambroafb.general.FilterModel;
import ambroafb.general.Names;
import ambroafb.general.SceneUtils;
import ambroafb.general.interfaces.Filterable;
import ambroafb.general.okay_cancel.FilterOkayCancelController;
import ambroafb.general.interfaces.UserInteractiveStage;
import ambroafb.licenses.License;
import ambroafb.licenses.helper.LicenseStatus;
import ambroafb.products.ProductComboBox;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.controlsfx.control.CheckComboBox;

/**
 *
 * @author Dato
 */
public class LicenseFilter extends UserInteractiveStage implements Filterable, Initializable {
    
    @FXML
    private ClientComboBox clients;
    @FXML
    private ProductComboBox products;
    @FXML
    private CheckComboBox<LicenseStatus> statuses;
    @FXML
    private CheckBox extraDays;
    @FXML
    private FilterOkayCancelController okayCancelController;
    
    private final LicenseFilterModel filterModel = new LicenseFilterModel();
    
    public LicenseFilter(Stage owner){
        super(owner, Names.LEVEL_FOR_PATH, "licenses", "/images/filter.png");
        
        Scene scene = SceneUtils.createScene("/ambroafb/licenses/filter/LicenseFilter.fxml", (LicenseFilter)this);
        this.setScene(scene);

        onCloseRequestProperty().set((EventHandler<WindowEvent>) (WindowEvent event) -> {
            okayCancelController.cancel(null);
            if(event != null) event.consume();
        });
        
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
        ArrayList<LicenseStatus> licenseStatuses = License.getAllLicenseStatusFromDB();
        licenseStatuses.sort((LicenseStatus status1, LicenseStatus status2) -> status1.getRecId() - status2.getRecId());
        statuses.getItems().setAll(licenseStatuses);
        
        filterModel.getSelectedStatusIndexes().stream().forEach((index) -> {
            statuses.getCheckModel().check(index);
        });
        
        // The order of these bellow command are importent.
        extraDays.setSelected(filterModel.onlyExtraDays());
        extraDays.setIndeterminate(filterModel.withAndWithoutExtraDays());
        
    }
    
}