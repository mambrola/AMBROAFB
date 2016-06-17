/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.clients.dialog;

import ambroafb.clients.Client;
import ambroafb.general.Names.EDITOR_BUTTON_TYPE;
import ambroafb.general.Utils;
import ambroafb.general.interfaces.Dialogable;
import ambroafb.general.interfaces.EditorPanelable;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author tabramishvili
 */
public class ClientDialog extends Stage implements Dialogable {
    
    public Client client, clientBackup;
    
    private ClientDialogController dialogController;
    
    public ClientDialog(EditorPanelable object, EDITOR_BUTTON_TYPE buttonType, Stage owner) {
        String ownerPath = Utils.getPathForStage(owner);
        String clientsDialogPath = ownerPath + Dialogable.LOCAL_NAME;
        Utils.saveShowingStageByPath(clientsDialogPath, (Stage)this);
        
        Client clientObject;
        if (object == null)
            clientObject = new Client();
        else
            clientObject = (Client) object; 
        
        this.client = clientObject;
        this.clientBackup = clientObject.cloneWithID();
        
        try {
            Scene currentScene = Utils.createScene("/ambroafb/clients/dialog/ClientDialog.fxml", null);
            dialogController = (ClientDialogController) currentScene.getProperties().get("controller");
            dialogController.bindClient(this.client); // this must be before of setNextVisibleAndActionParameters() method, because of sets items in phonelist.
            dialogController.setNextVisibleAndActionParameters(buttonType);
            dialogController.setBackupClient(this.clientBackup);
            this.setScene(currentScene);
        } catch (IOException ex) { Logger.getLogger(ClientDialog.class.getName()).log(Level.SEVERE, null, ex); }
        setResizable(false);
        initOwner(owner);

        onCloseRequestProperty().set((EventHandler<WindowEvent>) (WindowEvent event) -> {
            dialogController.getOkayCancelController().getCancelButton().getOnAction().handle(null);
            event.consume();
        });
        
    }
    
    @Override
    public Client getResult() {
        showAndWait();
        return client;
    }
    
    @Override
    public void operationCanceled(){
        client = null;
    }

}
