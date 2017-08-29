/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.docs.types;

import ambroafb.AmbroAFB;
import ambroafb.general.GeneralConfig;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;

/**
 *
 * @author dkobuladze
 */
public class SceneWithVBoxRoot extends VBox {
    
    public SceneWithVBoxRoot(){
    
    }
    
    /**
     * The method finds appropriate FXML and set root on it.
     * @param fxmlPath FXML file path.
     * @param root Object that must be root of given FXML.
     */
    protected final void assignLoader(String fxmlPath, Object root){
        FXMLLoader loader = new FXMLLoader(AmbroAFB.class.getResource(fxmlPath));
        loader.setResources(GeneralConfig.getInstance().getBundle());
        loader.setRoot(root);
        loader.setController(root);
        
        try {
            loader.load();
        } catch (IOException ex) {
            Logger.getLogger(SceneWithVBoxRoot.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
