/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general_scene.doc_table_list;

import ambro.AFilterableTableView;
import ambroafb.docs.Doc;
import ambroafb.docs.doc_editor_panel.DocEditorPanelController;
import java.net.URL;
import java.util.ArrayList;
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
 * @author dkobuladze
 */
public class DocTableListController implements Initializable {

    private AFilterableTableView<Doc> aview;
    
    @FXML
    private DocEditorPanelController docEditorPanelController;
    
    @FXML
    private MaskerPane masker;
    
    @FXML
    private StackPane containerPane;
    
    private final ObservableList<Doc> contents = FXCollections.observableArrayList();
    private ResourceBundle bundle;
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        bundle = rb;
        docEditorPanelController.setOuterController(this);
    }
    
    public void reAssignTable(Supplier<ArrayList<Doc>> fetchData){
        int selectedIndex = aview.getSelectionModel().getSelectedIndex();
        contents.clear();
        
        new Thread(() -> {
            Platform.runLater(() -> {
                masker.setVisible(true);
            });
            
            ArrayList<Doc> list = fetchData.get();
            contents.setAll(list);
            
            Platform.runLater(() -> {
                masker.setVisible(false);
                if (selectedIndex >= 0){
                    aview.getSelectionModel().select(selectedIndex);
                }
            });
        }).start();
    }
    
    public void addTableByClass(Class targetClass){
        aview = new AFilterableTableView<>(targetClass);
        aview.setId("aview");
        aview.setBundle(bundle);
        docEditorPanelController.buttonsMainPropertysBinder(aview);
        docEditorPanelController.setTableDataList(aview, contents);

        containerPane.getChildren().add(0, aview);
    }
    
    public DocEditorPanelController getDocEditorPanelController(){
        return docEditorPanelController;
    }
}
