/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general_scene.table_list;

import ambro.AFilterableTableView;
import ambroafb.general.editor_panel.EditorPanelActionObserver;
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
public class TableListController extends ListingController implements EditorPanelActionObserver {

    @FXML
    private AFilterableTableView<EditorPanelable> aview;
    
    @FXML
    private MaskerPane masker;
    
    private final ObservableList<EditorPanelable> contents = FXCollections.observableArrayList();
    
    @Override
    protected void componentsInitialize(URL url, ResourceBundle rb) {
        aview.setBundle(rb);
        aview.setItems(contents);
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
    
    @Override
    public void addListWith(Class content) {
        aview.initialize(content);
        editorPanel.setTableDataList(aview);
        editorPanel.registerObserver(this);
        
        aview.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends EditorPanelable> observable, EditorPanelable oldValue, EditorPanelable newValue) -> {
            if (newValue != null){
                observers.stream().forEach((observer) -> observer.notify(newValue));
            }
        });
    }

    @Override
    public void notifyDelete(EditorPanelable deleted) {
        contents.remove(deleted);
    }

    @Override
    public void notifyEdit(EditorPanelable edited) {
        
    }

    @Override
    public void notifyAdd(EditorPanelable added) {
        contents.add(added);
    }

    @Override
    public void notifyAddBySample(EditorPanelable addedBySample) {
        contents.add(addedBySample);
    }
    
}
