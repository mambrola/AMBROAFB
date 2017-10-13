/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.docs.types.doc_in_order.dialog;

import ambroafb.docs.types.doc_in_order.DocInOrder;
import ambroafb.general.Names;
import ambroafb.general.interfaces.Dialogable;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.general.interfaces.UserInteractiveDialogStage;
import javafx.stage.Stage;

/**
 *
 * @author dkobuladze
 */
public class DocInOrderDialog extends UserInteractiveDialogStage implements Dialogable{

    private DocInOrder docInOrder;
    private final DocInOrder docInOrderBackup;
    
    public DocInOrderDialog(EditorPanelable object, Names.EDITOR_BUTTON_TYPE buttonType, Stage owner, String stageTitleBundleKey) {
        super(owner, "/ambroafb/docs/types/doc_in_order/dialog/DocInOrderDialog.fxml", stageTitleBundleKey);
        
        if (object == null)
            docInOrder = new DocInOrder();
        else
            docInOrder = (DocInOrder) object;
        docInOrderBackup = (DocInOrder) docInOrder.cloneWithID();
        
        dialogController.setSceneData(docInOrder, docInOrderBackup, buttonType);
    }
    
    public DocInOrderDialog(EditorPanelable object, Names.EDITOR_BUTTON_TYPE buttonType, Stage owner) {
        this(object, buttonType, owner, "doc_order_dialog_title");
    }

    @Override
    public EditorPanelable getResult() {
        showAndWait();
        return docInOrder;
    }

    @Override
    public void operationCanceled() {
        docInOrder = null;
    }

    @Override
    public boolean anyComponentChanged() {
        return dialogController.anySceneComponentChanged();
    }
    
}
