/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.docs.types.conversion;

import ambroafb.docs.Doc;
import ambroafb.general.DBUtils;
import ambroafb.general.interfaces.DataFetchProvider;
import ambroafb.general.interfaces.FilterModel;
import authclient.AuthServerException;
import authclient.db.ConditionBuilder;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

/**
 *
 * @author dkobuladze
 */
public class ConversionDataFetchProvider extends DataFetchProvider {

    public ConversionDataFetchProvider(){
        DB_VIEW_NAME = "docs_whole";
    }
    
    @Override
    public List<Conversion> getFilteredBy(JSONObject params) throws IOException, AuthServerException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Conversion> getFilteredBy(FilterModel model) throws IOException, AuthServerException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Conversion getOneFromDB(int recId) throws IOException, AuthServerException {
        JSONObject params = new ConditionBuilder().where().orGroup().or(DB_ID, "=", recId).or("parent_rec_id", "=", recId).closeGroup().condition().build();
        ArrayList<Doc> bouquet = DBUtils.getObjectsListFromDB(Doc.class, DB_VIEW_NAME, params);
        Conversion conversion = makeConversionFrom(bouquet);
        return conversion;
    }
    
    private Conversion makeConversionFrom(ArrayList<Doc> docs){
        Conversion result = new Conversion();
        Doc firstDoc = docs.get(0);
        Doc secondDoc = docs.get(1);
        result.setRecId(firstDoc.getRecId());
        result.setDocDate(firstDoc.getDocDate());
        result.setDocInDocDate(firstDoc.getDocInDocDate());
        result.descripProperty().set(firstDoc.getDescrip());
       
        result.setSellCurrency(firstDoc.getIso());
        result.setSellAccount(firstDoc.debitProperty().get());
        result.setSellAmount(firstDoc.getAmount());
        
        result.setBuyingCurrency(secondDoc.getIso());
        result.setBuyingAccount(secondDoc.creditProperty().get());
        result.setBuyingAmount(secondDoc.getAmount());
        return result;
    }
    
}
