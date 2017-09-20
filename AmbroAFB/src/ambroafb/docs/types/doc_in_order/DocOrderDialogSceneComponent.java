/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.docs.types.doc_in_order;

import ambro.ADatePicker;
import ambroafb.AmbroAFB;
import ambroafb.accounts.Account;
import ambroafb.accounts.AccountComboBox;
import ambroafb.currencies.IsoComboBox;
import ambroafb.docs.Doc;
import ambroafb.docs.DocCodeComboBox;
import ambroafb.general.DBUtils;
import ambroafb.general.GeneralConfig;
import ambroafb.general.Names;
import ambroafb.general.Utils;
import ambroafb.general.interfaces.Annotations;
import authclient.db.ConditionBuilder;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.json.JSONObject;

/**
 *
 * @author dkobuladze
 */
public class DocOrderDialogSceneComponent extends VBox {
    
    @FXML
    private VBox docDatePane;
    
    @FXML
    private ADatePicker docDate, docInDocDate;
    
    @FXML
    private IsoComboBox currency;
    
    @FXML @Annotations.ContentNotEmpty
    private AccountComboBox debits, credits;
    
    @FXML @Annotations.ContentNotEmpty
    private TextField amount;
    
    @FXML
    private DocCodeComboBox docCodes;
    
    @FXML @Annotations.ContentNotEmpty
    private TextField descrip;

    private ArrayList<Node> focusTraversableNodes;
    private boolean isDocDateRemoved;
    
    private final String docDatePaneID = "docDatePane";
            
    private VBox spaceFiller;
    private Label spaceFillerLabel;
    
    private final String withoutBorderStylesheetPath = "/styles/css/doclistdialog.css";
    private final String dottedBorderStylesheetPath = "/styles/css/doclistdialogdotted.css";
    
    public DocOrderDialogSceneComponent(){
        super();
        assignLoader();
        componentsInit();
        addComponentFeatures();
    }
    
    private void assignLoader(){
        FXMLLoader loader = new FXMLLoader(AmbroAFB.class.getResource("/ambroafb/docs/types/doc_in_order/DocOrderDialogSceneComponent.fxml"));
        loader.setResources(GeneralConfig.getInstance().getBundle());
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException ex) {
            Logger.getLogger(DocOrderDialogSceneComponent.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Initialize scene components.
     */
    private void componentsInit(){
        focusTraversableNodes = Utils.getFocusTraversableBottomChildren(this);
        spaceFiller = new VBox();
        spaceFiller.getStyleClass().add("couple");
        spaceFillerLabel = new Label("");
        spaceFiller.getChildren().add(spaceFillerLabel);
    }
    
    /**
     * The method adds components  extra features.
     */
    private void addComponentFeatures(){
        currency.valueProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            resetAccounts(newValue);
        });
        
        Utils.validateTextFieldContentListener(amount, "\\d+|\\d+\\.|\\d+\\.\\d*");
    }
    
    private void resetAccounts(String newValue){
        new Thread(new FetchAccountsFromDB(newValue)).start();
    }
    
    
    /**
     * The method removes docDate whole pane  (with top label)  from scene.
     */
    public void removeDocDateComponent(){
        HBox firstRow = (HBox)getChildren().get(0);
        if (!isDocDateRemoved){
            firstRow.getChildren().remove(0);
            isDocDateRemoved = true;
            setHelperVBox(true);
        }
    }
    
    /**
     * The method adds helper component (empty label VBox) into scene first row. , Scene components had disordered after  remove docDatePane.
     * @param flag remove or add helper component. True - add. False - remove.
     */
    private void setHelperVBox(boolean flag){
        HBox firstRow = (HBox)getChildren().get(0);
        if (flag){
            firstRow.getChildren().add(spaceFiller);
        }
        else {
            firstRow.getChildren().remove(2);
        }
    }
    
    /**
     * The method finds docDate VBox id in scene parent node.
     * @return True, if this id is found. Otherwise - false.
     */
    private boolean existsDocDatePane(){
        HBox firstRow = (HBox)getChildren().get(0);
        return firstRow.getChildren().stream().filter((Node node) -> {
            if (node.getId() != null)
                return node.getId().equals(docDatePaneID);
            else
                return false;
            }).count() != 0;
    }
    
