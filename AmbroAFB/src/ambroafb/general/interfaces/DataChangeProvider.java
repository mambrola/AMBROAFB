/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general.interfaces;

import java.util.function.Consumer;
import javafx.application.Platform;

/**
 *
 * @author dkobuladze
 */
public abstract class DataChangeProvider extends DataProvider {
    
    /**
     *  The method removes one object from DB.
     * @param recId The unique identifier for object.
     * @throws java.io.IOException
     * @throws authclient.AuthServerException
     */
    public abstract void deleteOneFromDB(int recId) throws Exception;
    
    /**
     *  The method removes one object from DB.
     * @param recId The unique identifier for object.
     * @param successAction
     * @param errorAction
     */
    public void deleteOneFromDB(int recId, Consumer<Void> successAction, Consumer<Exception> errorAction){
        new Thread(() -> {
            try {
                deleteOneFromDB(recId);
                Platform.runLater(() -> {
                    if (successAction != null) successAction.accept(null);
                });
            } catch (Exception ex) {
                Platform.runLater(() -> {
                    if (errorAction != null) errorAction.accept(ex);
                });
            }
        }).start();
    }
    
    
    /**
     *  The method changes existed object.
     * @param <T>
     * @param object The object that must be change.
     * @return The generic object that change in DB.
     * @throws java.io.IOException
     * @throws authclient.AuthServerException
     */
    public abstract <T> T editOneToDB(EditorPanelable object) throws Exception;
    
    /**
     *  The method changes existed object.
     * @param <T>
     * @param object The object that must be change.
     * @param successAction
     * @param errorAction
     */
    public <T> void editOneToDB(EditorPanelable object, Consumer<T> successAction, Consumer<Exception> errorAction) {
        new Thread(() -> {
            try {
                T obj = editOneToDB(object);
                Platform.runLater(() -> {
                    if (successAction != null) successAction.accept(obj);
                });
            } catch (Exception ex) {
                Platform.runLater(() -> {
                    if (errorAction != null) errorAction.accept(ex);
                });
            }
        }).start();
    }
    
    
    /**
     *  The method saves  new object to DB.
     * @param <T>
     * @param object The new Object.
     * @return The generic object that save in DB.
     * Note: It will contain DB id too.
     * @throws java.io.IOException
     * @throws authclient.AuthServerException
     */
    public abstract <T> T saveOneToDB(EditorPanelable object) throws Exception;
    
    /**
     *  The method saves  new object to DB.
     * @param <T>
     * @param object The new Object.
     * @param successAction
     * @param errorAction
     */
    public <T> void saveOneToDB(EditorPanelable object,  Consumer<T> successAction, Consumer<Exception> errorAction){
        new Thread(() -> {
            try {
                T obj = saveOneToDB(object);
                Platform.runLater(() -> {
                    if (successAction != null) successAction.accept(obj);
                });
            } catch (Exception ex) {
                Platform.runLater(() -> {
                    if (errorAction != null) errorAction.accept(ex);
                });
            }
        }).start();
    }
    
}
