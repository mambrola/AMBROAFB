/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general.monthcountercombobox;

import javafx.scene.control.ComboBox;

/**
 *
 * @author dato
 */
public class MonthCounterComboBox extends ComboBox<MonthCounterItem> {
    
//    private int start = 1, end = 12;
    
    public MonthCounterComboBox(){
//        getItems().add("0.25");
//        getItems().add("0.5");
        
//        for (int i = start; i <= end; i++){
//            String count = "" + i + ".00";
//            getItems().add(count);
//        }

        MonthCounterItem item1 = new MonthCounterItem("1");
        MonthCounterItem item2 = new MonthCounterItem("3");
        MonthCounterItem item3 = new MonthCounterItem("6");
        MonthCounterItem item4 = new MonthCounterItem("12");
        
        getItems().addAll(item1, item2, item3, item4);
        getSelectionModel().select(0);
    }
    
}
