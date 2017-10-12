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
import ambroafb.general.SceneUtils;
import ambroafb.general.interfaces.FilterModel;
import ambroafb.general.interfaces.Filterable;
import ambroafb.general.interfaces.UserInteractiveFilterStage;
import ambroafb.general.okay_cancel.FilterOkayCancelController;
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
 * @author mambroladze
 */
public class ClientFilter  extends UserInteractiveFilterStage implements Filterable, Initializable {
    @FXML
    private ADatePicker dateBigger, dateLess;
    @FXML
    private CheckBox juridical, rezident, type;
    @FXML
    private CountryComboBox countries;
    @FXML
    private CheckComboBox<ClientStatus> statuses;
    @FXML
    private FilterOkayCancelController okayCancelController;
    
    private final ClientFilterModel clientFilterModel = new ClientFilterModel();
    
    public ClientFilter(Stage owner) {
        super(owner, "clients");
        
        Scene scene = SceneUtils.createScene("/ambroafb/clients/filter/ClientFilter.fxml", (ClientFilter)this);
        this.setScene(scene);
        
        onCloseRequestProperty().set((EventHandler<WindowEvent>) (WindowEvent event) -> {
            okayCancelController.cancel(null);
            if(event != null) event.consume();
        });
        
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
            clientFilterModel.setSelectedCountry(countries.getValue());
            clientFilterModel.setSelectedStatusesIndexes(statuses.getCheckModel().getCheckedIndices());
            clientFilterModel.setSelectedStatuses(statuses.getCheckModel().getCheckedItems());
            clientFilterModel.setJuridicalIndeterminate(juridical.isIndeterminate());
            clientFilterModel.setJuridicalSelected(juridical.isSelected());
            clientFilterModel.setRezidentIndeterminate(rezident.isIndeterminate());
            clientFilterModel.setRezidentSelected(rezident.isSelected());
            clientFilterModel.setTypeIndeterminate(type.isIndeterminate());
            clientFilterModel.setTypeSelected(type.isSelected());
        }
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ArrayList<ClientStatus> clientStatuses = Client.getAllStatusFromDB();
        clientStatuses.sort((ClientStatus status1, ClientStatus status2) -> status1.getRecId() - status2.getRecId());
        statuses.getItems().addAll(clientStatuses);
        
        dateBigger.setValue(clientFilterModel.getFromDate());
        dateLess.setValue(clientFilterModel.getToDate());
        juridical.setSelected(clientFilterModel.isJuridicalSelected());
        juridical.setIndeterminate(clientFilterModel.isJuridicalIndeterminate());
        rezident.setSelected(clientFilterModel.isRezidentSelected());
        rezident.setIndeterminate(clientFilterModel.isRezidentIndeterminate());
        type.setSelected(clientFilterModel.isTypeSelected());
        type.setIndeterminate(clientFilterModel.isTypeIndeterminate());
        clientFilterModel.getSelectedStatusesIndexes().stream().forEach((index) -> {
            statuses.getCheckModel().check(index);
        });
    }
    
}
