/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general;

import java.util.function.Consumer;
import javafx.application.Platform;
import javafx.scene.control.Alert;

/**
 *
 * @author dkobuladze
 */
public class AfBConsumersManager {
    
    /**
     * The method returns consumer that run in {@link  javafx.application.Platform#runLater(Runnable) Platform.runLater} 
     * and show alert with exception message text.
     * @return Consumer object.
     */
    public static Consumer<Exception> getStandardConsumerForDBException(){
        return (Exception ex) -> {
                            Platform.runLater(() -> {
                                DBExceptionManager exManager = new DBExceptionManager(ex);
                                new AlertMessage(Alert.AlertType.ERROR, ex, exManager.getExceptionMessage(), "").showAndWait();
                            });
                        };
    }
}
