/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general.interfaces;

import ambroafb.general.AlertMessage;
import ambroafb.general.GeneralConfig;
import ambroafb.general.Names;
import ambroafb.general.SceneUtils;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author dkobuladze
 */
public abstract class UserInteractiveDialogStage extends UserInteractiveStage {
    
    protected DialogController dialogController;

    private boolean permissionToClose = true;
    
    protected Names.EDITOR_BUTTON_TYPE editorButtonType;
    protected DataProvider dataProvider;
    
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
    
    public UserInteractiveDialogStage(Stage owner, Names.EDITOR_BUTTON_TYPE buttonType, String sceneFXMLFilePath, String stageTitleBundleKey){
        this(owner, sceneFXMLFilePath, stageTitleBundleKey);
        
        editorButtonType = buttonType;
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
    
    public void cancelAction(){
        switch(editorButtonType){
            case EDIT: 
            case ADD:
            case ADD_SAMPLE:
                boolean anyFieldWasChanged = anyComponentChanged();
                if (anyFieldWasChanged) {
                    String alertText = GeneralConfig.getInstance().getTitleFor("dialog_cancel_confirm");
                    AlertMessage alert = new AlertMessage(Alert.AlertType.CONFIRMATION, null, alertText, getTitle());
                    alert.setOwner(this);
                    ButtonType buttonType = alert.showAndWait().get();
                    if (buttonType.equals(ButtonType.OK)){
                        operationCanceled();
                        changePermissionForClose(true);
                        close();
                    }
                    else{
                        changePermissionForClose(false);
                    }
                }else{ // This case is needed. If nothing change the real object must become null.
                    operationCanceled();
                    changePermissionForClose(true);
                    close();
                }
                break;
            default: // DELETE, VIEW
                operationCanceled();
                close();
                break;
        }
    }
    
    
    ////////////////////////////// ---------------------------------------------------------------------------------------- abstract
    public void operationCanceled(){
        System.out.println("--- UserInteractive Op Cancel ---");
    }
    
    ////////////////////////////// ---------------------------------------------------------------------------------------- abstract
    protected EditorPanelable getSceneObject(){
        System.out.println("--- UserInteractive getSceneObject ---");
        
        return null;
    }
    
}
