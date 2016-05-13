/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.clients.dialog;

import ambroafb.AmbroAFB;
import ambroafb.clients.Client;
import ambroafb.countries.Country;
import ambroafb.general.AlertMessage;
import ambroafb.general.GeneralConfig;
import ambroafb.general.KFZClient;
import ambroafb.general.ListEditor;
import ambroafb.general.Names.EDITOR_BUTTON_TYPE;
import ambroafb.general.PhoneNumber;
import ambroafb.general.Utils;
import ambroafb.general.interfaces.Dialogable;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.general.okay_cancel.OkayCancel;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.StringConverter;

/**
 *
 * @author tabramishvili
 */
public class ClientDialog extends Stage implements Dialogable {
    
    private EDITOR_BUTTON_TYPE callerButtonType; 

    GeneralConfig conf = GeneralConfig.getInstance();
//    ArrayList<Node> focusTraversableNodes;
    Client client, clientBackup;

//    @FXML
//    VBox formPane;
//    @FXML
//    private Label first_name, last_name;
//    @FXML
//    DatePicker openDate;
//    @FXML
//    CheckBox juridical, rezident;
//    @FXML
//    TextField firstName, lastName, idNumber, email, fax, address, zipCode, city;
//    @FXML
//    ComboBox<Country> country;
//    @FXML
//    ListEditor<PhoneNumber> phone;
//    @FXML
//    OkayCancel okayCancel;

    private boolean askClose = true;
    private ClientDialogController dialogController;
    
    public ClientDialog() {
        this(new Client(), EDITOR_BUTTON_TYPE.ADD);
    }

    public ClientDialog(EditorPanelable object, EDITOR_BUTTON_TYPE buttonType) {
        this((Client) object, buttonType);
    }
    
    public ClientDialog(Client client, EDITOR_BUTTON_TYPE buttonType) {
        super();
        this.callerButtonType = buttonType;
        this.client = client;
        this.clientBackup = client.cloneWithID();
        
        try {
            Scene currentScene = Utils.createScene("/ambroafb/clients/dialog/ClientDialog.fxml");
            dialogController = (ClientDialogController) currentScene.getProperties().get("controller");
            dialogController.bindClient(this.client);
            dialogController.setNextVisibleAndActionParameters(buttonType);
            this.setScene(currentScene);
        } catch (IOException ex) {
            Logger.getLogger(ClientDialog.class.getName()).log(Level.SEVERE, null, ex);
        }
        initOwner(AmbroAFB.mainStage);
        setResizable(false);

//        setOnCloseRequest((WindowEvent event) -> {
//            event.consume();
////            onClose();
//            this.client = null;
////            dialogController.onClose();
//        });
        
    }
    
//    private void onClose() {
//        System.out.println("on close");
//        boolean close = askClose ? new AlertMessage(Alert.AlertType.CONFIRMATION, null, "Do you want to exit without saving?").showAndWait().get().equals(ButtonType.OK) : true;
//        if (close) {
//            client = null;
//            System.out.println("cancelling");
//            close();
//        }
//    }

    /**
     * თუ isCancelled() არის true, აბრუნებს null-ს, თუ არადა შესაბამის კლიენტს
     *
     * @return
     */
    @Override
    public Client getResult() {
        showAndWait();
//        ButtonType type = dialogController.getAlertButtonType();
//        if (type.equals(ButtonType.OK))
//            client = null;
//        dialogController.onClose();
        return client;
    }

//    /**
//     * დიალოგის დახურვისას ამოაგდებს გაფრთხილებას ნამდვილად უნდა თუ არა დახურვა
//     *
//     * @param ask
//     */
//    @Override
//    public void askClose(boolean ask) {
//        askClose = ask;
//    }

//    @FXML
//    private void cancel(ActionEvent e) {
//        System.out.println("CCCCCCCCCCCCCCCCCCanceled");
//        onClose();
////        if (onCancell != null) {
////            onCancell.accept(null);
////        }
//    }
//
//    @FXML
//    private void okay(ActionEvent e) {
//        System.out.println("OOOOOOOOOOOOOOOOOOkaied");
//        try {
//            Client.saveOneToDB(client);
//            close();
//        } catch (Exception ex) {
//            client.copyFrom(clientBackup);
//            new AlertMessage(Alert.AlertType.ERROR, ex, ex.getMessage()).showAlert();
//        }
//    }



    @Override
    public void setDisabled() {
//        dialogController.setDisableComponents();
    }

    @Override
    public boolean allowToMakeOperation() {
        return dialogController.allowToMakeOperation();
    }

}
