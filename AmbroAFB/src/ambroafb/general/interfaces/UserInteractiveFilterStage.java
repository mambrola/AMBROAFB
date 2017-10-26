/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general.interfaces;

import ambroafb.general.Names;
import ambroafb.general.okay_cancel.FilterOkayCancelController;
import java.util.concurrent.Semaphore;
import java.util.function.Consumer;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author dkobuladze
 */
public abstract class UserInteractiveFilterStage extends UserInteractiveStage {
    
    protected final int comboBoxCount = 3;
    protected int counter = 0;
    protected final Semaphore sem = new Semaphore(1);
    protected final Consumer<Object> increaseCounter;
    
    public UserInteractiveFilterStage(Stage owner, String stageTitleBundleKey){
        super(owner, Names.LEVEL_FOR_PATH, stageTitleBundleKey, "/images/filter.png");
        
        onCloseRequestProperty().set((EventHandler<WindowEvent>) (WindowEvent event) -> {
            getOkayCancelController().cancel(null);
            if(event != null) event.consume();
        });
        
        increaseCounter = (obj) -> {
            try {
                sem.acquire();
                counter = counter + 1;
                sem.release();
                if (counter == comboBoxCount){
                    getOkayCancelController().setOkaydisable(false);
                }
            } catch (InterruptedException ex) {
            }
        };
    }
    
    protected abstract FilterOkayCancelController getOkayCancelController();
}
