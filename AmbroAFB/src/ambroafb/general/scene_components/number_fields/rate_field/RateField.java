/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general.scene_components.number_fields.rate_field;

import ambroafb.general.scene_components.number_fields.NumberField;

/**
 *
 * @author dkobuladze
 */
public class RateField extends NumberField {
    
    public static final String FINALY_CONTENT_PATTERN = "[1-9]\\.\\d{4}";
    public static final String FINALY_CONTENT_DESCRIP = "Rate text is incorrect (ex: 2.1234)";
    
    public RateField(){
        super();
        addComponentFeatures();
    }
    
    private void addComponentFeatures(){
       String pattern = "[1-9](|\\.\\d{0,4})";
//       String pattern = "[1-9](|\\.)";
       contentRuntimePatternListener(pattern);
    }
    
}
