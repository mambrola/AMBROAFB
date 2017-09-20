/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.docs.types.conversion;

import ambroafb.accounts.Account;
import ambroafb.general.DateConverter;
import ambroafb.general.interfaces.EditorPanelable;
import java.time.LocalDate;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author dkobuladze
 */
public class Conversion extends EditorPanelable {

    private final ObjectProperty<LocalDate> docDate, docInDocDate;
    private final StringProperty currencyFromAccount, currencyToAccount;
    private final ObjectProperty<Account> accountFrom, accountTo;
    private final StringProperty amountFromAccount, amountToAccount;
    
    public Conversion(){
        docDate = new SimpleObjectProperty<>(LocalDate.now());
        docInDocDate = new SimpleObjectProperty<>(LocalDate.now());
        
        currencyFromAccount = new SimpleStringProperty("");
        currencyToAccount = new SimpleStringProperty("");
        
        accountFrom = new SimpleObjectProperty<>(new Account());
        accountTo = new SimpleObjectProperty<>(new Account());
        
        amountFromAccount = new SimpleStringProperty("");
        amountToAccount = new SimpleStringProperty("");
    }
    
    public ObjectProperty<LocalDate> docDateProperty(){
        return docDate;
    }
    
    public ObjectProperty<LocalDate> docInDocDateProperty(){
        return docInDocDate;
    }
    
    public StringProperty currencyFromAccountProperty(){
        return currencyFromAccount;
    }
    
    public StringProperty currencyToAccountProperty(){
        return currencyToAccount;
    }
    
    public ObjectProperty<Account> accountFromProperty(){
        return accountFrom;
    }

    public ObjectProperty<Account> accountToProperty(){
        return accountTo;
    }
    
    public StringProperty amountFromAccountProperty(){
        return amountFromAccount;
    }
    
    public StringProperty amountToAccountProperty(){
        return amountToAccount;
    }
    
    
    // Getters:
    public String getDocDate(){
        return (docDate.get() == null) ? "" : docDate.get().toString();
    }
    
    public String getDocInDocDate(){
        return (docInDocDate.get() == null) ? "" : docInDocDate.get().toString();
    }
    
    public String getCurrencyFromAccount(){
        return currencyFromAccount.get();
    }
    
    public String getCurrencyToAccount(){
        return currencyToAccount.get();
    }
    
    public Account getAccountFrom(){
        return accountFrom.get();
    }
    
    public Account getAccountTo(){
        return accountTo.get();
    }
    
    public float getAmountFromAccount(){
        return (amountFromAccount.get().isEmpty()) ? 0 : Float.parseFloat(amountFromAccount.get());
    }
    
    public float getAmountToAccount(){
        return (amountToAccount.get().isEmpty()) ? 0 : Float.parseFloat(amountToAccount.get());
    }
    
    
    // Setters:
    public void setDocDate(String date) {
        this.docDate.set(DateConverter.getInstance().parseDate(date));
    }

    public void setDocInDocDate(String date) {
        this.docInDocDate.set(DateConverter.getInstance().parseDate(date));
    }
    
    public void setCurrencyFromAccount(String iso){
        this.currencyFromAccount.set(iso);
    }
    
    public void setCurrencyToAccount(String iso){
        this.currencyToAccount.set(iso);
    }
    
    public void setAccountFrom(Account account){
        this.accountFrom.set(account);
    }
    
    public void setAccountTo(Account account){
        this.accountTo.set(account);
    }
    
    public void setAmountFromAccount(float amount){
        this.amountFromAccount.set("" + amount);
    }
    
    public void setAmountToAccount(float amount){
        this.amountToAccount.set("" + amount);
    }
    
    
    @Override
    public Conversion cloneWithoutID() {
        Conversion clone = new Conversion();
        clone.copyFrom(this);
        return clone;
    }

    @Override
    public Conversion cloneWithID() {
        Conversion clone = cloneWithoutID();
        clone.setRecId(this.getRecId());
        return clone;
    }

    @Override
    public void copyFrom(EditorPanelable other) {
        Conversion otherConversion = (Conversion)other;
        setDocDate(otherConversion.getDocDate());
        setDocInDocDate(otherConversion.getDocInDocDate());
        setCurrencyFromAccount(otherConversion.getCurrencyFromAccount());
        setCurrencyToAccount(otherConversion.getCurrencyToAccount());
        setAccountFrom(otherConversion.getAccountFrom().cloneWithoutID());
        setAccountTo(otherConversion.getAccountTo().cloneWithoutID());
        setAmountFromAccount(otherConversion.getAmountFromAccount());
        setAmountToAccount(otherConversion.getAmountToAccount());
    }

    @Override
    public boolean compares(EditorPanelable backup) {
        Conversion other = (Conversion)backup;
        return  docDate.get().equals(other.docDateProperty().get()) &&
                docInDocDate.get().equals(other.docInDocDateProperty().get()) &&
                getCurrencyFromAccount().equals(other.getCurrencyFromAccount()) &&
                getCurrencyToAccount().equals(other.getCurrencyToAccount()) &&
                getAccountFrom().compares(other.getAccountFrom()) &&
                getAccountTo().compares(other.getAccountTo()) &&
                getAmountFromAccount() == other.getAmountFromAccount() &&
                getAmountToAccount() == other.getAmountToAccount();
    }

    @Override
    public String toStringForSearch() {
        return "";
    }
    
}
