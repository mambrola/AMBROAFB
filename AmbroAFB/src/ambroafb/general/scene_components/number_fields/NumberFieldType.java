/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general.scene_components.number_fields;

/**
 *
 * @author dkobuladze
 */
public enum NumberFieldType {
    /** Float number with infinity length of digits after point. */
    FLOAT_INFINITY, 
    
    /** Integer number with infinity length of digits. */
    INTEGER_INFINITY,
    
    /** Amount number: max 15 digit before point and 2 digit after point. */
    AMOUNT,
    
    /** Percent number with float value. The min is 0 and max is 100. */
    PERCENT;
}
