/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general;

import ambroafb.clients.Client;
import ambroafb.docs.Doc;
import ambroafb.docs.types.utilities.charge.ChargeUtility;
import ambroafb.docs.types.utilities.payment.PaymentUtility;
import ambroafb.invoices.Invoice;
import ambroafb.invoices.helper.InvoiceFinaces;
import ambroafb.invoices.helper.PartOfLicense;
import ambroafb.licenses.License;
import ambroafb.licenses.helper.LicenseFinaces;
import ambroafb.params_general.ParamGeneral;
import ambroafb.params_general.helper.ParamGeneralDBResponse;
import authclient.AuthServerException;
import authclient.Response;
import authclient.db.ConditionBuilder;
import authclient.db.DBClient;
import authclient.db.WhereBuilder;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author dato
 */
public class DBUtils {
    
    /**
     * The static function gets a ArrayList of specified class elements from DB.
     * @param <T>
     * @param listElementClass The class of elements which must be in list.
     * @param dbTableOrViewName The table or view name where entries are in DB.
     * @param params The parameter JSON for filter DB select.
     *                  It could be empty JSON, if user wants every column values from DB table or view.
     * @return 
     */
    public static <T> ArrayList<T> getObjectsListFromDB(Class<?> listElementClass, String dbTableOrViewName, JSONObject params){
        try {
            System.out.println(dbTableOrViewName + " params For DB: " + params);
            
            JSONArray data = GeneralConfig.getInstance().getDBClient().select(dbTableOrViewName, params);
            
            System.out.println(dbTableOrViewName + " data from DB: " + data);
            
            return Utils.getListFromJSONArray(listElementClass, data);
        } catch (IOException | AuthServerException ex) {
            Logger.getLogger(DBUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<>();
    }
    
    public static <T> ArrayList<T> getObjectsListFromDBProcedure(Class<?> listElementClass, String procName, JSONObject params){
        try {
            System.out.println(procName + " params For DB: " + params);
            
            DBClient dbClient = GeneralConfig.getInstance().getDBClient();
            JSONArray data = dbClient.callProcedureAndGetAsJson(procName, dbClient.getLang(), params);
            
            System.out.println(procName + " data from DB: " + data);
            
            return Utils.getListFromJSONArray(listElementClass, data);
        } catch (IOException | AuthServerException ex) {
            Logger.getLogger(DBUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<>();
    }
    
    /**
     * The function returns list from DB procedure by appropriate class.
     * Note: DB procedure must return one ResultSet!
     * @param <T>
     * @param listElementClass
     * @param procName
     * @param params
     * @return 
     */
    public static <T> ArrayList<T> getObjectsListFromDBProcedure(Class<?> listElementClass, String procName, Object... params){
        try {
            System.out.println(procName + " params For DB: ");
            for (int i = 0; i < params.length; i++) {
                System.out.println("param[" + i + "] = " + params[i]);
            }
            
            DBClient dbClient = GeneralConfig.getInstance().getDBClient();
//            String data = dbClient.callProcedureAndGetAsJson(procName, dbClient.getLang(), params).toString();
            JSONArray data;
            if (params.length > 0){
                Object[] paramsObject = new Object[params.length + 1];
                paramsObject[0] = dbClient.getLang();
                System.arraycopy(params, 0, paramsObject, 1, params.length);
                data = dbClient.callProcedureAndGetAsJson(procName, paramsObject);
            }
            else {
                data = dbClient.callProcedureAndGetAsJson(procName, dbClient.getLang());
            }
            System.out.println(procName + " data from DB: " + data.toString());
            
            return Utils.getListFromJSONArray(listElementClass, data);
        } catch (IOException | AuthServerException ex) {
            Logger.getLogger(DBUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<>();
    }
    
    
    public static ParamGeneralDBResponse getParamsGeneral(String procedureName, JSONObject params){
        ParamGeneralDBResponse response = new ParamGeneralDBResponse();
        try {
            DBClient dbClient = GeneralConfig.getInstance().getDBClient();
            System.out.println("params: " + params.toString());
            JSONArray resultDB = dbClient.callProcedureAndGetAsJson(procedureName, dbClient.getLang(), params);
            
            String generalParams = authclient.Utils.toCamelCase(resultDB).toString();
            
            System.out.println("params general from DB: " + generalParams);
            
            ObjectMapper mapper = new ObjectMapper();
            ArrayList<ParamGeneral> generalParamsList = mapper.readValue(generalParams, mapper.getTypeFactory().constructCollectionType(ArrayList.class, ParamGeneral.class));
            response.setParamGenerals(generalParamsList);
            
        } catch (IOException | AuthServerException ex) {
            Logger.getLogger(DBUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return response;
    }
    
    /**
     * The static function gets one element from DB.
     * @param <T>
     * @param targetClass The element class
     * @param dbTableOrViewName The table or view name in DB.
     * @param params The parameter JSON for filter DB select.
     *                  It could be empty JSON, if user wants every column values from DB table or view.
     * @return 
     */
    public static <T> T getObjectFromDB(Class<?> targetClass, String dbTableOrViewName, JSONObject params){
        try {
            JSONArray selectResultAsArray = GeneralConfig.getInstance().getDBClient().select(dbTableOrViewName, params);
            JSONObject jsonResult = selectResultAsArray.optJSONObject(0);
            
            System.out.println("one " + targetClass + " data: " + jsonResult);
            
            return Utils.getClassFromJSON(targetClass, jsonResult);
        } catch (IOException | AuthServerException ex) {
            Logger.getLogger(License.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public static <T> T getObjectFromDBProcedure(Class<?> targetClass, String procName, JSONObject params){
        try {
            DBClient dbClient = GeneralConfig.getInstance().getDBClient();
            JSONArray selectResultAsArray = dbClient.callProcedureAndGetAsJson(procName, dbClient.getLang(), params);
            JSONObject jsonResult = selectResultAsArray.optJSONObject(0);
            
            System.out.println("one " + targetClass + " data: " + jsonResult);
            
            return Utils.getClassFromJSON(targetClass, jsonResult);
        } catch (IOException | AuthServerException ex) {
            Logger.getLogger(License.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    /**
     * The static function saves one element to DB and gets appropriate entry from DB.
     * Note: If this element is new for DB, then it has not DB id. 
     * So after this function the element will has every old value and a DB id too.
     * @param <T>
     * @param source The element which must save to DB.
     * @param dbTableName The table name in DB.
     * @return 
     */
    public static <T> T saveObjectToDBSimple(Object source, String dbTableName){
        try {
            JSONObject targetJson = authclient.Utils.toUnderScore(Utils.getJSONFromClass(source));
            DBClient dbClient = GeneralConfig.getInstance().getDBClient();
            JSONObject newSourceFromDB = dbClient.callProcedureAndGetAsJson("general_insert_update_simple", dbTableName, dbClient.getLang(), targetJson).getJSONObject(0);
            
            System.out.println("save " + source.getClass() + " data: " + newSourceFromDB.toString());
            
            return Utils.getClassFromJSON(source.getClass(), newSourceFromDB);
        } catch (IOException | AuthServerException | JSONException ex) {
            Logger.getLogger(DBUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    /**
     * The static function saves element into DB.
     * Note: If this element is new for DB, then it has not DB id. 
     * So after this function the element will has every old value and a DB id too.
     * @param <T>
     * @param source The element which must save to DB.
     * @param tableName The DB table name. (Specific for clients and products: client, product - singular form)
     *                                  
     * @return 
     */
    public static <T> T saveObjectToDB(Object source, String tableName){
        try {
            return saveObjectSample(source, tableName);
        } 
        catch (IOException | AuthServerException ex) {
            Logger.getLogger(DBUtils.class.getName()).log(Level.SEVERE, null, ex);
            new AlertMessage(Alert.AlertType.ERROR, ex, ex.getLocalizedMessage(), "").showAlert();
        }
        return null;
    }
    
    public static <T> T saveObjectToDBByProcedure(Object source, String procName){
        try {
            return saveObjectByProcedure(source, procName);
        } 
        catch (IOException | AuthServerException | JSONException ex) {
            Logger.getLogger(DBUtils.class.getName()).log(Level.SEVERE, null, ex);
            new AlertMessage(Alert.AlertType.ERROR, ex, ex.getLocalizedMessage(), "").showAlert();
        }
        return null;
    }
    
    private static <T> T saveObjectSample(Object source, String tableName) throws IOException, AuthServerException{
        JSONObject targetJson = Utils.getJSONFromClass(source);

        System.out.println("data for simple table to server: " + targetJson);

        DBClient dbClient = GeneralConfig.getInstance().getDBClient();
        JSONObject newSourceFromDB;
        newSourceFromDB = dbClient.insertUpdate(tableName, targetJson);

        System.out.println("data for simple table from server: " + newSourceFromDB);

        return Utils.getClassFromJSON(source.getClass(), newSourceFromDB);
    }
    
    private static <T> T saveObjectByProcedure(Object source, String procName) throws IOException, AuthServerException, JSONException{
        JSONObject targetJson = authclient.Utils.toUnderScore(Utils.getJSONFromClass(source));
            
        System.out.println("data for simple table to server: " + targetJson);

        DBClient dbClient = GeneralConfig.getInstance().getDBClient();
        JSONArray newSourceFromDB = dbClient.callProcedureAndGetAsJson(procName, dbClient.getLang(), targetJson); // insertUpdate(tableName, targetJson);

        System.out.println("data for simple table from server: " + newSourceFromDB);
        if (newSourceFromDB == null || newSourceFromDB.length() == 0) return null;
        return Utils.getClassFromJSON(source.getClass(), newSourceFromDB.getJSONObject(0));
    }
    
    
    // Concrete class methods:
    public static Invoice saveInvoice(Object invoice){
        try {
            JSONObject targetJson = Utils.getJSONFromClass(invoice);
            
            System.out.println("invoice target json: " + targetJson);
            
            DBClient dbClient = GeneralConfig.getInstance().getDBClient();
            Response r = dbClient.post("invoices/fromAfb?lang=" + dbClient.getLang(), targetJson.toString());
            return Utils.getClassFromJSON(invoice.getClass(), new JSONObject(r.getDataAsString()));
        } catch (IOException | AuthServerException | JSONException ex) {
            Logger.getLogger(DBUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    
    public static ParamGeneral saveParamGeneral(ParamGeneral paramGeneral, String procName){
        ParamGeneral result = null;
        try {
            result = saveObjectByProcedure(paramGeneral, procName);
        } catch (IOException | JSONException ex) {
            Logger.getLogger(DBUtils.class.getName()).log(Level.SEVERE, null, ex);
        } catch (AuthServerException ex) {
            String[] errorData = Utils.processAuthServerError(ex);
            if (errorData[0].equals("4042")){ // conflicted entry
                String startStr = ": ";
                int startIndex = errorData[1].indexOf(startStr) + startStr.length();
                int endIndex = errorData[1].indexOf(";");
                String[] ids = errorData[1].substring(startIndex, endIndex).split(",");
                String headerTxt = GeneralConfig.getInstance().getTitleFor("param_general_error") + "\n";
                ArrayList<ParamGeneral> entries = selectConflictedEntries(ids);
                String newMsg = entries.stream().map((entry) -> "[" + entry.toString() + "]" + ",\n").reduce("", String::concat);
                AlertMessage alert = new AlertMessage(Alert.AlertType.ERROR, ex, newMsg, GeneralConfig.getInstance().getTitleFor("conflict_params_general"));
                alert.setHeaderText(headerTxt);
                alert.showAlert();
            }
        }
        return result;
    }
    
    private static ArrayList<ParamGeneral> selectConflictedEntries(String[] ids){
        WhereBuilder whereBuilder = new ConditionBuilder().where();
        for (int i = 0; i < ids.length - 1; i++) {
            whereBuilder.or("rec_id", "=", ids[i]);
        }
        JSONObject conflictedIDs = whereBuilder.condition().build();
        return getObjectsListFromDBProcedure(ParamGeneral.class, ParamGeneral.DB_SELECT_PROC_NAME, conflictedIDs);
    }
    
    public static Client saveClient(Client client){
        try {
            JSONObject targetJson = Utils.getJSONFromClass(client);
            
            System.out.println("client json: " + targetJson);
            
            DBClient dbClient = GeneralConfig.getInstance().getDBClient();
            JSONObject jsonFromDB = dbClient.insertUpdateFromAfb("client", targetJson);
            return Utils.getClassFromJSON(Client.class, jsonFromDB);
        } catch (IOException | AuthServerException ex) {
            Logger.getLogger(DBUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public static Doc savePaymentUtility(PaymentUtility paymentUtility){
        DBClient dbClient = GeneralConfig.getInstance().getDBClient();
        Integer id = (paymentUtility.getRecId() == 0) ? null : paymentUtility.getRecId();
        try {
            JSONArray data = dbClient.callProcedureAndGetAsJson("doc_utilities_insert_update", dbClient.getLang(), id, paymentUtility.getDocCode(),
                                                            paymentUtility.utilityProperty().get().getMerchandise(),
                                                            paymentUtility.docDateProperty().get(),
                                                            paymentUtility.docInDocDateProperty().get(),
                                                            paymentUtility.getAmount(),
                                                            paymentUtility.utilityProperty().get().getVatRate(),
                                                            -1);
            System.out.println("save PaymentUtility data from DB: " + data);
            return Utils.getClassFromJSON(Doc.class, data.getJSONObject(0));
        } catch (IOException | AuthServerException | JSONException ex) {
            Logger.getLogger(DBUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public static ArrayList<Doc> saveChargeUtility(ChargeUtility chargeUtility){
        DBClient dbClient = GeneralConfig.getInstance().getDBClient();
        Integer id = (chargeUtility.getRecId() == 0) ? null : chargeUtility.getRecId();
        try {
            JSONArray data = dbClient.callProcedureAndGetAsJson("doc_utilities_insert_update", dbClient.getLang(), id, chargeUtility.getDocCode(),
                                                            chargeUtility.merchandiseProperty().get().getMerchandise(),
                                                            chargeUtility.docDateProperty().get(),
                                                            chargeUtility.docInDocDateProperty().get(),
                                                            chargeUtility.getAmount(),
                                                            chargeUtility.getVat(),
                                                            chargeUtility.getOwnerId());
            System.out.println("save ChangeUtility data from DB: " + data);
            return Utils.getListFromJSONArray(Doc.class, data);
        } catch (IOException | AuthServerException ex) {
            Logger.getLogger(DBUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
      return new ArrayList();
    }
    
    public static boolean deleteObjectFromDB(String deleteProcName, Object... params) {
        boolean result = false;
        try {
            DBClient dbClient = GeneralConfig.getInstance().getDBClient();
            
            System.out.println("delete" + deleteProcName + " params: " + params);
            
            dbClient.callProcedure(deleteProcName, params);
            result = true;
        } catch (IOException | AuthServerException ex) {
            Logger.getLogger(DBUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
    
    public static boolean deleteObjectFromDB(String deleteProcName, int id){
        boolean result = false;
        try {
            DBClient dbClient = GeneralConfig.getInstance().getDBClient();
            dbClient.callProcedure(deleteProcName, id);
            result = true;
        } catch (IOException | AuthServerException ex) {
            Logger.getLogger(DBUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
    
    private static JSONArray licenses;
    private static JSONArray licensesFinaces;
    private static JSONArray invoicesFinaces;
    
    public static void callInvoiceSuitedLicenses(Integer invoiceId, Integer clientId, LocalDate beginDate, LocalDate endDate, JSONArray products, String additionalDiscount, JSONArray licensesIds) throws AuthServerException{
        DBClient dbClient = GeneralConfig.getInstance().getDBClient();
        try {
            JSONArray licensesArray = dbClient.callProcedureAndGetAsJson("invoice_get_suited_licenses",
                                                                            dbClient.getLang(),
                                                                            invoiceId, clientId,
                                                                            beginDate, endDate,
                                                                            products, additionalDiscount, licensesIds);
            System.out.println("licenses array from DB: " + licensesArray);
            licenses = licensesArray.getJSONArray(0);
            licensesFinaces = licensesArray.getJSONArray(1);
            invoicesFinaces = licensesArray.getJSONArray(2);
        } catch (IOException | JSONException ex) {
            Logger.getLogger(DBUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void callInvoiceExistedLicenses(int invoiceId){
        DBClient dbClient = GeneralConfig.getInstance().getDBClient();
        try {
            JSONArray financesData = dbClient.callProcedureAndGetAsJson("invoice_get_existed_licenses", invoiceId, dbClient.getLang());
            System.out.println("invoice finances: " + financesData);
            
            licenses = financesData.getJSONArray(0);
            licensesFinaces = financesData.getJSONArray(1);
            invoicesFinaces = financesData.getJSONArray(2);
        } catch (IOException | AuthServerException | JSONException ex) {
            Logger.getLogger(DBUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static ArrayList<PartOfLicense> getLicenses(){
        try {
            String licensesAsString = licenses.toString();
            licenses = null; // free static variable
            return new ObjectMapper().readValue(licensesAsString, new TypeReference<ArrayList<PartOfLicense>>(){});
        } catch (IOException ex) {
            Logger.getLogger(DBUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<>();
    }
    
    public static ArrayList<LicenseFinaces> getLicensesFinaces(){
        try {
            String licensesFinacesAsString = licensesFinaces.toString();
            licensesFinaces = null; // free static variable
            return new ObjectMapper().readValue(licensesFinacesAsString, new TypeReference<ArrayList<LicenseFinaces>>(){});
        } catch (IOException ex) {
            Logger.getLogger(DBUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<>();
    }
    
    public static ArrayList<InvoiceFinaces> getInvoicesFinaces(){
        try {
            String invoicesFinacesAsString = invoicesFinaces.toString();
            invoicesFinaces = null; // free static variable
            return new ObjectMapper().readValue(invoicesFinacesAsString, new TypeReference<ArrayList<InvoiceFinaces>>(){});
        } catch (IOException ex) {
            Logger.getLogger(DBUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<>();
    }
}
