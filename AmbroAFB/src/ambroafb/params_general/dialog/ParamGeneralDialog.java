/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.params_general.dialog;

import ambroafb.general.editor_panel.EditorPanel;
import ambroafb.general.interfaces.Dialogable;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.general.interfaces.UserInteractiveDialogStage;
import ambroafb.params_general.ParamGeneral;
import authclient.db.ConditionBuilder;
import authclient.db.WhereBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import javafx.stage.Stage;
import org.json.JSONObject;

/**
 *
 * @author dato
 */
public class ParamGeneralDialog extends UserInteractiveDialogStage implements Dialogable {
    
    private ParamGeneral paramGeneral;
    private final ParamGeneral paramGeneralBackup;
    
    public ParamGeneralDialog(EditorPanelable object, EditorPanel.EDITOR_BUTTON_TYPE buttonType, Stage owner){
        super(owner, buttonType, "/ambroafb/params_general/dialog/ParamGeneralDialog.fxml");
        
        if (object == null)
            paramGeneral = new ParamGeneral();
        else
            paramGeneral = (ParamGeneral) object;
        this.paramGeneralBackup = paramGeneral.cloneWithID();
        
        dialogController.setSceneData(paramGeneral, paramGeneralBackup, buttonType);
    }

    @Override
    public ParamGeneral getResult() {
        showAndWait();
        return paramGeneral;
    }

    @Override
    public void operationCanceled() {
        paramGeneral = null;
    }

    @Override
    protected EditorPanelable getSceneObject() {
        return paramGeneral;
    }

    @Override
    protected Consumer<Object> getAddSuccessAction() {
        return (obj) -> paramGeneral = (ParamGeneral)obj;
    }

//    @Override
//    protected Consumer<Exception> getErrorAction() {
//        return (ex) -> {
//            if (ex instanceof DBActionException){
//                DBActionException dbEx = (DBActionException)ex;
//                String msg = dbEx.getLocalizedMessage();
//                if (dbEx.getCode() == 4042){ // conflicted entry
//                    String startStr = ": ";
//                    int startIndex = msg.indexOf(startStr) + startStr.length();
//                    int endIndex = msg.indexOf(";");
//                    String[] ids = msg.substring(startIndex, endIndex).split(",");
//                    String headerTxt = GeneralConfig.getInstance().getTitleFor("param_general_error");
//                    List<ParamGeneral> entries = new ArrayList<>(); // selectConflictedEntries(ids);
//                    String newMsg = entries.stream().map((entry) -> "[" + entry.toString() + "]" + ",\n").reduce("", String::concat);
//                    new AlertMessage(this, Alert.AlertType.ERROR, headerTxt, newMsg, ex).showAndWait();
//                }
//            }
//        };
//    }
    
    // კარგი იქნება თუ ex ექნება ინფორმაცია არა მარტო რომელ id-ებთან მოხდა კონფლიქტი არამედ ასეთი ParamGeneral-ების სრულ ინფორმაციასთან.
    // მაშინ ხელმეორედ კითხვა აღარ დაგვჭირდება.
    private List<ParamGeneral> selectConflictedEntries(String[] ids){
        WhereBuilder whereBuilder = new ConditionBuilder().where();
        for (int i = 0; i < ids.length - 1; i++) {
            whereBuilder.or("rec_id", "=", ids[i]);
        }
        JSONObject conflictedIDs = whereBuilder.condition().build();
//        try {
//            return getObjectsListFromDBProcedure(ParamGeneral.class, DB_SELECT_PROC_NAME, conflictedIDs);
//        } catch (Exception ex) {
//        }
        return new ArrayList<>();
    }

    
}
