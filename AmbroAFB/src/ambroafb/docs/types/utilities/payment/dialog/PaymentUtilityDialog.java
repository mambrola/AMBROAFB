/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.docs.types.utilities.payment.dialog;

import ambroafb.docs.types.utilities.payment.PaymentUtility;
import ambroafb.general.Names;
import ambroafb.general.interfaces.Dialogable;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.general.interfaces.UserInteractiveDialogStage;
import javafx.stage.Stage;

/**
 *
 * @author dkobuladze
 */
public class PaymentUtilityDialog extends UserInteractiveDialogStage implements Dialogable {

    private PaymentUtility paymentUtility;
    private final PaymentUtility paymentUtilityBackup;
    
    public PaymentUtilityDialog(EditorPanelable object, Names.EDITOR_BUTTON_TYPE buttonType, Stage owner) {
        super(owner, "/ambroafb/docs/types/utilities/payment/dialog/PaymentUtilityDialog.fxml", "doc_payment_utility_dialog_title");

        if (object == null)
            paymentUtility = new PaymentUtility();
        else 
            paymentUtility = (PaymentUtility) object;
        paymentUtilityBackup = paymentUtility.cloneWithID();
        
        dialogController.setSceneData(paymentUtility, paymentUtilityBackup, buttonType);
    }
    
    @Override
    public EditorPanelable getResult() {
        showAndWait();
        return paymentUtility;
    }

    @Override
    public void operationCanceled(){
        paymentUtility = null;
    }

}
