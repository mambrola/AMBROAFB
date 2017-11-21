/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general_scene.tree_table_list;

import ambro.AFilterableTreeTableView;
import ambroafb.general.interfaces.DataFetchProvider;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.general.interfaces.FilterModel;
import ambroafb.general.interfaces.ListingController;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Function;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import org.controlsfx.control.MaskerPane;

/**
 * FXML Controller class
 *
 * @author dato
 */
public class TreeTableListController extends ListingController {

    @FXML
    private AFilterableTreeTableView<EditorPanelable> aview;
    
    @FXML
    private MaskerPane masker;
    
    private final ObservableList<EditorPanelable> roots = FXCollections.observableArrayList();
    private Function<List<EditorPanelable>, ObservableList<EditorPanelable>> treeMakerFn;
    private int expandDepth = 1;
    
    @Override
    protected void componentsInitialize(URL url, ResourceBundle rb) {
        aview.setBundle(rb);
        aview.getColumns().stream().forEach((column) -> {
            column.setSortable(false);
        });
    }
    
    @Override
    public void reAssignTable(FilterModel model){
        masker.setVisible(true);
        aview.removeAll();
        new Thread(() -> {
            try {
                List<EditorPanelable> data = (model == null) ? dataFetchProvider.getFilteredBy(DataFetchProvider.PARAM_FOR_ALL) 
                                                             : dataFetchProvider.getFilteredBy(model);
                roots.setAll(treeMakerFn.apply(data));
                roots.stream().forEach((elem) -> {
                    aview.append(elem);
                });
                aview.expand(expandDepth);
            } catch (Exception ex) {
                System.err.println(ex.getMessage());
            }
            
            Platform.runLater(() -> {
                masker.setVisible(false);
            });
        }).start();
    }
    
    @Override
    public void addListWith(Class content) {
        aview.initialize(content);
        editorPanel.buttonsMainPropertiesBinder(aview);
        editorPanel.setTreeTable(aview);
        
        aview.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends TreeItem<EditorPanelable>> observable, TreeItem<EditorPanelable> oldValue, TreeItem<EditorPanelable> newValue) -> {
            if (newValue != null && newValue.getValue() != null){
                observers.stream().forEach((observer) -> observer.notify(newValue.getValue()));
            }
        });
    }
    
    public void setTreeFeatures(Function<List<EditorPanelable>, ObservableList<EditorPanelable>> treeMakerFn, int depth){
        this.treeMakerFn = treeMakerFn;
        expandDepth = depth;
    }
    
    
}
