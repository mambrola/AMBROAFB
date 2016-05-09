/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general.save_button;

import ambroafb.AmbroAFB;
import ambroafb.general.GeneralConfig;
import ambroafb.general.Utils;
import ambroafb.general.okay_cancel.OkayCancel;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;

/**
 *
 * @author dato
 */
public class SaveButton extends HBox {
    
    @FXML 
    private Button saver;
    
    public SaveButton(){
        FXMLLoader loader = new FXMLLoader(AmbroAFB.class.getResource("/ambroafb/general/save_button/SaveButton.fxml"));
        loader.setResources(GeneralConfig.getInstance().getBundle());
        assignLoader(loader);
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
    
    public void setOnSave(EventHandler<ActionEvent> handler){ 
        saver.setOnAction(handler); 
    }
    
    public EventHandler<ActionEvent> getOnSave(){ 
        return saver.getOnAction();
    }
}
