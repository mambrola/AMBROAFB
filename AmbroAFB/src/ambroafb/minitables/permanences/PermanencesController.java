/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.minitables.permanences;

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
 *
 * @author dato
 */
public class PermanencesController implements Initializable {
    
    @FXML
    private AFilterableTableView<EditorPanelable> aview;
    
    @FXML
    private EditorPanelController editorPanelController;
    
    @FXML
    private MaskerPane masker;
    
    private final ObservableList<EditorPanelable> permanences = FXCollections.observableArrayList();
    
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
        editorPanelController.setTableDataList(aview, permanences);
        editorPanelController.removeButtonsByFxIDs("#search");
        reAssignTable(null);
    }    
    
    public void reAssignTable(FilterModel model){
        int selectedIndex = aview.getSelectionModel().getSelectedIndex();
        permanences.clear();
        masker.setVisible(true);
        
        new Thread(() -> {
            ArrayList<Permanence> PermanenceList = Permanence.getAllFromDB();
            PermanenceList.sort((Permanence b1, Permanence b2) -> b1.getDescrip().compareTo(b2.getDescrip()));
            permanences.setAll(PermanenceList);
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
