/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general.interfaces;

import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author mambroladze
 */
public interface Dialogable {
    public EditorPanelable getResult();
    public Scene getScene();
    public void showAndWait();
    public void operationCanceled();
    public String getFullTitle();
//    public void setOwnerStage(Stage owner);
}
