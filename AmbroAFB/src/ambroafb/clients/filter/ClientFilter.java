/*
 * To setLanguage this license header, choose License Headers in Project Properties.
 * To setLanguage this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.clients.filter;

import ambro.ADatePicker;
import ambroafb.clients.Client;
import ambroafb.clients.helper.ClientStatus;
import ambroafb.countries.CountryComboBox;
import ambroafb.general.FilterModel;
import ambroafb.general.GeneralConfig;
import ambroafb.general.Names;
import ambroafb.general.SceneUtils;
import ambroafb.general.interfaces.Filterable;
import ambroafb.general.okay_cancel.FilterOkayCancelController;
import ambroafb.general.StageUtils;
import ambroafb.general.StagesContainer;
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

/**
 *
 * @author mambroladze
 */
public class ClientFilter  extends Stage implements Filterable, Initializable {
    @FXML
    private ADatePicker dateBigger, dateLess;
    @FXML
    private CheckBox juridical, rezident;
    @FXML
    private CountryComboBox countries;
    @FXML
    private CheckComboBox<ClientStatus> statuses;
    @FXML
    private FilterOkayCancelController okayCancelController;
    
    private final ClientFilterModel clientFilterModel = new ClientFilterModel();
    
    public ClientFilter(Stage owner) {
        StagesContainer.registerStageByOwner(owner, Names.LEVEL_FOR_PATH, (Stage)this);
        
        this.initStyle(StageStyle.UNIFIED);
        this.setTitle(GeneralConfig.getInstance().getTitleFor("clients_filter"));
        Scene scene = SceneUtils.createScene("/ambroafb/clients/filter/ClientFilter.fxml", (ClientFilter)this);
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
    public FilterModel getResult() {
        showAndWait();
        return clientFilterModel;
    }

    @Override
    public void setResult(boolean isOk){
        if(!isOk){
            clientFilterModel.changeModelAsEmpty();
        }
        else {
            dateBigger.setEditingValue();
            dateLess.setEditingValue();
            
            clientFilterModel.setFromDate(dateBigger.getValue());
            clientFilterModel.setToDate(dateLess.getValue());
            clientFilterModel.setJuridicalIndeterminate(juridical.isIndeterminate());
            clientFilterModel.setJuridicalSelected(juridical.isSelected());
            clientFilterModel.setSelectedCountryIndex(countries.getSelectionModel().getSelectedIndex());
            clientFilterModel.setSelectedCountry(countries.getValue());
            clientFilterModel.setRezidentIndeterminate(rezident.isIndeterminate());
            clientFilterModel.setRezidentSelected(rezident.isSelected());
            clientFilterModel.setSelectedStatusesIndexes(statuses.getCheckModel().getCheckedIndices());
            clientFilterModel.setSelectedStatuses(statuses.getCheckModel().getCheckedItems());
        }
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Client.getAllStatusFromDB().forEach((clientStatus) -> {
            statuses.getItems().add(clientStatus);
        });
        
        dateBigger.setValue(clientFilterModel.getFromDate());
        dateLess.setValue(clientFilterModel.getToDate());
        juridical.setSelected(clientFilterModel.isJuridicalSelected());
        juridical.setIndeterminate(clientFilterModel.isJuridicalIndeterminate());
        countries.getSelectionModel().select(clientFilterModel.getSelectedCountryIndex());
        rezident.setSelected(clientFilterModel.isRezidentSelected());
        rezident.setIndeterminate(clientFilterModel.isRezidentIndeterminate());
        clientFilterModel.getSelectedStatusesIndexes().stream().forEach((index) -> {
            statuses.getCheckModel().check(index);
        });
    }
    
}
