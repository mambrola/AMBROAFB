/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.docs.types.doc_in_order.dialog;

import ambro.ADatePicker;
import ambroafb.docs.types.doc_in_order.DocInOrder;
import ambroafb.docs.types.doc_in_order.DocOrderComponent;
import ambroafb.general.editor_panel.EditorPanel;
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
public class DocInOrderDialogController extends DialogController {

    @FXML
    private VBox formPane;
    
    @FXML
    private ADatePicker docDate;
    
    @FXML
    private DialogOkayCancelController okayCancelController;
    
    @FXML
    private VBox listVBox;

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
            DocInOrder docInOrder = (DocInOrder)object;
            docDate.valueProperty().bindBidirectional(docInOrder.docDateProperty());
        }
    }

    @Override
    protected void makeExtraActions(EditorPanel.EDITOR_BUTTON_TYPE buttonType) {
        if (buttonType.equals(EditorPanel.EDITOR_BUTTON_TYPE.ADD)){
            formPane.getChildren().remove(1); // remove scrollpane.
        }
        else {
            ((DocInOrder)sceneObj).getDocs().stream().forEach((doc) -> {
                DocOrderComponent lsComp = new DocOrderComponent();
                lsComp.removeDocDateComponent();
                lsComp.binTo(doc);
                lsComp.setDialogType(buttonType);
                if ((buttonType.equals(EditorPanel.EDITOR_BUTTON_TYPE.ADD) || buttonType.equals(EditorPanel.EDITOR_BUTTON_TYPE.EDIT)) 
                    && !doc.isParentDoc()){
                    lsComp.setDialogType(EditorPanel.EDITOR_BUTTON_TYPE.VIEW);
                }
                lsComp.setDottedBorder('a');
                listVBox.getChildren().add(lsComp);
            });
        }
    }
    
    @Override
    public DialogOkayCancelController getOkayCancelController() {
        return okayCancelController;
    }
    
}
