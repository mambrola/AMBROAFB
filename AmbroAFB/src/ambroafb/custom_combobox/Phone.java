/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ambroafb.custom_combobox;

import javafx.beans.property.StringProperty;

/**
 *
 * @author mambroladze
 */
public class Phone {
    private StringProperty number;

    public Phone(String number) {
        this.number.set(number);
    }

    public void setNumber(String number) {
        this.number.set(number);
    }

    public String getNumber() {
        return number.get();
    }
    
    public StringProperty getNumberProperty(){
        return number;
    }
}
