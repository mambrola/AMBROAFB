/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general_scene.table_list;

import ambro.AFilterableTableView;
import ambroafb.general.interfaces.DataProvider;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.general.interfaces.FilterModel;
import ambroafb.general.interfaces.ListingController;
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
 * @author dato
 */
public class TableListController extends ListingController {

    private AFilterableTableView<EditorPanelable> aview;
    
    @FXML
    private MaskerPane masker;
    
    @FXML
    private StackPane containerPane;
    
    private final ObservableList<EditorPanelable> contents = FXCollections.observableArrayList();
    
    
    @Override
    protected void componentsInitialize(URL url, ResourceBundle rb) {
        
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
    public void reAssignTable(FilterModel model){
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
            } catch (Exception ex) {
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
    public void addListWith(Class content) {
        aview = new AFilterableTableView<>(content);
        aview.setId("aview");
        aview.setBundle(bundle);
        editorPanel.buttonsMainPropertysBinder(aview);
        editorPanel.setTableDataList(aview, contents);
        containerPane.getChildren().add(0, aview);
    }
    
}
