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
    
    
    private Integer balAccId, clientId;
    
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
    public Long getAccount(){
        return (accountNumber.isNull().get()) ? null : NumberConverter.stringToLong(accountNumber.get(), null);
    }
    
    public String getIso(){
        return (iso.isNull().get()) ? null : iso.get();
    }
    
    public Integer getBalAccountId(){
        return balAccId;
    }
    
    @JsonIgnore
    public Integer getBalAccount(){
        return (balAccountObj.isNull().get()) ? null : balAccountObj.get().getBalAcc();
    }
    
    public String getDescrip(){
        return (descrip.isNull().get()) ? null : descrip.get();
    }
    
    public Integer getClientId(){
        return clientId;
    }
    
    @JsonIgnore
    public String getClientDescrip(){
        return (clientObj.isNull().get()) ? null : clientObj.get().getFirstName();
    }
    
    public String getDateOpen(){
        return (dateOpenedObj.isNull().get()) ? null : dateOpenedObj.get().toString();
    }
    
    public String getRemark(){
        return (remark.isNull().get()) ? null : remark.get();
    }
    
    @JsonIgnore
    public String getBalAccountDescrip(){
        return (balAccountObj.isNull().get()) ? null : balAccountObj.get().getDescrip();
    }
    
    public String getDateClose(){
        return (dateClosedObj.isNull().get()) ? null : dateClosedObj.get().toString();
    }
    
    
    // Setters:
    public void setAccount(Long account){
        this.accountNumber.set("" + account);
    }
    
    public void setIso(String iso){
        this.iso.set(iso);
    }
    
    @JsonProperty
    public void setBalAccountId(Integer recId){
        balAccId = recId;
    }

    @JsonSetter("balAcc")
    public void setBalAccount(Integer balAcc){
        this.balAccountObj.get().setBalAcc(balAcc);
    }
    
    @JsonProperty
    public void setBalAccountDescrip(String balAccDescrip){
        balAccountObj.get().setDescrip(balAccDescrip);
    }
    
    public void setDescrip(String descrip){
        this.descrip.set(descrip);
    }
    
    public void setClientId(Integer recId){
        clientId = recId;
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
        return  Objects.equals(accountNumber.get(), other.accountNumberProperty().get()) &&
                Objects.equals(getIso(), other.getIso()) &&
                Objects.equals(balAccountObj.get(), other.balAccProperty().get()) &&
                Objects.equals(getDescrip(), other.getDescrip()) &&
                Objects.equals(clientProperty().get(), other.clientProperty().get()) &&
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
