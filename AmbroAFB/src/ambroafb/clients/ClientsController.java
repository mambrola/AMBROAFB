/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.clients;

import ambroafb.general.Names;
import ambroafb.general.editor_panel.EditorPanelController;
import ambroafb.general.interfaces.EditorPanelable;
import java.net.URL;
import java.util.Collections;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import org.controlsfx.control.table.TableFilter;

/**
 * FXML Controller class
 *
 * @author mambroladze
 */
public class ClientsController implements Initializable {

    @FXML
    private TableView<EditorPanelable> table;
    
    @FXML
    private TextField filter;

    @FXML
    private EditorPanelController editorPanelController;
    
    private ObservableList<EditorPanelable> clients;
    private SortedList<EditorPanelable> sorterData;
    
    /**
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        editorPanelController.setOuterController(this);
        editorPanelController.buttonsMainPropertysBinder(table);
        assignTable();
        editorPanelController.setInnerTableDataList(clients);
    }

    //შეიძლება გატანა მშობელ კლასში
    private void assignTable() {
        clients = FXCollections.observableArrayList();
        Client.getClients().stream().forEach((client) -> {
            clients.add(client);
        });
        
        FilteredList<EditorPanelable> filterData = new FilteredList<>(clients, p -> true);
        FilterFieldChangeListener filtrListener = new FilterFieldChangeListener(filterData);
        filter.textProperty().addListener(filtrListener);
        
        // For columns sort: 
        sorterData = new SortedList<>(filterData);
        sorterData.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sorterData);

    }
    
    private class FilterFieldChangeListener implements ChangeListener<String> {
        
        private FilteredList<EditorPanelable> clientsData;
        
        public FilterFieldChangeListener(FilteredList<EditorPanelable> filterData){
            clientsData = filterData;
        }

        @Override
        public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
            PredicateListener predListener = new PredicateListener(newValue);
            clientsData.setPredicate(predListener);
        }
        
    }
    
    
    private class PredicateListener implements Predicate<EditorPanelable> {
        
        private boolean predicate;
        private final String searchStr;
        
        public PredicateListener(String searchable){
            searchStr = searchable.toLowerCase();
            predicate = false;
        }
        
        @Override
        public boolean test(EditorPanelable param) {
            Client c = (Client)param;
            
            if (c == null || searchStr.isEmpty())
                predicate = true;
            else {
                String values = c.getFilterFieldValues();
                predicate = values.contains(searchStr);
            }
            return predicate;
        }
        
    }
}
