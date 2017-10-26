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
import authclient.AuthServerException;
import java.io.IOException;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    private Names.EDITOR_BUTTON_TYPE editorButtonType;
    private DataChangeProvider dataChangeProvider;
    Consumer<Object> closeFn;
    
    public UserInteractiveDialogStage(Stage owner, String sceneFXMLFilePath, String stageTitleBundleKey){
        super(owner, Names.LEVEL_FOR_PATH, stageTitleBundleKey, "/images/dialog.png");
        
        Scene currentScene = SceneUtils.createScene(sceneFXMLFilePath, null);
        setScene(currentScene);
        dialogController = (DialogController) currentScene.getProperties().get("controller");
        
        onCloseRequestProperty().set((EventHandler<WindowEvent>) (WindowEvent event) -> {
            dialogController.getOkayCancelController().getCancelButton().getOnAction().handle(null);
            if (event != null) event.consume();
        });
        
        closeFn = (Object t) -> {
            close();
        };
    }
    
    public UserInteractiveDialogStage(Stage owner, Names.EDITOR_BUTTON_TYPE buttonType, String sceneFXMLFilePath, String stageTitleBundleKey){
        this(owner, sceneFXMLFilePath, stageTitleBundleKey);
        
        editorButtonType = buttonType;
    }
    
    public void setDataChangeProvider(DataChangeProvider provider){
        this.dataChangeProvider = provider;
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
    
    /**
     *  According to dialog type (DELETE, EDIT, VIEW, ADD, ADD_SMAPLE), the method execute okay actions.
     */
    public final void okayAction(){
        if (dataChangeProvider != null){
            switch(editorButtonType){
                case DELETE:
                    String alertText = GeneralConfig.getInstance().getTitleFor("dialog_delete_confirm");
                    if (new AlertMessage(Alert.AlertType.CONFIRMATION, null, alertText, "").showAndWait().get().equals(ButtonType.OK)){
                    try {
                        dataChangeProvider.deleteOneFromDB(getSceneObject().getRecId());
                    } catch (IOException | AuthServerException ex) {
                        Logger.getLogger(UserInteractiveDialogStage.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    }
                    break;
                case EDIT: 
                    {
                        try {
                            dataChangeProvider.editOneToDB(getSceneObject());
                        } catch (IOException | AuthServerException ex) {
                            Logger.getLogger(UserInteractiveDialogStage.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    break;
                case ADD:
                case ADD_SAMPLE:
                    {
                        try {
                            dataChangeProvider.saveOneToDB(getSceneObject());
                        } catch (IOException | AuthServerException ex) {
                            Logger.getLogger(UserInteractiveDialogStage.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    break;
                case VIEW:
                    close();
                    break;
                default:
                    break;
            }
        }
    }
    
    private Consumer<Object> builSuccessFunction(){
        Consumer<Object> successFn = getSuccessFunction();
        return (successFn == null) ? closeFn : successFn.andThen(closeFn);
    }
    
    /**
     *  The function will execute before stage close, if DB action was successful.
     * @return 
     */
    protected Consumer<Object> getSuccessFunction(){
        return null;
    }
    
    /**
     *  The function will execute before stage close, if DB action was not successful.
     * @return 
     */
    protected Consumer<Exception> getErrorFunction(){
        return  (ex) -> {
                    new AlertMessage(Alert.AlertType.ERROR, ex, ex.getMessage(), getTitle()).showAndWait();
                };
    }
    
    /**
     * According to dialog type (DELETE, EDIT, VIEW, ADD, ADD_SMAPLE), the method execute cancel actions.
     */
    public final void cancelAction(){
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
            case DELETE: 
            case VIEW:
                operationCanceled();
                close();
            default:
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
