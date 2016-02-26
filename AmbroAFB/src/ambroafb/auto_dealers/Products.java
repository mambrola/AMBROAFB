/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.auto_dealers;

import ambro.AView;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;

/**
 *
 * @author mambroladze
 */
public class Products {
    
    
    
    @AView.Column(title = "%invoice", width = "100")
    public SimpleStringProperty invice;
    
    @AView.Column(title = "%tick", useCheckBoxTableCell = true, width = "70")
    public SimpleBooleanProperty isMarked;
    
    @AView.Column(title = "%product", width = "400")
    public SimpleStringProperty product;
    
    
    
    public Products (String p, String i, boolean m){
        product = new SimpleStringProperty(p);
        invice = new SimpleStringProperty(i);
        isMarked = new SimpleBooleanProperty(m);
    }
    
    
    
    @Override
    public String toString (){
        return invice + ":" + isMarked + ":" + product;
    }
}
