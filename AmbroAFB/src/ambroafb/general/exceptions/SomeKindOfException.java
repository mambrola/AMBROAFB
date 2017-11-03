/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general.exceptions;

/**
 *
 * @author dkobuladze
 */
public class SomeKindOfException extends Exception {

    private final String messageText;
    
    public SomeKindOfException(String message){
        messageText = message;
    }
    
    @Override
    public String getLocalizedMessage(){
        return messageText;
    }
}
