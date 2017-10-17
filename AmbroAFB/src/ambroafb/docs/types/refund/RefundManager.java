/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.docs.types.refund;

import ambroafb.docs.Doc;
import ambroafb.docs.types.DocManager;
import ambroafb.docs.types.refund.dialog.RefundDialog;
import ambroafb.general.DBUtils;
import ambroafb.general.Names;
import ambroafb.general.interfaces.Dialogable;
import ambroafb.general.interfaces.EditorPanelable;
import authclient.db.ConditionBuilder;
import java.util.ArrayList;
import javafx.stage.Stage;
import org.json.JSONObject;

/**
 *
 * @author dkobuladze
 */
public class RefundManager implements DocManager {

    private final String DB_VIEW_NAME = "docs_whole";
    private final String DB_DELETE_PROCEDURE_NAME = "doc_delete";
    
    @Override
    public EditorPanelable getOneFromDB(int id) {
        JSONObject params = new ConditionBuilder().where().and("rec_id", "=", id).condition().build();
        return DBUtils.getObjectFromDB(Doc.class, DB_VIEW_NAME, params);
    }

    @Override
    public ArrayList<Doc> saveOneToDB(EditorPanelable newDocComponent) {
        Doc newDoc = (Doc) newDocComponent;
        Doc newFromDB = DBUtils.saveCustomDoc(newDoc);
        ArrayList<Doc> docsFromDB = new ArrayList<>();
        if (newFromDB != null){
            docsFromDB.add(newFromDB);
        }
        return docsFromDB;
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
        RefundDialog dialog = new RefundDialog(object, type, owner);
        return dialog;
    }
    
}
