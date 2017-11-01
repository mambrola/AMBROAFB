/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.docs.types.conversion;

import ambroafb.docs.Doc;
import ambroafb.general.DBUtils;
import ambroafb.general.interfaces.DataChangeProvider;
import ambroafb.general.interfaces.EditorPanelable;
import authclient.AuthServerException;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author dkobuladze
 */
public class ConversionDataChangeProvider extends DataChangeProvider {

    private final String DB_DELETE_PROCEDURE_NAME = "doc_delete";
    
    @Override
    public boolean deleteOneFromDB(int recId) throws Exception {
        boolean result = true;
        try {
            callProcedure(DB_DELETE_PROCEDURE_NAME, recId);
        } catch(IOException | AuthServerException ex){
            result = false;
            throw ex;
        }
        return result;
    }

    @Override
    public ArrayList<Doc> editOneToDB(EditorPanelable object) throws Exception {
        return DBUtils.saveConversionDoc((Conversion)object); // ------
    }

    @Override
    public ArrayList<Doc> saveOneToDB(EditorPanelable object) throws Exception {
        return DBUtils.saveConversionDoc((Conversion)object); // ------
    }
    
}
