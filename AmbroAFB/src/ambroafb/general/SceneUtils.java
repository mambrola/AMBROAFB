/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general;

import ambroafb.AmbroAFB;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

/**
 *
 * @author dato
 */
public class SceneUtils {

    /**
     * ქმნის სცენას გადმოცემული პარამეთრების მიხედვით
     *
     * @param name - fxml დოკუმენტის მისამართი
     * @param controller
     * @return
     */
    public static Scene createScene(String name, Object controller) {
        Scene scene = null;
        FXMLLoader loader = new FXMLLoader();
        loader.setResources(GeneralConfig.getInstance().getBundle());
        if (controller != null) {
            loader.setController(controller);
        }
        try {
            Parent root = loader.load(AmbroAFB.class.getResource(name).openStream());
            scene = new Scene(root);
            scene.getProperties().put("controller", loader.getController());
        } catch (IOException ex) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return scene;
    }
    
}
