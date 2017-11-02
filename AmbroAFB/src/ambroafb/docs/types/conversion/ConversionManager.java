/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.docs.types.conversion;

import ambroafb.docs.types.conversion.dialog.ConversionDialog;
import ambroafb.general.Names;
import ambroafb.general.interfaces.Dialogable;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.general.interfaces.EditorPanelableManager;
import ambroafb.general.interfaces.Filterable;
import javafx.stage.Stage;

/**
 *
 * @author dkobuladze
 */
public class ConversionManager extends EditorPanelableManager {

//    private final String DB_DELETE_PROCEDURE_NAME = "doc_delete";
//    private final String DB_VIEW_NAME = "docs_whole";
    
    public ConversionManager(){
        dataFetchProvider = new ConversionDataFetchProvider();
        dataChangeProvider = new ConversionDataChangeProvider();
    }
    
//    @Override
//    public EditorPanelable getOneFromDB(int id) {
//        return null;
//        JSONObject params = new ConditionBuilder().where().orGroup().or("rec_id", "=", id).or("parent_rec_id", "=", id).closeGroup().condition().build();
//        ArrayList<Doc> bouquet = DBUtils.getObjectsListFromDB(Doc.class, DB_VIEW_NAME, params);
//        Conversion conversion = makeConversionFrom(bouquet);
//        return conversion;
//    }
//    
//    private Conversion makeConversionFrom(ArrayList<Doc> docs){
//        Conversion result = new Conversion();
//        Doc firstDoc = docs.get(0);
//        Doc secondDoc = docs.get(1);
//        result.setRecId(firstDoc.getRecId());
//        result.setDocDate(firstDoc.getDocDate());
//        result.setDocInDocDate(firstDoc.getDocInDocDate());
//        result.descripProperty().set(firstDoc.getDescrip());
//       
//        
//        result.setSellCurrency(firstDoc.getIso());
//        result.setSellAccount(firstDoc.debitProperty().get());
//        result.setSellAmount(firstDoc.getAmount());
//        
//        result.setBuyingCurrency(secondDoc.getIso());
//        result.setBuyingAccount(secondDoc.creditProperty().get());
//        result.setBuyingAmount(secondDoc.getAmount());
//        return result;
//    }
//    
//    @Override
//    public ArrayList<Doc> saveOneToDB(EditorPanelable newDocComponent) {
//        return DBUtils.saveConversionDoc((Conversion)newDocComponent);
//    }
//
//    @Override
//    public boolean deleteOneFromDB(int id) {
//        return DBUtils.deleteObjectFromDB(DB_DELETE_PROCEDURE_NAME, id);
//    }
//
//    @Override
//    public void undo() {
//    }
//
//    @Override
//    public Dialogable getDocDialogFor(Stage owner, Names.EDITOR_BUTTON_TYPE type, EditorPanelable object) {
//        ConversionDialog dialog = new ConversionDialog(object, type, owner);
//        return dialog;
//    }

    @Override
    public Dialogable getDialogFor(Stage owner, Names.EDITOR_BUTTON_TYPE type, EditorPanelable object) {
        ConversionDialog dialog = new ConversionDialog(object, type, owner);
        dialog.setDataChangeProvider(dataChangeProvider);
        dialog.setFrameFeatures(type, "doc_conversion_dialog_title");
        return dialog;
    }

    @Override
    public Filterable getFilterFor(Stage owner) {
        return null;
    }
    
}
