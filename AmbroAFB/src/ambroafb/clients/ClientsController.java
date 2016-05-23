/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.clients;

import ambroafb.general.editor_panel.EditorPanelController;
import java.net.URL;
import java.util.Collections;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author mambroladze
 */
public class ClientsController implements Initializable {

    @FXML
    private TableView<Client> table;
    
    @FXML
    private TextField filter;

    @FXML
    private EditorPanelController editorPanelController;
    

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
    }

    //შეიძლება გატანა მშობელ კლასში
    public void assignTable() {
        ObservableList<Client> clients = FXCollections.observableArrayList();
        Client.getClients().stream().forEach((client) -> {
            clients.add(client);
        });
        
        FilteredList<Client> filterData = new FilteredList<>(clients, p -> true);
        FilterFieldChangeListener filtrListener = new FilterFieldChangeListener(filterData);
        filter.textProperty().addListener(filtrListener);
        
        SortedList<Client> sorterData = new SortedList<>(filterData);
        table.setItems(sorterData);
        
//        Client.getClients().stream().forEach((client) -> {
//            table.getItems().add(client);
//        });
    }
    
    
    private class FilterFieldChangeListener implements ChangeListener<String> {
        
        private FilteredList<Client> clientsData;
        
        public FilterFieldChangeListener(FilteredList<Client> filterData){
            clientsData = filterData;
        }

        @Override
        public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
            PredicateListener predListener = new PredicateListener(newValue);
            clientsData.setPredicate(predListener);
        }
        
    }
    
    
    private class PredicateListener implements Predicate<Client> {
        
        private boolean predicate;
        private String searchStr;
        
        public PredicateListener(String searchable){
            searchStr = searchable.toLowerCase();
            predicate = false;
        }
        
        @Override
        public boolean test(Client c) {
            if (c == null || searchStr.isEmpty())
                predicate = true;
            else {
                String firstName = c.getFirstName().toLowerCase();
                String lastName = c.getLastName().toLowerCase();

                predicate = firstName.contains(searchStr) || lastName.contains(searchStr);
            }
            return predicate;
        }
        
    }
}
