/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general.interfaces;

import authclient.AuthServerException;
import java.io.IOException;

/**
 *
 * @author dkobuladze
 */
public abstract class DataChangeProvider extends DataProvider {
    
    /**
     *  The method removes one object from DB.
     * @param recId The unique identifier for object.
     * @return True - if object has removed, false - otherwise.
     * @throws java.io.IOException
     * @throws authclient.AuthServerException
     */
    public abstract boolean deleteOneFromDB(int recId) throws IOException, AuthServerException;
    
    
    /**
     *  The method changes existed object.
     * @param <T>
     * @param object The object that must be change.
     * @return The generic object that change in DB.
     * @throws java.io.IOException
     * @throws authclient.AuthServerException
     */
    public abstract <T> T editOneToDB(EditorPanelable object) throws IOException, AuthServerException;
    
    
    /**
     *  The method saves  new object to DB.
     * @param <T>
     * @param object The new Object.
     * @return The generic object that save in DB.
     * Note: It will contain DB id too.
     * @throws java.io.IOException
     * @throws authclient.AuthServerException
     */
    public abstract <T> T saveOneToDB(EditorPanelable object) throws IOException, AuthServerException;
    
}
