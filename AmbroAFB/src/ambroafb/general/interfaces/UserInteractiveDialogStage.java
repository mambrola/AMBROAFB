/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general.interfaces;

import ambroafb.general.AlertMessage;
import ambroafb.general.AnnotiationUtils;
import ambroafb.general.GeneralConfig;
import ambroafb.general.Names;
import ambroafb.general.SceneUtils;
import ambroafb.general.editor_panel.EditorPanel;
import java.util.function.Consumer;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author dkobuladze
 */
public abstract class UserInteractiveDialogStage extends UserInteractiveStage implements DialogCloseObserver {
    
    protected DialogController dialogController;
    protected DataChangeProvider dataChangeProvider;
    protected DataFetchProvider dataFetchProvider;
    
    private boolean permissionToClose = true;
    private EditorPanel.EDITOR_BUTTON_TYPE editorButtonType;
    private Consumer<Object> closeAction, editAction, addAction;
    
    public UserInteractiveDialogStage(Stage owner, String sceneFXMLFilePath, String stageTitleBundleKey){
        super(owner, Names.LEVEL_FOR_PATH, stageTitleBundleKey, "/images/dialog.png");
        
        Scene currentScene = SceneUtils.createScene(sceneFXMLFilePath, null);
        setScene(currentScene);
        dialogController = (DialogController) currentScene.getProperties().get("controller");
        dialogController.getOkayCancelController().registerObserver(this);
        
        onCloseRequestProperty().set((EventHandler<WindowEvent>) (WindowEvent event) -> {
            dialogController.getOkayCancelController().getCancelButton().getOnAction().handle(null);
            if (event != null) event.consume();
        });
        
        closeAction = (Object t) -> close();
        Consumer<Object> editFn = (Object obj) -> getSceneObject().copyFrom((EditorPanelable)obj);
        Consumer<Object> addFn = (Object obj) -> {
                                        getSceneObject().setRecId(((EditorPanelable)obj).getRecId());
                                        getSceneObject().copyFrom((EditorPanelable)obj);
                                    };
        editAction = editFn.andThen(closeAction);
        addAction = addFn.andThen(closeAction);
    }
    
    public UserInteractiveDialogStage(Stage owner, EditorPanel.EDITOR_BUTTON_TYPE buttonType, String sceneFXMLFilePath){
        this(owner, sceneFXMLFilePath, "");
        
        editorButtonType = buttonType;
    }
    
    /**
     * The method sets icon and title for stage according to type.
     * @param type The dialog type.
     * @param classNameBundleKey The object bundle key that dialog it is.
     */
    public void setFrameFeatures(EditorPanel.EDITOR_BUTTON_TYPE type, String classNameBundleKey){
        String titleByTypeBundleKey;
        String iconPath = "/images/";
        switch (type){
            case ADD:
                titleByTypeBundleKey = "new";
                iconPath += "new.png";
                break;
            case ADD_BY_SAMPLE:
                titleByTypeBundleKey = type.name().toLowerCase();
                iconPath += "clone.png";
                break;
            default:
                titleByTypeBundleKey = type.name().toLowerCase();
                iconPath += type.name().toLowerCase() + ".png";
                break;
        }
        String titleByType = GeneralConfig.getInstance().getTitleFor(titleByTypeBundleKey);
        String titleByClass = GeneralConfig.getInstance().getTitleFor(classNameBundleKey);
        setTitle(titleByClass + ":  " + titleByType);

        this.getIcons().add(new Image(iconPath));
    }
    
    public void setDataChangeProvider(DataChangeProvider provider){
        this.dataChangeProvider = provider;
    }
    
    public void setDataFetchProvider(DataFetchProvider provider){
        this.dataFetchProvider = provider;
    }
    
    public DataFetchProvider getDataFetchProvider(){
        return dataFetchProvider;
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
    @Override
    public final void okayAction(){
        if (dataChangeProvider != null){
            switch(editorButtonType){
                case DELETE:
                    String alertText = GeneralConfig.getInstance().getTitleFor("dialog_delete_confirm");
                    if (new AlertMessage(this, Alert.AlertType.CONFIRMATION, alertText, "").showAndWait().get().equals(ButtonType.OK))
                        dataChangeProvider.deleteOneFromDB(getSceneObject().getRecId(), builDeleteSuccessAction(), getErrorAction());
                    break;
                case EDIT: 
                case ADD:
                case ADD_BY_SAMPLE:
                    boolean allRequiredFieldsAreValid = AnnotiationUtils.everyFieldContentIsValidFor(dialogController, editorButtonType);
                    System.out.println("allRequiredFieldsAreValid: " + allRequiredFieldsAreValid);
                    if (allRequiredFieldsAreValid){
                        if (editorButtonType.equals(EditorPanel.EDITOR_BUTTON_TYPE.EDIT))
                            dataChangeProvider.editOneToDB(getSceneObject(), builEditSuccessAction(), getErrorAction());
                        else 
                            dataChangeProvider.saveOneToDB(getSceneObject(), builAddSuccessAction(), getErrorAction());
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
    
    private Consumer<Object> builDeleteSuccessAction(){
        Consumer<Object> successFn = getDeleteSuccessAction();
        return (successFn == null) ? closeAction : successFn.andThen(closeAction);
    }
    
    private Consumer<Object> builEditSuccessAction(){
        Consumer<Object> successFn = getEditSuccessAction();
        return (successFn == null) ? editAction : successFn.andThen(closeAction);
    }
    
    private Consumer<Object> builAddSuccessAction(){
        Consumer<Object> successFn = getAddSuccessAction();
        return (successFn == null) ? addAction : successFn.andThen(closeAction);
    }
    
    /**
     *  The function will execute before stage close, if DB action was successful.
     * @return The action that will execute if get success  from DB.
     */
    protected Consumer<Object> getDeleteSuccessAction(){
        return null;
    }
    
    /**
     *  The function will execute before stage close, if DB action was successful.
     * @return The action that will execute if get success  from DB.
     */
    protected Consumer<Object> getEditSuccessAction(){
        return null;
    }
    
    /**
     *  The function will execute before stage close, if DB action was successful.
     * @return The action that will execute if get success  from DB.
     */
    protected Consumer<Object> getAddSuccessAction(){
        return null;
    }
    
    /**
     *  The function will execute before stage close, if DB action was not successful.
     * @return The action that will execute if get error from DB.
     */
    protected Consumer<Exception> getErrorAction(){
        return  (ex) -> {
                    new AlertMessage(this, Alert.AlertType.ERROR, getTitle(), ex.getLocalizedMessage(), ex).showAndWait();
                };
    }
    
    /**
     * According to dialog type (DELETE, EDIT, VIEW, ADD, ADD_SMAPLE), the method execute cancel actions.
     */
    @Override
    public final void cancelAction(){
        switch(editorButtonType){
            case EDIT: 
            case ADD:
            case ADD_BY_SAMPLE:
                boolean anyFieldWasChanged = anyComponentChanged();
                if (anyFieldWasChanged) {
                    String alertText = GeneralConfig.getInstance().getTitleFor("dialog_cancel_confirm");
                    AlertMessage alert = new AlertMessage(this, Alert.AlertType.CONFIRMATION, alertText, "");
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
    
    
    public abstract void operationCanceled();
    
    protected abstract EditorPanelable getSceneObject();
    
}
