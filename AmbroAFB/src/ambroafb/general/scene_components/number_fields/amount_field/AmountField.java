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
    
    public static final String FINALY_CONTENT_PATTERN = "(0|[1-9]\\d{0,14})(\\.\\d{2})?";
    public static final String FINALY_CONTENT_DESCRIP = "Amount field content is incorrect"; // must be bundle key
    
    public AmountField(){
        super();
        addComponentFeatures();
    }
    
    private void addComponentFeatures(){
       String contentRuntimePattern = "(0|[1-9]\\d{0,14})(\\.|\\.\\d|\\.\\d\\d?)?";
       contentRuntimePatternListener(contentRuntimePattern);
    }
    
}
