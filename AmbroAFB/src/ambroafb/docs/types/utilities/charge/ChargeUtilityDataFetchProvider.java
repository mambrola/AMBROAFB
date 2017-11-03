/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.docs.types.utilities.charge;

import ambroafb.docs.Doc;
import ambroafb.docs.DocMerchandise;
import ambroafb.general.GeneralConfig;
import ambroafb.general.interfaces.DataFetchProvider;
import ambroafb.general.interfaces.FilterModel;
import authclient.db.ConditionBuilder;
import authclient.db.DBClient;
import java.util.List;
import java.util.Optional;
import org.json.JSONObject;

/**
 *
 * @author dkobuladze
 */
public class ChargeUtilityDataFetchProvider extends DataFetchProvider {

    private final String DB_MERCHANDISES_PROCEDURE_NAME = "utility_get_merchandises";
    
    public ChargeUtilityDataFetchProvider(){
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
    public ChargeUtility getOneFromDB(int recId) throws Exception {
        JSONObject params = new ConditionBuilder().where().orGroup().or(DB_ID, "=", recId).or("parent_rec_id", "=", recId).closeGroup().condition().build();
        List<Doc> bouquet = getObjectsListFromDB(Doc.class, DB_VIEW_NAME, params);
        ChargeUtility chargeFromBouqet = new ChargeUtility();
        Doc mainDoc = getDocFromBouquet(bouquet, true);
        Doc vatDoc = getDocFromBouquet(bouquet, false);
        fillChargeUtility(mainDoc, chargeFromBouqet);
        chargeFromBouqet.setVat(vatDoc.getAmount());
        return chargeFromBouqet;
    }
    
    private Doc getDocFromBouquet(List<Doc> bouquet, boolean isParent){
        return bouquet.stream().filter((doc) -> isParent ?  doc.getParentRecId() == -1 : 
                                                            doc.getParentRecId() != -1).
                                findFirst().get();
    }
    
    private void fillChargeUtility(Doc doc, ChargeUtility utility) throws Exception {
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
    
    private DocMerchandise getDocMerchandise(int merchandiseProcessId) throws Exception {
        DBClient dbClient = GeneralConfig.getInstance().getDBClient();
        List<DocMerchandise> merchandises = callProcedure(DocMerchandise.class, DB_MERCHANDISES_PROCEDURE_NAME, dbClient.getLang());
        Optional<DocMerchandise> opt = merchandises.stream().filter((DocMerchandise dm) -> dm.getRecId() == merchandiseProcessId).findFirst();
        return (opt.isPresent()) ? opt.get() : null;
    }
}
