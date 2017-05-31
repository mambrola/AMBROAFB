/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.minitables.attitudes;

import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;

/**
 *
 * @author dkobuladze
 */
public class AttitudeComboBox extends ComboBox<Attitude> {
    
    private final ObservableList<Attitude> items = FXCollections.observableArrayList();
    private final Attitude all = new Attitude();
    
    public AttitudeComboBox(){
        this.setItems(items);
        all.setRecId(0);
        all.setDescrip("ALL");
        items.add(all);

        this.setConverter(new StringConverter<Attitude>() {
            @Override
            public String toString(Attitude attitude) {
                return attitude.toString();
            }
            @Override
            public Attitude fromString(String input) {
                return null;
            }
        });
        ArrayList<Attitude> attitudes = Attitude.getAllFromDB();
        attitudes.sort((Attitude b1, Attitude b2) -> b1.getRecId() - b2.getRecId());
        items.addAll(attitudes);
        this.setValue(all);
    }
    
    public void selectItem(Attitude attitude){
        this.getSelectionModel().select(attitude);
    }

    public void showCategoryAll(boolean show){
        if (!show){
            if (getItems().contains(all)){
                getItems().remove(0);
            }
        }
        else {
            if (!getItems().contains(all)){
                getItems().add(all);
            }
        }
    }
}
