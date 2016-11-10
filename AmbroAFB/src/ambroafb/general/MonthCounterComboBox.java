/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general;

import javafx.scene.control.ComboBox;

/**
 *
 * @author dato
 */
public class MonthCounterComboBox extends ComboBox<String> {
    
    private int start = 1, end = 12;
    
    public MonthCounterComboBox(){
        getItems().add("0.25");
        getItems().add("0.5");
        
        for (int i = start; i <= end; i++){
            String count = "" + i + ".00";
            getItems().add(count);
        }
    }
    
}
