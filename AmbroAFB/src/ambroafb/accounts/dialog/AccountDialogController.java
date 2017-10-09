/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.accounts.dialog;

import ambro.ADatePicker;
import ambroafb.accounts.Account;
import ambroafb.balance_accounts.BalanceAccountTreeComboBox;
import ambroafb.clients.Client;
import ambroafb.clients.ClientComboBox;
import ambroafb.currencies.IsoComboBox;
import ambroafb.general.Names;
import ambroafb.general.interfaces.DialogController;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.general.okay_cancel.DialogOkayCancelController;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.Parent;
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
    private BalanceAccountTreeComboBox balAccounts;
    @FXML
    private ClientComboBox clients;
    @FXML
    private TextField accountNumber, descrip, remark;
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
        balAccounts.fillComboBoxWithoutALL(null);
        clients.showCategoryALL(false);
    }

    @Override
    public DialogOkayCancelController getOkayCancelController() {
        return okayCancelController;
    }

    @Override
    protected void bindObjectToSceneComponents(EditorPanelable object) {
        if (object != null){
            Account account = (Account)object;
            openDate.valueProperty().bindBidirectional(account.openedProperty());
            currencies.valueProperty().bindBidirectional(account.isoProperty());
            balAccounts.valueProperty().bindBidirectional(account.balAccProperty());
            clients.valueProperty().bindBidirectional(account.clientProperty());
//            accountNumber
            descrip.textProperty().bindBidirectional(account.descripProperty());
            closeDate.valueProperty().bindBidirectional(account.closedProperty());
            remark.textProperty().bindBidirectional(account.remarkProperty());
        }
    }

    @Override
    protected void makeExtraActions(EditorPanelable object, Names.EDITOR_BUTTON_TYPE buttonType) {
        int clientId = ((Account)object).getClientId();
        Optional<Client> optclient = clients.getItems().stream().filter((client) -> client.getRecId() == clientId).findFirst();
        if (optclient.isPresent()){
            System.out.println("optclient.get(): " + optclient.get().getRecId());
            clients.getSelectionModel().select(optclient.get());
        }
        else {
            System.out.println("---------------- else ---------------");
        }
    }
    
}
