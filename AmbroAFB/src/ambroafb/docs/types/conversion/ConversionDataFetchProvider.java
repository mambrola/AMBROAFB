/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.docs.types.conversion;

import ambroafb.accounts.Account;
import ambroafb.docs.Doc;
import ambroafb.general.interfaces.DataFetchProvider;
import ambroafb.general.interfaces.FilterModel;
import authclient.db.ConditionBuilder;
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
    public List<Conversion> getFilteredBy(JSONObject params) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Conversion> getFilteredBy(FilterModel model) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Conversion getOneFromDB(int recId) throws Exception {
        JSONObject params = new ConditionBuilder().where().orGroup().or(DB_ID, "=", recId).or("parent_rec_id", "=", recId).closeGroup().condition().build();
        List<Doc> bouquet = getObjectsListFromDBTable(Doc.class, DB_VIEW_NAME, params);
        Conversion conversion = makeConversionFrom(bouquet);
        return conversion;
    }
    
    private Conversion makeConversionFrom(List<Doc> docs){
        Conversion result = new Conversion();
        Doc firstDoc = docs.get(0);
        Doc secondDoc = docs.get(1);
        result.setRecId(firstDoc.getRecId());
        result.setDocDate(firstDoc.getDocDate());
        result.setDocInDocDate(firstDoc.getDocInDocDate());
        result.descripProperty().set(firstDoc.getDescrip());
       
        result.setSellCurrency(firstDoc.getIso());
        result.setSellAmount(firstDoc.getAmount());
        Account debitAcc = new Account();
        debitAcc.setRecId(firstDoc.getDebitId());
        debitAcc.setAccount(firstDoc.getDebitAccount());
        debitAcc.setDescrip(firstDoc.getDebitDescrip());
        result.setSellAccount(debitAcc);
        
        result.setBuyingCurrency(secondDoc.getIso());
        result.setBuyingAmount(secondDoc.getAmount());
        Account creditAcc = new Account();
        creditAcc.setRecId(secondDoc.getRecId());
        creditAcc.setAccount(secondDoc.getCreditAccount());
        creditAcc.setDescrip(secondDoc.getCreditDescrip());
        result.setBuyingAccount(creditAcc);
        return result;
    }
    
}
