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
import org.json.JSONObject;

/**
 *
 * @author dkobuladze
 */
public abstract class DataProvider {
    
    public static final JSONObject PARAM_FOR_ALL = new ConditionBuilder().build();
    
    protected String DB_VEIW_NAME = "";
    
    
    protected void callProcedure(String procedureName, Object... params) throws IOException, AuthServerException {
        DBClient dbClient = GeneralConfig.getInstance().getDBClient();
        dbClient.callProcedure(procedureName, params);
    }
    
    protected <T> ArrayList<T> callProcedure(Class<?> listElementClass, String procedureName, Object... params) throws IOException, AuthServerException {
        DBClient dbClient = GeneralConfig.getInstance().getDBClient();
        JSONArray data = dbClient.callProcedureAndGetAsJson(procedureName, params);
        return Utils.getListFromJSONArray(listElementClass, data);
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
    public <T> T getObjectFromDB(Class<?> targetClass, String dbTableOrViewName, JSONObject params) throws IOException, AuthServerException {
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
    public <T> List<T> getObjectsListFromDB(Class<?> listElementClass, String dbTableOrViewName, JSONObject params) throws IOException, AuthServerException{
        System.out.println(dbTableOrViewName + " params For DB: " + params);

        JSONArray data = GeneralConfig.getInstance().getDBClient().select(dbTableOrViewName, params);

        System.out.println(dbTableOrViewName + " data from DB: " + data);

        return Utils.getListFromJSONArray(listElementClass, data);
    }
    
    
    
    
//    
//    /**
//     *  The method returns {@link ambroafb.general.interfaces.EditorPanelable EditorPanelable} list by condition.
//     * @param params The JSON object for condition.
//     * @param successAction The action executes when list returning from DB was successful. It will call in Platform.runLater. 
//     *                                      If you want to nothing will be executed, please give the null value for it.
//     * @param errorAction The action executes if list returning from DB was not successful.  It will call in Platform.runLater.
//     *                                      If you want to nothing will be executed, please give the null value for it.
//     */
//    public abstract void getListByCondition(JSONObject params, Consumer<List<EditorPanelable>> successAction, Consumer<Exception> errorAction);
//    
//    
//    /**
//     *  According to filter model,  the method returns {@link ambroafb.general.interfaces.EditorPanelable EditorPanelable} list.
//     * @param model The filterable model
//     * @param successAction The action executes when list returning from DB was successful. It will call in Platform.runLater. 
//     *                                      If you want to nothing will be executed, please give the null value for it.
//     * @param errorAction The action executes if list returning from DB was not successful.  It will call in Platform.runLater.
//     *                                      If you want to nothing will be executed, please give the null value for it.
//     */
//    public abstract void getListBy(FilterModel model, Consumer<List<EditorPanelable>> successAction, Consumer<Exception> errorAction);
//    
//    
//    /**
//     *  The method gets one by id.
//     * @param recId The unique identifier for object.
//     * @param successAction The action executes if object returning from DB was successful. It will call in Platform.runLater and before stage close. 
//     *                                       If you want to nothing will be executed, please give the null value for it.
//     * @param errorAction The action  executes if object returning from DB was not successful.  It will call in Platform.runLater.
//     *                                      If you want to nothing will be executed, please give the null value for it.
//     */
//    public abstract void getOneFromDB(int recId, Consumer<Object> successAction, Consumer<Exception> errorAction);
//    
//    
//    /**
//     *  The method removes one object by recId.
//     * @param recId The unique identifier for object.
//     * @param successAction The action when delete was successful. It will call in Platform.runLater and before stage close. 
//     *                                      If nothing is executed, then gives null value.
//     * @param errorAction The action when delete was not successful.  If nothing is executed, then gives null value.
//     */
//    public abstract void deleteOneFromDB(int recId, Consumer<Object> successAction, Consumer<Exception> errorAction);
//    
//    
//    /**
//     *  The method changes existed object.
//     * @param object The object that must be change.
//     */
//    public abstract void editOneToDB(EditorPanelable object, Consumer<Object> successAction, Consumer<Exception> errorAction);
//    
//    
//    /**
//     *  The method saveOneToDB new object.
//     * @param object The new Object.
//     */
//    public abstract void saveOneToDB(EditorPanelable object, Consumer<Object> successAction, Consumer<Exception> errorAction);
    
}
