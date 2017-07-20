/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.docs.dialog;

import ambroafb.docs.Doc;
import ambroafb.docs.DocType;
import ambroafb.docs.DocTypeComboBox;
import ambroafb.docs.types.DocDialogAbstraction;
import ambroafb.docs.types.DocDialogsFactory;
import ambroafb.general.Names;
import ambroafb.general.Utils;
import ambroafb.general.interfaces.Dialogable;
import ambroafb.general.okay_cancel.DialogOkayCancelController;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author dkobuladze
 */
public class DocDialogController implements Initializable {

    @FXML
    private VBox formPane;
    @FXML
    private DocTypeComboBox docTypes;
    @FXML
    private HBox concreteScene;
    
    @FXML
    private DialogOkayCancelController okayCancelController;
    
    private ArrayList<Node> focusTraversableNodes;

    private Doc doc, docBackup;
    private boolean permissionToClose;
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        focusTraversableNodes = Utils.getFocusTraversableBottomChildren(formPane);
        permissionToClose = true;
        
        if (docTypes.getValue() != null){
            int docTypeId = docTypes.getValue().getId();
            setConcreteNodeFrom(DocDialogsFactory.getDocDialogObject(docTypeId));
        }
        
        docTypes.valueProperty().addListener((ObservableValue<? extends DocType> observable, DocType oldValue, DocType newValue) -> {
            DocDialogAbstraction dda = DocDialogsFactory.getDocDialogObject(newValue.getId());
            setConcreteNodeFrom(dda);
            doc.setDialogAbstraction(dda);
        });
    }
    
    /**
     * Sets new node instead of old.
     * @param dda Abstraction that gives node object. The node object must draw on scene, according to value of DocTypesComboBox.
     */
    public void setConcreteNodeFrom(DocDialogAbstraction dda){
        List<Node> concreteDocDialogsNodes = concreteScene.getChildren();
        if (!concreteDocDialogsNodes.isEmpty()){
            concreteDocDialogsNodes.remove(0);
        }
        concreteDocDialogsNodes.add(dda.getSceneNode());
    }
    
    
    public void bindDoc(Doc doc) {
        this.doc = doc;
        if (doc != null){
            // binds
            
        }
    }

    public void setNextVisibleAndActionParameters(Names.EDITOR_BUTTON_TYPE buttonType) {
        if (buttonType.equals(Names.EDITOR_BUTTON_TYPE.VIEW) || buttonType.equals(Names.EDITOR_BUTTON_TYPE.DELETE)){
            setDisableComponents();
        }
        okayCancelController.setButtonsFeatures(buttonType);
    }
    
     /**
     * Disables all fields on Dialog stage.
     */
    private void setDisableComponents(){
        focusTraversableNodes.forEach((Node t) -> {
            t.setDisable(true);
        });
    }

//    public void setBackupDoc(Doc docBackup) {
//        this.docBackup = docBackup;
//    }
    
    public DialogOkayCancelController getOkayCancelController() {
        return okayCancelController;
    }
    
    public boolean anyComponentChanged(){
        return false;
//        return !doc.compares(docBackup);
    }
    
    public void operationCanceled(){
        ((Dialogable)formPane.getScene().getWindow()).operationCanceled();
    }
    
    public void changePermissionForClose(boolean value){
        permissionToClose = value;
    }
    
    public boolean getPermissionToClose(){
        return permissionToClose;
    }
}
