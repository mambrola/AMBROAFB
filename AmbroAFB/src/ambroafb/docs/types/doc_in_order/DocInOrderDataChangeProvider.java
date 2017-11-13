/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.docs.types.doc_in_order;

import ambroafb.docs.Doc;
import ambroafb.general.DBUtils;
import ambroafb.general.interfaces.DataChangeProvider;
import ambroafb.general.interfaces.EditorPanelable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author dkobuladze
 */
public class DocInOrderDataChangeProvider extends DataChangeProvider {

    private final String DELETE_PROCEDURE = "doc_delete";
    
    @Override
    public List<Doc> deleteOneFromDB(int recId) throws Exception {
        return callProcedure(Doc.class, DELETE_PROCEDURE, recId);
    }

    @Override
    public List<Doc> editOneToDB(EditorPanelable object) throws Exception {
        return saveOneToDB(object);
    }

    
    @Override
    public List<Doc> saveOneToDB(EditorPanelable object) throws Exception {
        ArrayList<Doc> result = new ArrayList<>();
        DocInOrder newComponent = (DocInOrder) object;
        if (newComponent.getDocs().isEmpty()){
            result = DBUtils.saveMonthlyAccrual(newComponent.docDateProperty().get());
        }
        else { //  შეიძლება აღარ გახდეს საჭირო. თუ Custom-ია მაშინ დიალოგიც custom-ის გაკეთდებოდა manager-ში.
            Doc parentDoc = newComponent.getDocs().stream().filter((doc) -> doc.isParentDoc()).findFirst().get();
            Doc newDocFromDB = DBUtils.saveCustomDoc(parentDoc);
            result.add(newDocFromDB);
        }
        return result;
    }
    
}
