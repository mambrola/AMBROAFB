/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.clients.dialog;

import ambroafb.clients.Client;
import ambroafb.general.editor_panel.EditorPanel.EDITOR_BUTTON_TYPE;
import ambroafb.general.interfaces.Dialogable;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.general.interfaces.UserInteractiveDialogStage;
import javafx.stage.Stage;

/**
 *
 * @author tabramishvili
 */
public class ClientDialog extends UserInteractiveDialogStage implements Dialogable {
    
    public Client client;
    public final Client clientBackup;
    
    public ClientDialog(EditorPanelable object, EDITOR_BUTTON_TYPE buttonType, Stage owner) {
        super(owner, buttonType, "/ambroafb/clients/dialog/ClientDialog.fxml");
        
        if (object == null)
            client = new Client();
        else
            client = (Client) object;
        clientBackup = client.cloneWithID();
        
        dialogController.setSceneData(client, clientBackup, buttonType);
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
    protected EditorPanelable getSceneObject() {
        return client;
    }

}
