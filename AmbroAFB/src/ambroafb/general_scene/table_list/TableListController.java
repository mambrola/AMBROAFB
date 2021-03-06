/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general_scene.table_list;

import ambro.AFilterableTableView;
import ambroafb.general.AlertMessage;
import ambroafb.general.GeneralConfig;
import ambroafb.general.editor_panel.EditorPanelActionObserver;
import ambroafb.general.interfaces.DataFetchProvider;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.general.interfaces.FilterModel;
import ambroafb.general.interfaces.ListingController;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.function.Predicate;
import javafx.beans.Observable;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import org.controlsfx.control.MaskerPane;

/**
 * FXML Controller class
 *
 * @author dato
 */
public class TableListController extends ListingController implements EditorPanelActionObserver {

    @FXML
    private AFilterableTableView<EditorPanelable> tableView;
    
    @FXML
    private MaskerPane masker;
    
    private final ObservableList<EditorPanelable> entries = FXCollections.observableArrayList();
    
    @Override
    protected void componentsInitialize(URL url, ResourceBundle rb) {
        tableView.setBundle(rb);
        tableView.setItems(entries);
    }
    
    public ObservableList<EditorPanelable> getTableContent(){
        return entries;
    }
    
    @Override
    public void reAssignTable(FilterModel model){
        int selectedIndex = tableView.getSelectionModel().getSelectedIndex();
        entries.clear();
        masker.setVisible(true);
        
        Consumer<List<EditorPanelable>> successAction = (editorPanelables) -> {
            entries.setAll(editorPanelables);
            masker.setVisible(false);
            if (selectedIndex >= 0){
                tableView.getSelectionModel().select(selectedIndex);
            }
        };
        
        Consumer<Exception> errorAction = (ex) -> {
            String errorHeaderText = GeneralConfig.getInstance().getTitleFor("error");
            new AlertMessage((Stage) tableView.getScene().getWindow(), Alert.AlertType.ERROR, errorHeaderText, ex.getMessage(), ex).showAndWait();
            masker.setVisible(false);
        };
        
        if (model == null){
            dataFetchProvider.filteredBy(DataFetchProvider.PARAM_FOR_ALL, successAction, errorAction);
        }
        else {
            
            dataFetchProvider.filteredBy(model, successAction, errorAction);
        }
        
    }
    
    @Override
    public void addListWith(Class content) {
        tableView.initialize(content);
        editorPanel.setTableDataList(tableView);
        editorPanel.registerObserver(this); // editorPanel will be setted at this time.

        tableView.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends EditorPanelable> observable, EditorPanelable oldValue, EditorPanelable newValue) -> {
            if (newValue != null){
                observers.stream().forEach((observer) -> observer.notify(newValue));
            }
        });
    }
    
    @Override
    public void setListFilterConditions(Predicate<EditorPanelable> predicate, Observable... dependencies) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void notifyDelete(EditorPanelable deleted) {
        int row = entries.indexOf(deleted);
        entries.remove(deleted);
        if (row >= entries.size()){
            row = entries.size() - 1;
        }
        tableView.getSelectionModel().select(row);
    }

    @Override
    public void notifyEdit(EditorPanelable edited) {
        tableView.getSelectionModel().select(entries.indexOf(edited));
    }

    @Override
    public void notifyAdd(EditorPanelable added) {
        entries.add(added);
        tableView.getSelectionModel().select(entries.indexOf(added));
    }

    @Override
    public void notifyAddBySample(EditorPanelable addedBySample) {
        entries.add(addedBySample);
        tableView.getSelectionModel().select(entries.indexOf(addedBySample));
    }

}
