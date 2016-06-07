/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.clients;

import ambroafb.AmbroAFB;
import ambroafb.general.GeneralConfig;
import ambroafb.general.Utils;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author murman
 */
public class Clients extends Stage {
    
    private ClientsController clientsController;
    
    public Clients(Stage owner) {
        try {
            String ownerPath = Utils.getPathForStage(owner);
            String clientsStagePath = ownerPath + "/Clients";
            Utils.saveShowingStageByPath(clientsStagePath, (Stage)this);
            
            Scene scene = Utils.createScene("/ambroafb/clients/Clients.fxml");
            clientsController = (ClientsController) scene.getProperties().get("controller");
            clientsController.setStage((Stage)this);
            this.setScene(scene);
        } catch (IOException ex) { Logger.getLogger(Clients.class.getName()).log(Level.SEVERE, null, ex); }
        initOwner(owner);
        
        onCloseRequestProperty().set((EventHandler<WindowEvent>) (WindowEvent event) -> {
            clientsController.getEditorPanelController().getExitButton().getOnAction().handle(null);
            event.consume();
        });

    }
    
    public ClientsController getClientsController(){
        return clientsController;
    }
}
