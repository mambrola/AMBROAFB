/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.custom_combobox;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.ComboBox;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.input.KeyEvent;
import javafx.util.StringConverter;

/**
 *
 * @author dato
 */
public class PhoneComboBox extends ComboBox<Phone> {
    private ArrayList<Phone> disabledItems = new ArrayList<>();
    private PhoneComboBox phoneComboBox;
    
    public PhoneComboBox(List<Phone> items, boolean isEditable){
        phoneComboBox = (PhoneComboBox)this;
        makeBinds();
        if(isEditable){
            this.setEditable(true);
            this.getEditor().addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
                phoneComboBox.show();
            });
            
            System.out.println("items.size: " + items.size());
            this.getItems().addAll(items);            
        } else {
            setViewableSetup();
            this.setValue(items.get(0));
            items.remove(0);
            this.getItems().addAll(items);
            disabledItems.addAll(items);
        }

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
    
    private void makeBinds(){
        for (Phone phone : disabledItems) {
//            editorProperty().
        }
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

    private void setEditableSetup() {
        this.setEditable(true);
    }
}
