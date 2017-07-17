/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.docs.types;

import javafx.scene.Node;
import org.json.JSONObject;

/**
 *
 * @author dkobuladze
 */
public interface DocDialogAbstraction {
    
    public Node getSceneNode();
    public JSONObject getResult();
    public void cancel();
    
}
