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
import ambroafb.general.Names;
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
    public Dialogable getDialogFor(Stage owner, Names.EDITOR_BUTTON_TYPE type, EditorPanelable object) {
        UserInteractiveDialogStage dialog = new DocInOrderDialog(object, type, owner);
        String StageTitleBundleKey = "doc_monthly_accrual";
        if (object == null){
            dialog = new DocInOrderDialog(object, type, owner, StageTitleBundleKey);
        }
        else if (type.equals(Names.EDITOR_BUTTON_TYPE.ADD_BY_SAMPLE)){
            Optional<Doc> optDoc = ((DocInOrder)object).getDocs().stream().filter((doc) -> doc.isParentDoc()).findFirst();
            dialog = (optDoc.isPresent()) ? new CustomDialog(owner, type, optDoc.get()) : new DocInOrderDialog(object, type, owner, StageTitleBundleKey);
        }
        dialog.setDataChangeProvider(dataChangeProvider);
        dialog.setFrameFeatures(type, ""); // ???
        return (Dialogable)dialog;
    }

    @Override
    public Filterable getFilterFor(Stage owner) {
        return null;
    }
    
}
