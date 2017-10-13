/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general.interfaces;

import ambroafb.general.Names;
import ambroafb.general.SceneUtils;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author dkobuladze
 */
public abstract class UserInteractiveDialogStage extends UserInteractiveStage {
    
    protected DialogController dialogController;

    private boolean permissionToClose = true;
    
    public UserInteractiveDialogStage(Stage owner, String sceneFXMLFilePath, String stageTitleBundleKey){
        super(owner, Names.LEVEL_FOR_PATH, stageTitleBundleKey, "/images/dialog.png");
        
        Scene currentScene = SceneUtils.createScene(sceneFXMLFilePath, null);
        setScene(currentScene);
        dialogController = (DialogController) currentScene.getProperties().get("controller");
        
        onCloseRequestProperty().set((EventHandler<WindowEvent>) (WindowEvent event) -> {
            dialogController.getOkayCancelController().getCancelButton().getOnAction().handle(null);
            if (event != null) event.consume();
        });
    }
    
    public void changePermissionForClose(boolean value){
        permissionToClose = value;
    }
    
    public boolean getPermissionToClose(){
        return permissionToClose;
    }
    
    public boolean anyComponentChanged(){
        return dialogController.anySceneComponentChanged();
    }
}
