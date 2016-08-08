/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.clients;

import ambroafb.general.Utils;
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
        Utils.saveShowingStageByPath(Utils.getPathForStage(owner) + "/" + Clients.class.getSimpleName(), (Stage)this);

        Scene scene = Utils.createScene("/ambroafb/clients/Clients.fxml", null);
        clientsController = (ClientsController) scene.getProperties().get("controller");
        this.setScene(scene);
        this.initOwner(owner);
        
        onCloseRequestProperty().set((EventHandler<WindowEvent>) (WindowEvent event) -> {
            clientsController.getEditorPanelController().getExitButton().getOnAction().handle(null);
            if(event != null) event.consume();
        });
        
        Utils.setSizeFor((Stage)this);
    }
    
    public ClientsController getClientsController(){
        return clientsController;
    }
}
