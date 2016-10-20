/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.clients;

import static ambroafb.clients.ClientComboBox.clientALL;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;
import org.apache.commons.lang3.StringUtils;

/**
 * Class provides to change behaviour whether its items must be filterable.
 * When user set any value, comboBox will not filterable.
 * When user want to make comboBox a filterable, he/she must remove value from it.
 * Note: 
 * To remove value means - textEditor must contain empty string and press 'enter'.
 * If user want to see a filter effect, he/she must show comboBox elements.
 * @author dato
 */
public class ClientComboBox extends ComboBox<Client> {
    
    public static final Client clientALL = new Client();
    private static final String ALL = "ALL";
    private static final String separator = ",  ";
    
    private ClientComboBox comboBoxInstance;
    
    private boolean hasFilterableData = false;
    private ObservableList<Client> items = FXCollections.observableArrayList();
    private FilteredList<Client> filteredItems;
    private final Predicate predicate;
    
    public ClientComboBox(){
        comboBoxInstance = (ClientComboBox) this;
        clientALL.setFirstName(ALL);
        clientALL.setRecId(0);
        items.add(clientALL);
        items.addAll(Client.getAllFromDB());
        
        this.setConverter(new CustomConverter());

        valueProperty().addListener((ObservableValue<? extends Client> observable, Client oldValue, Client newValue) -> {
            if (hasFilterableData){
                setItems(items);
                hasFilterableData = false;
            }
        });
        
        getEditor().textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (newValue != null && !newValue.isEmpty()) {
                if (!hasFilterableData && getValue() == null) {
                    setItems(filteredItems);
                    hasFilterableData = true;
                }
            }            
        });
        
        predicate = (Predicate<Client>) (Client client) -> {
                String searchText = client.getFirstName() + client.getLastName() + client.getEmail();
                return searchText.toLowerCase().contains(getEditor().getText().toLowerCase());
        };
        
        filteredItems = getFilterableData(items);
        this.setEditable(true);
        this.setItems(items);
        this.setValue(clientALL);
    }
    
    private FilteredList<Client> getFilterableData(ObservableList<Client> list){
        FilteredList filteredList = new FilteredList(list);
        this.setItems(filteredList);
        filteredList.predicateProperty().bind(Bindings.createObjectBinding(() -> {
            if (getEditor() == null || getEditor().getText().isEmpty()){
                return null;
            }
            return predicate;
        }, getEditor().textProperty()));
        return filteredList;
    }
    
    public void selectItem(Client client){
        this.getSelectionModel().select(client);
    }
    
    
    private class CustomConverter extends StringConverter<Client> {

        @Override
            public String toString(Client client) {
                String result = null;
                if (client != null){
                    result = (client.equals(clientALL)) ? client.getFirstName() 
                                                        : client.getFirstName() + separator + client.getLastName() + separator + client.getEmail();
                }
                return result;
            }

            @Override
            public Client fromString(String data) {
                List<Client> clients = getItems().stream().filter((Client client) -> {
                                            String enteredFirstName = StringUtils.substringBefore(data, separator);
                                            String enteredLastName = StringUtils.substringBefore(StringUtils.substringAfter(data, separator), separator);
                                            String enteredEmail = StringUtils.substringAfterLast(data, separator);

                                            return  client.getFirstName().equals(enteredFirstName) &&
                                                    client.getLastName().equals(enteredLastName) &&
                                                    client.getEmail().equals(enteredEmail);
                }).collect(Collectors.toList());
                return (clients.isEmpty()) ? null : clients.get(0);
            }
        
    }
}
