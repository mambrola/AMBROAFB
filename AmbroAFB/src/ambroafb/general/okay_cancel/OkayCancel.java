/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general.okay_cancel;

import ambroafb.AmbroAFB;
import ambroafb.general.GeneralConfig;
import ambroafb.general.Utils;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;


/**
 *
 * @author tabramishvili
 */
public class OkayCancel extends HBox  {

    @FXML private Button okay, cancel;

    public OkayCancel() {
        FXMLLoader loader = new FXMLLoader(AmbroAFB.class.getResource("/ambroafb/general/okay_cancel/OkayCancel.fxml"));
        loader.setResources(GeneralConfig.getInstance().getBundle());
        assignLoader(loader);
        try {
            loader.load();
        } catch (IOException ex) {
            Logger.getLogger(OkayCancel.class.getName()).log(Level.SEVERE, null, ex);
        }
        kayEventChange();
    }
    private void assignLoader(FXMLLoader loader) {
        loader.setRoot(this);
        loader.setController(this);
        
    }
    private void kayEventChange(){
        Utils.getFocusTraversableBottomChildren(this).stream().forEach((node) -> {
            node.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
                if (event.getCode().equals(KeyCode.SPACE)) {
                    event.consume();
                } 
                else if (event.getCode().equals(KeyCode.ENTER)) {
                    ((Button) node).fire();
                    event.consume();
                }
            });
        });
    }

    public void setOnOkay   (EventHandler<ActionEvent> handler) { okay.setOnAction(handler);}
    public void setOnCancel (EventHandler<ActionEvent> handler) { cancel.setOnAction(handler);}
    
    public EventHandler<ActionEvent> getOnOkay()    { return okay.getOnAction();}
    public EventHandler<ActionEvent> getOnCancel()  { return cancel.getOnAction();}
}