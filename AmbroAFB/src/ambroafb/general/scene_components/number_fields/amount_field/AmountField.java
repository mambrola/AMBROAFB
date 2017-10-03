/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general.scene_components.number_fields.amount_field;

import ambroafb.general.scene_components.number_fields.NumberField;

/**
 *
 * @author dkobuladze
 */
public class AmountField extends NumberField {
    
    public static final String FINALY_CONTENT_PATTERN = "(0|[1-9]\\d{0,10})(\\.\\d{2})?";
    public static final String FINALY_CONTENT_DESCRIP = "Amount field content is incorrect"; // must be bundle key
    
    private final String extraIntegerValueLength = "{0,9}"; // default max length is 10, but 0 or [1-9] any digit on the first place - decrease this count by 1.
    private final String contentRuntimePattern = "(0|[1-9]\\d" + extraIntegerValueLength + ")(\\.|\\.\\d|\\.\\d\\d?)?";
    
    public AmountField(){
        super();
        addComponentFeatures();
    }
    
    private void addComponentFeatures(){
       contentRuntimePatternListener(contentRuntimePattern);
    }
    
    /**
     *  The method changes minimum and maximum lengths of float number integer part.
     * If minLength less then 1 or maxLength less then 1 or minLength greater than maxLength, the change will not be apply.
     * @param minLength The minimum length of float number integer part.
     * @param maxLength The maximum length of float number integer part..
     */
    public void setIntegerPartLength(int minLength, int maxLength){
        if (minLength < 1 || maxLength < 1 || minLength > maxLength) return;
        String newLength = "{" + (minLength - 1) + "," + (maxLength - 1) + "}"; // On the first place must be 0 or [1-9] any digit, so length count decrease by 1 on both sides.
        String newPattern = contentRuntimePattern.replace(extraIntegerValueLength, newLength);
        contentRuntimePatternListener(newPattern);
    }
}
