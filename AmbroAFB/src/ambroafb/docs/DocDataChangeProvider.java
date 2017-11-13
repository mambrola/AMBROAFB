/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.docs;

import ambroafb.general.interfaces.DataChangeProvider;
import ambroafb.general.interfaces.EditorPanelable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author dkobuladze
 */
public class DocDataChangeProvider extends DataChangeProvider {

    private final String DELETE_PROCEDURE = "doc_delete";
    private final String SAVE_UPDATE_PROCEDURE = "doc_memorial_insert_update";
    
    @Override
    public List<Doc> deleteOneFromDB(int recId) throws Exception {
        return callProcedure(Doc.class, DELETE_PROCEDURE, recId);
    }

    @Override
    public Doc editOneToDB(EditorPanelable object) throws Exception {
        return saveOneToDB(object);
    }

    @Override
    public Doc saveOneToDB(EditorPanelable object) throws Exception {
        Doc docFromDB = null;
        Doc docFromAfb = (Doc) object;
        ArrayList<Doc> docs;
        Integer id = (docFromAfb.getRecId() == 0) ? null : docFromAfb.getRecId();
        docs = callProcedure(Doc.class, SAVE_UPDATE_PROCEDURE, 
                                                        id,
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
    
}
