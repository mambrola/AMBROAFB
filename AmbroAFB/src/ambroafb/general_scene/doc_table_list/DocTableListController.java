/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general_scene.doc_table_list;

import ambro.AFilterableTableView;
import ambroafb.docs.doc_editor_panel.DocEditorPanelController;
import ambroafb.general.editor_panel.EditorPanel;
import ambroafb.general.interfaces.DataProvider;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.general.interfaces.FilterModel;
import ambroafb.general.interfaces.ListingController;
import authclient.AuthServerException;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Supplier;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import org.controlsfx.control.MaskerPane;

/**
 * FXML Controller class
 *
 * @author dkobuladze
 */
public class DocTableListController extends ListingController {

    private AFilterableTableView<EditorPanelable> aview;
    
    @FXML
    private DocEditorPanelController docEditorPanelController;
    
    @FXML
    private MaskerPane masker;
    
    @FXML
    private StackPane containerPane;
    
    private final ObservableList<EditorPanelable> contents = FXCollections.observableArrayList();
    private ResourceBundle bundle;
    
//    /**
//     * Initializes the controller class.
//     * @param url
//     * @param rb
//     */
//    @Override
//    public void initialize(URL url, ResourceBundle rb) {
//        bundle = rb;
//        docEditorPanelController.setOuterController(this);
//    }
    
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
    
//    public void addTableByClass(Class targetClass){
//        aview = new AFilterableTableView<>(targetClass);
//        aview.setId("aview");
//        aview.setBundle(bundle);
//        docEditorPanelController.buttonsMainPropertysBinder(aview);
//        docEditorPanelController.setTableDataList(aview, contents);
//
//        containerPane.getChildren().add(0, aview);
//    }
    
    public void setSelected(int selectedIndex){
        if (selectedIndex >= 0) {
            aview.getSelectionModel().select(selectedIndex);
        }
    }
    
    public DocEditorPanelController getDocEditorPanelController(){
        return docEditorPanelController;
    }

    @Override
    protected void componentsInitialize(URL url, ResourceBundle rb) {
        bundle = rb;
//        docEditorPanelController.setOuterController(this);
    }

    @Override
    public void reAssignTable(FilterModel model) {
        int selectedIndex = aview.getSelectionModel().getSelectedIndex();
        contents.clear();
        
        new Thread(() -> {
            Platform.runLater(() -> {
                masker.setVisible(true);
            });
            
            try {
                List<EditorPanelable> list = (model == null) ? dataFetchProvider.getFilteredBy(DataProvider.PARAM_FOR_ALL)
                                                             : dataFetchProvider.getFilteredBy(model);
                contents.setAll(list);
            } catch (IOException | AuthServerException ex) {
            }
            
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
        docEditorPanelController.buttonsMainPropertysBinder(aview);
        docEditorPanelController.setTableDataList(aview, contents);

        containerPane.getChildren().add(0, aview);
    }

//    @Override
//    public EditorPanelController getEditorPanelController() {
//        return null;
//    }

    @Override
    public void removeElementsFromEditorPanel(String... componentFXids) {
        
    }

    @Override
    public EditorPanel getEditorPanel() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
