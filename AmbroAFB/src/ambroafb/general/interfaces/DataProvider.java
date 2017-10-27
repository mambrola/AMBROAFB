/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general.interfaces;

import ambroafb.general.GeneralConfig;
import ambroafb.general.Utils;
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
    
    protected String DB_VEIW_NAME = "";
    
    /**
     *  The method calls DB procedure and gives parameters to it.
     * @param procedureName The name of DB procedure.
     * @param params Parameters for procedure.
     * @throws IOException
     * @throws AuthServerException 
     */
    protected void callProcedure(String procedureName, Object... params) throws IOException, AuthServerException {
        DBClient dbClient = GeneralConfig.getInstance().getDBClient();
        dbClient.callProcedure(procedureName, params);
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
    protected <T> ArrayList<T> callProcedure(Class<?> listElementClass, String procedureName, Object... params) throws IOException, AuthServerException {
        DBClient dbClient = GeneralConfig.getInstance().getDBClient();
        JSONArray data = dbClient.callProcedureAndGetAsJson(procedureName, params);
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
    protected static <T> T saveSimple(Object source, String tableName, boolean dataWithUnderscores) throws IOException, AuthServerException, JSONException {
        JSONObject targetJson = (dataWithUnderscores) ? authclient.Utils.toUnderScore(Utils.getJSONFromClass(source))
                                                      : Utils.getJSONFromClass(source);
        DBClient dbClient = GeneralConfig.getInstance().getDBClient();
        JSONArray data = dbClient.callProcedureAndGetAsJson("general_insert_update_simple", tableName, dbClient.getLang(), targetJson);
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
    protected static <T> T saveObjectByProcedure(Object source, String procName) throws IOException, AuthServerException, JSONException{
        JSONObject targetJson = authclient.Utils.toUnderScore(Utils.getJSONFromClass(source));
            
        System.out.println("data for simple table to server: " + targetJson);

        DBClient dbClient = GeneralConfig.getInstance().getDBClient();
        JSONArray newSourceFromDB = dbClient.callProcedureAndGetAsJson(procName, dbClient.getLang(), targetJson); // insertUpdate(tableName, targetJson);

        System.out.println("data for simple table from server: " + newSourceFromDB);
        if (newSourceFromDB == null || newSourceFromDB.length() == 0) return null;
        return Utils.getClassFromJSON(source.getClass(), newSourceFromDB.getJSONObject(0));
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
    protected <T> T getObjectFromDB(Class<?> targetClass, String dbTableOrViewName, JSONObject params) throws IOException, AuthServerException {
        JSONArray selectResultAsArray = GeneralConfig.getInstance().getDBClient().select(dbTableOrViewName, params);
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
    protected <T> List<T> getObjectsListFromDB(Class<?> listElementClass, String dbTableOrViewName, JSONObject params) throws IOException, AuthServerException{
        System.out.println(dbTableOrViewName + " params For DB: " + params);

        JSONArray data = GeneralConfig.getInstance().getDBClient().select(dbTableOrViewName, params);

        System.out.println(dbTableOrViewName + " data from DB: " + data);

        return Utils.getListFromJSONArray(listElementClass, data);
    }
    
}
