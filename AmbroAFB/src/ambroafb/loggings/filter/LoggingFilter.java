/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.loggings.filter;

import ambro.ADatePicker;
import ambroafb.clients.Client;
import ambroafb.clients.ClientComboBox;
import ambroafb.general.SceneUtils;
import ambroafb.general.interfaces.FilterModel;
import ambroafb.general.interfaces.Filterable;
import ambroafb.general.interfaces.UserInteractiveFilterStage;
import ambroafb.general.okay_cancel.FilterOkayCancelController;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author dato
 */
public class LoggingFilter extends UserInteractiveFilterStage implements Initializable, Filterable {

    @FXML
    private ADatePicker dateBigger, dateLess;
    @FXML
    private ClientComboBox clients;
    @FXML
    private FilterOkayCancelController okayCancelController;
    
    private final LoggingFilterModel loggingFilterModel = new LoggingFilterModel();
    
    public LoggingFilter(Stage owner){
        super(owner, "loggings");
        
        Scene scene = SceneUtils.createScene("/ambroafb/loggings/filter/LoggingFilter.fxml", (LoggingFilter)this);
        this.setScene(scene);
    }
    
    @Override
    public FilterModel getResult() {
        showAndWait();
        return loggingFilterModel;
    }

    @Override
    public void setResult(boolean isOk) {
        if (!isOk){
            loggingFilterModel.changeModelAsEmpty();
        }
        else {
            dateBigger.setEditingValue();
            dateLess.setEditingValue();
            
            loggingFilterModel.setFromDate(dateBigger.getValue());
            loggingFilterModel.setToDate(dateLess.getValue());
            Client selectedclient = (clients.getValue() == null) ? clients.getClientWithDescripALL() : clients.getValue();
            loggingFilterModel.setSelectedClient(selectedclient);
        }
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dateBigger.setValue(loggingFilterModel.getFromDate());
        dateLess.setValue(loggingFilterModel.getToDate());

        Consumer<ObservableList<Client>> clientConsumer = (clientList) -> {
            Optional<Client> optClient = clientList.stream().filter((client) -> client.getRecId() == loggingFilterModel.getSelectedClientId()).findFirst();
            if (optClient.isPresent()){
                clients.valueProperty().set(optClient.get());
            }
        };
        clients.fillComboBoxOnlyClientsWithALL(clientConsumer);
    }

    @Override
    protected FilterOkayCancelController getOkayCancelController() {
        return okayCancelController;
    }
}
