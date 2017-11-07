/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.docs.types.utilities.payment;

import ambroafb.docs.types.utilities.payment.dialog.PaymentUtilityDialog;
import ambroafb.general.editor_panel.EditorPanel;
import ambroafb.general.interfaces.Dialogable;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.general.interfaces.EditorPanelableManager;
import ambroafb.general.interfaces.Filterable;
import javafx.stage.Stage;

/**
 *
 * @author dkobuladze
 */
public class PaymentUtilityManager extends EditorPanelableManager {
    
//    private final String DB_VIEW_NAME = "docs";
//    private final String DB_MERCHANDISES_PROCEDURE_NAME = "utility_get_merchandises";
//    private final String DB_DELETE_PROCEDURE_NAME = "doc_delete";
//    private PaymentUtility transferUtility, transferUtilityBackup;

    public PaymentUtilityManager(){
        dataFetchProvider = new PaymentUtilityDataFetchProvider();
        dataChangeProvider = new PaymentUtilityDataChangeProvider();
    }
    
//    @Override
//    public EditorPanelable getOneFromDB(int id) {
//        JSONObject params = new ConditionBuilder().where().and("rec_id", "=", id).condition().build();
//        PaymentUtility paymentUtility = DBUtils.getObjectFromDB(PaymentUtility.class, DB_VIEW_NAME, params);
//        paymentUtility.utilityProperty().set(getDocMerchandise(paymentUtility.getProcessId()));
//        return paymentUtility;
//    }
//    
//    private DocMerchandise getDocMerchandise(int merchandiseProcessId) {
//        ArrayList<DocMerchandise> merchandises = DBUtils.getObjectsListFromDBProcedure(DocMerchandise.class, DB_MERCHANDISES_PROCEDURE_NAME);
//        Optional<DocMerchandise> opt = merchandises.stream().filter((DocMerchandise dm) -> dm.getRecId() == merchandiseProcessId).findFirst();
//        return (opt.isPresent()) ? opt.get() : null;
//    }
//
//    @Override
//    public ArrayList<Doc> saveOneToDB(EditorPanelable newDocComponent) {
//        ArrayList<Doc> docs = new ArrayList<>();
//        docs.add(DBUtils.savePaymentUtility((PaymentUtility) newDocComponent));
//        return docs;
//    }
//
//    @Override
//    public boolean deleteOneFromDB(int id) {
//        return DBUtils.deleteObjectFromDB(DB_DELETE_PROCEDURE_NAME, id);
//    }
//
//    @Override
//    public void undo() {
//        
//    }
//
//    @Override
//    public Dialogable getDocDialogFor(Stage owner, EditorPanel.EDITOR_BUTTON_TYPE type, EditorPanelable object) {
//        PaymentUtilityDialog tranferDialog = new PaymentUtilityDialog(object, type, owner);
//        
//        return tranferDialog;
//    }
//
//    @Override
//    public String toString() {
//        return "TransferUtilityManager";
//    }

    @Override
    public Dialogable getDialogFor(Stage owner, EditorPanel.EDITOR_BUTTON_TYPE type, EditorPanelable object) {
        PaymentUtilityDialog dialog = new PaymentUtilityDialog(object, type, owner);
        dialog.setDataChangeProvider(dataChangeProvider);
        dialog.setFrameFeatures(type, "doc_payment_utility_dialog_title");
        return dialog;
    }

    @Override
    public Filterable getFilterFor(Stage owner) {
        return null;
    }
    
    
}
