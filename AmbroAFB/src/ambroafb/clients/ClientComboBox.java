/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.clients;

import static ambroafb.clients.ClientComboBox.clientALL;
import java.util.function.Predicate;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author dato
 */
public class ClientComboBox extends ComboBox<Client> {
    
    public static final Client clientALL = new Client();
    
    private static final String ALL = "ALL";
    private static final String separator = ",  ";
    
    private ClientComboBox comboBoxInstance;
    
    public ClientComboBox(){
        comboBoxInstance = (ClientComboBox) this;
        
        this.setConverter(new StringConverter<Client>() {
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
                getItems().stream().filter((Client client) -> {
                    String enteredFirstName = StringUtils.substringBefore(data, separator);
                    String enteredLastName = StringUtils.substringBetween(data, separator, separator);
                    String enteredEmail = StringUtils.substringAfterLast(data, separator);
                    return  client.getFirstName().equals(enteredFirstName) &&
                            client.getLastName().equals(enteredLastName) &&
                            client.getEmail().equals(enteredEmail);
                });
                return null;
            }
        });

        this.setEditable(true);
        
        clientALL.setFirstName(ALL);
        clientALL.setRecId(0);
        
        this.getItems().add(clientALL);
        this.getItems().addAll(Client.getAllFromDB());
        this.getEditor().setPromptText(ALL);
        this.setValue(clientALL);
        
//        makeFilterableData(this.getItems());
    }
    
    private void makeFilterableData(ObservableList<Client> list){
        FilteredList filteredList = new FilteredList(list);
        this.setItems(filteredList);
        filteredList.predicateProperty().bind(Bindings.createObjectBinding(() -> {
            if (getEditor() == null || getEditor().getText().isEmpty()){
                return null;
            }
            comboBoxInstance.show();
            return (Predicate<Client>) (Client client) -> {
                String searchText = client.getFirstName() + client.getLastName() + client.getEmail();
                return searchText.toLowerCase().contains(getEditor().getText().toLowerCase());
            };
        }, getEditor().textProperty()));
    }
    
    public void selectItem(Client client){
        this.getSelectionModel().select(client);
    }
    
}
