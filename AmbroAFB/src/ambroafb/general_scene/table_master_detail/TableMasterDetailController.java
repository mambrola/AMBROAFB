/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general_scene.table_master_detail;

import ambro.AFilterableTableView;
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
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;
import org.controlsfx.control.MaskerPane;

/**
 * FXML Controller class
 *
 * @author dkobuladze
 */
public class TableMasterDetailController extends ListingController {

    @FXML
    private AFilterableTableView<EditorPanelable> aview;
    
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
        aview.setBundle(rb);
    }
    
    public void setDetailNode(Node node){
        splitPane.getItems().add(node);
        node.setDisable(true);
    }
    
    @Override
    public void reAssignTable(FilterModel model){
        int selectedIndex = aview.getSelectionModel().getSelectedIndex();
        contents.clear();
        
        masterMasker.setVisible(true);
        
        Consumer<List<EditorPanelable>> successAction = (editorPanelables) -> {
            contents.setAll(editorPanelables);
            masterMasker.setVisible(false);
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
//        editorPanel.buttonsMainPropertiesBinder(aview);
        editorPanel.setTableDataList(aview, contents);
        
        aview.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends EditorPanelable> observable, EditorPanelable oldValue, EditorPanelable newValue) -> {
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
