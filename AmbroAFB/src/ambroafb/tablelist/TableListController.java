/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.tablelist;

import ambro.AFilterableTableView;
import ambroafb.general.FilterModel;
import ambroafb.general.editor_panel.EditorPanelController;
import ambroafb.general.interfaces.EditorPanelable;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.StackPane;
import org.controlsfx.control.MaskerPane;

/**
 * FXML Controller class
 *
 * @author dato
 */
public class TableListController implements Initializable {

    private AFilterableTableView<EditorPanelable> aview;
    
    @FXML
    private EditorPanelController editorPanelController;
    
    @FXML
    private MaskerPane masker;
    
    @FXML
    private StackPane containerPane;
    
    private final ObservableList<EditorPanelable> contents = FXCollections.observableArrayList();
    
    private ResourceBundle bundle;
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        bundle = rb;
        editorPanelController.setOuterController(this);
    } 
    
    public void reAssignTable(List<? extends EditorPanelable> sortedList, FilterModel model){
        // Stage has not filter or it is canceled from user:
        if (model == null || !model.isCanceled()) {
            int selectedIndex = aview.getSelectionModel().getSelectedIndex();
            contents.clear();
            masker.setVisible(true);

            new Thread(() -> {
                contents.setAll(sortedList);
                Platform.runLater(() -> {
                    masker.setVisible(false);
                    if (selectedIndex >= 0){
                        aview.getSelectionModel().select(selectedIndex);
                    }
                });
            }).start();
        }
    }
    
    public void addTableByClass(Class targetClass){
        aview = new AFilterableTableView<>(targetClass);
        aview.setId("aview");
        aview.setBundle(bundle);
        editorPanelController.buttonsMainPropertysBinder(aview);
        editorPanelController.setTableDataList(aview, contents);

        containerPane.getChildren().add(0, aview);
    }
    
    public void removeElementsFromEditorPanel(String... componentFXids){
        for (String id : componentFXids) {
            editorPanelController.removeButtonsByFxIDs(id);
        }
    }
    
    public EditorPanelController getEditorPanelController() {
        return editorPanelController;
    }
}
