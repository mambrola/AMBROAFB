/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general.interfaces;

import ambroafb.general.GeneralConfig;
import ambroafb.general.Utils;
import ambroafb.general.exceptions.ExceptionsFactory;
import authclient.AuthServerException;
import authclient.db.ConditionBuilder;
import authclient.db.DBClient;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author dkobuladze
 */
public abstract class DataProvider {
    
    public static final JSONObject PARAM_FOR_ALL = new ConditionBuilder().build();
    
    protected final String DB_ID = "rec_id";
    
    protected String DB_VIEW_NAME = "";
    
    
    /**
     *  The method calls DB procedure and gives parameters to it.
     * @param procedureName The name of DB procedure.
     * @param params Parameters for procedure.
     * @throws IOException
     * @throws AuthServerException 
     */
    protected void callProcedure(String procedureName, Object... params) throws Exception {
        DBClient dbClient = GeneralConfig.getInstance().getDBClient();
        try {
            dbClient.callProcedure(procedureName, params);
        } catch (AuthServerException ex){
            throw ExceptionsFactory.getAppropriateException(ex);
        }
    }
    
    /**
     *  The method calls DB procedure, gives parameters to it and returns objects list.
     * @param <T>
     * @param listElementClass The class of list elements.
     * @param procedureName The name of DB procedure.
     * @param params Parameters for DB procedure.
     * @return The list of generic objects.
     * @throws IOException
     * @throws AuthServerException 
     */
    protected <T> ArrayList<T> callProcedure(Class<?> listElementClass, String procedureName, Object... params) throws Exception {
        DBClient dbClient = GeneralConfig.getInstance().getDBClient();
        JSONArray data;
        try {
            data = dbClient.callProcedureAndGetAsJson(procedureName, params);
        } catch (AuthServerException ex){
            throw ExceptionsFactory.getAppropriateException(ex);
        }
        return Utils.getListFromJSONArray(listElementClass, data);
    }
    
    /**
     *  The method saves 'simple' objects to DB.
     * @param <T>
     * @param source The object data.
     * @param tableName The name of table.
     * @param dataWithUnderscores Flag provides to send data with or without underscores to DB (getter method camalCase names change to underscores:  getClientId   ->  get_client_id). 
     * @return The new object that saved in DB. It will has DB id too.
     * @throws IOException
     * @throws AuthServerException 
     */
    protected static <T> T saveSimple(Object source, String tableName, boolean dataWithUnderscores) throws Exception {
        JSONObject targetJson = (dataWithUnderscores) ? authclient.Utils.toUnderScore(Utils.getJSONFromClass(source))
                                                      : Utils.getJSONFromClass(source);
        DBClient dbClient = GeneralConfig.getInstance().getDBClient();
        JSONArray data;
        try {
            data = dbClient.callProcedureAndGetAsJson("general_insert_update_simple", tableName, dbClient.getLang(), targetJson);
        } catch (AuthServerException ex) {
            throw ExceptionsFactory.getAppropriateException(ex);
        }
        if (data == null || data.length() == 0) return null;
        return Utils.getClassFromJSON(source.getClass(), data.getJSONObject(0));
    }
    
    /**
     *  The method saves object and uses specific procedure for it.
     * @param <T>
     * @param source The object data.
     * @param procName The name of procedure.
     * @return The new object that saved in DB. It will has DB id too.
     * @throws IOException
     * @throws AuthServerException
     * @throws JSONException 
     */
    protected static <T> T saveObjectByProcedure(Object source, String procName) throws Exception {
        JSONObject targetJson = authclient.Utils.toUnderScore(Utils.getJSONFromClass(source));
        return saveObjectByProcedureHelper(source.getClass(), targetJson, procName);
    }
    
