/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.docs.types.monthly;

import ambro.ADatePicker;
import ambroafb.AmbroAFB;
import ambroafb.accounts.AccountComboBox;
import ambroafb.currencies.IsoComboBox;
import ambroafb.docs.Doc;
import ambroafb.docs.DocCodeComboBox;
import ambroafb.general.GeneralConfig;
import ambroafb.general.interfaces.Annotations;
import ambroafb.general.save_button.SaveButton;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/**
 *
 * @author dkobuladze
 */
public class DocListDialogSceneComponent extends VBox {
    
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

    
    private boolean isDocDateRemoved;
    
    public DocListDialogSceneComponent(){
        super();
        assignLoader();
    }
    
    private void assignLoader(){
        FXMLLoader loader = new FXMLLoader(AmbroAFB.class.getResource("/ambroafb/docs/monthly/DocListDialogSceneComponent.fxml"));
        loader.setResources(GeneralConfig.getInstance().getBundle());
        loader.setRoot(this);
        loader.setController(this);
        
        try {
            loader.load();
        } catch (IOException ex) {
            Logger.getLogger(SaveButton.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * The method removes docDate component from scene.
     */
    public void removeDocDateComponent(){
        getChildren().remove(0);
        isDocDateRemoved = true;
    }
    
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
}
