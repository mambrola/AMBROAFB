/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.clients.combobox;

import ambroafb.AmbroAFB;
import ambroafb.clients.Client;
import ambroafb.general.GeneralConfig;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;

/**
 *
 * @author dato
 */
public class ClientComboBox extends ComboBox<Client> {
    
    public ClientComboBox(){
        FXMLLoader loader = new FXMLLoader(AmbroAFB.class.getResource("/ambroafb/clients/combobox/ClientComboBox.fxml"));
        loader.setResources(GeneralConfig.getInstance().getBundle());
        loader.setRoot((ComboBox)this);
        loader.setController((ComboBox)this);
        try {
            loader.load();
        } catch (IOException ex) {
            Logger.getLogger(ClientComboBox.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        setConverter(new StringConverter<Client>() {
            @Override
            public String toString(Client client) {
                String result = client.getFirstName()
                                .concat("\t").concat(client.getLastName())
                                .concat("\t").concat(client.getEmail());
                return result;
            }

            @Override
            public Client fromString(String data) {
                return null;
            }
        });
    }
    
    public void selectItem(Client client){
        this.getSelectionModel().select(client);
    }
}
