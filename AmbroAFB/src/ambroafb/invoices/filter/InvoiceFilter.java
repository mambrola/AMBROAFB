/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.invoices.filter;

import ambro.ADatePicker;
import ambroafb.clients.ClientComboBox;
import ambroafb.general.FilterModel;
import ambroafb.general.GeneralConfig;
import ambroafb.general.Names;
import ambroafb.general.SceneUtils;
import ambroafb.general.interfaces.Filterable;
import ambroafb.general.StageUtils;
import ambroafb.general.StagesContainer;
import ambroafb.general.okay_cancel.FilterOkayCancelController;
import ambroafb.invoices.Invoice;
import ambroafb.invoices.helper.InvoiceReissuing;
import ambroafb.invoices.helper.InvoiceStatusClarify;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import org.controlsfx.control.CheckComboBox;

/**
 *
 * @author dato
 */
public class InvoiceFilter extends Stage implements Filterable, Initializable {

    
    @FXML
    private ADatePicker startDateFrom, startDateTo, endDateFrom, endDateTo;
    @FXML
    private ClientComboBox clients;
    @FXML
    private CheckComboBox<InvoiceReissuing> invoiceReissuings;
    @FXML
    private FilterOkayCancelController okayCancelController;
    @FXML
    private CheckComboBox<InvoiceStatusClarify> clarifyType;
    
    private final InvoiceFilterModel invoiceFilterModel = new InvoiceFilterModel();
    
    public static final String DATE_BIGGER = "1970-01-01";
    public static final String DATE_LESS = "9999-01-01";
    
    public InvoiceFilter(Stage owner){
        StagesContainer.registerStageByOwner(owner, Names.LEVEL_FOR_PATH, (Stage)this);
        
        this.initStyle(StageStyle.UNIFIED);
        this.setTitle(GeneralConfig.getInstance().getTitleFor("invoices_filter"));
        Scene scene = SceneUtils.createScene("/ambroafb/invoices/filter/InvoiceFilter.fxml", (InvoiceFilter)this);
        this.setScene(scene);
        this.initOwner(owner);
        this.setResizable(false);
        
        onCloseRequestProperty().set((EventHandler<WindowEvent>) (WindowEvent event) -> {
            okayCancelController.cancel(null);
            if (event != null) event.consume();
        });
        
        StageUtils.centerChildOf(owner, (Stage)this);
        StageUtils.followChildTo(owner, (Stage)this);
    }

    @Override
    public FilterModel getResult() {
        showAndWait();
        return invoiceFilterModel;
    }
    
    @Override
    public void setResult(boolean isOk) {
        if (!isOk){
            invoiceFilterModel.changeModelAsEmpty();
        }
        else {
            startDateFrom.setEditingValue();
            startDateTo.setEditingValue();
            endDateFrom.setEditingValue();
            endDateTo.setEditingValue();
            
            invoiceFilterModel.setStartDateFrom(startDateFrom.getValue());
            invoiceFilterModel.setStartDateTo(startDateTo.getValue());
            invoiceFilterModel.setEndDateFrom(endDateFrom.getValue());
            invoiceFilterModel.setEndDateTo(endDateTo.getValue());
            invoiceFilterModel.setSelectedClient(clients.getValue());
            invoiceFilterModel.setCheckedReissuingsIndexes(invoiceReissuings.getCheckModel().getCheckedIndices());
            invoiceFilterModel.setCheckedReissuings(invoiceReissuings.getCheckModel().getCheckedItems());
            invoiceFilterModel.setCheckedClarifiesIndexes(clarifyType.getCheckModel().getCheckedIndices());
            invoiceFilterModel.setCheckedClarifies(clarifyType.getCheckModel().getCheckedItems());
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        clarifyType.getItems().setAll(Invoice.getAllIvoiceClarifiesFromDB());
        invoiceReissuings.getItems().setAll(Invoice.getAllIvoiceReissuingsesFromDB());
        
        startDateFrom.setValue(invoiceFilterModel.getStartDate(true));
        startDateTo.setValue(invoiceFilterModel.getStartDate(false));
        endDateFrom.setValue(invoiceFilterModel.getEndDate(true));
        endDateTo.setValue(invoiceFilterModel.getEndDate(false));
        invoiceFilterModel.getCheckedClarifiesIndexes().stream().forEach((index) -> {
            clarifyType.getCheckModel().check(index);
        });
        invoiceFilterModel.getCheckedReissuingsIndexes().stream().forEach((index) -> {
            invoiceReissuings.getCheckModel().check(index);
        });
    }
    
}
