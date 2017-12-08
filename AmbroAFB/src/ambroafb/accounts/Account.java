/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.accounts;

import ambro.AView;
import ambroafb.general.DateConverter;
import ambroafb.general.NumberConverter;
import ambroafb.general.Utils;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.general.interfaces.TableColumnFeatures;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import java.time.LocalDate;
import java.util.Objects;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.binding.StringExpression;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;

/**
 *
 * @author dkobuladze
 */
public class Account extends EditorPanelable {

    @AView.Column(title = "%account_number", width = "80", styleClass = TableColumnFeatures.Style.TEXT_RIGHT)
    private final StringProperty accountNumber;
    
    @AView.Column(title = "%iso", width = TableColumnFeatures.Width.ISO)
    private final StringProperty iso;
    
    @AView.Column(title = "%descrip", width = "360")
    private final StringProperty descrip;
    
    @AView.Column(title = "%bal_accounts_min", width = "70", styleClass = TableColumnFeatures.Style.TEXT_RIGHT)
    private final StringProperty balAccount;
    private final StringProperty balAccountId;
    private final StringProperty balAccountDescrip;
    
    @AView.Column(title = "%client", width = "60", styleClass = TableColumnFeatures.Style.TEXT_RIGHT)
    private final StringProperty clientId;
    private final StringProperty clientDescrip;
    
