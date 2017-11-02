/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.invoices;

import ambroafb.general.GeneralConfig;
import ambroafb.general.Utils;
import ambroafb.general.interfaces.DataChangeProvider;
import ambroafb.general.interfaces.EditorPanelable;
import authclient.Response;
import authclient.db.DBClient;
import org.json.JSONObject;

/**
 *
 * @author dkobuladze
 */
public class InvoiceDataChangeProvider extends DataChangeProvider {

    private final String DELETE_PROCEDURE = "invoice_delete";
    
    @Override
    public void deleteOneFromDB(int recId) throws Exception {
        callProcedure(DELETE_PROCEDURE, recId);
    }

    @Override
    public Invoice editOneToDB(EditorPanelable object) throws Exception {
        return saveOneToDB(object);
    }

    @Override
    public Invoice saveOneToDB(EditorPanelable object) throws Exception {
        JSONObject targetJson = Utils.getJSONFromClass((Invoice)object);
        DBClient dbClient = GeneralConfig.getInstance().getDBClient();
        Response r = dbClient.post("invoices/fromAfb?lang=" + dbClient.getLang(), targetJson.toString());
        return Utils.getClassFromJSON(Invoice.class, new JSONObject(r.getDataAsString()));
    }
    
}
