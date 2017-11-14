/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.docs.types.monthly;

import ambroafb.docs.Doc;
import ambroafb.docs.types.custom.dialog.CustomDialog;
import ambroafb.docs.types.doc_in_order.DocInOrder;
import ambroafb.docs.types.doc_in_order.DocInOrderDataChangeProvider;
import ambroafb.docs.types.doc_in_order.DocInOrderDataFetchProvider;
import ambroafb.docs.types.doc_in_order.dialog.DocInOrderDialog;
import ambroafb.general.editor_panel.EditorPanel;
import ambroafb.general.interfaces.Dialogable;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.general.interfaces.EditorPanelableManager;
import ambroafb.general.interfaces.Filterable;
import ambroafb.general.interfaces.UserInteractiveDialogStage;
import java.util.Optional;
import javafx.stage.Stage;

/**
 *
 * @author dkobuladze
 */
public class MonthlyManager extends EditorPanelableManager {
    
    public MonthlyManager(){
        dataFetchProvider = new DocInOrderDataFetchProvider();
        dataChangeProvider = new DocInOrderDataChangeProvider();
    }
    
    @Override
    public Dialogable getDialogFor(Stage owner, EditorPanel.EDITOR_BUTTON_TYPE type, EditorPanelable object) {
        UserInteractiveDialogStage dialog = new DocInOrderDialog(object, type, owner);
        String stageTitleBundleKey = "doc_monthly_accrual";
        if (object == null){
            stageTitleBundleKey = "doc_order_dialog_title";
            dialog = new DocInOrderDialog(object, type, owner);
        }
        else if (type.equals(EditorPanel.EDITOR_BUTTON_TYPE.ADD_BY_SAMPLE)){
            Optional<Doc> optDoc = ((DocInOrder)object).getDocs().stream().filter((doc) -> doc.isParentDoc()).findFirst();
            if (optDoc.isPresent()){
                stageTitleBundleKey = "doc_custom_dialog_title";
                dialog = new CustomDialog(owner, type, optDoc.get());
            }
            else {
                stageTitleBundleKey = "doc_order_dialog_title";
                dialog = new DocInOrderDialog(object, type, owner);
            }
        }
        dialog.setDataChangeProvider(dataChangeProvider);
        dialog.setFrameFeatures(type, stageTitleBundleKey);
        return (Dialogable)dialog;
    }

    @Override
    public Filterable getFilterFor(Stage owner) {
        return null;
    }
    
}
