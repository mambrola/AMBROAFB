/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.docs.types.monthly;

import ambroafb.docs.Doc;
import ambroafb.docs.types.DocManager;
import ambroafb.docs.types.custom.dialog.CustomDialog;
import ambroafb.docs.types.doc_in_order.DocInOrder;
import ambroafb.docs.types.doc_in_order.dialog.DocInOrderDialog;
import ambroafb.general.DBUtils;
import ambroafb.general.Names;
import ambroafb.general.interfaces.Dialogable;
import ambroafb.general.interfaces.EditorPanelable;
import authclient.db.ConditionBuilder;
import java.util.ArrayList;
import java.util.Optional;
import javafx.stage.Stage;
import org.json.JSONObject;

/**
 *
 * @author dkobuladze
 */
public class MonthlyManager implements DocManager {
    
    private final String DB_VIEW_NAME = "docs_whole";
    private final String DB_DELETE_PROCEDURE_NAME = "doc_delete";

    @Override
    public EditorPanelable getOneFromDB(int id) {
        JSONObject params = new ConditionBuilder().where().orGroup().or("rec_id", "=", id).or("parent_rec_id", "=", id).closeGroup().condition().build();
        ArrayList<Doc> docsFromDB = DBUtils.getObjectsListFromDB(Doc.class, DB_VIEW_NAME, params);
        DocInOrder docInOrder = new DocInOrder();
        docInOrder.setDocs(docsFromDB);
        if (!docsFromDB.isEmpty()){
            docInOrder.setDocDate(docsFromDB.get(0).getDocDate()); // DocDate will be the same for all doc in docsFromDB.
        }
        return docInOrder;
    }
    

    @Override
    public ArrayList<Doc> saveOneToDB(EditorPanelable newDocComponent) {
        ArrayList<Doc> result = new ArrayList<>();
        DocInOrder newComponent = (DocInOrder) newDocComponent;
        if (newComponent.getDocs().isEmpty()){
            result = DBUtils.saveMonthlyAccrual(newComponent.docDateProperty().get());
        }
        else {
            Doc parentDoc = newComponent.getDocs().stream().filter((doc) -> doc.isParentDoc()).findFirst().get();
            Doc newDocFromDB = DBUtils.saveCustomDoc(parentDoc);
            result.add(newDocFromDB);
        }
        return result;
    }

    @Override
    public boolean deleteOneFromDB(int id) {
        return DBUtils.deleteObjectFromDB(DB_DELETE_PROCEDURE_NAME, id);
    }

    @Override
    public void undo() {

    }

    @Override
    public Dialogable getDocDialogFor(Stage owner, Names.EDITOR_BUTTON_TYPE type, EditorPanelable object) {
        Dialogable dialog = new DocInOrderDialog(object, type, owner);
        String StageTitleBundleKey = "doc_monthly_accrual";
        if (object == null){
            dialog = new DocInOrderDialog(object, type, owner, StageTitleBundleKey);
        }
        else if (type.equals(Names.EDITOR_BUTTON_TYPE.ADD_BY_SAMPLE)){
            Optional<Doc> optDoc = ((DocInOrder)object).getDocs().stream().filter((doc) -> doc.isParentDoc()).findFirst();
            dialog = (optDoc.isPresent()) ? new CustomDialog(owner, type, optDoc.get()) : new DocInOrderDialog(object, type, owner, StageTitleBundleKey);
        }
        return dialog;
    }
    
}
