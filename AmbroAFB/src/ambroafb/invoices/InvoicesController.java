/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.invoices;

import ambro.AFilterableTableView;
import ambroafb.general.FilterModel;
import ambroafb.general.editor_panel.EditorPanelController;
import ambroafb.general.interfaces.EditorPanelable;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import org.controlsfx.control.MaskerPane;

/**
 * FXML Controller class
 *
 * @author mambroladze
 */
public class InvoicesController implements Initializable {

    @FXML
    private AFilterableTableView<EditorPanelable> aview;
    
    @FXML
    private EditorPanelController editorPanelController;
    
    @FXML
    private MaskerPane masker;
    
    private final ObservableList<EditorPanelable> invoices = FXCollections.observableArrayList();
    
    /**
     * 
     * @param url
     * @param rb
     */
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        aview.setBundle(rb);
        editorPanelController.setOuterController(this);
        editorPanelController.buttonsMainPropertysBinder(aview);
        editorPanelController.setTableDataList(aview, invoices);
    }
    
    public void reAssignTable(FilterModel model) {
        if (!model.isCanceled()){
            int selectedIndex = aview.getSelectionModel().getSelectedIndex();
            invoices.clear();
            masker.setVisible(true);
                
            new Thread(() -> {
                Invoice.getFilteredFromDB(model).stream().forEach((invoice) -> {
                    invoices.add(invoice);
                });
                Platform.runLater(() -> {
                    masker.setVisible(false);
                    if (selectedIndex >= 0){
                        aview.getSelectionModel().select(selectedIndex);
                    }
                });
            }).start();
        }
    }
    
    
    public EditorPanelController getEditorPanelController(){
        return editorPanelController;
    }
}
