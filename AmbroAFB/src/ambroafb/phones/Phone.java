/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.phones;

import ambroafb.general.Editable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;

/**
 *
 * @author tabramishvili
 */
@SuppressWarnings("EqualsAndHashcode")
public class Phone implements Editable<String> {

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private int recId;
    
//    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
//    private int clientId;

    private final StringProperty number = new SimpleStringProperty();

    public Phone() {
    }

    public Phone(int id, String number) {
        this.recId = id;
        this.number.set(number);
    }

    public Phone(String number) {
        this.number.set(number);
    }

    public int getRecId() {
        return recId;
    }

    public void setRecId(int id) {
        this.recId = id;
    }
    
//    public int getClientId() {
//        return clientId;
//    }
//
//    public void setClientId(int clientId) {
//        this.clientId = clientId;
//    }

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
    
    @JsonIgnore
    public StringProperty getNumberProperty(){
        return number;
    }

    @Override
    public String toString() {
        return "PhoneNumber{" + "id=" + recId + ", number=" + number + '}';
    }

    @Override
    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    public boolean equals(Object other){
        if (other == null) return false;
        Phone phoneOther = (Phone) other;
        String thisNumber = getOnlyDigitsFrom(number.get());
        String otherNumber = getOnlyDigitsFrom(phoneOther.getNumber());
        return thisNumber.equals(otherNumber);
    }
    
    private String getOnlyDigitsFrom(String phone){
        String result = "";
        for(int i = 0; i < phone.length(); i++){
            char ch = phone.charAt(i);
            if (Character.isDigit(ch)){
                result += ch;
            }
        }
        return result;
    }
    
    /**
     * The method compares clients phones list and pays attention size and order of them. 
     * @param first
     * @param second
     * @return 
     */
    public static boolean compareLists(List<Phone> first, List<Phone> second) {
        if (first.size() != second.size()) return false;

        for(int i = 0; i < first.size(); i++){
            Phone phone = first.get(i);
            Phone otherPhone = second.get(i);
            if ( !phone.getNumber().equals(otherPhone.getNumber()) ) return false;
        }

        return true;
    }
    
    
}
