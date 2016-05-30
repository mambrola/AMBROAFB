/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.phones;

/**
 *
 * @author mambroladze
 */
public class Phone {
    private String number;

    public Phone(String number) {
        this.number = number;
        
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getNumber() {
        return number;
    }
    
}