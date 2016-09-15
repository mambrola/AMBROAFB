/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.clients;

import ambroafb.general.GeneralConfig;
import ambroafb.general.KFZClient;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.ComboBox;
import org.json.JSONArray;
import org.json.JSONException;

/**
 *
 * @author dato
 */
public class StatusComboBox extends ComboBox<String> {
    
    public StatusComboBox(){
        try {
            JSONArray statuses = new JSONArray(GeneralConfig.getInstance().getServerClient().get("/clients/statuses"));
            for (int i = 0; i < statuses.length(); i++){
                String status = statuses.getString(i).trim();
                if (!status.isEmpty())
                    this.getItems().add(status);
            }
        } catch (JSONException | IOException | KFZClient.KFZServerException ex) {
            Logger.getLogger(StatusComboBox.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
