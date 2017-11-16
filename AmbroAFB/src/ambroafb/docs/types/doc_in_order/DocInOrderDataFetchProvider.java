/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.docs.types.doc_in_order;

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
public class DocInOrderDataFetchProvider extends DataFetchProvider {

    public DocInOrderDataFetchProvider(){
        DB_VIEW_NAME = "docs_whole";
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
    public DocInOrder getOneFromDB(int recId) throws Exception {
        JSONObject params = new ConditionBuilder().where().orGroup().or(DB_ID, "=", recId).or("parent_rec_id", "=", recId).closeGroup().condition().build();
        List<Doc> docsFromDB = getObjectsListFromDBTable(Doc.class, DB_VIEW_NAME, params);
        DocInOrder docInOrder = new DocInOrder();
        docInOrder.setDocs(docsFromDB);
        if (!docsFromDB.isEmpty()){
            docInOrder.setDocDate(docsFromDB.get(0).getDocDate()); // DocDate will be the same for all doc in docsFromDB.
        }
        return docInOrder;
    }
    
}
