/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.licenses.filter;

import ambroafb.clients.ClientComboBox;
import ambroafb.general.FilterModel;
import ambroafb.general.GeneralConfig;
import ambroafb.general.Names;
import ambroafb.general.SceneUtils;
import ambroafb.general.StageUtils;
import ambroafb.general.StagesContainer;
import ambroafb.general.interfaces.Filterable;
import ambroafb.general.okay_cancel.FilterOkayCancelController;
import ambroafb.licenses.License;
import ambroafb.licenses.helper.LicenseStatus;
import ambroafb.products.ProductComboBox;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import org.controlsfx.control.CheckComboBox;
import org.json.JSONObject;

/**
 *
 * @author Dato
 */
public class LicenseFilter extends Stage implements Filterable, Initializable {
    
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
    
    private final JSONObject jsonResult = new JSONObject();
    private final LicenseFilterModel filterModel = new LicenseFilterModel();
    
    public LicenseFilter(Stage owner){
        StagesContainer.registerStageByOwner(owner, Names.LEVEL_FOR_PATH, (Stage)this);
        
        this.initStyle(StageStyle.UNIFIED);
        this.setTitle(GeneralConfig.getInstance().getTitleFor("license_filter"));
        Scene scene = SceneUtils.createScene("/ambroafb/licenses/filter/LicenseFilter.fxml", (LicenseFilter)this);
        this.setScene(scene);
        this.initOwner(owner);
        this.setResizable(false);

        onCloseRequestProperty().set((EventHandler<WindowEvent>) (WindowEvent event) -> {
            okayCancelController.cancel(null);
            if(event != null) event.consume();
        });
        
        StageUtils.centerChildOf(owner, (Stage)this);
        StageUtils.followChildTo(owner, (Stage)this);
    }

    @Override
    public JSONObject getResult() {
        showAndWait();
        return jsonResult;
    }

    @Override
    public void setResult(boolean isOk) {
        if (!isOk) {
            filterModel.changeModelAsEmpty();
        }
        else {
            filterModel.setClient(clients.getSelectionModel().getSelectedItem());
            filterModel.setProduct(products.getSelectionModel().getSelectedItem());
            filterModel.setStatus(statuses.getCheckModel().getCheckedItems());
            filterModel.setExtraDays(extraDays.isSelected());
        }
    }

    @Override
    public FilterModel getFilterResult() {
        showAndWait();
        return filterModel;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        statuses.getItems().setAll(License.getAllLicenseStatusFromDB()); // calke klasad
        
//        clients.setValue(filterModel.getClient());
        
//        clients.getEditor().setPromptText(filterModel.getClient().toString());
        products.setValue(filterModel.getProduct());
        filterModel.getStatuses().stream().forEach((status) -> {
            statuses.getCheckModel().check(status.getLicenseStatusId()); // --
        });
        extraDays.setSelected(filterModel.areExtraDays());
        
    }
    
}
