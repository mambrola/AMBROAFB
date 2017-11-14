/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.minitables.merchandises;

import ambroafb.general.interfaces.DataFetchProvider;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;

/**
 *
 * @author dkobuladze
 */
public class MerchandiseComboBox extends ComboBox<Merchandise> {
    
    private final ObservableList<Merchandise> items = FXCollections.observableArrayList();
    private final Merchandise all = new Merchandise();
    private MerchandiseDataFetchProvider dataFetchProvider = new MerchandiseDataFetchProvider();
    
    public MerchandiseComboBox(){
        this.setItems(items);
        all.setRecId(0);
        all.setDescrip("ALL");
        items.add(all);

        this.setConverter(new StringConverter<Merchandise>() {
            @Override
            public String toString(Merchandise merchandise) {
                return merchandise.toString();
            }
            @Override
            public Merchandise fromString(String input) {
                return null;
            }
        });
        
        try {
            List<Merchandise> merchandises = dataFetchProvider.getFilteredBy(DataFetchProvider.PARAM_FOR_ALL);
            merchandises.sort((Merchandise b1, Merchandise b2) -> b1.getRecId() - b2.getRecId());
            items.addAll(merchandises);
            this.setValue(all);
        } catch (Exception ex) {
            Logger.getLogger(MerchandiseComboBox.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void selectItem(Merchandise merchandise){
        this.getSelectionModel().select(merchandise);
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
