/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.phones;

import ambroafb.phones.Phone;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SingleSelectionModel;
import javafx.util.Callback;
import javafx.util.StringConverter;

/**
 *
 * @author dato
 */
public class PhoneComboBox extends ComboBox<Phone> {
    private final ArrayList<Phone> disabledItems = new ArrayList<>();
    private final PhoneComboBox phoneComboBox;
    
    public PhoneComboBox(ObservableList<Phone> items, boolean isEditable){
        phoneComboBox = (PhoneComboBox)this;
        
        if(isEditable){
            this.setEditable(true);
            this.setItems(items);
        } else {
            setViewableSetup();
            this.setValue(items.get(0));
            items.remove(0);
            this.setItems(items);
            disabledItems.addAll(items);
        }
        
        setConverter();
    }
    
    private void setConverter(){
        this.setConverter(new StringConverter<Phone>() {
            @Override
            public String toString(Phone object) {
                if(object == null)
                    return "";
                return object.getNumber();
            }
            @Override
            public Phone fromString(String string) {
                int selectedIndex = phoneComboBox.getSelectionModel().getSelectedIndex();
                Phone selectedItem = phoneComboBox.getSelectionModel().getSelectedItem();
                if(selectedIndex < 0){
                    selectedItem = new Phone(string);
                    phoneComboBox.getItems().add(0, selectedItem);
                } else {
                    selectedItem.setNumber(string);
                    phoneComboBox.getItems().remove(selectedIndex);
                    if(!string.equals(""))
                        phoneComboBox.getItems().add(selectedIndex, selectedItem);
                    phoneComboBox.setValue(null);
                }
                return selectedItem;
            }
        });
    }

    private void setViewableSetup() {
        SingleSelectionModel<Phone> model = new SingleSelectionModel<Phone>() { 
            @Override 
            public void select(Phone item) { 
                if (disabledItems.contains(item)) { 
                    return; 
                } super.select(item); 
            } 
            @Override 
            public void select(int index) { 
                Phone item = getItems().get(index); 
                if (disabledItems.contains(item)) { 
                    return; 
                } super.select(index); 
            } 
            @Override 
            protected int getItemCount() { 
                return getItems().size(); 
            } 
            @Override 
            protected Phone getModelItem(int index) { 
                return getItems().get(index); 
            } 
        }; 
        setSelectionModel(model); 
    }

}
