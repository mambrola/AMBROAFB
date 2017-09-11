/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.docs.types.utilities.charge;

import ambroafb.docs.Doc;
import ambroafb.docs.DocMerchandise;
import ambroafb.docs.types.DocManager;
import ambroafb.docs.types.utilities.charge.dialog.ChargeUtilityDialog;
import ambroafb.docs.types.utilities.payment.PaymentUtility;
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
public class ChargeUtilityManager implements DocManager {
    
    private final String DB_VIEW_NAME = "docs_whole";
    private final String DB_MERCHANDISES_PROCEDURE_NAME = "utility_get_merchandises";
    private final String DB_DELETE_PROCEDURE_NAME = "general_delete";
    private PaymentUtility chargeUtility, chargeUtilityBackup;
    
    @Override
    public EditorPanelable getOneFromDB(int id) {
        JSONObject params = new ConditionBuilder().where().orGroup().or("rec_id", "=", id).or("parent_rec_id", "=", id).closeGroup().condition().build();
        ArrayList<Doc> bouquet = DBUtils.getObjectsListFromDB(Doc.class, DB_VIEW_NAME, params);
        ChargeUtility chargeFromBouqet = new ChargeUtility();
        Doc mainDoc = getDocFromBouquet(bouquet, true);
        Doc vatDoc = getDocFromBouquet(bouquet, false);
        fillChargeUtility(mainDoc, chargeFromBouqet);
        chargeFromBouqet.setVat(vatDoc.getAmount());
        return chargeFromBouqet;
    }
    
    private Doc getDocFromBouquet(ArrayList<Doc> bouquet, boolean isParent){
        return bouquet.stream().filter((doc) -> isParent ?  doc.getParentRecId() == -1 : 
                                                            doc.getParentRecId() != -1).
                                findFirst().get();
    }
    
    private void fillChargeUtility(Doc doc, ChargeUtility utility){
        utility.merchandiseProperty().set(getDocMerchandise(doc.getProcessId()));
        utility.setRecId(doc.getRecId());
        utility.setParentRecId(doc.getParentRecId());
        utility.setProcessId(doc.getProcessId());
        utility.setDocDate(doc.getDocDate());
        utility.setDocInDocDate(doc.getDocInDocDate());
        utility.setIso(doc.getIso());
        utility.setDebitId(doc.debitProperty().get().getRecId());
        utility.setCreditId(doc.creditProperty().get().getRecId());
        utility.setAmount(doc.getAmount());
        utility.setDocCode(doc.getDocCode());
        utility.setDescrip(doc.getDescrip());
        utility.setDocType(doc.getDocType());
        utility.setOwnerId(doc.getOwnerId());
    }
    
    private DocMerchandise getDocMerchandise(int merchandiseProcessId) {
        ArrayList<DocMerchandise> merchandises = DBUtils.getObjectsListFromDBProcedure(DocMerchandise.class, DB_MERCHANDISES_PROCEDURE_NAME);
        Optional<DocMerchandise> opt = merchandises.stream().filter((DocMerchandise dm) -> dm.getRecId() == merchandiseProcessId).findFirst();
        return (opt.isPresent()) ? opt.get() : null;
    }

    @Override
    public ArrayList<Doc> saveOneToDB(EditorPanelable newDocComponent) {
        return DBUtils.saveChargeUtility((ChargeUtility) newDocComponent);
    }

    @Override
    public boolean deleteOneFromDB(int id) {
        return false;
//        return DBUtils.deleteObjectFromDB(DB_DELETE_PROCEDURE_NAME, id);
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
