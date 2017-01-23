/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.minitables.buysells;

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
public class BuySellsController implements Initializable {

    @FXML
    private AFilterableTableView<EditorPanelable> aview;
    
    @FXML
    private EditorPanelController editorPanelController;
    
    @FXML
    private MaskerPane masker;
    
    private final ObservableList<EditorPanelable> buysells = FXCollections.observableArrayList();
    
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
        editorPanelController.setTableDataList(aview, buysells);
        editorPanelController.removeButtonsByFxIDs("#search");
        reAssignTable(null);
    }    
    
    public void reAssignTable(FilterModel model){
        int selectedIndex = aview.getSelectionModel().getSelectedIndex();
        buysells.clear();
        masker.setVisible(true);
        
        new Thread(() -> {
            ArrayList<BuySell> buysellList = BuySell.getAllFromDB();
            buysellList.sort((BuySell b1, BuySell b2) -> b1.getDescrip().compareTo(b2.getDescrip()));
            buysells.setAll(buysellList);
            Platform.runLater(() -> {
                masker.setVisible(false);
                if (selectedIndex >= 0){
                    aview.getSelectionModel().select(selectedIndex);
                }
            });
        }).start();
    }
    
    public EditorPanelController getEditorPanelController(){
        return editorPanelController;
    }
}
