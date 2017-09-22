/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.docs.types.conversion.dialog;

import ambroafb.docs.types.conversion.Conversion;
import ambroafb.general.Names;
import ambroafb.general.SceneUtils;
import ambroafb.general.interfaces.Dialogable;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.general.interfaces.UserInteractiveStage;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author dkobuladze
 */
public class ConversionDialog extends UserInteractiveStage implements Dialogable {

    private Conversion conversion, conversionBackup;
    
    private ConversionDialogController dialogController;
    
    public ConversionDialog(EditorPanelable object, Names.EDITOR_BUTTON_TYPE buttonType, Stage owner) {
        super(owner, Names.LEVEL_FOR_PATH, "doc_conversion_dialog_title", "/images/dialog.png");
        
        if (object == null){
            conversion = new Conversion();
        }
        else {
            conversion = (Conversion)object;
        }
        conversionBackup = conversion.cloneWithID();
        
        Scene currentScene = SceneUtils.createScene("/ambroafb/docs/types/conversion/dialog/ConversionDialog.fxml", null);
        dialogController = (ConversionDialogController) currentScene.getProperties().get("controller");
        dialogController.bindObject(this.conversion); // this must be before of setNextVisibleAndActionParameters() method, because of sets items in phonelist.
        dialogController.setNextVisibleAndActionParameters(buttonType);
        dialogController.setBackupDoc(this.conversionBackup);
        this.setScene(currentScene);
        
        onCloseRequestProperty().set((EventHandler<WindowEvent>) (WindowEvent event) -> {
            dialogController.getOkayCancelController().getCancelButton().getOnAction().handle(null);
            if (event != null) event.consume();
        });
    }

    @Override
    public EditorPanelable getResult() {
        showAndWait();
        return conversion;
    }

    @Override
    public void operationCanceled() {
        conversion = null;
    }
    
}
