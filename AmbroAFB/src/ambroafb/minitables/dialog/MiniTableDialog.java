/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.minitables.dialog;

import ambroafb.general.Names;
import ambroafb.general.SceneUtils;
import ambroafb.general.interfaces.Dialogable;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.general.stages.UserInteractiveStage;
import ambroafb.minitables.MiniTable;
import ambroafb.minitables.buysells.BuySell;
import ambroafb.minitables.subjects.Subject;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author dato
 */
public class MiniTableDialog extends UserInteractiveStage implements Dialogable {

    private MiniTable miniTable;
    private final MiniTable miniTableBackup;
    
    private MiniTableDialogController dialogController;
    
    public MiniTableDialog(EditorPanelable object, Names.EDITOR_BUTTON_TYPE buttonType, Stage owner) {
        super(owner, Names.LEVEL_FOR_PATH, "", "/images/dialog.png");
        
        if (object == null)
            this.miniTable = getConcreteMinitableFrom(object);
        else
            this.miniTable = (MiniTable) object;
        this.miniTableBackup = (MiniTable)miniTable.cloneWithID();
        
        Scene currentScene = SceneUtils.createScene("/ambroafb/minitables/dialog/MiniTableDialog.fxml", null);
        dialogController = (MiniTableDialogController) currentScene.getProperties().get("controller");
        dialogController.bindSceneContentTo(this.miniTable);
        dialogController.setNextVisibleAndActionParameters(buttonType);
        dialogController.setBackup(this.miniTableBackup);
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
        return dialogController;
    }
    
    private MiniTable getConcreteMinitableFrom(EditorPanelable object) {
        MiniTable result = new BuySell();
        if (object instanceof Subject) {
            result = new Subject();
        }
        return result;
    }
}
