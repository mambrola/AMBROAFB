/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.accounts;

import ambro.AView;
import ambroafb.balance_accounts.BalanceAccount;
import ambroafb.clients.Client;
import ambroafb.general.DateConverter;
import ambroafb.general.Utils;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.general.interfaces.TableColumnWidths;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import java.time.LocalDate;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.binding.StringExpression;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;

/**
 *
 * @author dkobuladze
 */
public class Account extends EditorPanelable {

    @AView.Column(title = "%account_number", width = "80", styleClass = "textRight")
    private final StringProperty accountNumber;
    
    @AView.Column(title = "%iso", width = TableColumnWidths.ISO)
    private final StringProperty iso;
    
    @AView.Column(title = "%descrip", width = "360")
    private final StringProperty descrip;
    
    @AView.Column(title = "%bal_accounts_min", width = "70", styleClass = "textRight")
    private final StringProperty balAccount;
    private final ObjectProperty<BalanceAccount> balAccountObj;
    
    @AView.Column(title = "%client", width = "60", styleClass = "textRight")
    private final StringProperty clientId;
    private final ObjectProperty<Client> clientObj;
    
    private final ObjectProperty<LocalDate> dateOpenedObj, dateClosedObj;
    private final StringProperty remark;
    
//    private static final String DB_VEIW_NAME = "accounts_whole";
    private final int clientIdDefaultValue = 0;
    
    public Account(){
        dateOpenedObj = new SimpleObjectProperty<>(LocalDate.now());
        accountNumber = new SimpleStringProperty("");
        iso = new SimpleStringProperty("");
        balAccount = new SimpleStringProperty("");
        balAccountObj = new SimpleObjectProperty<>(new BalanceAccount());
        descrip = new SimpleStringProperty("");
        clientId = new SimpleStringProperty("");
        clientObj = new SimpleObjectProperty<>(new Client());
        dateClosedObj = new SimpleObjectProperty<>();
        remark = new SimpleStringProperty("");
        
        clientObj.addListener((ObservableValue<? extends Client> observable, Client oldValue, Client newValue) -> {
            String newClientId = "";
            if (newValue != null){
                newClientId = "" + newValue.getRecId();
            }
            clientId.set(newClientId);
        });
    }
    
    public StringProperty accountNumberProperty(){
        return accountNumber;
    }
    
    public StringProperty isoProperty(){
        return iso;
    }
    
    public ObjectProperty<BalanceAccount> balAccProperty(){
        return balAccountObj;
    }
    
    public StringProperty descripProperty(){
        return descrip;
    }
    
    public StringProperty remarkProperty(){
        return remark;
    }
    
    public ObjectProperty<Client> clientProperty(){
        return clientObj;
    }
    
    public ObjectProperty<LocalDate> openedProperty(){
        return dateOpenedObj;
    }
    
    public ObjectProperty<LocalDate> closedProperty(){
        return dateClosedObj;
    }
    
    
    // Getters:
    public long getAccount(){
        return (accountNumber.get().isEmpty()) ? -1 : Long.parseLong(accountNumber.get());
    }
    
    public String getIso(){
        return iso.get();
    }
    
    @JsonGetter("bal_acc")
    public int getBalAccount(){
        return (balAccountObj.isNull().get()) ? -1 : balAccountObj.get().getBalAcc();
    }
    
    public String getDescrip(){
        return descrip.get();
    }
    
    public int getClientId(){
        return (clientObj.isNull().get()) ? clientIdDefaultValue : clientObj.get().getRecId();
    }
    
    @JsonIgnore
    public String getClientDescrip(){
        return (clientObj.isNull().get()) ? "" : clientObj.get().getFirstName();
    }
    
    public String getDateOpen(){
        return (dateOpenedObj.isNull().get()) ? null : dateOpenedObj.get().toString();
    }
    
    public String getRemark(){
        return remark.get();
    }
    
    public String getDateClose(){
        return (dateClosedObj.isNull().get()) ? null : dateClosedObj.get().toString();
    }
    
    
    // Setters:
    public void setAccount(long account){
        this.accountNumber.set("" + account);
    }
    
    public void setIso(String iso){
        this.iso.set(iso);
    }

    @JsonSetter("balAccount")
    public void setBalAccount(int balAcc){
        this.balAccount.set("" + balAcc);
        if (balAccountObj.isNotNull().get()){
            this.balAccountObj.get().setBalAcc(balAcc);
        }
    }
    
    public void setDescrip(String descrip){
        this.descrip.set(descrip);
    }
    
    public void setClientId(int clientId){
        this.clientId.set((clientId == 0) ? "" : "" + clientId);
        if (clientObj.isNotNull().get()){
            clientObj.get().setRecId(clientId);
        }
    }
    
    @JsonProperty
    public void setClientDescrip(String descrip){
        if (clientObj.isNotNull().get()){
            clientObj.get().setFirstName(descrip);
        }
    }
    
    public void setDateOpen(String date){
        this.dateOpenedObj.set(DateConverter.getInstance().parseDate(date));
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
        setBalAccount(otherAccount.getBalAccount());
        setDescrip(otherAccount.getDescrip());
        setClientId(otherAccount.getClientId());
        setClientDescrip(otherAccount.getClientDescrip());
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
        return  getAccount() == other.getAccount() &&
                getIso().equals(other.getIso());
    }

    @Override
    public boolean equals(Object other){
        if (other == null) return false;
        Account otherAccount = (Account)other;
        return  getRecId() == otherAccount.getRecId() ||
                (getAccount() == otherAccount.getAccount() && getIso().equals(otherAccount.getIso()));
    }
    
    @Override
    public boolean compares(EditorPanelable backup) {
        Account other = (Account)backup;
        return  getAccount() == other.getAccount() &&
                getIso().equals(other.getIso()) &&
                getBalAccount() == other.getBalAccount() &&
                getDescrip().equals(other.getDescrip()) &&
                getClientId() == other.getClientId() &&
                Utils.dateEquals(openedProperty().get(), other.openedProperty().get()) &&
                getRemark().equals(other.getRemark()) &&
                Utils.dateEquals(closedProperty().get(), other.closedProperty().get());
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
    
    private final int max_account_num_length = 10;
    
    private String getSpaces(int accountNumLength){
        String result = "";
        int diff = max_account_num_length - accountNumLength;
        if (diff > 0){
            for (int i = 0; i < diff; i++) {
                result += " ";
            }
        }
        return result;
    }
}
