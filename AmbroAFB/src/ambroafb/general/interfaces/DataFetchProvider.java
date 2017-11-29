/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general.interfaces;

import java.util.List;
import java.util.function.Consumer;
import javafx.application.Platform;
import org.json.JSONObject;

/**
 *
 * @author dkobuladze
 */
public abstract class DataFetchProvider extends DataProvider {
    
    /**
     *  The method returns {@link ambroafb.general.interfaces.EditorPanelable EditorPanelable} list by condition.
     * @param <T>
     * @param params The JSON object for condition.
     * @return The list of Generic objects.
     * @throws java.io.IOException
     * @throws authclient.AuthServerException
     */
    public abstract <T> List<T> getFilteredBy(JSONObject params) throws Exception;
    
    /**
     *  The method returns {@link ambroafb.general.interfaces.EditorPanelable EditorPanelable} list by condition.
     * @param <T>
     * @param params The JSON object for condition.
     * @param successAction The action executes when list returning from DB was successful. It will call in Platform.runLater. 
     *                                      If you want to nothing will be executed, please give the null value for it.
     * @param errorAction The action executes if list returning from DB was not successful.  It will call in Platform.runLater.
     *                                      If you want to nothing will be executed, please give the null value for it.
     */
    public <T> void filteredBy(JSONObject params, Consumer<List<T>> successAction, Consumer<Exception> errorAction){
        new Thread(() -> {
            try {
                List<T> list = getFilteredBy(params);
                Platform.runLater(() -> {
                    if (successAction != null) successAction.accept(list);
                });
            } catch (Exception ex) {
                Platform.runLater(() -> {
                    if (errorAction != null) errorAction.accept(ex);
                });
            }
        }).start();
    }
    
    
    /**
     *  According to filter model,  the method returns {@link ambroafb.general.interfaces.EditorPanelable EditorPanelable} list.
     * @param <T>
     * @param model The filterable model
     * @return The list of Generic objects.
     * @throws java.io.IOException
     * @throws authclient.AuthServerException
     */
    public abstract <T> List<T> getFilteredBy(FilterModel model) throws Exception;
    
    /**
     *  According to filter model,  the method returns {@link ambroafb.general.interfaces.EditorPanelable EditorPanelable} list.
     * @param <T>
     * @param model The filterable model
     * @param successAction The action executes when list returning from DB was successful. It will call in Platform.runLater. 
     *                                      If you want to nothing will be executed, please give the null value for it.
     * @param errorAction The action executes if list returning from DB was not successful.  It will call in Platform.runLater.
     *                                      If you want to nothing will be executed, please give the null value for it.
     */
    public <T> void filteredBy(FilterModel model, Consumer<List<T>> successAction, Consumer<Exception> errorAction) {
        new Thread(() -> {
            try {
                List<T> list = getFilteredBy(model);
                Platform.runLater(() -> {
                    if (successAction != null) successAction.accept(list);
                });
            } catch (Exception ex) {
                Platform.runLater(() -> {
                    if (errorAction != null) errorAction.accept(ex);
                });
            }
        }).start();
    }
    
    protected <T> Consumer<List<T>> getInfluenceAction(){
        return null;
    }
    
    
    /**
     *  The method gets one by id.
     * @param <T>
     * @param recId The unique identifier for object.
     * @return The generic object.
     * @throws java.io.IOException
     * @throws authclient.AuthServerException
     */
    public abstract <T> T getOneFromDB(int recId) throws Exception;
    
    /**
     *  The method gets one by id.
     * @param <T>
     * @param recId The unique identifier for object.
     * @param successAction The action executes if object returning from DB was successful. It will call in Platform.runLater and before stage close. 
     *                                       If you want to nothing will be executed, please give the null value for it.
     * @param errorAction The action  executes if object returning from DB was not successful.  It will call in Platform.runLater.
     *                                      If you want to nothing will be executed, please give the null value for it.
     */
    public <T> void getOneFromDB(int recId, Consumer<T> successAction, Consumer<Exception> errorAction) {
        new Thread(() -> {
            try {
                Object obj = getOneFromDB(recId);
                Platform.runLater(() -> {
                    if (successAction != null) successAction.accept((T)obj);
                });
            } catch (Exception ex) {
                Platform.runLater(() -> {
                    if (errorAction != null) errorAction.accept(ex);
                });
            }
        }).start();
    }
    
    
}
