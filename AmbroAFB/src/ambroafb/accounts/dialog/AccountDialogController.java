/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.accounts.dialog;

import ambro.ADatePicker;
import ambroafb.clients.ClientComboBox;
import ambroafb.currencies.IsoComboBox;
import ambroafb.general.Names;
import ambroafb.general.interfaces.DialogController;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.general.okay_cancel.DialogOkayCancelController;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author dkobuladze
 */
public class AccountDialogController extends DialogController {

    @FXML
    private VBox formPane;
    
    @FXML
    private ADatePicker openDate;
    @FXML
    private IsoComboBox currencies;
    @FXML
    private ComboBox<String> balAccounts; // ???
    @FXML
    private ClientComboBox clients;
    @FXML
    private TextField accountNumber, descrip, note;
    @FXML
    private ADatePicker closeDate;
    
    
    
    @FXML
    private DialogOkayCancelController okayCancelController;
    
    @Override
    protected Parent getSceneRoot() {
        return formPane;
    }

    @Override
    protected void componentsInitialize(URL url, ResourceBundle rb) {
        
    }

    @Override
    public DialogOkayCancelController getOkayCancelController() {
        return okayCancelController;
    }

    @Override
    protected void bindObjectToSceneComonents(EditorPanelable object) {
        System.out.println("<<<< bindObjectToSceneComonents >>>>");
    }

    @Override
    protected void makeSceneFor(Names.EDITOR_BUTTON_TYPE buttonType) {
        System.out.println("<<<< makeSceneFor >>>>");
    }
    
}
