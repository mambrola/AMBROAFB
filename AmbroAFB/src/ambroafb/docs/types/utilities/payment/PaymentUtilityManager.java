/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.docs.types.utilities.payment;

import ambroafb.docs.types.DocComponent;
import ambroafb.docs.types.DocManager;
import ambroafb.docs.types.utilities.payment.dialog.PaymentUtilityDialog;
import ambroafb.general.DBUtils;
import ambroafb.general.Names;
import ambroafb.general.interfaces.DocDialogable;
import ambroafb.general.interfaces.EditorPanelable;
import authclient.db.ConditionBuilder;
import javafx.stage.Stage;
import org.json.JSONObject;

/**
 *
 * @author dkobuladze
 */
public class PaymentUtilityManager implements DocManager {
    
    private final String DB_VIEW_NAME = "docs_whole";
    private PaymentUtility transferUtility, transferUtilityBackup;

    @Override
    public DocComponent getOneFromDB(int id) {
        JSONObject params = new ConditionBuilder().where().and("rec_id", "=", id).condition().build();
        return DBUtils.getObjectFromDB(PaymentUtility.class, DB_VIEW_NAME, params);
    }

    @Override
    public EditorPanelable saveOneToDB(EditorPanelable newDocComponent) {
        DBUtils.savePaymentUtility((PaymentUtility) newDocComponent);
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
    public DocDialogable getDocDialogFor(Stage owner, Names.EDITOR_BUTTON_TYPE type) {
        PaymentUtilityDialog tranferDialog = new PaymentUtilityDialog(transferUtility, type, owner);
        
        return tranferDialog;
    }

    @Override
    public String toString() {
        return "TransferUtilityManager";
    }
    
    
}
