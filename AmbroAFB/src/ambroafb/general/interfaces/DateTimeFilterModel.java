/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general.interfaces;

import ambroafb.general.DateConverter;
import static ambroafb.general.interfaces.FilterModel.DATE_BIGGER;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 *
 * @author dkobuladze
 */
public abstract class DateTimeFilterModel extends FilterModel {
    
    /**
     * If user select to nothing in datePicker, then must return specified Object which is min date;
     * @return 
     */
    public LocalDateTime getFromDateForDB(){
        LocalDate fromDate = getFromDate();
        if (fromDate == null){
            fromDate = DateConverter.getInstance().parseDate(DATE_BIGGER);
        }
        String fromDateWithTime = fromDate.toString() + " " + EXTRA_TIME_BIGGER;
        System.out.println("fromDateWithTime: " + fromDateWithTime);
        LocalDateTime result = DateConverter.getInstance().parseDateTime(fromDateWithTime);
        System.out.println("result from datetime: " + result);
        return result;
    }
    
    /**
     * If user select to nothing in datePicker, then must return specified Object which is max date;
     * @return 
     */
    public LocalDateTime getToDateForDB(){
        LocalDate toDate = getToDate();
        if (toDate == null){
            toDate = DateConverter.getInstance().parseDate(DATE_LESS);
        }
        String toDateWithTime = toDate.toString() + " " + EXTRA_TIME_LESS;
        System.out.println("toDateWithTime: " + toDateWithTime);
        LocalDateTime result = DateConverter.getInstance().parseDateTime(toDateWithTime);
        System.out.println("result to datetime: " + result);
        return result;
    }
    
    public abstract LocalDate getFromDate();
    public abstract LocalDate getToDate();
    
}
