/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general.editor_panel.elements.buttons;

import ambroafb.AmbroAFB;
import ambroafb.general.GeneralConfig;
import ambroafb.general.save_button.SaveButton;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author dkobuladze
 */
public class SimpleButton extends Button {

    @FXML private Tooltip tooltip;
    @FXML private ImageView buttonImg;
    
    private final String fxmlPath = "/ambroafb/general/editor_panel/elements/buttons/SimpleButton.fxml";
    
    public SimpleButton(String tooltipTextKey, String imgURL){
        FXMLLoader loader = new FXMLLoader(AmbroAFB.class.getResource(fxmlPath));
        loader.setResources(GeneralConfig.getInstance().getBundle());
        assignLoader(loader);
        
        String langTitle = GeneralConfig.getInstance().getTitleFor(tooltipTextKey);
        tooltip.setText(langTitle);
        buttonImg.setImage(new Image(imgURL));
    }
    
    private void assignLoader(FXMLLoader loader) {
        loader.setRoot(this);
        loader.setController(this);
        
        try {
            loader.load();
        } catch (IOException ex) {
            Logger.getLogger(SaveButton.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
}
