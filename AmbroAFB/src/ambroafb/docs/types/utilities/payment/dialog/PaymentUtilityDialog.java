/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.docs.types.utilities.payment.dialog;

import ambroafb.docs.types.utilities.payment.PaymentUtility;
import ambroafb.general.Names;
import ambroafb.general.SceneUtils;
import ambroafb.general.interfaces.DialogController;
import ambroafb.general.interfaces.Dialogable;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.general.interfaces.UserInteractiveDialogStage;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author dkobuladze
 */
public class PaymentUtilityDialog extends UserInteractiveDialogStage implements Dialogable {

    private PaymentUtility paymentUtility, paymentUtilityBackup;
    
    private DialogController dialogController;
    
    public PaymentUtilityDialog(EditorPanelable object, Names.EDITOR_BUTTON_TYPE buttonType, Stage owner) {
        super(owner, "doc_payment_utility_dialog_title");

        if (object == null){
            paymentUtility = new PaymentUtility();
        }
        else {
            paymentUtility = (PaymentUtility) object;
        }
        paymentUtilityBackup = paymentUtility.cloneWithID();
        
        Scene currentScene = SceneUtils.createScene("/ambroafb/docs/types/utilities/payment/dialog/PaymentUtilityDialog.fxml", null);
        dialogController = (PaymentUtilityDialogController) currentScene.getProperties().get("controller");
        dialogController.setSceneData(paymentUtility, paymentUtilityBackup, buttonType);
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

    @Override
    public boolean anyComponentChanged() {
        return dialogController.anySceneComponentChanged();
    }
}
