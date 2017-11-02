/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.docs;

import ambroafb.docs.Doc;
import ambroafb.general.interfaces.DataChangeProvider;
import ambroafb.general.interfaces.EditorPanelable;
import authclient.AuthServerException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dkobuladze
 */
public class DocDataChangeProvider extends DataChangeProvider {

    private final String DELETE_PROCEDURE = "doc_delete";
    private final String SAVE_UPDATE_PROCEDURE = "doc_memorial_insert_update";
    
    @Override
    public void deleteOneFromDB(int recId) throws Exception {
        callProcedure(DELETE_PROCEDURE, recId);
    }

    @Override
    public Doc editOneToDB(EditorPanelable object) throws Exception {
        Doc docFromDB = null;
        Doc docFromAfb = (Doc) object;
        ArrayList<Doc> docs = callProcedure(Doc.class, SAVE_UPDATE_PROCEDURE, 
                                                            docFromAfb.getRecId(),
                                                            docFromAfb.docDateProperty().get(),
                                                            docFromAfb.docInDocDateProperty().get(),
                                                            docFromAfb.getIso(),
                                                            docFromAfb.getDebitId(),
                                                            docFromAfb.getCreditId(),
                                                            docFromAfb.getAmount(),
                                                            docFromAfb.getDocCode(),
                                                            docFromAfb.getDescrip(),
                                                            docFromAfb.getOwnerId());
        if (!docs.isEmpty()) docFromDB = docs.get(0);
        return docFromDB;
    }

    @Override
    public Doc saveOneToDB(EditorPanelable object) throws Exception {
        Doc docFromDB = null;
        Doc docFromAfb = (Doc) object;
        ArrayList<Doc> docs;
        try {
            docs = callProcedure(Doc.class, SAVE_UPDATE_PROCEDURE, 
                    null,
                    docFromAfb.docDateProperty().get(),
                    docFromAfb.docInDocDateProperty().get(),
                    docFromAfb.getIso(),
                    docFromAfb.getDebitId(),
                    docFromAfb.getCreditId(),
                    docFromAfb.getAmount(),
                    docFromAfb.getDocCode(),
                    docFromAfb.getDescrip(),
                    docFromAfb.getOwnerId());
        } catch (IOException | AuthServerException ex) {
            Logger.getLogger(DocDataChangeProvider.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }
        if (!docs.isEmpty()) docFromDB = docs.get(0);
        return docFromDB;
    }
    
}
