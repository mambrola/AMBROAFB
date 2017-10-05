/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general.scene_components.number_fields.integer_field;

import ambroafb.general.scene_components.number_fields.NumberField;

/**
 *
 * @author dkobuladze
 */
public class IntegerField extends NumberField {
    
    private final String runtimePattern = "[\\d]*";
    
    public IntegerField(){
        super();
        addComponentFeatures();
    }
    
    private void addComponentFeatures(){
       contentRuntimePatternListener(runtimePattern);
    }
    
}
