/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.docs.types.utilities.charge;

import ambroafb.docs.Doc;
import ambroafb.docs.types.DocManager;
import ambroafb.docs.types.utilities.charge.dialog.ChargeUtilityDialog;
import ambroafb.docs.types.utilities.payment.PaymentUtility;
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
public class ChargeUtilityManager implements DocManager {
    
    private final String DB_VIEW_NAME = "docs_whole";
    private PaymentUtility chargeUtility, chargeUtilityBackup;
    
    @Override
    public EditorPanelable getOneFromDB(int id) {
        JSONObject params = new ConditionBuilder().where().orGroup().or("rec_id", "=", id).or("parent_rec_id", "=", id).closeGroup().condition().build();
        ArrayList<EditorPanelable> bouquet = DBUtils.getObjectsListFromDB(Doc.class, DB_VIEW_NAME, params);
        
        ChargeUtility cu = new ChargeUtility();
        return cu;
    }

    @Override
    public ArrayList<Doc> saveOneToDB(EditorPanelable newDocComponent) {
        return DBUtils.saveChargeUtility((ChargeUtility) newDocComponent);
    }

    @Override
    public boolean deleteOneFromDB(int id) {
        return false;
    }

    @Override
    public void undo() {

    }

    @Override
    public Dialogable getDocDialogFor(Stage owner, Names.EDITOR_BUTTON_TYPE type, EditorPanelable object) {
        ChargeUtilityDialog dialog = new ChargeUtilityDialog(object, type, owner);
        return dialog;
    }

    @Override
    public String toString() {
        return "ChargeUtilityManager";
    }
    
    
}
