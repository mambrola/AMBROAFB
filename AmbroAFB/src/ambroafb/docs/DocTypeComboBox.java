/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.docs;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;

/**
 *
 * @author dkobuladze
 */
public class DocTypeComboBox extends ComboBox<DocType> {
    
    public DocTypeComboBox(){
        super();
        
        ObservableList<DocType> types = FXCollections.observableArrayList();
        types.add(new DocType(1, "Custom"));
        types.add(new DocType(2, "Utilities"));
        types.add(new DocType(3, "Monthly"));
        types.add(new DocType(4, "Refund"));
        this.setItems(types);
        this.setValue(types.get(0));
    }
    
}
