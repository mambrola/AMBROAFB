/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.minitables.attitudes;

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
public class AttitudeComboBox extends ComboBox<Attitude> {
    
    private final ObservableList<Attitude> items = FXCollections.observableArrayList();
    private final Attitude all = new Attitude();
    private final AttitudeDataFetchProvider dataFetchProvider = new AttitudeDataFetchProvider();
    
    public AttitudeComboBox(){
        try {
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
            List<Attitude> attitudes = dataFetchProvider.getFilteredBy(DataFetchProvider.PARAM_FOR_ALL);
            items.addAll(attitudes);
            this.setValue(all);
        } catch (Exception ex) {
            Logger.getLogger(AttitudeComboBox.class.getName()).log(Level.SEVERE, null, ex);
        }
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
