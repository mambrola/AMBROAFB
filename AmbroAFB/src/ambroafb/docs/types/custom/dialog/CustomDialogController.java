/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.docs.types.custom.dialog;

import ambroafb.docs.Doc;
import ambroafb.docs.types.monthly.DocListDialogSceneComponent;
import ambroafb.general.Names;
import ambroafb.general.Utils;
import ambroafb.general.interfaces.Dialogable;
import ambroafb.general.okay_cancel.DialogOkayCancelController;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author dkobuladze
 */
public class CustomDialogController implements Initializable {

    @FXML
    private VBox formPane;
    
//    @FXML
//    private ADatePicker docDate, docInDocDate;
//    
//    @FXML @ContentNotEmpty
//    private AccountComboBox debits, credits;
//    
//    @FXML
//    private DocCodeComboBox docCodes;
//    
//    @FXML @ContentNotEmpty
//    private TextField amount, descrip;
//    
//    @FXML
//    private IsoComboBox currency;
    
    @FXML
    private DialogOkayCancelController okayCancelController;
    
    private ArrayList<Node> focusTraversableNodes;
    private boolean permissionToClose;
    private Doc doc, docBackup;
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        focusTraversableNodes = Utils.getFocusTraversableBottomChildren(formPane);
        permissionToClose = true;
        
//        Utils.validateTextFieldContentListener(amount, "\\d+|\\d+\\.|\\d+\\.\\d*");
        
//        currency.valueProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
//            resetAccounts(newValue);
//        });
    }
    
//    private void resetAccounts(String newValue){
//        new Thread(new FetchAccountsFromDB(newValue)).start();
//    }

    public void bindDoc(Doc doc) {
        this.doc = doc;
        if (doc != null){
//            docDate.valueProperty().bindBidirectional(doc.docDateProperty());
//            docInDocDate.valueProperty().bindBidirectional(doc.docInDocDateProperty());
//            debits.valueProperty().bindBidirectional(doc.debitProperty());
//            credits.valueProperty().bindBidirectional(doc.creditProperty());
//            amount.textProperty().bindBidirectional(doc.amountProperty());
//            currency.valueProperty().bindBidirectional(doc.isoProperty());
//            docCodes.valueProperty().bindBidirectional(doc.docCodeProperty());
//            descrip.textProperty().bindBidirectional(doc.descripProperty());
            
        }
    }

    public void setNextVisibleAndActionParameters(Names.EDITOR_BUTTON_TYPE buttonType) {
        if (buttonType.equals(Names.EDITOR_BUTTON_TYPE.VIEW) || buttonType.equals(Names.EDITOR_BUTTON_TYPE.DELETE)){
            setDisableComponents();
        }
        DocListDialogSceneComponent lsComp = new DocListDialogSceneComponent();
        lsComp.binTo(doc);
        lsComp.setDiableComponents(buttonType);
        formPane.getChildren().add(0, lsComp);
        
        okayCancelController.setButtonsFeatures(buttonType);
    }
    
    private void setDisableComponents(){
        focusTraversableNodes.forEach((Node t) -> {
            t.setDisable(true);
        });
    }

    public void setBackupDoc(Doc docBackup) {
        this.docBackup = docBackup;
    }
    
    public boolean anyComponentChanged(){
        return !doc.compares(docBackup);
    }
    
    public void changePermissionForClose(boolean value){
        permissionToClose = value;
    }
    
    public boolean getPermissionToClose(){
        return permissionToClose;
    }
    
    public void operationCanceled(){
        ((Dialogable)formPane.getScene().getWindow()).operationCanceled();
    }
    
    public DialogOkayCancelController getOkayCancelController() {
        return okayCancelController;
    }

//    private class FetchAccountsFromDB implements Runnable {
//
//        private final String iso;
//        private final String DB_TABLE_NAME = "accounts";
//        
//        public FetchAccountsFromDB(String iso) {
//            this.iso = iso;
//        }
//
//        @Override
//        public void run() {
//            JSONObject params = new ConditionBuilder().where().and("iso", "=", iso).condition().build();
//            ArrayList<Account> accountFromDB = DBUtils.getObjectsListFromDB(Account.class, DB_TABLE_NAME, params);
//            accountFromDB.sort((Account ac1, Account ac2) -> ac1.getRecId() - ac2.getRecId());
//            Platform.runLater(() -> {
//                fillItemsToList(debits, accountFromDB);
//                fillItemsToList(credits, accountFromDB);
//            });
//        }
//        
//        private void fillItemsToList(AccountComboBox accBox, ArrayList<Account> accounts){
//            long oldAccNum = 0;
//                if (accBox.getValue() != null){
//                    oldAccNum = accBox.getValue().getAccount();
//                }
//                accBox.getItems().clear();
//                accBox.getItems().addAll(accounts); // not use setAll method because by this method "debits" and "credits" components will has the same list object.
//                for (Account account : accounts) {
//                    if (account.getAccount() == oldAccNum){
//                        accBox.setValue(account);
//                        break;
//                    }
//                }
//                
//        }
//    }
    
}
