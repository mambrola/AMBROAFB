/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.docs.types.utilities.payment;

import ambroafb.docs.Doc;
import ambroafb.general.GeneralConfig;
import ambroafb.general.Utils;
import ambroafb.general.interfaces.DataChangeProvider;
import ambroafb.general.interfaces.EditorPanelable;
import authclient.AuthServerException;
import authclient.db.DBClient;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;

/**
 *
 * @author dkobuladze
 */
public class PaymentUtilityDataChangeProvider extends DataChangeProvider {

    private final String DELETE_PROCEDURE = "doc_delete";
    private final String SAVE_UPDATE_PROCEDURE = "doc_utilities_insert_update";
    
    @Override
    public void deleteOneFromDB(int recId) throws Exception {
        callProcedure(DELETE_PROCEDURE, recId);
    }

    @Override
    public List<Doc> editOneToDB(EditorPanelable object) throws Exception {
        return saveOneToDB(object);
    }

    @Override
    public List<Doc> saveOneToDB(EditorPanelable object) throws Exception {
        ArrayList<Doc> docs = new ArrayList<>();
        docs.add(savePaymentUtility((PaymentUtility) object));
        return docs;
    }
    
    private Doc savePaymentUtility(PaymentUtility paymentUtility) throws IOException, AuthServerException, JSONException {
        DBClient dbClient = GeneralConfig.getInstance().getDBClient();
        Integer id = (paymentUtility.getRecId() == 0) ? null : paymentUtility.getRecId();
        JSONArray data = dbClient.callProcedureAndGetAsJson(SAVE_UPDATE_PROCEDURE, dbClient.getLang(), id, paymentUtility.getDocCode(),
                                                        paymentUtility.utilityProperty().get().getMerchandise(),
                                                        paymentUtility.docDateProperty().get(),
                                                        paymentUtility.docInDocDateProperty().get(),
                                                        paymentUtility.getAmount(),
                                                        paymentUtility.utilityProperty().get().getVatRate(),
                                                        -1);
        return Utils.getClassFromJSON(Doc.class, data.getJSONObject(0));
    }
}
