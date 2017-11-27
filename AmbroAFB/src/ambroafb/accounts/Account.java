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
import ambroafb.general.interfaces.TableColumnFeatures;
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
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

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
    
    @AView.Column(title = "%bal_accounts_min", width = "70", styleClass = TableColumnFeatures.Style.TEXT_RIGHT, cellFactory = BalAccountCellFactory.class)
    private final ObjectProperty<BalanceAccount> balAccountObj;
    
    @AView.Column(title = "%client", width = "60", styleClass = TableColumnFeatures.Style.TEXT_RIGHT, cellFactory = ClientCellFactory.class)
    private final ObjectProperty<Client> clientObj;
    
    private final ObjectProperty<LocalDate> dateOpenedObj, dateClosedObj;
    private final StringProperty remark;
    
    private final int clientIdDefaultValue = 0;
    
    public Account(){
        dateOpenedObj = new SimpleObjectProperty<>(LocalDate.now());
        accountNumber = new SimpleStringProperty("");
        iso = new SimpleStringProperty("");
        balAccountObj = new SimpleObjectProperty<>(new BalanceAccount());
        descrip = new SimpleStringProperty("");
        clientObj = new SimpleObjectProperty<>(new Client());
        dateClosedObj = new SimpleObjectProperty<>();
        remark = new SimpleStringProperty("");
        
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
    
    public int getBalAccountId(){
        return (balAccountObj.isNull().get()) ? -1 : balAccountObj.get().getRecId();
    }
    
    @JsonIgnore
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
    
    @JsonIgnore
    public String getBalAccountDescrip(){
        return balAccountObj.get().getDescrip();
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
    
    @JsonProperty
    public void setBalAccountId(int id){
        this.balAccountObj.get().setRecId(id);
    }

    @JsonSetter("balAcc")
    public void setBalAccount(int balAcc){
        this.balAccountObj.get().setBalAcc(balAcc);
    }
    
    @JsonProperty
    public void setBalAccountDescrip(String balAccDescrip){
        balAccountObj.get().setDescrip(balAccDescrip);
    }
    
    public void setDescrip(String descrip){
        this.descrip.set(descrip);
    }
    
    public void setClientId(int clientId){
        clientObj.get().setRecId(clientId);
    }
    
    @JsonProperty
    public void setClientDescrip(String descrip){
        clientObj.get().setFirstName(descrip);
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
        balAccountObj.set(otherAccount.balAccProperty().get().cloneWithID());
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
                Utils.objectEquals(openedProperty().get(), other.openedProperty().get()) &&
                getRemark().equals(other.getRemark()) &&
                Utils.objectEquals(closedProperty().get(), other.closedProperty().get());
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
    
    
    public static class BalAccountCellFactory implements Callback<TableColumn<Account, BalanceAccount>, TableCell<Account, BalanceAccount>> {

        @Override
        public TableCell<Account, BalanceAccount> call(TableColumn<Account, BalanceAccount> param) {
            return new TableCell<Account, BalanceAccount>() {
                @Override
                protected void updateItem(BalanceAccount item, boolean empty) {
                    super.updateItem(item, empty);
                    setText((item == null || empty) ? null : "" + item.getBalAcc());
                }
            };
        }
    }
    
    
    public static class ClientCellFactory implements Callback<TableColumn<Account, Client>, TableCell<Account, Client>> {

        @Override
        public TableCell<Account, Client> call(TableColumn<Account, Client> param) {
            return new TableCell<Account, Client>() {
                @Override
                protected void updateItem(Client item, boolean empty) {
                    super.updateItem(item, empty);
                    setText((item == null || item.getRecId() == 0 || empty) ? null : "" + item.getRecId());
                }
                
            };
        }
    }
}