    /**
     *  The method saves object and uses specific procedure for it. The method must use if object have separate saving elements.
     * @param <T>
     * @param source The object data.
     * @param procName The name of procedure.
     * @param separateSaving The JSON for separate saving entries. It will put into eventually sending JSON and has its appropriate key.
     * @return The new object that saved in DB. It will has DB id too.
     * @throws IOException
     * @throws AuthServerException
     * @throws JSONException 
     */
    protected static <T> T saveObjectByProcedure(Object source, String procName, JSONObject separateSaving) throws Exception {
        JSONObject targetJson = authclient.Utils.toUnderScore(Utils.getJSONFromClass(source));
        targetJson.put("sets_for_separate_saving", separateSaving);
        return saveObjectByProcedureHelper(source.getClass(), targetJson, procName);
    }
    
    
    private static <T> T saveObjectByProcedureHelper(Class target, JSONObject sendingJson, String procName) throws Exception{
        DBClient dbClient = GeneralConfig.getInstance().getDBClient();
        JSONArray newSourceFromDB;
        try {
            newSourceFromDB = dbClient.callProcedureAndGetAsJson(procName, dbClient.getLang(), sendingJson); // insertUpdate(tableName, targetJson); -> old version
        } catch (AuthServerException ex) {
            throw ExceptionsFactory.getAppropriateException(ex);
        }

        System.out.println("data for simple table from server: " + newSourceFromDB);
        if (newSourceFromDB == null || newSourceFromDB.length() == 0) return null;
        return Utils.getClassFromJSON(target, newSourceFromDB.getJSONObject(0));
    }
    
    
    /**
     * The static function gets one element from DB.
     * @param <T>
     * @param targetClass The element class
     * @param dbTableOrViewName The table or view name in DB.
     * @param params The parameter JSON for filter DB select.
     *                  It could be empty JSON, if user wants every column values from DB table or view.
     * @return 
     * @throws java.io.IOException 
     * @throws authclient.AuthServerException 
     */
    protected <T> T getObjectFromDB(Class<?> targetClass, String dbTableOrViewName, JSONObject params) throws Exception {
        JSONArray selectResultAsArray;
        try {
            selectResultAsArray = GeneralConfig.getInstance().getDBClient().select(dbTableOrViewName, params);
        } catch (AuthServerException ex) {
            throw ExceptionsFactory.getAppropriateException(ex);
        }
        JSONObject jsonResult = selectResultAsArray.optJSONObject(0);

        System.out.println("one " + targetClass + " data: " + jsonResult);

        return Utils.getClassFromJSON(targetClass, jsonResult);
    }
    
    protected <T> T getObjectFromDBProcedure(Class<?> targetClass, String procName, JSONObject params) throws Exception{
        DBClient dbClient = GeneralConfig.getInstance().getDBClient();
        JSONArray selectResultAsArray;
        try {
            selectResultAsArray = dbClient.callProcedureAndGetAsJson(procName, dbClient.getLang(), params);
        } catch (AuthServerException ex) {
            throw ExceptionsFactory.getAppropriateException(ex);
        }
        JSONObject jsonResult = selectResultAsArray.optJSONObject(0);

        System.out.println("one " + targetClass + " data: " + jsonResult);

        return Utils.getClassFromJSON(targetClass, jsonResult);
    }
    
    /**
     *  The static function gets a ArrayList of specified class elements from DB.
     * If exception returns from DB, the method uses errorAction Consumer.
     * @param <T>
     * @param listElementClass The class of elements which must be in list.
     * @param dbTableOrViewName The table or view name where entries are in DB.
     * @param params The parameter JSON for filter DB select.
     *                  It could be empty JSON, if user wants every column values from DB table or view.
     * @return Empty ArrayList if becomes exception. Otherwise - Full of specific objects.
     * @throws java.io.IOException
     * @throws authclient.AuthServerException
     */
    protected <T> List<T> getObjectsListFromDBTable(Class<?> listElementClass, String dbTableOrViewName, JSONObject params) throws Exception {
        JSONArray data;
        try {
            data = GeneralConfig.getInstance().getDBClient().select(dbTableOrViewName, params);
        } catch (AuthServerException ex) {
            throw ExceptionsFactory.getAppropriateException(ex);
        }
        return Utils.getListFromJSONArray(listElementClass, data);
    }
    
    /**
     *  The method fetches data from database procedure and give it to appropriate parameters. 
     * If 'params' is empty, DB procedure will call only with default language; Otherwise default language will put into 'params'.
     * @param <T>
     * @param listElementClass The elements class that will be 
     * @param procName
     * @param params
     * @return
     * @throws Exception 
     */
    protected <T> List<T> getObjectsListFromDBProcedure(Class<?> listElementClass, String procName, Object... params) throws Exception {
        DBClient dbClient = GeneralConfig.getInstance().getDBClient();
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
        return Utils.getListFromJSONArray(listElementClass, data);
    }
    
    /**
     *  The method removes entry on appropriate index.
     * @param tableName The name of table where want to remove entry.
     * @param recId The identifier of entry.
     * @throws Exception 
     */
    protected void generalDelete(String tableName, int recId) throws Exception {
        callProcedure("general_delete", tableName, recId);
    }
}
