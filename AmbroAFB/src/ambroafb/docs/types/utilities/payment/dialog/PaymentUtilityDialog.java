/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.docs.types.utilities.payment.dialog;

import ambroafb.docs.types.utilities.payment.PaymentUtility;
import ambroafb.general.Names;
import ambroafb.general.SceneUtils;
import ambroafb.general.interfaces.Dialogable;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.general.interfaces.UserInteractiveStage;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author dkobuladze
 */
public class PaymentUtilityDialog extends UserInteractiveStage implements Dialogable {

    private PaymentUtility paymentUtility, paymentUtilityBackup;
    
    private PaymentUtilityDialogController dialogController;
    
    public PaymentUtilityDialog(EditorPanelable object, Names.EDITOR_BUTTON_TYPE buttonType, Stage owner) {
        super(owner,  Names.LEVEL_FOR_PATH, "doc_payment_utility_dialog_title", "/images/dialog.png");

        if (object == null){
            paymentUtility = new PaymentUtility();
        }
        else {
            paymentUtility = (PaymentUtility) object;
        }
        paymentUtilityBackup = paymentUtility.cloneWithID();
        
        Scene currentScene = SceneUtils.createScene("/ambroafb/docs/types/utilities/payment/dialog/PaymentUtilityDialog.fxml", null);
        dialogController = (PaymentUtilityDialogController) currentScene.getProperties().get("controller");
        dialogController.bindUtility(this.paymentUtility); // this must be before of setNextVisibleAndActionParameters() method, because of sets items in phonelist.
        dialogController.setNextVisibleAndActionParameters(buttonType);
        dialogController.setBackupPayment(this.paymentUtilityBackup);
        this.setScene(currentScene);
        
        onCloseRequestProperty().set((EventHandler<WindowEvent>) (WindowEvent event) -> {
            dialogController.getOkayCancelController().getCancelButton().getOnAction().handle(null);
            if (event != null) event.consume();
        });
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
