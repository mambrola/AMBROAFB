/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general.editor_panel.elements.buttons;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 *
 * @author dkobuladze
 */
public class DocsButton extends SimpleButton {
    
    public DocsButton() {
        super("docs", "/images/docs.png");
        this.setOnAction(new DocsEventHandler());
    }
    
    private class DocsEventHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            System.out.println("--------------------- ACTION ---------------------");
        }
        
    }
        
}
