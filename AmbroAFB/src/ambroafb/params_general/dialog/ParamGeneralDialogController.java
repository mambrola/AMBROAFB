/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.params_general.dialog;

import ambroafb.general.editor_panel.EditorPanel;
import ambroafb.general.interfaces.DialogController;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.general.okay_cancel.DialogOkayCancelController;
import ambroafb.minitables.attitudes.AttitudeComboBox;
import ambroafb.minitables.merchandises.MerchandiseComboBox;
import ambroafb.params_general.ParamGeneral;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author dato
 */
public class ParamGeneralDialogController extends DialogController {

    @FXML
    private VBox formPane;
    @FXML 
    private AttitudeComboBox attitudes;
    @FXML 
    private MerchandiseComboBox merchandises;
    @FXML 
    private TextField paramType;
    @FXML 
    private TextField param;
    @FXML
    private DialogOkayCancelController okayCancelController;

    @Override
    protected void componentsInitialize(URL url, ResourceBundle rb) {

    }

    @Override
    protected Parent getSceneRoot() {
        return formPane;
    }

    @Override
    protected void bindObjectToSceneComponents(EditorPanelable object) {
        if (object != null){
            ParamGeneral paramGeneral = (ParamGeneral)object;
            attitudes.valueProperty().bindBidirectional(paramGeneral.attitudeProperty());
            merchandises.valueProperty().bindBidirectional(paramGeneral.merchandiseProperty());
            paramType.textProperty().bindBidirectional(paramGeneral.paramTypeProperty());
            param.textProperty().bindBidirectional(paramGeneral.paramProperty());
        }
    }

    @Override
    protected void makeExtraActions(EditorPanel.EDITOR_BUTTON_TYPE buttonType) {
        
    }
    
    @Override
    public DialogOkayCancelController getOkayCancelController() {
        return okayCancelController;
    }
    
}
