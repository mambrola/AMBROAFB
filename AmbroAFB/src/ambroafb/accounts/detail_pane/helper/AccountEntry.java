/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.accounts.detail_pane.helper;

import ambro.AView;
import ambroafb.general.DateConverter;
import ambroafb.general.NumberConverter;
import ambroafb.general.interfaces.TableColumnFeatures;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author dkobuladze
 */
public class AccountEntry {
    
    @AView.Column(title = "%date", width = TableColumnFeatures.Width.DATE)
    private final StringProperty dateDescrip = new SimpleStringProperty("");
    private final ObjectProperty<LocalDate> dateObj = new SimpleObjectProperty<>();
    
    @AView.Column(title = "%correspondent", width = "280")
    private final StringProperty correspondent = new SimpleStringProperty("");
    
    @AView.Column(title = "%debit", width = TableColumnFeatures.Width.MONEY, styleClass = TableColumnFeatures.Style.TEXT_RIGHT)
    private final StringProperty debit = new SimpleStringProperty("");
    
    @AView.Column(title = "%credit", width = TableColumnFeatures.Width.MONEY, styleClass = TableColumnFeatures.Style.TEXT_RIGHT)
    private final StringProperty credit = new SimpleStringProperty("");
    
    private int docId;
    private boolean isDebitEntry;
    
    public AccountEntry() {
        
    }

    public LocalDate getDate() {
        return dateObj.get();
    }
    
    public String getCorrespondent() {
        return correspondent.get();
    }
    
    public float getDebit() {
        return NumberConverter.stringToFloat(debit.get(), 2, 0f);
    }
    
    public float getCredit() {
        return NumberConverter.stringToFloat(credit.get(), 2, 0f);
    }
    
    public boolean isDebit(){
        return isDebitEntry;
    }
    
    public int getDocId(){
        return docId;
    }
    
    
    public void setDocDate(String date){
        dateObj.set(DateConverter.getInstance().parseDate(date));
        dateDescrip.set(DateConverter.getInstance().getDayMonthnameYearBySpace(dateObj.get()));
    }
    
    public void setDocId(int id){
        docId = id;
    }
    
    @JsonProperty(value = "corrDescrip")
    public void setCorrespondent(String descrip){
        correspondent.set(descrip);
    }
    
    public void setAmount(float amount) {
        debit.set(NumberConverter.makeFloatStringBySpecificFraction(amount, 2));
        credit.set(NumberConverter.makeFloatStringBySpecificFraction(amount, 2));
    }
    
    public void setIsDebit(boolean isDebit) {
        isDebitEntry = isDebit;
        if (isDebit){
            credit.set(""); // If amunt is debit, credit value must be empty.
        }
        else {
            debit.set(""); // If amunt is credit, debit value must be empty.
        }
    }

    @Override
    public String toString() {
        return "AccountEntry{" + "dateDescrip=" + dateDescrip + ", dateObj=" + dateObj + ", correspondent=" + correspondent + ", debit=" + debit + ", credit=" + credit + ", docId=" + docId + ", isDebitEntry=" + isDebitEntry + '}';
    }
    
    
}