    /**
     * The method adds docDate whole pane (with top label)  on scene.
     */
    public void addDocDateComponent(){
        HBox firstRow = (HBox)getChildren().get(0);
        if (isDocDateRemoved){
            firstRow.getChildren().add(0, docDatePane);
            isDocDateRemoved = false;
            setHelperVBox(false);
        }
    }
    
    /**
     * The method remove or add dotted border from scene. 
     * @param flag If flag parameter is 'r', then dotted border remove from scene (if it exists).
     *                          If flag parameter is 'a', then dotted border add on scene (if not exists).
     */
    public void setDottedBorder(char flag){
        ObservableList<String> stylesheetsFilePathes = this.getStylesheets();
        if (flag == 'r' && stylesheetsFilePathes.contains(this.dottedBorderStylesheetPath)){
            stylesheetsFilePathes.remove(dottedBorderStylesheetPath);
            stylesheetsFilePathes.add(withoutBorderStylesheetPath);
        }
        if (flag == 'a' && !stylesheetsFilePathes.contains(this.dottedBorderStylesheetPath)){
            stylesheetsFilePathes.remove(withoutBorderStylesheetPath);
            stylesheetsFilePathes.add(dottedBorderStylesheetPath);
        }
    }
    
    /**
     * The method binds (bidirectional) doc object to scene components.
     * @param doc Object from that must be show on scene.
     */
    public void binTo(Doc doc){
        if (doc != null){
            if (!isDocDateRemoved){
                docDate.valueProperty().bindBidirectional(doc.docDateProperty());
            }
            docInDocDate.valueProperty().bindBidirectional(doc.docInDocDateProperty());
            currency.valueProperty().bindBidirectional(doc.isoProperty());
            debits.valueProperty().bindBidirectional(doc.debitProperty());
            credits.valueProperty().bindBidirectional(doc.creditProperty());
            amount.textProperty().bindBidirectional(doc.amountProperty());
            docCodes.valueProperty().bindBidirectional(doc.docCodeProperty());
            descrip.textProperty().bindBidirectional(doc.descripProperty());
        }
    }

    public void setDiableComponents(Names.EDITOR_BUTTON_TYPE buttonType) {
        if (buttonType.equals(Names.EDITOR_BUTTON_TYPE.VIEW) || buttonType.equals(Names.EDITOR_BUTTON_TYPE.DELETE)){
            setDisableComponents();
        }
    }
    
    private void setDisableComponents(){
        focusTraversableNodes.forEach((Node t) -> {
            t.setDisable(true);
        });
    }
    
    /**
     * The Runnable class provides  to fetch data from server.
     */
    private class FetchAccountsFromDB implements Runnable {

        private final String iso;
        private final String DB_TABLE_NAME = "accounts";
        
        public FetchAccountsFromDB(String iso) {
            this.iso = iso;
        }

        @Override
        public void run() {
            JSONObject params = new ConditionBuilder().where().and("iso", "=", iso).condition().build();
            ArrayList<Account> accountFromDB = DBUtils.getObjectsListFromDB(Account.class, DB_TABLE_NAME, params);
            accountFromDB.sort((Account ac1, Account ac2) -> ac1.getRecId() - ac2.getRecId());
            Platform.runLater(() -> {
                fillItemsToList(debits, accountFromDB);
                fillItemsToList(credits, accountFromDB);
            });
        }
        
        private void fillItemsToList(AccountComboBox accBox, ArrayList<Account> accounts){
            long oldAccNum = 0;
                if (accBox.getValue() != null){
                    oldAccNum = accBox.getValue().getAccount();
                }
                accBox.getItems().clear();
                accBox.getItems().addAll(accounts); // not use setAll method because by this method "debits" and "credits" components will has the same list object.
                for (Account account : accounts) {
                    if (account.getAccount() == oldAccNum){
                        accBox.setValue(account);
                        break;
                    }
                }
                
        }
    }
}
