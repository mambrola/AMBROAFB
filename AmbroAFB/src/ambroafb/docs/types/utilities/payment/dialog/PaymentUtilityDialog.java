/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.docs.types.utilities.payment.dialog;

import ambroafb.docs.Doc;
import ambroafb.docs.types.utilities.payment.PaymentUtility;
import ambroafb.general.Names;
import ambroafb.general.interfaces.Dialogable;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.general.interfaces.UserInteractiveDialogStage;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import javafx.stage.Stage;

/**
 *
 * @author dkobuladze
 */
public class PaymentUtilityDialog extends UserInteractiveDialogStage implements Dialogable {

    private PaymentUtility paymentUtility;
    private final PaymentUtility paymentUtilityBackup;
    
    private List<Doc> docs = new ArrayList<>();
    
    public PaymentUtilityDialog(EditorPanelable object, Names.EDITOR_BUTTON_TYPE buttonType, Stage owner) {
        super(owner, buttonType, "/ambroafb/docs/types/utilities/payment/dialog/PaymentUtilityDialog.fxml");

        if (object == null)
            paymentUtility = new PaymentUtility();
        else 
            paymentUtility = (PaymentUtility) object;
        paymentUtilityBackup = paymentUtility.cloneWithID();
        
        dialogController.setSceneData(paymentUtility, paymentUtilityBackup, buttonType);
    }
    
    @Override
    public List<Doc> getResult() {
        showAndWait();
        return docs;
    }

    @Override
    public void operationCanceled(){
        docs.clear();
    }

    @Override
    protected EditorPanelable getSceneObject() {
        return paymentUtility;
    }

    @Override
    protected Consumer<Void> getDeleteSuccessAction() {
        return (Void) -> {
                        docs.clear();
                        docs.add(paymentUtility.convertToDoc());
                    };
    }
    

    @Override
    protected Consumer<Object> getEditSuccessAction() {
        return getAddSuccessAction();
    }
    
    
    @Override
    protected Consumer<Object> getAddSuccessAction() {
        return (obj) -> {
                    docs.clear();
                    docs.addAll((List<Doc>)obj);
                };
    }

    
    
}
