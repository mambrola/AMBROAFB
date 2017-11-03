/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.docs.types.utilities.payment;

import ambroafb.docs.DocMerchandise;
import ambroafb.general.DBUtils;
import ambroafb.general.interfaces.DataFetchProvider;
import ambroafb.general.interfaces.FilterModel;
import authclient.db.ConditionBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.json.JSONObject;

/**
 *
 * @author dkobuladze
 */
public class PaymentUtilityDataFetchProvider extends DataFetchProvider {

    private final String DB_MERCHANDISES_PROCEDURE_NAME = "utility_get_merchandises";
    private final String DB_TABLE_NAME = "docs";
    
    public PaymentUtilityDataFetchProvider(){
    }
    
    @Override
    public <T> List<T> getFilteredBy(JSONObject params) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public <T> List<T> getFilteredBy(FilterModel model) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public PaymentUtility getOneFromDB(int recId) throws Exception {
        JSONObject params = new ConditionBuilder().where().and(DB_ID, "=", recId).condition().build();
        PaymentUtility paymentUtility = getObjectFromDB(PaymentUtility.class, DB_TABLE_NAME, params);
        paymentUtility.utilityProperty().set(getDocMerchandise(paymentUtility.getProcessId()));
        return paymentUtility;
    }
    
    private DocMerchandise getDocMerchandise(int merchandiseProcessId) {
        ArrayList<DocMerchandise> merchandises = DBUtils.getObjectsListFromDBProcedure(DocMerchandise.class, DB_MERCHANDISES_PROCEDURE_NAME);
        Optional<DocMerchandise> opt = merchandises.stream().filter((DocMerchandise dm) -> dm.getRecId() == merchandiseProcessId).findFirst();
        return (opt.isPresent()) ? opt.get() : null;
    }
    
}
