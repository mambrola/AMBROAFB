/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general.interfaces;

import ambroafb.general.Names;
import ambroafb.general.okay_cancel.FilterOkayCancelController;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author dkobuladze
 */
public abstract class UserInteractiveFilterStage extends UserInteractiveStage {
    
    public UserInteractiveFilterStage(Stage owner, String stageTitleBundleKey){
        super(owner, Names.LEVEL_FOR_PATH, stageTitleBundleKey, "/images/filter.png");
        
        onCloseRequestProperty().set((EventHandler<WindowEvent>) (WindowEvent event) -> {
            getOkayCancelController().cancel(null);
            if(event != null) event.consume();
        });
    }
    
    protected abstract FilterOkayCancelController getOkayCancelController();
}
