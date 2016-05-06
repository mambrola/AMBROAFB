/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;

/**
 *
 * @author tabramishvili
 */
public class PhoneNumber implements Editable<String> {

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private int recId;
    private final StringProperty number = new SimpleStringProperty();

    public PhoneNumber() {
    }

    public PhoneNumber(int id, String number) {
        this.recId = id;
        this.number.set(number);
    }

    public PhoneNumber(String number) {
        this.number.set(number);
    }

    public int getRecId() {
        return recId;
    }

    public void setRecId(int id) {
        this.recId = id;
    }

    public String getNumber() {
        return number.get();
    }

    public void setNumber(String value) {
        number.set(value);
    }

    public StringProperty numberProperty() {
        return number;
    }

    @Override
    public void edit(String param) {
        setNumber(param);
    }

    @Override
    @JsonIgnore
    public ObservableValue<String> getObservableString() {
        return number;
    }

    @Override
    public String toString() {
        return "PhoneNumber{" + "id=" + recId + ", number=" + number + '}';
    }

    public boolean equals(PhoneNumber other){
        return this.number.get().equals(other.getNumber());
    }
    
    public int compare(PhoneNumber other){
        return this.recId - other.recId;
    }
}
