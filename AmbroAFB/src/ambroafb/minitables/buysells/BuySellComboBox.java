/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.minitables.buysells;

import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;

/**
 *
 * @author dkobuladze
 */
public class BuySellComboBox extends ComboBox<BuySell> {
    
    private final ObservableList<BuySell> items = FXCollections.observableArrayList();
    private final BuySell all = new BuySell();
    
    public BuySellComboBox(){
        this.setItems(items);
        all.setRecId(0);
        all.setDescrip("ALL");
        items.add(all);

        this.setConverter(new StringConverter<BuySell>() {
            @Override
            public String toString(BuySell buySell) {
                return buySell.toString();
            }
            @Override
            public BuySell fromString(String input) {
                return null;
            }
        });
        ArrayList<BuySell> buysells = BuySell.getAllFromDB();
        buysells.sort((BuySell b1, BuySell b2) -> b1.getRecId() - b2.getRecId());
        items.addAll(buysells);
        this.setValue(all);
    }
    
    public void selectItem(BuySell buysell){
        this.getSelectionModel().select(buysell);
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
