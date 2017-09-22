/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.docs.types.doc_in_order.dialog;

import ambro.ADatePicker;
import ambroafb.docs.types.doc_in_order.DocInOrder;
import ambroafb.docs.types.doc_in_order.DocOrderDialogSceneComponent;
import ambroafb.general.Names;
import ambroafb.general.Utils;
import ambroafb.general.interfaces.Dialogable;
import ambroafb.general.okay_cancel.DialogOkayCancelController;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author dkobuladze
 */
public class DocInOrderDialogController implements Initializable {

    @FXML
    private VBox formPane;
    
    @FXML
    private ADatePicker docDate;
    
    @FXML
    private DialogOkayCancelController okayCancelController;
    
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox listVBox;
    
    private  DocInOrder docInOrder, docInOrderBackup;
    private ArrayList<Node> focusTraversableNodes;
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
    }    

    public void bindMonthly(DocInOrder docInOrder) {
        this.docInOrder = docInOrder;
        if (docInOrder != null){
            docDate.valueProperty().bindBidirectional(docInOrder.docDateProperty());
        }
    }

    public void setNextVisibleAndActionParameters(Names.EDITOR_BUTTON_TYPE buttonType) {
        if (buttonType.equals(Names.EDITOR_BUTTON_TYPE.VIEW) || buttonType.equals(Names.EDITOR_BUTTON_TYPE.DELETE)){
            setDisableComponents();
        }
        if (buttonType.equals(Names.EDITOR_BUTTON_TYPE.ADD)){
            formPane.getChildren().remove(1);
        }
        else {
            docInOrder.getDocs().stream().forEach((doc) -> {
                DocOrderDialogSceneComponent lsComp = new DocOrderDialogSceneComponent();
                lsComp.removeDocDateComponent();
                lsComp.binTo(doc);
                lsComp.setDiableComponents(buttonType);
                if ((buttonType.equals(Names.EDITOR_BUTTON_TYPE.ADD) || buttonType.equals(Names.EDITOR_BUTTON_TYPE.EDIT)) 
                    && !doc.isParentDoc()){
                    lsComp.setDiableComponents(Names.EDITOR_BUTTON_TYPE.VIEW);
                }
                lsComp.setDottedBorder('a');
                listVBox.getChildren().add(lsComp);
            });
        }
        okayCancelController.setButtonsFeatures(buttonType);
    }
    
    private void setDisableComponents(){
        focusTraversableNodes.forEach((Node t) -> {
            t.setDisable(true);
        });
    }

    public void setBackupCharge(DocInOrder docInOrderBackup) {
        this.docInOrderBackup = docInOrderBackup;
    }
    
    public boolean anyComponentChanged(){
        return !docInOrder.compares(docInOrderBackup);
    }
    
    public void changePermissionForClose(boolean value){
        permissionToClose = value;
    }
    
    public boolean getPermissionToClose(){
        return permissionToClose;
    }
    
    public void operationCanceled(){
        ((Dialogable)formPane.getScene().getWindow()).operationCanceled();
    }
    
    public DialogOkayCancelController getOkayCancelController() {
        return okayCancelController;
    }
    
}
