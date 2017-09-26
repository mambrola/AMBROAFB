/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general.amount_textfield;

import ambroafb.AmbroAFB;
import ambroafb.docs.types.doc_in_order.DocOrderDialogSceneComponent;
import ambroafb.general.GeneralConfig;
import ambroafb.general.Utils;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;

/**
 *
 * @author dkobuladze
 */
public class AmountField extends TextField {
    
    public AmountField(){
        super();
        assignLoader();
        addComponentFeatures();
    }
    
    private void assignLoader(){
        FXMLLoader loader = new FXMLLoader(AmbroAFB.class.getResource("/ambroafb/general/amount_textfield/AmountField.fxml"));
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
     * The method adds components  extra features.
     */
    private void addComponentFeatures(){
       Utils.validateAmountField(this);
    }
}
