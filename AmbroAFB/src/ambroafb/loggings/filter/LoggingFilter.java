/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.loggings.filter;

import ambro.ADatePicker;
import ambroafb.clients.ClientComboBox;
import ambroafb.general.FilterModel;
import ambroafb.general.Names;
import ambroafb.general.SceneUtils;
import ambroafb.general.interfaces.Filterable;
import ambroafb.general.okay_cancel.FilterOkayCancelController;
import ambroafb.general.stages.UserInteractiveStage;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author dato
 */
public class LoggingFilter extends UserInteractiveStage implements Initializable, Filterable {

    @FXML
    private ADatePicker dateBigger, dateLess;
    @FXML
    private ClientComboBox clients;
    @FXML
    private FilterOkayCancelController okayCancelController;
    
    private final LoggingFilterModel loggingFilterModel = new LoggingFilterModel();
    
    public LoggingFilter(Stage owner){
        super(owner, Names.LEVEL_FOR_PATH, "loggings", "/images/filter.png");
        
        Scene scene = SceneUtils.createScene("/ambroafb/loggings/filter/LoggingFilter.fxml", (LoggingFilter)this);
        this.setScene(scene);
        
        onCloseRequestProperty().set((EventHandler<WindowEvent>) (WindowEvent event) -> {
            okayCancelController.cancel(null);
            if(event != null) event.consume();
        });
        
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
            loggingFilterModel.setSelectedClient(clients.getValue());
            loggingFilterModel.setSelectedClientIndex(clients.getSelectionModel().getSelectedIndex());
        }
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dateBigger.setValue(loggingFilterModel.getFromDate());
        dateLess.setValue(loggingFilterModel.getToDate());
        clients.getSelectionModel().select(loggingFilterModel.getSelectedClientIndex());
    }
}
