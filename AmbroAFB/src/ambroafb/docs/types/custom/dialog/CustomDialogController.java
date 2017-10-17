/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.docs.types.custom.dialog;

import ambroafb.docs.Doc;
import ambroafb.docs.types.doc_in_order.DocOrderComponent;
import ambroafb.general.Names;
import ambroafb.general.interfaces.DialogController;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.general.okay_cancel.DialogOkayCancelController;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author dkobuladze
 */
public class CustomDialogController extends DialogController {

    @FXML
    private VBox formPane;
    
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
    }

    @Override
    protected void makeExtraActions(EditorPanelable sceneObject, Names.EDITOR_BUTTON_TYPE buttonType) {
        DocOrderComponent lsComp = new DocOrderComponent();
        lsComp.binTo((Doc)sceneObject);
        lsComp.setDialogType(buttonType);
        formPane.getChildren().add(0, lsComp);
    }
    
    @Override
    public DialogOkayCancelController getOkayCancelController() {
        return okayCancelController;
    }

}
