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
    private TableView<Client> table;
    
    @FXML
    private TextField filter;

    @FXML
    private EditorPanelController editorPanelController;
    
    private ObservableList<Client> clients;
    private SortedList<Client> sorterData;
    
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
    private void assignTable() {
        clients = FXCollections.observableArrayList();
        Client.getClients().stream().forEach((client) -> {
            clients.add(client);
        });
        
        FilteredList<Client> filterData = new FilteredList<>(clients, p -> true);
        FilterFieldChangeListener filtrListener = new FilterFieldChangeListener(filterData);
        filter.textProperty().addListener(filtrListener);
        
        // For columns sort:
        sorterData = new SortedList<>(filterData);
        sorterData.comparatorProperty().bind(table.comparatorProperty());
//        table.setItems(sorterData);
        
        table.setItems(clients);
        
//        Client.getClients().stream().forEach((client) -> {
//            table.getItems().add(client);
//        });
        
//        TableFilter<Client> filter = new TableFilter<>(table);
    }
    
    public void makeAppropriateActionOn(Names.EDITOR_BUTTON_TYPE buttonType, EditorPanelable tableElem){
        if (buttonType.equals(Names.EDITOR_BUTTON_TYPE.DELETE)){
            clients.remove((Client) tableElem);
        }
        else if(buttonType.equals(Names.EDITOR_BUTTON_TYPE.ADD)){
           clients.add((Client) tableElem);
        }
    }
    
    @FXML
    public void clickOnTable(MouseEvent event){
        table.setItems(clients);
    }
    
    private class FilterFieldChangeListener implements ChangeListener<String> {
        
        private FilteredList<Client> clientsData;
        
        public FilterFieldChangeListener(FilteredList<Client> filterData){
            clientsData = filterData;
        }

        @Override
        public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
            if (newValue.isEmpty()){
                table.setItems(sorterData);
            }
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
                String email = c.getEmail().toLowerCase();
                String address = c.getAddress().toLowerCase();
                String zipCode = c.getZipCode().toLowerCase();
                String city = c.getCity().toLowerCase();
                String country = c.getCountry().getName().toLowerCase();
                
                predicate = firstName.contains(searchStr) || 
                            lastName.contains(searchStr)  || 
                            address.contains(searchStr)   ||
                            zipCode.contains(searchStr)   ||
                            city.contains(searchStr)      ||
                            country.contains(searchStr)   ||
                            email.contains(searchStr);
            }
            return predicate;
        }
        
    }
}
