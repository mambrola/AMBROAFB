/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general.interfaces;

import ambroafb.general.Utils;
import ambroafb.general.editor_panel.EditorPanel.EDITOR_BUTTON_TYPE;
import ambroafb.general.okay_cancel.DialogOkayCancelController;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;

/**
 *
 * @author dkobuladze
 */
public abstract class DialogController implements Initializable {
    
    private ArrayList<Node> focusTraversableNodes;
    
    protected EditorPanelable sceneObj, backupObj;
    
    @FXML
    protected DialogOkayCancelController okayCancelController;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        focusTraversableNodes = Utils.getFocusTraversableBottomChildren(getSceneRoot());
        componentsInitialize(url, rb);
    }
    
    protected abstract Parent getSceneRoot();
    
    protected abstract void componentsInitialize(URL url, ResourceBundle rb);
    
    public void setSceneData(EditorPanelable sceneObject, EditorPanelable backupObject, EDITOR_BUTTON_TYPE buttonType){
        this.sceneObj = sceneObject;
        this.backupObj = backupObject;
        
        bindObjectToSceneComponents(sceneObject);
        makeSceneFor(buttonType);
    }
    
    private void makeSceneFor(EDITOR_BUTTON_TYPE buttonType) {
        if (buttonType.equals(EDITOR_BUTTON_TYPE.VIEW) || buttonType.equals(EDITOR_BUTTON_TYPE.DELETE)){
            focusTraversableNodes.forEach((Node t) -> { t.setDisable(true); });
        }
        if (buttonType.equals(EDITOR_BUTTON_TYPE.VIEW)){
            getOkayCancelController().visibleCancel(false);
        }
        makeExtraActions(buttonType);
    }
    
    protected abstract void bindObjectToSceneComponents(EditorPanelable object);
    protected abstract void makeExtraActions(EDITOR_BUTTON_TYPE buttonType);
    
    protected void removeBinds(){}
    protected void removeListeners(){}
    
    
    public DialogOkayCancelController getOkayCancelController(){
        return okayCancelController;
    }
    
    public boolean anySceneComponentChanged() {
        return !sceneObj.compares(backupObj);
    }
}
