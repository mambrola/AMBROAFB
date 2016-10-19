/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.clients;

import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;

/**
 *
 * @author dato
 */
public class ClientComboBox extends ComboBox<Client> {
    
    public ClientComboBox(){
        this.setConverter(new StringConverter<Client>() {
            @Override
            public String toString(Client client) {
                if (client == null) return null;
                String result = client.getFirstName() + ",  " + client.getLastName() + ",  " + client.getEmail();
                return result;
            }

            @Override
            public Client fromString(String data) {
                return null;
            }
        });

//        TextFields.bindAutoCompletion(getEditor(), 
//                                      (AutoCompletionBinding.ISuggestionRequest param) -> 
//                                                                getItems().stream().filter((Client client) -> 
//                                                                                    getConverter().toString(client).toLowerCase().contains(param.getUserText().toLowerCase()))
//                                                                                .collect(Collectors.toList()), 
//                                      this.getConverter());
//        
//        this.setEditable(true);
        this.getItems().addAll(Client.getAllFromDB());
    }
    
    public void selectItem(Client client){
        this.getSelectionModel().select(client);
    }
}
