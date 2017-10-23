/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.accounts.dialog;

import ambroafb.accounts.Account;
import ambroafb.general.AlertMessage;
import ambroafb.general.Names;
import ambroafb.general.interfaces.DataProvider;
import ambroafb.general.interfaces.Dialogable;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.general.interfaces.UserInteractiveDialogStage;
import java.util.function.Function;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

/**
 *
 * @author dkobuladze
 */
public class AccountDialog extends UserInteractiveDialogStage implements Dialogable {

    private Account account;
    private final Account accountBackup;
    
    public AccountDialog(EditorPanelable object, Names.EDITOR_BUTTON_TYPE buttonType, Stage owner) {
        super(owner, buttonType, "/ambroafb/accounts/dialog/AccountDialog.fxml", "account_dialog_title");
    
        if (object == null)
            account = new Account();
        else 
            account = (Account) object;
        accountBackup = account.cloneWithID();
        
        dialogController.setSceneData(account, accountBackup, buttonType);
        
        System.out.println("--- Account Dialog ---");
    }

    @Override
    public EditorPanelable getResult() {
        showAndWait();
        return account;
    }

    @Override
    public void operationCanceled() {
        System.out.println("--- AccountDialog op Cancel ---");
        account = null;
    }
    
    // შესატანია Dialogable-ში.
    public void setDataProvider(DataProvider dp){
        this.dataProvider = dp;
    }
    
    public void okAction(){
        switch(editorButtonType){
            case DELETE:
                System.out.println("--- User Interactive DELETE ---");
                
                Function<Exception, ButtonType> error = (ex) -> {
                    return new AlertMessage(Alert.AlertType.ERROR, ex, ex.getMessage(), getTitle()).showAndWait().get();
                };
                dataProvider.deleteOneFromDB(getSceneObject().getRecId(), null, error);
                break;
            case EDIT: 
                dataProvider.editOneToDB(getSceneObject());
                break;
            case ADD:
            case ADD_SAMPLE:
                dataProvider.saveOneToDB(getSceneObject());
                break;
            default:
                break;
        }
    }
    
    @Override
    public Account getSceneObject(){
        System.out.println("--- AccountDialog getSceneObject ---");
        System.out.println("account: " + account);
        return account;
    }
}
