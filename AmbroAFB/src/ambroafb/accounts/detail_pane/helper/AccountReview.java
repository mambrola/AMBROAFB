/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.accounts.detail_pane.helper;

import ambroafb.general.DateConverter;
import java.time.LocalDate;

/**
 *
 * @author dkobuladze
 */
public class AccountReview {
    
    private LocalDate docDate;
    private boolean isTo, isDebit, isRed;
    private float amount;
    
    public AccountReview(){
        
    }

    public LocalDate getDocDate() {
        return docDate;
    }

    public boolean isTo() {
        return isTo;
    }

    public boolean isDebit() {
        return isDebit;
    }

    public boolean isRed() {
        return isRed;
    }

    public float getAmount() {
        return amount;
    }

    
    public void setDocDate(String docDate) {
        this.docDate = DateConverter.getInstance().parseDate(docDate);
    }

    public void setIsTo(boolean isTo) {
        this.isTo = isTo;
    }

    public void setIsDebit(boolean isDebit) {
        this.isDebit = isDebit;
    }

    public void setIsRed(boolean isRed) {
        this.isRed = isRed;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "AccountReview{" + "docDate=" + docDate + ", isTo=" + isTo + ", isDebit=" + isDebit + ", isRed=" + isRed + ", amount=" + amount + '}';
    }
    
    
}
