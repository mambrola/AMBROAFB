/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.docs.types;

import ambroafb.AmbroAFB;
import ambroafb.docs.types.utilities.Utility;
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
    
    public SceneWithVBoxRoot(String fxmlPath){
        FXMLLoader loader = new FXMLLoader(AmbroAFB.class.getResource(fxmlPath));
        loader.setResources(GeneralConfig.getInstance().getBundle());
        assignLoader(loader);
    }
    
    private void assignLoader(FXMLLoader loader){
        loader.setRoot(this);
        loader.setController(this);
        
        try {
            loader.load();
        } catch (IOException ex) {
            Logger.getLogger(Utility.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
