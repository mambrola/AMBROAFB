/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.docs.types.doc_in_order.dialog;

import ambroafb.docs.Doc;
import ambroafb.docs.types.doc_in_order.DocInOrder;
import ambroafb.general.editor_panel.EditorPanel;
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
public class DocInOrderDialog extends UserInteractiveDialogStage implements Dialogable{

    private DocInOrder docInOrder;
    private final DocInOrder docInOrderBackup;
    
    private List<Doc> docs = new ArrayList<>();
    
    public DocInOrderDialog(EditorPanelable object, EditorPanel.EDITOR_BUTTON_TYPE buttonType, Stage owner, String stageTitleBundleKey) {
        super(owner, "/ambroafb/docs/types/doc_in_order/dialog/DocInOrderDialog.fxml", stageTitleBundleKey);
        
        if (object == null)
            docInOrder = new DocInOrder();
        else
            docInOrder = (DocInOrder) object;
        docInOrderBackup = (DocInOrder) docInOrder.cloneWithID();
        
        dialogController.setSceneData(docInOrder, docInOrderBackup, buttonType);
    }
    
    public DocInOrderDialog(EditorPanelable object, EditorPanel.EDITOR_BUTTON_TYPE buttonType, Stage owner) {
        this(object, buttonType, owner, "doc_order_dialog_title");
    }

    @Override
    public List<Doc> getResult() {
        showAndWait();
        return docs;
    }

    @Override
    public void operationCanceled() {
        docs.clear();
    }

    @Override
    protected EditorPanelable getSceneObject() {
        return docInOrder;
    }

    @Override
    protected Consumer<Void> getDeleteSuccessAction() {
        return (Void) -> {
                    docs.clear();
                    docs.addAll(docInOrder.getDocs());
                };
    }

    @Override
    protected Consumer<Object> getEditSuccessAction() {
        return (obj) -> {
                        docs.clear();
                        docs.addAll((List<Doc>)obj);
                    };
    }

    
    @Override
    protected Consumer<Object> getAddSuccessAction() {
        return getEditSuccessAction();
    }

    
    
}
