/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.currency_rates;

import ambro.AFilterableTableView;
import ambroafb.general.FilterModel;
import ambroafb.general.editor_panel.EditorPanelController;
import ambroafb.general.interfaces.EditorPanelable;
import java.net.URL;
import java.util.ArrayList;
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
 * @author dato
 */
public class CurrencyRatesController implements Initializable {

    @FXML
    private AFilterableTableView<EditorPanelable> aview;
    
    @FXML
    private MaskerPane masker;
    
    @FXML
    private EditorPanelController editorPanelController;
    
    private final ObservableList<EditorPanelable> currencyRates = FXCollections.observableArrayList();
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        aview.setBundle(rb);
        editorPanelController.setOuterController(this);
        editorPanelController.buttonsMainPropertysBinder(aview);
        editorPanelController.setTableDataList(aview, currencyRates);
    }
    
    public void reAssignTable(FilterModel model){
        if (!model.isEmpty()){
            int selectedIndex = aview.getSelectionModel().getSelectedIndex();
            currencyRates.clear();
            masker.setVisible(true);
            
            new Thread(() -> {
                ArrayList<CurrencyRate> currencyRateList = CurrencyRate.getFilteredFromDB(model);
                currencyRateList.sort((CurrencyRate c1, CurrencyRate c2) -> c2.dateProperty().get().compareTo(c1.dateProperty().get()));
                currencyRates.setAll(currencyRateList);
                
                Platform.runLater(() -> {
                    masker.setVisible(false);
                    if (selectedIndex >= 0){
                        aview.getSelectionModel().select(selectedIndex);
                    }
                });
            }).start();
        }
    }

    public EditorPanelController getEditorPanelController() {
        return editorPanelController;
    }
    
}
