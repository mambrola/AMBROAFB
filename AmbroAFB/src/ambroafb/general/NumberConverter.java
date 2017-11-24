/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general;

import java.text.NumberFormat;
import java.text.ParseException;

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
    public static String makeFloatStringBySpecificFraction(Float number, int fractionDigitCount){
        NumberFormat nf = NumberFormat.getNumberInstance(); // It is not static variable, because it is ... on fractionDigitCount parameter. Is Static variable change this value, change result also in every place where it is used.
        nf.setMaximumFractionDigits(fractionDigitCount);
        nf.setMinimumFractionDigits(fractionDigitCount);
        return nf.format(number);
    }
    
    /**
     * The method converts formated string to float.
     * @param floatStr The string that contains numbers, comma and point.
     * @param fractionDigitCount The quantity of digits after point.
     * @return Null if string can not convert to float.
     */
    public static Float stringToFloat(String floatStr, int fractionDigitCount){
        Float result = null;
        if (floatStr != null){
            NumberFormat nf = NumberFormat.getNumberInstance(); // It is not static variable, because it is ... on fractionDigitCount parameter. Is Static variable change this value, change result also in every place where it is used.
            nf.setMaximumFractionDigits(fractionDigitCount);
            nf.setMinimumFractionDigits(fractionDigitCount);
            try {
                result = nf.parse(floatStr).floatValue();
            } catch (ParseException ex) {
            }
        }
        return result;
    }
    
    /**
     * The method converts formated string to float. If converted is impossible,then  returns defaultValue parameter.
     * @param floatStr The string that contains numbers, comma and point.
     * @param fractionDigitCount The quantity of digits after point.
     * @param defaultValue Default value if converting throw error.
     * @return Float value by string.
     */
    public static Float stringToFloat(String floatStr, int fractionDigitCount, Float defaultValue){
        Float result = stringToFloat(floatStr, fractionDigitCount);
        return (result == null) ? defaultValue : result;
    }
    
    /**
     *  The method converts string to Integer. If conversation is impossible, returns 'defaultValue' parameter.
     * @param intStr The number in string.
     * @param defaultValue The default value if conversation is not possible.
     * @return 
     */
    public static Integer stringToInteger(String intStr, Integer defaultValue) {
        Integer result = defaultValue;
        try {
            result = Integer.parseInt(intStr);
        } catch (NumberFormatException ex){
        }
        return result;
    }
}
