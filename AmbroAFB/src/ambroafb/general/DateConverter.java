/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Dato
 */
public class DateConverter {
    
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
    private static final DateTimeFormatter parser = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter parserWithTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
    
    
    public static String getDayMonthnameYearBySpace(LocalDate date){
        return formatter.format(date);
    }
    
    public static LocalDate parseDateWithoutTime(String date){
        return getResult(date, parser);
    }
    
    public static LocalDate parseDateWithTime(String date){
        return getResult(date, parserWithTime);
    }
    
    private static LocalDate getResult(String date, DateTimeFormatter pattern){
        LocalDate result = null;
        try {
            result = LocalDate.parse(date, pattern);
        } catch (Exception ex){
            Logger.getLogger(DateConverter.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
}
