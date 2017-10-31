/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general.editor_panel;

import javafx.event.ActionEvent;
import javafx.scene.layout.HBox;

/**
 *
 * @author dkobuladze
 */
public abstract class EditorPanel extends HBox {
    
    public abstract void delete(ActionEvent event);
    public abstract void edit(ActionEvent event);
    public abstract void view(ActionEvent event);
    public abstract void add(ActionEvent event);
    public abstract void addBySample(ActionEvent event);
    
}
