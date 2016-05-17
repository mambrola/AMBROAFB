/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.clients.dialog;

import ambroafb.AmbroAFB;
import ambroafb.clients.Client;
import ambroafb.general.GeneralConfig;
import ambroafb.general.Names.EDITOR_BUTTON_TYPE;
import ambroafb.general.PhoneNumber;
import ambroafb.general.Utils;
import ambroafb.general.interfaces.Dialogable;
import ambroafb.general.interfaces.EditorPanelable;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author tabramishvili
 */
public class ClientDialog extends Stage implements Dialogable {
    
    public Client client, clientBackup;

    private ClientDialogController dialogController;
    
    public ClientDialog() {
        this(new Client(), EDITOR_BUTTON_TYPE.ADD);
    }

    public ClientDialog(EditorPanelable object, EDITOR_BUTTON_TYPE buttonType) {
        this((Client) object, buttonType);
    }
    
    public ClientDialog(Client client, EDITOR_BUTTON_TYPE buttonType) {
        super();
        this.client = client;
        this.clientBackup = client.cloneWithID();
        
//        client.getPhoneList().set(0, new PhoneNumber("1"));
//        boolean comp = client.comparePhones(client.getPhoneList(), clientBackup.getPhoneList());
//        System.out.println("comp: " + comp);
        
        try {
            Scene currentScene = Utils.createScene("/ambroafb/clients/dialog/ClientDialog.fxml");
            dialogController = (ClientDialogController) currentScene.getProperties().get("controller");
            dialogController.setNextVisibleAndActionParameters(buttonType);
            dialogController.bindClient(this.client);
            this.setScene(currentScene);
        } catch (IOException ex) { Logger.getLogger(ClientDialog.class.getName()).log(Level.SEVERE, null, ex); }
        initOwner(AmbroAFB.mainStage);
        setResizable(false);

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
