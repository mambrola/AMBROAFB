/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.clients.viewadd.client_dialog;

import ambroafb.AmbroAFB;
import ambroafb.clients.Client;
import ambroafb.general.Utils;
import java.io.IOException;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author tabramishvili
 */
public class ClientDialog extends Stage{
    
    private ClientDialogController controller;
    private Client result;
    private boolean cancelled = true;
    
    public ClientDialog(){
        this(new Client());
    }
    
    public ClientDialog(Client client){
        super();

        try {
            Scene scene = Utils.createScene("/ambroafb/clients/viewadd/client_dialog/ClientDialog.fxml");
            setScene(scene);
            controller = (ClientDialogController) scene.getProperties().get("controller");
            
        } catch (IOException ex) {
            Logger.getLogger(ClientDialog.class.getName()).log(Level.SEVERE, null, ex);
        }
        initOwner(AmbroAFB.mainStage);
        setResizable(false);
        
        
        controller.setClient(client);
        controller.onCreate((Client t) -> {
            System.out.println("on create: "+t);
            result = t;
            cancelled = false;
            close();
        });
        
        controller.onCancell((Void t) -> {
            cancelled = true;
            close();
        });
        
    }
    
    
    /**
     * თუ isCancelled() არის true, აბრუნებს null-ს, თუ არადა შესაბამის კლიენტს
     * @return 
     */
    public Client getResult(){
        return isCancelled() ? null : result;
    }
    
    public boolean isCancelled(){
        return cancelled;
    }
    
    public void setDisabled(){
        controller.setDisabled();
    }
    
}
