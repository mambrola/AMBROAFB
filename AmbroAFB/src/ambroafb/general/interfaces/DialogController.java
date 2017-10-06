/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general.interfaces;

import ambroafb.general.Names.EDITOR_BUTTON_TYPE;
import ambroafb.general.Utils;
import ambroafb.general.okay_cancel.DialogOkayCancelController;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;

/**
 *
 * @author dkobuladze
 */
public abstract class DialogController implements Initializable {
    
    private ArrayList<Node> focusTraversableNodes;
    
    private EditorPanelable sceneObj, backupObj;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        focusTraversableNodes = Utils.getFocusTraversableBottomChildren(getSceneRoot());
        componentsInitialize(url, rb);
    }
    
    protected abstract Parent getSceneRoot();
    
    protected abstract void componentsInitialize(URL url, ResourceBundle rb);
    
    public void setSceneData(EditorPanelable object, EDITOR_BUTTON_TYPE buttonType){
        sceneObj = object;
        backupObj = sceneObj.cloneWithID();
        
        if (buttonType.equals(EDITOR_BUTTON_TYPE.VIEW) || buttonType.equals(EDITOR_BUTTON_TYPE.DELETE)){
            focusTraversableNodes.forEach((Node t) -> { t.setDisable(true); });
        }
        getOkayCancelController().setButtonsFeatures(buttonType);
        
        bindObjectToSceneComonents(object);
    }
    
    public abstract DialogOkayCancelController getOkayCancelController();
    
    protected abstract void bindObjectToSceneComonents(EditorPanelable object);
    
    protected abstract void makeSceneFor(EDITOR_BUTTON_TYPE buttonType);
}