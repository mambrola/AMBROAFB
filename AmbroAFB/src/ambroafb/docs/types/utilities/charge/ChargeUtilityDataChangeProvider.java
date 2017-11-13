/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.docs.types.utilities.charge;

import ambroafb.docs.Doc;
import ambroafb.general.GeneralConfig;
import ambroafb.general.Utils;
import ambroafb.general.interfaces.DataChangeProvider;
import ambroafb.general.interfaces.EditorPanelable;
import authclient.db.DBClient;
import java.util.List;
import org.json.JSONArray;

/**
 *
 * @author dkobuladze
 */
public class ChargeUtilityDataChangeProvider extends DataChangeProvider {

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
        DBClient dbClient = GeneralConfig.getInstance().getDBClient();
        ChargeUtility chargeUtility = (ChargeUtility) object;
        Integer id = (chargeUtility.getRecId() == 0) ? null : chargeUtility.getRecId();
        JSONArray data = dbClient.callProcedureAndGetAsJson(SAVE_UPDATE_PROCEDURE, dbClient.getLang(), id, chargeUtility.getDocCode(),
                                                        chargeUtility.merchandiseProperty().get().getMerchandise(),
                                                        chargeUtility.docDateProperty().get(),
                                                        chargeUtility.docInDocDateProperty().get(),
                                                        chargeUtility.getAmount(),
                                                        chargeUtility.getVat(),
                                                        chargeUtility.getOwnerId());
        return Utils.getListFromJSONArray(Doc.class, data);
    }
    
}
