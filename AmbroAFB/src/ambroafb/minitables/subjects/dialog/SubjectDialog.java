/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.minitables.subjects.dialog;

import ambroafb.general.Names;
import ambroafb.general.SceneUtils;
import ambroafb.general.interfaces.Dialogable;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.general.interfaces.UserInteractiveStage;
import ambroafb.minitables.subjects.Subject;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author dato
 */
public class SubjectDialog extends UserInteractiveStage implements Dialogable {
    
    private Subject subject;
    private final Subject subjectBackup;
    
    private SubjectDialogController dialogController;
    
    public SubjectDialog(EditorPanelable object, Names.EDITOR_BUTTON_TYPE buttonType, Stage owner){
        super(owner, Names.LEVEL_FOR_PATH, "subject", "/images/dialog.png");
        
        if (object == null)
            this.subject = new Subject();
        else
            this.subject = (Subject) object;
        this.subjectBackup = subject.cloneWithID();
        
        Scene currentScene = SceneUtils.createScene("/ambroafb/minitables/subjects/dialog/SubjectDialog.fxml", null);
        dialogController = (SubjectDialogController) currentScene.getProperties().get("controller");
        dialogController.bindSubject(this.subject);
        dialogController.setNextVisibleAndActionParameters(buttonType);
        dialogController.setBackupSubject(this.subjectBackup);
        this.setScene(currentScene);
        
        onCloseRequestProperty().set((EventHandler<WindowEvent>) (WindowEvent event) -> {
            dialogController.getOkayCancelController().getCancelButton().getOnAction().handle(null);
            if (event != null) event.consume();
        });
        
    }
    
    @Override
    public EditorPanelable getResult() {
        showAndWait();
        return subject;
    }

    @Override
    public void operationCanceled() {
        subject = null;
    }
    
}
