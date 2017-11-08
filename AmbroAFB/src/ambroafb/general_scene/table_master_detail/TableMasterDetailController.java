/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general_scene.table_master_detail;

import ambro.AFilterableTableView;
import ambroafb.general.Printer;
import ambroafb.general.interfaces.DataProvider;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.general.interfaces.FilterModel;
import ambroafb.general.interfaces.ListingController;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.function.Supplier;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import org.controlsfx.control.MaskerPane;
import org.controlsfx.control.MasterDetailPane;

/**
 * FXML Controller class
 *
 * @author dkobuladze
 */
public class TableMasterDetailController extends ListingController {

    private AFilterableTableView<EditorPanelable> tableView;
    
    @FXML
    private MasterDetailPane masterDetailPane;
    
    @FXML
    private MaskerPane masterMasker, detailMasker;
    
    private final ObservableList<EditorPanelable> contents = FXCollections.observableArrayList();

    private Consumer<EditorPanelable> selectionAction;

    @Override
    protected void componentsInitialize(URL url, ResourceBundle rb) {
        
    }
    
    
    public void setDetailNode(Node node, Consumer<EditorPanelable> action){
        ((StackPane)masterDetailPane.getDetailNode()).getChildren().add(0, node);
        selectionAction = action;
    }

    @Override
    public void reAssignTable(Supplier<List<EditorPanelable>> fetchData) {
        Printer.printInfo("TableMasterDetailController", "reAssignTable", "Supplier<List<EditorPanelable>> fetchData");
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

    @Override
    public void addListWith(Class content) {
        tableView = new AFilterableTableView<>(content);
        tableView.setId("aview");
        tableView.setBundle(bundle);
        editorPanel.buttonsMainPropertysBinder(tableView);
        editorPanel.setTableDataList(tableView, contents);
        ((StackPane)masterDetailPane.getMasterNode()).getChildren().add(0, tableView);
        
        tableView.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends EditorPanelable> observable, EditorPanelable oldValue, EditorPanelable newValue) -> {
            if (selectionAction != null){
                detailMasker.setVisible(true);
                new Thread(() -> {
                    selectionAction.accept(newValue);
                    Platform.runLater(() -> {
                        detailMasker.setVisible(false);
                    });
                }).start();
            }
        });
    }

}
