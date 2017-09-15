/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.docs.types.monthly;

import ambroafb.docs.Doc;
import ambroafb.docs.types.DocManager;
import ambroafb.docs.types.monthly.dialog.MonthlyDialog;
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
public class MonthlyManager implements DocManager {
    
    private final String DB_VIEW_NAME = "docs_whole";
    private final String DB_PROCEDURE_NAME = "doc_kfz_soft_invoices_monthly_accrual";
    private final String DB_DELETE_PROCEDURE_NAME = "doc_delete";

    @Override
    public EditorPanelable getOneFromDB(int id) {
        JSONObject params = new ConditionBuilder().where().orGroup().or("rec_id", "=", id).or("parent_rec_id", "=", id).closeGroup().condition().build();
        ArrayList<Doc> docsFromDB = DBUtils.getObjectsListFromDB(Doc.class, DB_VIEW_NAME, params);
        Monthly monthly = new Monthly();
        monthly.setDocs(docsFromDB);
        return monthly;
    }
    

    @Override
    public ArrayList<Doc> saveOneToDB(EditorPanelable newDocComponent) {
        Monthly newComponent = (Monthly) newDocComponent;
        return DBUtils.getObjectsListFromDBProcedure(Doc.class, DB_PROCEDURE_NAME, newComponent.docDateProperty().get(), -1);
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
        MonthlyDialog dialog = new MonthlyDialog(object, type, owner);
        return dialog;
    }
    
}
