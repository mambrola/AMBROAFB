/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general_scene.table_list;

import ambro.AFilterableTableView;
import ambroafb.general.editor_panel.EditorPanelController;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.general.interfaces.ListingController;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Supplier;
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
public class TableListController extends ListingController implements Initializable {

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
    
    @Override
    public void reAssignTable(Supplier<List<EditorPanelable>> fetchData){
        int selectedIndex = aview.getSelectionModel().getSelectedIndex();
        contents.clear();
        
        new Thread(() -> {
            Platform.runLater(() -> {
                masker.setVisible(true);
            });
            
            List<EditorPanelable> list = fetchData.get();
            contents.setAll(list);
            
            Platform.runLater(() -> {
                masker.setVisible(false);
                if (selectedIndex >= 0){
                    aview.getSelectionModel().select(selectedIndex);
                }
            });
        }).start();
        
    }
    
    @Override
    public void addListByClass(Class content) {
        aview = new AFilterableTableView<>(content);
        aview.setId("aview");
        aview.setBundle(bundle);
        editorPanelController.buttonsMainPropertysBinder(aview);
        editorPanelController.setTableDataList(aview, contents);

        containerPane.getChildren().add(0, aview);
    }
    
    @Override
    public void removeElementsFromEditorPanel(String... componentFXids){
        for (String id : componentFXids) {
            editorPanelController.removeButtonsByFxIDs(id);
        }
    }
    
    @Override
    public EditorPanelController getEditorPanelController() {
        return editorPanelController;
    }

}
