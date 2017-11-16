/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.docs;

import ambroafb.docs.filter.DocFilterModel;
import ambroafb.general.interfaces.DataFetchProvider;
import ambroafb.general.interfaces.FilterModel;
import authclient.db.ConditionBuilder;
import authclient.db.WhereBuilder;
import java.util.List;
import org.json.JSONObject;

/**
 *
 * @author dkobuladze
 */
public class DocDataFetchProvider extends DataFetchProvider {

    private final String DB_DOC_CODES_VIEW_NAME = "doc_codes";
    private final String DB_DOC_MERCHANDISE_PROCEDURE = "utility_get_merchandises";
    
    public DocDataFetchProvider(){
        DB_VIEW_NAME = "docs_whole";
    }
    
    @Override
    public List<Doc> getFilteredBy(JSONObject params) throws Exception {
        return getObjectsListFromDBTable(Doc.class, DB_VIEW_NAME, params);
    }

    @Override
    public List<Doc> getFilteredBy(FilterModel model) throws Exception {
        DocFilterModel docFilterModel = (DocFilterModel) model;
        WhereBuilder whereBuilder = new ConditionBuilder().where().
                        and("doc_date", ">=", docFilterModel.getDocDateForDB(true)).
                        and("doc_date", "<=", docFilterModel.getDocDateForDB(false)).
                        and("doc_in_doc_date", ">=", docFilterModel.getDocInDocDateForDB(true)).
                        and("doc_in_doc_date", "<=", docFilterModel.getDocInDocDateForDB(false));
        if (docFilterModel.isSelectedConcreteAccount()){
            whereBuilder.andGroup().or("debit_id", "=", docFilterModel.getSelectedAccountId()).
                                    or("credit_id", "=", docFilterModel.getSelectedAccountId()).closeGroup();
        }
        if (docFilterModel.isSelectedConcreteCurrency()){
            whereBuilder.and("iso", "=", docFilterModel.getSelectedCurrencyIso());
        }
        if (docFilterModel.isSelectedConcreteDocCode()){
            whereBuilder.and("doc_code", "=", docFilterModel.getSelectedDocCode());
        }
        JSONObject params = whereBuilder.condition().build();
        return getObjectsListFromDBTable(Doc.class, DB_VIEW_NAME, params);
    }

    @Override
    public Doc getOneFromDB(int recId) throws Exception {
        JSONObject params = new ConditionBuilder().where().and(DB_ID, "=", recId).condition().build();
        return getObjectFromDB(Doc.class, DB_VIEW_NAME, params);
    }
    
    
    public List<DocCode> getDocCodes(JSONObject params) throws Exception {
        return getObjectsListFromDBTable(DocCode.class, DB_DOC_CODES_VIEW_NAME, params);
    }

    public List<DocMerchandise> getDocMerchandises() throws Exception {
        return getObjectsListFromDBProcedure(DocMerchandise.class, DB_DOC_MERCHANDISE_PROCEDURE);
    }
}
