/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general_scene.table_list;

import ambro.AFilterableTableView;
import ambroafb.general.interfaces.DataFetchProvider;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.general.interfaces.FilterModel;
import ambroafb.general.interfaces.ListingController;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import org.controlsfx.control.MaskerPane;

/**
 * FXML Controller class
 *
 * @author dato
 */
public class TableListController extends ListingController {

    @FXML
    private AFilterableTableView<EditorPanelable> aview;
    
    @FXML
    private MaskerPane masker;
    
    private final ObservableList<EditorPanelable> contents = FXCollections.observableArrayList();
    
    @Override
    protected void componentsInitialize(URL url, ResourceBundle rb) {
        aview.setBundle(rb);
    }
    
    @Override
    public void reAssignTable(FilterModel model){
        int selectedIndex = aview.getSelectionModel().getSelectedIndex();
        contents.clear();
        
        masker.setVisible(true);
        
        Consumer<List<EditorPanelable>> successAction = (editorPanelables) -> {
            contents.setAll(editorPanelables);
            masker.setVisible(false);
            if (selectedIndex >= 0){
                aview.getSelectionModel().select(selectedIndex);
            }
        };
        
        if (model == null){
            dataFetchProvider.filteredBy(DataFetchProvider.PARAM_FOR_ALL, successAction, null);
        }
        else {
            Consumer<Exception> error = (ex) -> {
                System.err.println("ex: " + ex.getMessage());
            };
            dataFetchProvider.filteredBy(model, successAction, error);
        }
        
    }
    
//    @Override
//    public void reAssignTable(FilterModel model){
//        int selectedIndex = aview.getSelectionModel().getSelectedIndex();
//        contents.clear();
//        
//        new Thread(() -> {
//            Platform.runLater(() -> {
//                masker.setVisible(true);
//            });
//            
//            try {
//                List<EditorPanelable> list = (model == null) ? dataFetchProvider.getFilteredBy(DataProvider.PARAM_FOR_ALL)
//                                                             : dataFetchProvider.getFilteredBy(model);
//                contents.setAll(list);
//            } catch (Exception ex) {
//            }
//            
//            Platform.runLater(() -> {
//                masker.setVisible(false);
//                if (selectedIndex >= 0){
//                    aview.getSelectionModel().select(selectedIndex);
//                }
//            });
//        }).start();
//        
//    }
//    
    @Override
    public void addListWith(Class content) {
        aview.initialize(content);
//        editorPanel.buttonsMainPropertiesBinder(aview);
        editorPanel.setTableDataList(aview, contents);
        
        aview.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends EditorPanelable> observable, EditorPanelable oldValue, EditorPanelable newValue) -> {
            if (newValue != null){
                observers.stream().forEach((observer) -> observer.notify(newValue));
            }
        });
    }
    
}
