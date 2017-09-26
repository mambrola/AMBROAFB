/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general;

import java.text.NumberFormat;

/**
 *
 * @author dkobuladze
 */
public class NumberConverter {
    
    /**
     * The method make float number that has fractionDigitCount quantity number after point.
     * @param number The float number that must be change.
     * @param fractionDigitCount The quantity of digits after point.
     * @return 
     */
    public static String makeFloatSpecificFraction(float number, int fractionDigitCount){
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMaximumFractionDigits(fractionDigitCount);
        nf.setMinimumFractionDigits(fractionDigitCount);
        return nf.format(number);
    }
    
}
