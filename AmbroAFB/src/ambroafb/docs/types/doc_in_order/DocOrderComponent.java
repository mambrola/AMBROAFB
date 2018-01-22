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
import ambroafb.general.GeneralConfig;
import ambroafb.general.Utils;
import ambroafb.general.editor_panel.EditorPanel;
import ambroafb.general.interfaces.Annotations.ContentAmount;
import ambroafb.general.interfaces.Annotations.ContentNotEmpty;
import ambroafb.general.scene_components.number_fields.amount_field.AmountField;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

/**
 *
 * @author dkobuladze
 */
public class DocOrderComponent extends VBox {
    
    @FXML
    private HBox docDatePane;
    
    @FXML
    private ADatePicker docDate, docInDocDate;
    
    @FXML
    private IsoComboBox currency;
    
    @FXML @ContentNotEmpty
    private AccountComboBox debits, credits;
    
    @FXML @ContentNotEmpty @ContentAmount
    private AmountField amount;
    
    @FXML
    private DocCodeComboBox docCodes;
    
    @FXML @ContentNotEmpty
    private TextField descrip;

    private ArrayList<Node> focusTraversableNodes;
    private boolean isDocDateRemoved;
    
    private final String docDatePaneID = "docDatePane";
            
    private VBox spaceFiller;
    private Label spaceFillerLabel;
    
    private final String withoutBorderStylesheetPath = "/styles/css/docinordercomponent.css";
    private final String dottedBorderStylesheetPath = "/styles/css/docinordercomponentdotted.css";
    
    public DocOrderComponent(){
        super();
        assignLoader();
        componentsInit();
        addComponentFeatures();
    }
    
    private void assignLoader(){
        FXMLLoader loader = new FXMLLoader(AmbroAFB.class.getResource("/ambroafb/docs/types/doc_in_order/DocOrderComponent.fxml"));
        loader.setResources(GeneralConfig.getInstance().getBundle());
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException ex) {
            Logger.getLogger(DocOrderComponent.class.getName()).log(Level.SEVERE, null, ex);
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
        
        docCodes.fillComboBoxWithoutALL(null);
    }
    
    /**
     * The method adds components  extra features.
     */
    private void addComponentFeatures(){
        currency.valueProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            debits.filterBy(newValue);
            credits.filterBy(newValue);
        });
        currency.fillComboBox(null);
    }
    
    /**
     * The method removes whole pane  of docDate from scene (It's title label also remove) .
     */
    public void removeDocDateComponent(){
        if (!isDocDateRemoved){
            getChildren().remove(0);
            isDocDateRemoved = true;
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
    public void bindBidirectTo(Doc doc){
        if (doc != null){
            if (!isDocDateRemoved){
                docDate.valueProperty().bindBidirectional(doc.docDateProperty());
            }
            docInDocDate.valueProperty().bindBidirectional(doc.docInDocDateProperty());
            currency.valueProperty().bindBidirectional(doc.isoProperty());
//            debits.valueProperty().bindBidirectional(doc.debitProperty());
//            credits.valueProperty().bindBidirectional(doc.creditProperty());
            amount.textProperty().bindBidirectional(doc.amountProperty());
            docCodes.valueProperty().bindBidirectional(doc.docCodeProperty());
            descrip.textProperty().bindBidirectional(doc.descripProperty());
            
            Consumer<ObservableList<Account>> selectDebitAccount = (accountsList) -> {
                Integer debitId = doc.getDebitId();
                Bindings.bindBidirectional(doc.debitIdProperty(), debits.valueProperty(), new AccountToIdBiConverter(debits.getItems()));
                doc.setDebitId(debitId);
            };
            debits.fillComboBoxWithoutALL(selectDebitAccount);
            
            Consumer<ObservableList<Account>> selectCreditAccount = (accountsList) -> {
                Integer creditId = doc.getCreditId();
                Bindings.bindBidirectional(doc.creditIdProperty(), credits.valueProperty(), new AccountToIdBiConverter(credits.getItems()));
                doc.setCreditId(creditId);
            };
            credits.fillComboBoxWithoutALL(selectCreditAccount);
        }
    }

    public void setDialogType(EditorPanel.EDITOR_BUTTON_TYPE buttonType) {
        if (buttonType.equals(EditorPanel.EDITOR_BUTTON_TYPE.VIEW) || buttonType.equals(EditorPanel.EDITOR_BUTTON_TYPE.DELETE)){
            setDisableComponents();
        }
    }
    
    private void setDisableComponents(){
        focusTraversableNodes.forEach((Node t) -> {
            t.setDisable(true);
        });
    }
    
    /**
     * The method provides to disabled components by fx:id.
     * @param fxIds The disabled components fx:id array.
     */
    public void disabledComponents(String... fxIds){
        for (String id : fxIds){
            Optional<Node> optNode = focusTraversableNodes.stream().filter((node) -> node.getId().equals(id)).findFirst();
            if (optNode.isPresent()){
                optNode.get().setDisable(true);
            }
        }
    }
    
    
    private class AccountToIdBiConverter extends StringConverter<Account> {

        private final ObservableList<Account> accounts;
        
        public AccountToIdBiConverter(ObservableList<Account> accounts){
            this.accounts = accounts;
        }
        
        @Override
        public String toString(Account acc) {
            String id = null;
            if (acc != null){
                id = "" + acc.getRecId();
            }
            return id;
        }

        @Override
        public Account fromString(String id) {
            Account account = null;
            Optional<Account> optAcc = accounts.stream().filter((acc) -> id.equals("" + acc.getRecId())).findFirst();
            if (optAcc.isPresent()){
                account = optAcc.get();
            }
            return account;
        }
        
    }
}
