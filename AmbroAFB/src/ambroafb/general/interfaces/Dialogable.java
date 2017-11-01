/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general.interfaces;

import javafx.scene.Scene;

/**
 *
 * @author mambroladze
 */
public interface Dialogable {
    
    /**
     * The method gets dialog result.
     * @param <T>
     * @return The object that change by dialog stage.
     */
    public <T> T getResult();
    
    /**
     * The method returns dialog scene.
     * @return The scene of dialog stage.
     */
    public Scene getScene();
    
    /**
     * The method wait while dialog close.
     */
    public void showAndWait();
    
    /**
     * The method change scene object by null.
     */
    public void operationCanceled();
    
}