    private final ObjectProperty<LocalDate> dateOpenedObj, dateClosedObj;
    private final StringProperty remark;
    
    
    public Account(){
        dateOpenedObj = new SimpleObjectProperty<>();
        accountNumber = new SimpleStringProperty();
        iso = new SimpleStringProperty();
        balAccountId = new SimpleStringProperty();
        balAccount = new SimpleStringProperty();
        balAccountDescrip = new SimpleStringProperty();
        clientId = new SimpleStringProperty();
        clientDescrip = new SimpleStringProperty();
        
        descrip = new SimpleStringProperty();
        dateClosedObj = new SimpleObjectProperty<>();
        remark = new SimpleStringProperty();
        
        balAccount.addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            System.out.println("change change change");
        });
    }
    
    public StringProperty accountNumberProperty(){
        return accountNumber;
    }
    
    public StringProperty isoProperty(){
        return iso;
    }
    
    public StringProperty balAccountIdProperty(){
        return balAccountId;
    }
    
    public StringProperty balAccountProperty(){
        return balAccount;
    }
    
    public ReadOnlyStringProperty balAccountDescripProperty(){
        return balAccountDescrip;
    }
    
    public StringProperty descripProperty(){
        return descrip;
    }
    
    public StringProperty remarkProperty(){
        return remark;
    }
    
    public StringProperty clientIdProperty(){
        return clientId;
    }
    
    public ObjectProperty<LocalDate> openedProperty(){
        return dateOpenedObj;
    }
    
    public ObjectProperty<LocalDate> closedProperty(){
        return dateClosedObj;
    }
    
    
    // Getters:
    public Long getAccount(){
        return NumberConverter.stringToLong(accountNumber.get(), null);
    }
    
    public String getIso(){
        return iso.get();
    }
    
    public Integer getBalAccountId(){
        return NumberConverter.stringToInteger(balAccountId.get(), null);
    }
    
    @JsonIgnore
    public Integer getBalAccount(){
        return NumberConverter.stringToInteger(balAccount.get(), null);
    }
    
    public String getDescrip(){
        return descrip.get();
    }
    
    public Integer getClientId(){
        return NumberConverter.stringToInteger(clientId.get(), null);
    }
    
    @JsonIgnore
    public String getClientDescrip(){
        return clientDescrip.get();
    }
    
    public String getDateOpen(){
        return (dateOpenedObj.get() == null) ? null : dateOpenedObj.get().toString();
    }
    
    public String getRemark(){
        return remark.get();
    }
    
    @JsonIgnore
    public String getBalAccountDescrip(){
        return balAccountDescrip.get();
    }
    
    public String getDateClose(){
        return (dateClosedObj.get() == null) ? null : dateClosedObj.get().toString();
    }
    
    
    // Setters:
    public void setAccount(Long account){
        accountNumber.set((account == null) ? null : account.toString());
    }
    
    public void setIso(String iso){
        this.iso.set(iso);
    }
    
    @JsonProperty
    public void setBalAccountId(Integer recId){
        balAccountId.set((recId == null) ? null : recId.toString());
    }

    @JsonSetter("balAcc")
    public void setBalAccount(Integer balAcc){
        balAccount.set((balAcc == null) ? null : balAcc.toString());
    }
    
    @JsonProperty
    public void setBalAccountDescrip(String balAccDescrip){
        balAccountDescrip.set(balAccDescrip);
    }
    
    public void setDescrip(String descrip){
        this.descrip.set(descrip);
    }
    
    public void setClientId(Integer recId){
        clientId.set((recId == null) ? "" : recId.toString());
    }
    
    @JsonProperty
    public void setClientDescrip(String descrip){
        clientDescrip.set(descrip);
    }
    
    public void setDateOpen(String date){
        dateOpenedObj.set(DateConverter.getInstance().parseDate(date));
    }
    
    public void setRemark(String remark){
        this.remark.set(remark);
    }
    
    public void setDateClose(String date){
        this.dateClosedObj.set(DateConverter.getInstance().parseDate(date));
    }
    
    
    
    @Override
    public Account cloneWithoutID() {
        Account clone = new Account();
        clone.copyFrom(this);
        return clone;
    }

    @Override
    public Account cloneWithID() {
        Account clone = cloneWithoutID();
        clone.setRecId(this.getRecId());
        return clone;
    }

    @Override
    public void copyFrom(EditorPanelable other) {
        Account otherAccount = (Account)other;
        setAccount(otherAccount.getAccount());
        setIso(otherAccount.getIso());
        
        setBalAccountId(otherAccount.getBalAccountId());
        setBalAccount(otherAccount.getBalAccount());
        setBalAccountDescrip(otherAccount.getBalAccountDescrip());
        setClientId(otherAccount.getClientId());
        setClientDescrip(otherAccount.getClientDescrip());

        setDescrip(otherAccount.getDescrip());
        setDateOpen(otherAccount.getDateOpen());
        setRemark(otherAccount.getRemark());
        setDateClose(otherAccount.getDateClose());
    }
    
    /**
     * The method use for partly data accounts (Docs in table list). It compares account numbers and iso. 
     * If they are same, account are same. Otherwise they are difference.
     * @param other
     * @return 
     */
    @JsonIgnore
    public boolean partlyCompare(Account other){
        return  Utils.objectEquals(accountNumber.get(), other.accountNumberProperty()) &&
                getIso().equals(other.getIso());
    }

    @Override
    public boolean equals(Object other){
        if (other == null) return false;
        Account otherAccount = (Account)other;
        return  getRecId() == otherAccount.getRecId() ||
                (Objects.equals(getAccount(), otherAccount.getAccount()) && Objects.equals(getIso(), otherAccount.getIso()));
    }
    
    @Override
    public boolean compares(EditorPanelable backup) {
        Account other = (Account)backup;
        return  Objects.equals(accountNumberProperty().get(), other.accountNumberProperty().get()) &&
                Objects.equals(getIso(), other.getIso()) &&
                Objects.equals(getBalAccountId(), other.getBalAccountId()) &&
                Objects.equals(getBalAccount(), other.getBalAccount()) &&
                Objects.equals(getBalAccountDescrip(), other.getBalAccountDescrip()) &&
                Objects.equals(getDescrip(), other.getDescrip()) &&
                Objects.equals(getClientId(), other.getClientId()) &&
                Objects.equals(getClientDescrip(), other.getClientDescrip()) &&
                Objects.equals(openedProperty().get(), other.openedProperty().get()) &&
                Objects.equals(getRemark(), other.getRemark()) &&
                Objects.equals(closedProperty().get(), other.closedProperty().get());
    }
    
    @Override
    public String toStringForSearch() {
        return getDescrip() + " " + getClientDescrip();
    }

    @Override
    public String toString() {
        return getShortDescrip(" / ", " - ").get();
    }
    
    public StringExpression getShortDescrip(String numberIsoDlmt, String isoDescripDlmt){
        StringBinding isoDescripBinding =  Bindings.when(iso.isEmpty()).
                                                        then(descrip.get()).
                                                    otherwise(iso.get()+ isoDescripDlmt + descrip.get());
        return Bindings.when(accountNumber.isEmpty()).
                            then(descrip.get()).
                        otherwise(accountNumber.get() + numberIsoDlmt + isoDescripBinding.get());
    }
    
}
