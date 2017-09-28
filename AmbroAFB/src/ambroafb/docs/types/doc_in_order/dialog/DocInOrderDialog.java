/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.docs.types.doc_in_order.dialog;

import ambroafb.docs.types.doc_in_order.DocInOrder;
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
public class DocInOrderDialog extends UserInteractiveStage implements Dialogable{

    private DocInOrder docInOrder, docInOrderBackup;
    
    private DocInOrderDialogController dialogController;
    
    public DocInOrderDialog(EditorPanelable object, Names.EDITOR_BUTTON_TYPE buttonType, Stage owner, String stageTitleBundleKey) {
        super(owner, Names.LEVEL_FOR_PATH, stageTitleBundleKey, "/images/dialog.png");
        
        if (object == null){
            docInOrder = new DocInOrder();
        }
        else {
            docInOrder = (DocInOrder) object;
        }
        docInOrderBackup = (DocInOrder) docInOrder.cloneWithID();
        
        Scene currentScene = SceneUtils.createScene("/ambroafb/docs/types/doc_in_order/dialog/DocInOrderDialog.fxml", null);
        dialogController = (DocInOrderDialogController) currentScene.getProperties().get("controller");
        dialogController.bindMonthly(this.docInOrder); // this must be before of setNextVisibleAndActionParameters() method, because of sets items in phonelist.
        dialogController.setNextVisibleAndActionParameters(buttonType);
        dialogController.setBackupCharge(this.docInOrderBackup);
        this.setScene(currentScene);
        
        onCloseRequestProperty().set((EventHandler<WindowEvent>) (WindowEvent event) -> {
            dialogController.getOkayCancelController().getCancelButton().getOnAction().handle(null);
            if (event != null) event.consume();
        });
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
    
}
