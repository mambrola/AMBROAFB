/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general.interfaces;

import ambroafb.general.DateConverter;
import static ambroafb.general.interfaces.FilterModel.DATE_BIGGER;
import java.time.LocalDate;

/**
 *
 * @author dkobuladze
 */
public abstract class DateFilterModel extends FilterModel {
    
    /**
     * If user select to nothing in datePicker, then must return specified Object which is min date;
     * @return 
     */
    public LocalDate getFromDateForDB(){
        LocalDate fromDate = getFromDate();
        if (fromDate == null){
            fromDate = DateConverter.getInstance().parseDate(DATE_BIGGER);
        }
        return fromDate;
    }
    
    /**
     * If user select to nothing in datePicker, then must return specified Object which is max date;
     * @return 
     */
    public LocalDate getToDateForDB(){
        LocalDate toDate = getToDate();
        if (toDate == null){
            toDate = DateConverter.getInstance().parseDate(DATE_LESS);
        }
        return toDate;
    }
    
    public abstract LocalDate getFromDate();
    public abstract LocalDate getToDate();
    
}
