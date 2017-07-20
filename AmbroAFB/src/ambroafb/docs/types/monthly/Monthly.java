/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.docs.types.monthly;

import ambroafb.docs.types.DocDialogAbstraction;
import ambroafb.docs.types.SceneWithVBoxRoot;
import javafx.scene.Node;
import org.json.JSONObject;

/**
 *
 * @author dkobuladze
 */
public class Monthly extends SceneWithVBoxRoot implements DocDialogAbstraction {

    public Monthly() {
        super("/ambroafb/docs/types/monthly/Monthly.fxml");
    }

    @Override
    public Node getSceneNode() {
        return this;
    }

    @Override
    public JSONObject getResult() {
        return null;
    }

    @Override
    public void cancel() {
        System.out.println("Monthly cancel method");
    }
    
}
