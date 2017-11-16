/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.docs.types.doc_in_order;

import ambroafb.docs.Doc;
import ambroafb.general.DBUtils;
import ambroafb.general.GeneralConfig;
import ambroafb.general.Utils;
import ambroafb.general.interfaces.DataChangeProvider;
import ambroafb.general.interfaces.EditorPanelable;
import authclient.AuthServerException;
import authclient.db.DBClient;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author dkobuladze
 */
public class DocInOrderDataChangeProvider extends DataChangeProvider {

    private final String DELETE_PROCEDURE = "doc_delete";
    
    @Override
    public List<Doc> deleteOneFromDB(int recId) throws Exception {
        return callProcedure(Doc.class, DELETE_PROCEDURE, recId);
    }

    @Override
    public List<Doc> editOneToDB(EditorPanelable object) throws Exception {
        return saveOneToDB(object);
    }

    
    @Override
    public List<Doc> saveOneToDB(EditorPanelable object) throws Exception {
        ArrayList<Doc> result = new ArrayList<>();
        DocInOrder newComponent = (DocInOrder) object;
        if (newComponent.getDocs().isEmpty()){
            result = saveMonthlyAccrual(newComponent.docDateProperty().get());
        }
        else { //  შეიძლება აღარ გახდეს საჭირო. თუ Custom-ია მაშინ დიალოგიც custom-ის გაკეთდებოდა manager-ში.
            Doc parentDoc = newComponent.getDocs().stream().filter((doc) -> doc.isParentDoc()).findFirst().get();
            Doc newDocFromDB = DBUtils.saveCustomDoc(parentDoc);
            result.add(newDocFromDB);
        }
        return result;
    }
    
    
    public ArrayList<Doc> saveMonthlyAccrual(LocalDate date) throws IOException, AuthServerException {
        ArrayList<Doc> docs = new ArrayList();
        DBClient dbClient = GeneralConfig.getInstance().getDBClient();
        String procedureName = "doc_kfz_soft_invoices_monthly_accrual";
        JSONArray data = dbClient.callProcedureAndGetAsJson(procedureName, dbClient.getLang(), date, -1);

        System.out.println(procedureName + " data from DB: " + data.toString());
        if (data.length() != 0){
            try {
                if (data.get(0) instanceof JSONObject){
                    System.out.println("--------- json object -------------");
                    docs = Utils.getListFromJSONArray(Doc.class, data);
                }
                else {
                    System.out.println("--------- json array -------------");
                    int updatedDocsIndex = data.length() % 3;
                    int deletedDocsIndex = updatedDocsIndex + 1;
                    int insertedDocsIndex = updatedDocsIndex + 2;

                    ArrayList<Doc> insertedDocs = new ArrayList();
                    if (data.length() == 4){
                        insertedDocs = Utils.getListFromJSONArray(Doc.class, data.getJSONArray(0));
                    }
                    ArrayList<Doc> updatedDocs = Utils.getListFromJSONArray(Doc.class, data.getJSONArray(updatedDocsIndex));
                    ArrayList<Doc> deletedDocsByRate = Utils.getListFromJSONArray(Doc.class, data.getJSONArray(deletedDocsIndex));
                    ArrayList<Doc> insertedDocsbyRate = Utils.getListFromJSONArray(Doc.class, data.getJSONArray(insertedDocsIndex));

                    appendArrayLists(docs, insertedDocs);
                    appendArrayLists(docs, updatedDocs);
                    appendArrayLists(docs, deletedDocsByRate);
                    appendArrayLists(docs, insertedDocsbyRate);
                }
            } catch (JSONException ex) {
                Logger.getLogger(DocInOrderDataChangeProvider.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return docs;
    }
    
    /**
     * The method appends docs ArrayList and adds null doc into dest ArrayList.
     * @param dest Destination arrayList, where must be src elements.
     * @param src Elements source ArrayList.
     */
    private static void appendArrayLists(ArrayList<Doc> dest, ArrayList<Doc> src){
        src.stream().forEach((doc) -> dest.add(doc));
    }
}
