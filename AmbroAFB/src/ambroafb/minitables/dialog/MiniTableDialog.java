/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.minitables.dialog;

import ambroafb.general.Names;
import ambroafb.general.SceneUtils;
import ambroafb.general.interfaces.DialogController;
import ambroafb.general.interfaces.Dialogable;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.general.interfaces.UserInteractiveDialogStage;
import ambroafb.minitables.MiniTable;
import ambroafb.minitables.attitudes.Attitude;
import ambroafb.minitables.merchandises.Merchandise;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author dato
 */
public class MiniTableDialog extends UserInteractiveDialogStage implements Dialogable {

    private MiniTable miniTable;
    private final MiniTable miniTableBackup;
    
    private DialogController dialogController;
    
    public MiniTableDialog(EditorPanelable object, Names.EDITOR_BUTTON_TYPE buttonType, Stage owner) {
        super(owner, "");
        
        if (object == null)
            this.miniTable = getConcreteMinitableFrom(object);
        else
            this.miniTable = (MiniTable) object;
        this.miniTableBackup = (MiniTable)miniTable.cloneWithID();
        
        Scene currentScene = SceneUtils.createScene("/ambroafb/minitables/dialog/MiniTableDialog.fxml", null);
        dialogController = (MiniTableDialogController) currentScene.getProperties().get("controller");
        dialogController.setSceneData(miniTable, miniTableBackup, buttonType);
        this.setScene(currentScene);
        
        onCloseRequestProperty().set((EventHandler<WindowEvent>) (WindowEvent event) -> {
            dialogController.getOkayCancelController().getCancelButton().getOnAction().handle(null);
            if (event != null) event.consume();
        });
    }
    
    @Override
    public EditorPanelable getResult() {
        showAndWait();
        return miniTable;
    }

    @Override
    public void operationCanceled() {
        miniTable = null;
    }
    
    public MiniTableDialogController getDialogController(){
        return (MiniTableDialogController)dialogController;
    }
    
    private MiniTable getConcreteMinitableFrom(EditorPanelable object) {
        MiniTable result = new Attitude();
        if (object instanceof Merchandise) {
            result = new Merchandise();
        }
        return result;
    }

    @Override
    public boolean anyComponentChanged() {
        return dialogController.anySceneComponentChanged();
    }
}
