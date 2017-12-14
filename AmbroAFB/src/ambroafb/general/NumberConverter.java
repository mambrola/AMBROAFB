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
     * The method make string from number that has fractionDigitCount after point.
     * @param number The number that must be convert.
     * @param fractionDigitCount The quantity of digits after point.
     * @return 
     */
    public static String convertNumberToStringBySpecificFraction(Number number, int fractionDigitCount){
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
     * The method converts string to double. If it is impossible, returns null.
     * @param doubleStr The double value as string.
     * @return Null if parsing is impossible, otherwise - appropriate double value.
     */
    public static Double stringToDouble(String doubleStr){
        Double result = null;
        try {
            result = Double.parseDouble(doubleStr);
        } catch (NumberFormatException ex){}
        return result;
    }
    
    
    public static Double stringToDouble(String doubleStr, Double defaultValue){
        Double result = stringToDouble(doubleStr);
        return (result == null) ? defaultValue : result;
    }
    
    /**
     *  The method converts string to Integer. If conversation is impossible, returns 'defaultValue' parameter.
     * @param intStr The number in string.
     * @param defaultValue The default value if conversation is not possible.
     * @return Null if converation is not possible. Integer value - otherwise.
     */
    public static Integer stringToInteger(String intStr, Integer defaultValue) {
        Integer result = defaultValue;
        try {
            result = Integer.parseInt(intStr);
        } catch (NumberFormatException ex){
        }
        return result;
    }
    
    /**
     *  The method converts string to Long. If conversation is impossible, returns 'defaultValue' parameter.
     * @param intStr The number in string.
     * @param defaultValue The default value if conversation is not possible.
     * @return Null if converation is not possible. Long value - otherwise.
     */
    public static Long stringToLong(String intStr, Long defaultValue) {
        Long result = defaultValue;
        try {
            result = Long.parseLong(intStr);
        } catch (NumberFormatException ex){
        }
        return result;
    }
}
