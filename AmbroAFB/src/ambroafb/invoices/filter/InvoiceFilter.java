/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.invoices.filter;

import ambro.ADatePicker;
import ambroafb.clients.Client;
import ambroafb.clients.ClientComboBox;
import ambroafb.general.SceneUtils;
import ambroafb.general.interfaces.FilterModel;
import ambroafb.general.interfaces.Filterable;
import ambroafb.general.interfaces.UserInteractiveFilterStage;
import ambroafb.general.okay_cancel.FilterOkayCancelController;
import ambroafb.invoices.Invoice;
import ambroafb.invoices.helper.InvoiceReissuing;
import ambroafb.invoices.helper.InvoiceStatusClarify;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.controlsfx.control.CheckComboBox;

/**
 *
 * @author dato
 */
public class InvoiceFilter extends UserInteractiveFilterStage implements Filterable, Initializable {

    
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
        super(owner, "invoices");
        
        Scene scene = SceneUtils.createScene("/ambroafb/invoices/filter/InvoiceFilter.fxml", (InvoiceFilter)this);
        this.setScene(scene);
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
        ArrayList<InvoiceStatusClarify> clarifies = Invoice.getAllIvoiceClarifiesFromDB();
        
//        System.out.println("Before sort");
//        printClarifiesIDsList(clarifies);
        
        clarifies.sort((InvoiceStatusClarify clarify1, InvoiceStatusClarify clarify2) -> clarify1.compareById(clarify2));
        
//        System.out.println("After sort");
//        printClarifiesIDsList(clarifies);
        
        clarifyType.getItems().setAll(clarifies);
        
        ArrayList<InvoiceReissuing> reissuings = Invoice.getAllIvoiceReissuingsesFromDB();
        reissuings.sort((InvoiceReissuing reissuing1, InvoiceReissuing reissuing2) -> reissuing1.getRecId() - reissuing2.getRecId());
        invoiceReissuings.getItems().setAll(reissuings);
        
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
        
        Consumer<ObservableList<Client>> clientConsumer = (clientList) -> {
            Optional<Client> optClient = clientList.stream().filter((client) -> client.getRecId() == invoiceFilterModel.getSelectedClientId()).findFirst();
            if (optClient.isPresent()){
                clients.valueProperty().set(optClient.get());
            }
        };
        clients.fillComboBoxOnlyClientsWithALL(clientConsumer);
    }
    
    private void printClarifiesIDsList(ArrayList<InvoiceStatusClarify> clarifies){
        clarifies.stream().forEach((cl) -> {
            System.out.print(cl.getRecId() + " ");
        });
        System.out.println("");
    }

    @Override
    protected FilterOkayCancelController getOkayCancelController() {
        return okayCancelController;
    }
}
