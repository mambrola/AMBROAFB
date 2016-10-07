/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author Dato
 */
public class DateConverter {
    
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
    private static final DateTimeFormatter parserWithoutTime = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter parserWithTimeWithoutMilisec = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter parserWithTimeWithMilisec = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
    
    
    public static DateConverter converterWithoutTime;
    private DateConverter next;
    private DateTimeFormatter dateFormatter;
    
    private DateConverter(){ }
    
    public static DateConverter getInstance(){
        if (converterWithoutTime == null){
            converterWithoutTime = new DateConverter();
            DateConverter converterWithTimeAndWithoutMilis = new DateConverter();
            DateConverter converterWithTimeAndWithMilis = new DateConverter();
            DateConverter converterLastStep = new DateConverter();

            converterWithoutTime.setFormatter(parserWithoutTime);
            converterWithTimeAndWithoutMilis.setFormatter(parserWithTimeWithoutMilisec);
            converterWithTimeAndWithMilis.setFormatter(parserWithTimeWithMilisec);
            converterLastStep.setFormatter(formatter);

            converterWithoutTime.setNext(converterWithTimeAndWithoutMilis);
            converterWithTimeAndWithoutMilis.setNext(converterWithTimeAndWithMilis);
            converterWithTimeAndWithMilis.setNext(converterLastStep);
        }
        return converterWithoutTime;
    }
    
    public String getDayMonthnameYearBySpace(LocalDate date){
        return formatter.format(date);
    }
    
    public LocalDate parseDate(String date){
        LocalDate localDate = getResult(date, dateFormatter);
        if (localDate == null){
            localDate = (next != null) ? next.parseDate(date) : null;
        }
        return localDate;
    }
    
    private void setNext(DateConverter nextConverter){
        this.next = nextConverter;
    }
    
    private void setFormatter(DateTimeFormatter formatter){
        this.dateFormatter = formatter;
    }
    
    private static LocalDate getResult(String date, DateTimeFormatter pattern){
        LocalDate result = null;
        try {
            result = LocalDate.parse(date, pattern);
        } catch (Exception ex){ }
        return result;
    }
}
