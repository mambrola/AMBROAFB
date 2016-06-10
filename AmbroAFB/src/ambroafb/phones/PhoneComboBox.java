/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.phones;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.SingleSelectionModel;
import javafx.util.StringConverter;

/**
 *
 * @author dato
 */
public class PhoneComboBox extends ComboBox<Phone> {
    private final ArrayList<Phone> disabledItems = new ArrayList<>();
    private final PhoneComboBox phoneComboBox;
    private final String regEx = "^\\+?[0-9. ()-]{1,30}$";
    private final Pattern pattern;
    
    public PhoneComboBox(ObservableList<Phone> items, boolean isEditable){
        phoneComboBox = (PhoneComboBox)this;
        pattern = Pattern.compile(regEx);
        
        if(isEditable){
            this.setEditable(true);
            this.setItems(items);
            makeInputWithoutLetters();
        }
        else {
            setViewableSetup();
            List<Phone> tmItems = new ArrayList<>();
            items.stream().forEach((ph) -> {
                tmItems.add(ph);
            });
            if (!tmItems.isEmpty()){
                this.setValue(tmItems.get(0));
                tmItems.remove(0);
            }
            this.getItems().addAll(tmItems);
            disabledItems.addAll(tmItems);
        }
        
        setConverter();
    }
    
    /**
     * The method compares clients phones list and pays attention size and order of them. გადასატანია ტელეფონებში
     * @param first
     * @param second
     * @return 
     */
    public static boolean comparePhones(ObservableList<Phone> first, ObservableList<Phone> second) {
        if (first.size() != second.size()) return false;

        for(int i = 0; i < first.size(); i++){
            Phone phone = first.get(i);
            Phone otherPhone = second.get(i);
            if ( !phone.getNumber().equals(otherPhone.getNumber()) ) return false;
        }

        return true;
    }
    
    private void makeInputWithoutLetters() {
        getEditor().textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if(!newValue.isEmpty()){
                Matcher matcher = pattern.matcher(newValue);
                if(!matcher.matches()){
                    getEditor().setText(oldValue);
                }
            }
        });
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
                    if (!containsPhone(selectedItem))
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
    
    private boolean containsPhone(Phone p){
        boolean result = false;
        for (Phone phone : getItems()) {
            if (phone.equals(p)){
                result = true;
                break;
            }
        }
        return result;
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
