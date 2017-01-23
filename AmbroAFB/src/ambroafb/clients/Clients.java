/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.clients;

import ambroafb.general.SceneUtils;
import ambroafb.general.stages.ListingStage;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author murman
 */
public class Clients extends ListingStage {
    
    private ClientsController clientsController;
    
    public Clients(Stage owner) {
        super(owner, StringUtils.substringAfterLast(Clients.class.getSimpleName(), "."), "clients", "/images/list.png");
        
        Scene scene = SceneUtils.createScene("/ambroafb/clients/Clients.fxml", null);
        clientsController = (ClientsController) scene.getProperties().get("controller");
        this.setScene(scene);
        
        onCloseRequestProperty().set((EventHandler<WindowEvent>) (WindowEvent event) -> {
            clientsController.getEditorPanelController().getExitButton().getOnAction().handle(null);
            if(event != null) event.consume();
        });
        
        super.setFeatures(() -> clientsController.getEditorPanelController().getPanelMinWidth());
    }
    
    public ClientsController getClientsController(){
        return clientsController;
    }
}
