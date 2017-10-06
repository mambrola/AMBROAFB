/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.accounts;

import ambroafb.clients.Client;
import ambroafb.general.DBUtils;
import ambroafb.general.DateConverter;
import ambroafb.general.interfaces.EditorPanelable;
import authclient.db.ConditionBuilder;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDate;
import java.util.ArrayList;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.binding.StringExpression;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.json.JSONObject;

/**
 *
 * @author dkobuladze
 */
public class Account extends EditorPanelable {

    private final StringProperty accountNumber, iso, balAccount, descrip;
    private final ObjectProperty<Client> clientObj;
    private final ObjectProperty<LocalDate> dateOpenedObj, dateClosedObj;
    private Object remark;
    
    private static final String DB_VEIW_NAME = "accounts_whole";
    
    public Account(){
        dateOpenedObj = new SimpleObjectProperty<>(LocalDate.now());
        accountNumber = new SimpleStringProperty("");
        iso = new SimpleStringProperty("");
        balAccount = new SimpleStringProperty("");
        descrip = new SimpleStringProperty("");
        clientObj = new SimpleObjectProperty<>(new Client());
        dateClosedObj = new SimpleObjectProperty<>(LocalDate.now());
    }
    
    public StringProperty accountNumberProperty(){
        return accountNumber;
    }
    
    public StringProperty isoProperty(){
        return iso;
    }
    
    public StringProperty balAccProperty(){
        return balAccount;
    }
    
    public StringProperty descripProperty(){
        return descrip;
    }
    
    public ObjectProperty<LocalDate> openedProperty(){
        return dateOpenedObj;
    }
    
    public ObjectProperty<LocalDate> closedProperty(){
        return dateClosedObj;
    }
    
    
    public static ArrayList<Account> getAllFromDB(){
        JSONObject params = new ConditionBuilder().build();
        ArrayList<Account> accountFromDB = DBUtils.getObjectsListFromDB(Account.class, DB_VEIW_NAME, params);
        accountFromDB.sort((Account ac1, Account ac2) -> ac1.getRecId() - ac2.getRecId());
        return accountFromDB;
    }
    
    
    // Getters:
    public long getAccount(){
        return (accountNumber.get().isEmpty()) ? -1 : Long.parseLong(accountNumber.get());
    }
    
    public String getIso(){
        return iso.get();
    }
    
    public int getbalAccount(){
        return (balAccount.get().isEmpty()) ? -1 : Integer.parseInt(balAccount.get());
    }
    
    public String getDescrip(){
        return descrip.get();
    }
    
    public int getClientId(){
        return clientObj.get().getRecId();
    }
    
    public String getClientDescrip(){
        return clientObj.get().getFirstName();
    }
    
    public String getDateOpen(){
        return dateOpenedObj.get().toString();
    }
    
    public Object getRemark(){
        return remark;
    }
    
    public String getDateClose(){
        return dateClosedObj.get().toString();
    }
    
    
    // Setters:
    public void setAccount(long account){
        this.accountNumber.set("" + account);
    }
    
    public void setIso(String iso){
        this.iso.set(iso);
    }
    
    public void setbalAccount(int balAcc){
        this.balAccount.set("" + balAcc);
    }
    
    public void setDescrip(String descrip){
        this.descrip.set(descrip);
    }
    
    public void setClientId(int clientId){
        clientObj.get().setRecId(recId);
    }
    
    public void setClientDescrip(String descrip){
        clientObj.get().setFirstName(descrip);
    }
    
    public void setDateOpen(String date){
        this.dateOpenedObj.set(DateConverter.getInstance().parseDate(date));
    }
    
    public void setRemark(Object remark){
        this.remark = remark;
    }
    
    public void setDateclose(String date){
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
        setbalAccount(otherAccount.getbalAccount());
        setDescrip(otherAccount.getDescrip());
        setClientId(otherAccount.getClientId());
        setDateOpen(otherAccount.getDateOpen());
//        setRemark(otherAccount.getRemark());
//        setDateclose(otherAccount.getFlag());
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
    public boolean compares(EditorPanelable backup) {
        Account other = (Account)backup;
        
        return  getAccount() == other.getAccount() &&
                getIso().equals(other.getIso()) &&
                getbalAccount() == other.getbalAccount() &&
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
        return getShortDescrip(" / ", " - ").get();
    }
    
    public StringExpression getShortDescrip(String numberIsoDlmt, String isoDescripDlmt){
        StringBinding isoDescripBinding =  Bindings.when(iso.isEmpty()).
                                                        then(descrip.get()).
                                                    otherwise(iso.get()+ isoDescripDlmt + descrip.get());
        return Bindings.when(accountNumber.isEmpty()).
                            then(isoDescripBinding).
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
