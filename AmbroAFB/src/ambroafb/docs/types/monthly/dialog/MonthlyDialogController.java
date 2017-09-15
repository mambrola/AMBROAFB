/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.docs.types.monthly.dialog;

import ambro.ADatePicker;
import ambroafb.docs.types.monthly.DocListDialogSceneComponent;
import ambroafb.docs.types.monthly.Monthly;
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
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author dkobuladze
 */
public class MonthlyDialogController implements Initializable {

    @FXML
    private VBox formPane;
    
    @FXML
    private ADatePicker docDate;
    
    @FXML
    private DialogOkayCancelController okayCancelController;
    
    private  Monthly monthly, monthlyBackup;
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

    public void bindMonthly(Monthly monthly) {
        this.monthly = monthly;
        if (monthly != null){
            docDate.valueProperty().bindBidirectional(monthly.docDateProperty());
        }
    }

    public void setNextVisibleAndActionParameters(Names.EDITOR_BUTTON_TYPE buttonType) {
      if (buttonType.equals(Names.EDITOR_BUTTON_TYPE.VIEW) || buttonType.equals(Names.EDITOR_BUTTON_TYPE.DELETE)){
            setDisableComponents();
        }
        okayCancelController.setButtonsFeatures(buttonType);
        monthly.getDocs().stream().forEach((doc) -> {
            DocListDialogSceneComponent lsComp = new DocListDialogSceneComponent();
            lsComp.removeDocDateComponent();
            lsComp.binTo(doc);
            formPane.getChildren().add(1, lsComp);
        });
    }
    
    private void setDisableComponents(){
        focusTraversableNodes.forEach((Node t) -> {
            t.setDisable(true);
        });
    }

    public void setBackupCharge(Monthly monthlyBackup) {
        this.monthlyBackup = monthlyBackup;
    }
    
    public boolean anyComponentChanged(){
        return !monthly.compares(monthlyBackup);
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
