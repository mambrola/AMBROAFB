/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.accounts;

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
public class Account extends EditorPanelable {

    private final StringProperty accountNumber, iso, balAcc, descrip;
    private int clientId;
    private final ObjectProperty<LocalDate> opened;
    private Object remark, flag;
    
    public Account(){
        accountNumber = new SimpleStringProperty("");
        iso = new SimpleStringProperty("");
        balAcc = new SimpleStringProperty("");
        descrip = new SimpleStringProperty("");
        opened = new SimpleObjectProperty<>(LocalDate.now());
    }
    
    public StringProperty accountNumberProperty(){
        return accountNumber;
    }
    
    public StringProperty isoProperty(){
        return iso;
    }
    
    public StringProperty balAccProperty(){
        return balAcc;
    }
    
    public StringProperty descripProperty(){
        return descrip;
    }
    
    public ObjectProperty<LocalDate> openedProperty(){
        return opened;
    }
    
    
    // Getters:
    public long getAccount(){
        return (accountNumber.get().isEmpty()) ? -1 : Long.parseLong(accountNumber.get());
    }
    
    public String getIso(){
        return iso.get();
    }
    
    public int getBalAcc(){
        return (balAcc.get().isEmpty()) ? -1 : Integer.parseInt(balAcc.get());
    }
    
    public String getDescrip(){
        return descrip.get();
    }
    
    public int getClientId(){
        return clientId;
    }
    
    public String getDateOpen(){
        return opened.get().toString();
    }
    
    public Object getRemark(){
        return remark;
    }
    
    public Object getFlag(){
        return flag;
    }
    
    
    // Setters:
    public void setAccount(long account){
        this.accountNumber.set("" + account);
    }
    
    public void setIso(String iso){
        this.iso.set(iso);
    }
    
    public void setBalAcc(int balAcc){
        this.balAcc.set("" + balAcc);
    }
    
    public void setDescrip(String descrip){
        this.descrip.set(descrip);
    }
    
    public void setClientId(int clientId){
        this.clientId = clientId;
    }
    
    public void setDateOpen(String date){
        this.opened.set(DateConverter.getInstance().parseDate(date));
    }
    
    public void setRemark(Object remark){
        this.remark = remark;
    }
    
    public void setFlag(Object flag){
        this.flag = flag;
    }
    
    
    
    @Override
    public Account cloneWithoutID() {
        Account clone = new Account();
        clone.copyFrom(this);
        return clone;
    }

    @Override
    public EditorPanelable cloneWithID() {
        Account clone = cloneWithoutID();
        clone.setRecId(this.getRecId());
        return clone;
    }

    @Override
    public void copyFrom(EditorPanelable other) {
        Account otherAccount = (Account)other;
        setAccount(otherAccount.getAccount());
        setIso(otherAccount.getIso());
        setBalAcc(otherAccount.getBalAcc());
        setDescrip(otherAccount.getDescrip());
        setClientId(otherAccount.getClientId());
        setDateOpen(otherAccount.getDateOpen());
//        setRemark(otherAccount.getRemark());
//        setFlag(otherAccount.getFlag());
    }

    @Override
    public boolean equals(Object other){
        if (other == null) return false;
        return compares((Account)other);
    }
    
    @Override
    public boolean compares(EditorPanelable backup) {
        Account other = (Account)backup;
        return  getAccount() == other.getAccount() &&
                getIso().equals(other.getIso()) &&
                getBalAcc() == other.getBalAcc() &&
                getDescrip().equals(other.getDescrip()) &&
                getClientId() == other.getClientId() &&
                openedProperty().get().equals(other.openedProperty().get());
//                getRemark().equals(other.getRemark()) &&
//                getFlag().equals(other.getFlag());
    }

    @Override
    public String toStringForSearch() {
        return getAccount() + " " + getIso() + " " + getDescrip();
    }

    @Override
    public String toString() {
        String toString = "";
        if (!accountNumber.get().isEmpty() && !descrip.get().isEmpty())
            toString = accountNumber.get() + " - " + descrip.get();
        return toString;
    }
    
    
}
