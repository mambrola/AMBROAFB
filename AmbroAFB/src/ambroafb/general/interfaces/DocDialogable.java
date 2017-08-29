/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general.interfaces;

import javafx.scene.Scene;

/**
 *
 * @author dkobuladze
 */
public interface DocDialogable {
//    public DataDistributor getResult();
    public EditorPanelable getResult();
    public Scene getScene();
    public void showAndWait();
}
