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
    
    /* The variable contains regex pattern, that checks amount string validation and integer part length (min - 1 digit, max - 10 digits). */
    public static final String AMOUNT_PATTERN = "(0|[1-9]\\d{0,9})(\\.\\d{2})?";
    
    /* The variable contains regex pattern, that checks amount string validation and integer part length (min - 1 digit, max - 8 digits). */
    public static final String PRODUCT_PRICE_PATTERN = "(0|[1-9]\\d{0,7})(\\.\\d{2})?";
    
    /* The Bundle key of amount field incorrect content explain . */
    public static final String INCORRECT_CONTENT_EXPLAIN = "amount_field_incorrect_explain";// "Amount field content is incorrect"; // must be bundle key
    
    private final String runtimePattern = "[\\d\\,\\.]*";
    
    public AmountField(){
        super();
        addComponentFeatures();
    }
    
    private void addComponentFeatures(){
       contentRuntimePatternListener(runtimePattern);
    }
    
}
