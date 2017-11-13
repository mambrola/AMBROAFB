/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.docs.types.conversion;

import ambroafb.docs.Doc;
import ambroafb.general.GeneralConfig;
import ambroafb.general.interfaces.DataChangeProvider;
import ambroafb.general.interfaces.EditorPanelable;
import authclient.db.DBClient;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author dkobuladze
 */
public class ConversionDataChangeProvider extends DataChangeProvider {

    private final String DELETE_PROCEDURE = "doc_delete";
    private final String SAVE_UPDATE_PROCEDURE = "doc_conversion_insert_update";
    
    @Override
    public List<Doc> deleteOneFromDB(int recId) throws Exception {
        System.out.println("child delete method...");
        ArrayList<Doc> d =  callProcedure(Doc.class, DELETE_PROCEDURE, recId);
        for (Doc doc : d) {
            System.out.println(doc);
        }
        return d;
    }

    @Override
    public ArrayList<Doc> editOneToDB(EditorPanelable object) throws Exception {
        return saveOneToDB(object);
    }

    @Override
    public ArrayList<Doc> saveOneToDB(EditorPanelable object) throws Exception {
        DBClient dbClient = GeneralConfig.getInstance().getDBClient();
        Conversion conversionFromAfB = (Conversion) object;
        Integer id = (conversionFromAfB.getRecId() == 0) ? null : conversionFromAfB.getRecId();
        return callProcedure(Doc.class, SAVE_UPDATE_PROCEDURE,
                                                        dbClient.getLang(),
                                                        id,
                                                        conversionFromAfB.docDateProperty().get(),
                                                        conversionFromAfB.docInDocDateProperty().get(),
                                                        conversionFromAfB.getSellAccount().getIso(),
                                                        conversionFromAfB.getSellAccount().getRecId(),
                                                        conversionFromAfB.getSellAmount(),
                                                        conversionFromAfB.getBuyingAccount().getIso(),
                                                        conversionFromAfB.getBuyingAccount().getRecId(),
                                                        conversionFromAfB.getBuyingAmount(),
                                                        -1);
    }
    
}
