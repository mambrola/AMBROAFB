/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.currencies;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;

/**
 *
 * @author dato
 */
public class IsoComboBox extends ComboBox<String> {
    
    private final ObservableList<String> isoes = FXCollections.observableArrayList();
    private final FilteredList<String> filteredList = new FilteredList<>(isoes);
    private final String GEL_ISO = "GEL";
    
    public IsoComboBox(){
        super();
        isoes.setAll(Currency.getAllIsoFromDB());
        this.setItems(filteredList);
        this.setConverter(new CustomIsoConverter());
    }
    
    public void filterBy(String iso){
        if (iso != null){
            filteredList.setPredicate((String boxIso) -> !boxIso.equals(iso));
            if (getValue() != null && getValue().equals(iso)){
                setValue(null);
            }
        }
    }
    
    public void setValueToGEL(){
        FilteredList<String> list = this.getItems().filtered((elem) -> elem.equals(GEL_ISO));
        if (!list.isEmpty()){
            setValue(list.get(0));
        }
    }

    private class CustomIsoConverter extends StringConverter<String> {

        @Override
        public String toString(String selected) {
            return selected;
        }

        @Override
        public String fromString(String input) {
            return input.toUpperCase();
        }
        
    }
}
