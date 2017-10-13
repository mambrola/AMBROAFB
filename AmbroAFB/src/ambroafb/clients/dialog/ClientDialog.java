/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.clients.dialog;

import ambroafb.clients.Client;
import ambroafb.general.Names.EDITOR_BUTTON_TYPE;
import ambroafb.general.SceneUtils;
import ambroafb.general.interfaces.DialogController;
import ambroafb.general.interfaces.Dialogable;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.general.interfaces.UserInteractiveDialogStage;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author tabramishvili
 */
public class ClientDialog extends UserInteractiveDialogStage implements Dialogable {
    
    public Client client;
    public final Client clientBackup;
    
    private DialogController dialogController;
    
    public ClientDialog(EditorPanelable object, EDITOR_BUTTON_TYPE buttonType, Stage owner) {
        super(owner, "client_dialog_title");
        
        if (object == null)
            client = new Client();
        else
            client = (Client) object;
        
        this.clientBackup = client.cloneWithID();
        
        Scene currentScene = SceneUtils.createScene("/ambroafb/clients/dialog/ClientDialog.fxml", null);
        dialogController = (ClientDialogController) currentScene.getProperties().get("controller");
        dialogController.setSceneData(client, clientBackup, buttonType);
        this.setScene(currentScene);
        
        onCloseRequestProperty().set((EventHandler<WindowEvent>) (WindowEvent event) -> {
            dialogController.getOkayCancelController().getCancelButton().getOnAction().handle(null);
            if (event != null) event.consume();
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

    @Override
    public boolean anyComponentChanged() {
        return dialogController.anySceneComponentChanged() || client.getClientImageGallery().anyViewerChanged();
    }

}
