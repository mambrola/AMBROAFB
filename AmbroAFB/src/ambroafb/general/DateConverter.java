/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

/**
 *
 * @author Dato
 */
public class DateConverter {
    
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
    private static final DateTimeFormatter parser = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    
    public static String getDayMonthnameYearBySpace(LocalDate date){
        return formatter.format(date);
    }
    
    public static LocalDate getLocalDateFor(String date){
        LocalDate result;
        if (Pattern.matches(date, formatter.toString())){
            result = LocalDate.parse(date, formatter);
        }
        else {
            result = LocalDate.parse(date, parser);
        }
        return result;
    }
}
