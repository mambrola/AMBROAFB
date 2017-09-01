/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.docs.types.utilities.charge;

import ambroafb.docs.Doc;
import ambroafb.docs.types.DocManager;
import ambroafb.docs.types.utilities.payment.PaymentUtility;
import ambroafb.general.DBUtils;
import ambroafb.general.Names;
import ambroafb.general.interfaces.Dialogable;
import ambroafb.general.interfaces.EditorPanelable;
import authclient.db.ConditionBuilder;
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
        JSONObject params = new ConditionBuilder().where().and("rec_id", "=", id).condition().build();
        return DBUtils.getObjectFromDB(ChargeUtility.class, DB_VIEW_NAME, params);
    }

    @Override
    public Doc saveOneToDB(EditorPanelable newDocComponent) {
        return null;
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
        return null;
    }

    @Override
    public String toString() {
        return "ChargeUtilityManager";
    }
    
    
}
