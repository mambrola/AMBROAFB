/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.docs.types.custom.dialog;

import ambroafb.docs.Doc;
import ambroafb.docs.types.doc_in_order.DocOrderComponent;
import ambroafb.general.editor_panel.EditorPanel;
import ambroafb.general.interfaces.DialogController;
import ambroafb.general.interfaces.EditorPanelable;
import java.net.URL;
import java.time.LocalDate;
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
    protected void makeExtraActions(EditorPanel.EDITOR_BUTTON_TYPE buttonType) {
        DocOrderComponent lsComp = new DocOrderComponent();
        lsComp.bindBidirectTo((Doc)sceneObj);
        lsComp.setDialogType(buttonType);
        formPane.getChildren().add(0, lsComp);
        
        if (buttonType.equals(EditorPanel.EDITOR_BUTTON_TYPE.ADD_BY_SAMPLE)){
            ((Doc)sceneObj).setDocDate(LocalDate.now().toString());
            ((Doc)sceneObj).setDocInDocDate(LocalDate.now().toString());
            ((Doc)sceneObj).amountProperty().set(""); // for empty amount field.
            backupObj.copyFrom(sceneObj);
        }
    }
    

}
