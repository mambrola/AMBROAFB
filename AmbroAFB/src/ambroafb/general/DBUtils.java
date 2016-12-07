/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general;

import ambroafb.invoices.Invoice;
import ambroafb.invoices.helper.InvoicesFinaces;
import ambroafb.licenses.License;
import ambroafb.licenses.helper.LicensesFinaces;
import authclient.AuthServerException;
import authclient.Response;
import authclient.db.DBClient;
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
            
            String data = GeneralConfig.getInstance().getDBClient().select(dbTableOrViewName, params).toString();
            
            System.out.println(dbTableOrViewName + " data from DB: " + data);
            
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(data, mapper.getTypeFactory().constructCollectionType(ArrayList.class, listElementClass));
        } catch (IOException | AuthServerException ex) {
            Logger.getLogger(DBUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<>();
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
            JSONObject targetJson = Utils.getJSONFromClass(source);
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
     * @param tableNameOrStoreProcedure The DB table name in singular form (ex: client, product ...)
     *                                  Or procedure name for AFB.
     * @param optionals
     * @return 
     */
    public static <T> T saveObjectToDB(Object source, String tableNameOrStoreProcedure){
        try {
            JSONObject targetJson = Utils.getJSONFromClass(source);
            
            System.out.println("data for simple table to server: " + targetJson);
            
            DBClient dbClient = GeneralConfig.getInstance().getDBClient();
            JSONObject newSourceFromDB;
            newSourceFromDB = dbClient.insertUpdate(tableNameOrStoreProcedure, targetJson);
            
            System.out.println("data for simple table from server: " + newSourceFromDB);
            
            return Utils.getClassFromJSON(source.getClass(), newSourceFromDB);
        } 
        catch (IOException | AuthServerException ex) {
            Logger.getLogger(DBUtils.class.getName()).log(Level.SEVERE, null, ex);
            new AlertMessage(Alert.AlertType.ERROR, ex, ex.getLocalizedMessage(), "").showAlert();
        }
        return null;
    }
    
    public static Invoice saveInvoice(Object invoice){
        try {
            JSONObject targetJson = Utils.getJSONFromClass(invoice);
            DBClient dbClient = GeneralConfig.getInstance().getDBClient();
            Response r = dbClient.post("invoices/fromAfb?lang=" + dbClient.getLang(), targetJson.toString());
            return Utils.getClassFromJSON(invoice.getClass(), new JSONObject(r.getDataAsString()));
        } catch (IOException | AuthServerException | JSONException ex) {
            Logger.getLogger(DBUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public static <T> T saveObjectToDBWith(String procedureName, Object source, Object... params) {
        try {
            JSONObject targetJson = Utils.getJSONFromClass(source);
            
            System.out.println("data for procedure to server: " + targetJson);
            
            DBClient dbClient = GeneralConfig.getInstance().getDBClient();
            JSONObject newSourceFromDB;
            newSourceFromDB = dbClient.callProcedureAndGetAsJson(procedureName, dbClient.getLang(), targetJson, params).getJSONObject(0);
            
            System.out.println("data for procedure from server: " + newSourceFromDB);
            
            return Utils.getClassFromJSON(source.getClass(), newSourceFromDB);
        }
        catch (IOException | AuthServerException | JSONException ex) {
            Logger.getLogger(DBUtils.class.getName()).log(Level.SEVERE, null, ex);
            new AlertMessage(Alert.AlertType.ERROR, ex, ex.getLocalizedMessage(), "").showAlert();
        }
        return null;
    }
    
    
    
    public static boolean deleteObjectFromDB(String deleteProcName, JSONObject params) {
        boolean result = false;
        try {
            DBClient dbClient = GeneralConfig.getInstance().getDBClient();
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
    
    public static void callInvoiceSuitedLicenses(Integer invoiceId, Integer clientId, LocalDate beginDate, LocalDate endDate, JSONArray products, String additionalDiscount, JSONArray licensesIds){
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
        } catch (IOException | AuthServerException | JSONException ex) {
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
    
    public static ArrayList<License> getLicenses(){
        try {
            String licensesAsString = licenses.toString();
            licenses = null; // free static variable
            return new ObjectMapper().readValue(licensesAsString, new TypeReference<ArrayList<License>>(){});
        } catch (IOException ex) {
            Logger.getLogger(DBUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<>();
    }
    
    public static ArrayList<LicensesFinaces> getLicensesFinaces(){
        try {
            String licensesFinacesAsString = licensesFinaces.toString();
            licensesFinaces = null; // free static variable
            return new ObjectMapper().readValue(licensesFinacesAsString, new TypeReference<ArrayList<LicensesFinaces>>(){});
        } catch (IOException ex) {
            Logger.getLogger(DBUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<>();
    }
    
    public static ArrayList<InvoicesFinaces> getInvoicesFinaces(){
        try {
            String invoicesFinacesAsString = invoicesFinaces.toString();
            invoicesFinaces = null; // free static variable
            return new ObjectMapper().readValue(invoicesFinacesAsString, new TypeReference<ArrayList<InvoicesFinaces>>(){});
        } catch (IOException ex) {
            Logger.getLogger(DBUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<>();
    }
}
