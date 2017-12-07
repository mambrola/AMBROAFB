/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general_scene.table_master_detail;

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
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;
import java.util.function.Predicate;
import javafx.beans.Observable;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.SplitPane;
import javafx.stage.Stage;
import org.controlsfx.control.MaskerPane;

/**
 * FXML Controller class
 *
 * @author dkobuladze
 */
public class TableMasterDetailController extends ListingController implements EditorPanelActionObserver {

    @FXML
    private AFilterableTableView<EditorPanelable> tableView;
    
    @FXML
    private SplitPane splitPane;
    
    @FXML
    private MaskerPane masterMasker;
    
    private final ObservableList<EditorPanelable> contents = FXCollections.observableArrayList();

    private final Timer timer = new Timer();
    private EditorPanelable newSelected;
    private final long waitingMilisecond = 300;
    
    @Override
    protected void componentsInitialize(URL url, ResourceBundle rb) {
        tableView.setBundle(rb);
        tableView.setItems(contents);
    }
    
    public void setDetailNode(Node node){
        splitPane.getItems().add(node);
        node.setDisable(true);
    }
    
    @Override
    public void reAssignTable(FilterModel model){
        int selectedIndex = tableView.getSelectionModel().getSelectedIndex();
        contents.clear();
        
        masterMasker.setVisible(true);
        
        Consumer<List<EditorPanelable>> successAction = (editorPanelables) -> {
            contents.setAll(editorPanelables);
            masterMasker.setVisible(false);
            if (selectedIndex >= 0){
                tableView.getSelectionModel().select(selectedIndex);
            }
        };
        
        Consumer<Exception> errorAction = (ex) -> {
            String errorHeaderText = GeneralConfig.getInstance().getTitleFor("error");
            new AlertMessage((Stage)tableView.getScene().getWindow(), Alert.AlertType.ERROR, errorHeaderText, ex.getMessage(), ex).showAndWait();
            masterMasker.setVisible(false);
        };
        
        if (model == null)
            dataFetchProvider.filteredBy(DataFetchProvider.PARAM_FOR_ALL, successAction, errorAction);
        else
            dataFetchProvider.filteredBy(model, successAction, errorAction);
        
    }

    @Override
    public void addListWith(Class content) {
        tableView.initialize(content);
        editorPanel.setTableDataList(tableView);
        editorPanel.registerObserver(this); // editorPanel will be setted for this line.
        
        tableView.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends EditorPanelable> observable, EditorPanelable oldValue, EditorPanelable newValue) -> {
            if (newValue != null){
                if (splitPane.getItems().size() > 1){
                    splitPane.getItems().get(1).setDisable(false);
                }
                observers.stream().forEach(observer -> observer.notify(newValue));
                timer.schedule(new ToDoTask(newValue), waitingMilisecond);
                newSelected = newValue;
            }
        });
    }
    
    @Override
    public void setListFilterConditions(Predicate<EditorPanelable> predicate, Observable... dependencies) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
    private class ToDoTask extends TimerTask {

        private final EditorPanelable selected;
        
        public ToDoTask(EditorPanelable selected) {
            this.selected = selected;
        }
        
        @Override
        public void run() {
            if (selected.getRecId() == newSelected.getRecId()){
                System.out.println("OK, time is out. Update detail...");
                observers.stream().forEach(observer -> observer.update(selected));
            }
        }
        
    }
    
    
    @Override
    public void notifyDelete(EditorPanelable deleted) {
        int row = contents.indexOf(deleted);
        contents.remove(deleted);
        if (row >= contents.size()){
            row = contents.size() - 1;
        }
        tableView.getSelectionModel().select(row);
    }

    @Override
    public void notifyEdit(EditorPanelable edited) {
        // Refresh selected row because of notify and update datail.
        tableView.getSelectionModel().select(null);
        tableView.getSelectionModel().select(edited);
    }

    @Override
    public void notifyAdd(EditorPanelable added) {
        contents.add(added);
        tableView.getSelectionModel().select(contents.indexOf(added));
    }

    @Override
    public void notifyAddBySample(EditorPanelable addedBySample) {
        contents.add(addedBySample);
        tableView.getSelectionModel().select(contents.indexOf(addedBySample));
    }
}
