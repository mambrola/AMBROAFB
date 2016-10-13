/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.clients;

import ambroafb.general.Utils;
import ambroafb.general.StageUtils;
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
        Utils.registerStageByOwner(Utils.getPathForStage(owner) + "/" + getClass().getSimpleName(), (Stage)this);
        
        Scene scene = Utils.createScene("/ambroafb/clients/Clients.fxml", null);
        clientsController = (ClientsController) scene.getProperties().get("controller");
        this.setScene(scene);
        this.initOwner(owner);
        
        onCloseRequestProperty().set((EventHandler<WindowEvent>) (WindowEvent event) -> {
            clientsController.getEditorPanelController().getExitButton().getOnAction().handle(null);
            if(event != null) event.consume();
        });
        
        StageUtils.centerChildOf(owner, (Stage)this);
        StageUtils.followChildTo(owner, (Stage)this);
        StageUtils.stopStageWidthDecrease((Stage)this, () -> clientsController.getEditorPanelController().getPanelMinWidth());
        Utils.setSizeFor((Stage)this);
    }
    
    public ClientsController getClientsController(){
        return clientsController;
    }
}
