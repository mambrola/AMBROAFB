/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general_scene.table_master_detail;

import ambro.AFilterableTableView;
import ambroafb.general.interfaces.DataProvider;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.general.interfaces.FilterModel;
import ambroafb.general.interfaces.ListingController;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.StackPane;
import org.controlsfx.control.MaskerPane;

/**
 * FXML Controller class
 *
 * @author dkobuladze
 */
public class TableMasterDetailController extends ListingController {

    private AFilterableTableView<EditorPanelable> tableView;
    
//    @FXML
//    private MasterDetailPane masterDetailPane;
    
    @FXML
    private SplitPane splitPane;
    
    @FXML
    private MaskerPane masterMasker;
    
    private final ObservableList<EditorPanelable> contents = FXCollections.observableArrayList();

    private final ArrayList<MasterObserver> observers = new ArrayList<>();
            
    @Override
    protected void componentsInitialize(URL url, ResourceBundle rb) {
        
    }
    
    public void setDetailNode(Node node){
        splitPane.getItems().add(node);
        node.setDisable(true);
    }
    
    public void registerObserver(MasterObserver observer){
        if (observer != null) observers.add(observer);
    }

    @Override
    public void reAssignTable(FilterModel model) {
        int selectedIndex = tableView.getSelectionModel().getSelectedIndex();
        contents.clear();
        
        new Thread(() -> {
            Platform.runLater(() -> {
                masterMasker.setVisible(true);
            });
            
            try {
                List<EditorPanelable> list = (model == null) ? dataFetchProvider.getFilteredBy(DataProvider.PARAM_FOR_ALL)
                                                             : dataFetchProvider.getFilteredBy(model);
                contents.setAll(list);
            } catch (Exception ex) {
            }
            
            Platform.runLater(() -> {
                masterMasker.setVisible(false);
                if (selectedIndex >= 0){
                    tableView.getSelectionModel().select(selectedIndex);
                }
            });
        }).start();
    }

    private final Timer timer = new Timer();
    private EditorPanelable newSelected;
    private final long waitingSecond = 1;
    
    @Override
    public void addListWith(Class content) {
        tableView = new AFilterableTableView<>(content);
        tableView.setId("aview");
        tableView.setBundle(bundle);
        editorPanel.buttonsMainPropertysBinder(tableView);
        editorPanel.setTableDataList(tableView, contents);
        ((StackPane)splitPane.getItems().get(0)).getChildren().add(0, tableView);
        
        tableView.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends EditorPanelable> observable, EditorPanelable oldValue, EditorPanelable newValue) -> {
            if (newValue != null){
                if (splitPane.getItems().size() > 1){
                    splitPane.getItems().get(1).setDisable(false);
                }
                observers.stream().forEach(observer -> observer.notify(newValue));
                timer.schedule(new ToDoTask(newValue), waitingSecond * 1000);
                newSelected = newValue;
            }
        });
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
}
