/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.minitables.merchandises.dialog;

import ambroafb.general.editor_panel.EditorPanel;
import ambroafb.general.interfaces.DialogController;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.minitables.merchandises.Merchandise;
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
public class MerchandiseDialogController extends DialogController {

     @FXML
    private VBox formPane;
    @FXML
    private TextField descrip;
    
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
            Merchandise merchandise = (Merchandise)object;
            descrip.textProperty().bindBidirectional(merchandise.descripProperty());
        }
    }

    @Override
    protected void makeExtraActions(EditorPanel.EDITOR_BUTTON_TYPE buttonType) {
        
    }

}
