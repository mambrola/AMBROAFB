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
    
    public static String makeTwoDigitFraction(float number){
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMaximumFractionDigits(2);
        nf.setMinimumFractionDigits(2);
        return nf.format(number);
    }
    
}
