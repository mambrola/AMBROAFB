/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general.okay_cancel;

import ambroafb.general.interfaces.DialogCloseObserver;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

/**
 * FXML Controller class
 *
 * @author dato
 */
public class DialogOkayCancelController implements Initializable {

    @FXML
    private Button okay, cancel;

    private final List<DialogCloseObserver> closeObservers = new ArrayList<>();
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        okay.setOnAction((ActionEvent event) -> {
            closeObservers.forEach((observer) -> observer.okayAction()); // Note: Observers may unregister after this iteration complete, but don't into okayAction() or cancelAction()
        });
        cancel.setOnAction((ActionEvent event) -> {
            closeObservers.forEach((observer) -> observer.cancelAction()); // Note: Observers may unregister after this iteration complete, but don't into okayAction() or cancelAction()
        });
    }
    
    public void registerObserver(DialogCloseObserver observer){
        if (observer != null){
            closeObservers.add(observer);
        }
    }
    
    public void removeObserver(DialogCloseObserver observer){
        if (observer != null){
            closeObservers.remove(observer);
        }
    }
    
    public Button getOkayButton(){
        return okay;
    }
    
    public Button getCancelButton(){
        return cancel;
    }
    
    public void visibleOkay(boolean visible){
        okay.setVisible(visible);
    }
    
    public void visibleCancel(boolean visible){
        cancel.setVisible(visible);
    }

}
