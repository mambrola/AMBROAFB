/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.minitables.merchandises.dialog;

import ambroafb.general.Names;
import ambroafb.general.SceneUtils;
import ambroafb.general.interfaces.Dialogable;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.general.interfaces.UserInteractiveStage;
import ambroafb.minitables.merchandises.Merchandise;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author dato
 */
public class MerchandiseDialog extends UserInteractiveStage implements Dialogable {
    
    private Merchandise merchandise;
    private final Merchandise merchandiseBackup;
    
    private MerchandiseDialogController dialogController;
    
    public MerchandiseDialog(EditorPanelable object, Names.EDITOR_BUTTON_TYPE buttonType, Stage owner){
        super(owner, Names.LEVEL_FOR_PATH, "merchandise", "/images/dialog.png");
        
        if (object == null)
            this.merchandise = new Merchandise();
        else
            this.merchandise = (Merchandise) object;
        this.merchandiseBackup = merchandise.cloneWithID();
        
        Scene currentScene = SceneUtils.createScene("/ambroafb/minitables/merchandises/dialog/MerchandiseDialog.fxml", null);
        dialogController = (MerchandiseDialogController) currentScene.getProperties().get("controller");
        dialogController.bindMerchandise(this.merchandise);
        dialogController.setNextVisibleAndActionParameters(buttonType);
        dialogController.setBackupMerchandise(this.merchandiseBackup);
        this.setScene(currentScene);
        
        onCloseRequestProperty().set((EventHandler<WindowEvent>) (WindowEvent event) -> {
            dialogController.getOkayCancelController().getCancelButton().getOnAction().handle(null);
            if (event != null) event.consume();
        });
        
    }
    
    @Override
    public EditorPanelable getResult() {
        showAndWait();
        return merchandise;
    }

    @Override
    public void operationCanceled() {
        merchandise = null;
    }
    
}
