/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.clients;

import java.util.stream.Collectors;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

/**
 *
 * @author dato
 */
public class ClientComboBox extends ComboBox<Client> {
    
    public static final Client clientALL = new Client();
    
    private static final String ALL = "ALL";
    
    public ClientComboBox(){
        this.setConverter(new StringConverter<Client>() {
            @Override
            public String toString(Client client) {
                String result = null;
                if (client != null){
                    result = (client.equals(clientALL)) ? client.getFirstName() 
                                                        : client.getFirstName() + ",  " + client.getLastName() + ",  " + client.getEmail();
                }
                return result;
            }

            @Override
            public Client fromString(String data) {
                return null;
            }
        });

        TextFields.bindAutoCompletion(getEditor(), 
                                      (AutoCompletionBinding.ISuggestionRequest param) -> 
                                                                getItems().stream().filter((Client client) -> 
                                                                                                getConverter().toString(client).toLowerCase().contains(param.getUserText().toLowerCase()))
                                                                                   .collect(Collectors.toList()), getConverter());
        
        this.setEditable(true);
        
        clientALL.setFirstName(ALL);
        clientALL.setRecId(0);
        
        this.getItems().add(clientALL);
        this.getItems().addAll(Client.getAllFromDB());
        this.setValue(clientALL);
    }
    
    public void selectItem(Client client){
        this.getSelectionModel().select(client);
    }
    
}
