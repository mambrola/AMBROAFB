/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.accounts;

import ambro.AView;
import ambroafb.accounts.filter.AccountFilterModel;
import ambroafb.balance_accounts.BalanceAccount;
import ambroafb.clients.Client;
import ambroafb.general.DBUtils;
import ambroafb.general.DateConverter;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.general.interfaces.FilterModel;
import ambroafb.general.interfaces.TableColumnWidths;
import authclient.db.ConditionBuilder;
import authclient.db.WhereBuilder;
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
import javafx.beans.value.ObservableValue;
import org.json.JSONObject;

/**
 *
 * @author dkobuladze
 */
public class Account extends EditorPanelable {

    @AView.Column(title = "%account_number", width = "120", styleClass = "textRight")
    private final StringProperty accountNumber;
    
    @AView.Column(title = "%iso", width = TableColumnWidths.ISO)
    private final StringProperty iso;
    
    @AView.Column(title = "%descrip", width = "360")
    private final StringProperty descrip;
    
    @AView.Column(title = "%bal_accounts", width = "150", styleClass = "textRight")
    private final StringProperty balAccount;
    private final ObjectProperty<BalanceAccount> balAccountObj;
    
    @AView.Column(title = "%id_number", width = "20", styleClass = "textRight")
    private final StringProperty clientId;
    private final ObjectProperty<Client> clientObj;
    
    private final ObjectProperty<LocalDate> dateOpenedObj, dateClosedObj;
    private final StringProperty remark;
    
    private static final String DB_VEIW_NAME = "accounts_whole";
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
        dateClosedObj = new SimpleObjectProperty<>(LocalDate.now());
        remark = new SimpleStringProperty("");
        
        clientObj.addListener((ObservableValue<? extends Client> observable, Client oldValue, Client newValue) -> {
            if (newValue == null){
                clientId.set("" + clientIdDefaultValue);
            }
            else {
                clientId.set("" + newValue.getRecId());
            }
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
    
    
    public static ArrayList<Account> getAllFromDB(){
        JSONObject params = new ConditionBuilder().build();
        ArrayList<Account> accountFromDB = DBUtils.getObjectsListFromDB(Account.class, DB_VEIW_NAME, params);
        accountFromDB.sort((Account ac1, Account ac2) -> ac1.getRecId() - ac2.getRecId());
        return accountFromDB;
    }
    
    public static ArrayList<Account> getFilteredFromDB(FilterModel filterModel){
        AccountFilterModel accountFiletModel = (AccountFilterModel) filterModel;
        WhereBuilder whereBuilder = new ConditionBuilder().where();
        
        if (accountFiletModel.isSelectedConcreteCurrency()){
            whereBuilder.and("iso", "=", accountFiletModel.getCurrencyIso());
        }
        if (accountFiletModel.isSelectedConcreteBalAccount()){
            whereBuilder.and("bal_account", "=", accountFiletModel.getBalAccountNumber());
        }
        if (accountFiletModel.isSelectedConcreteClient()){
            whereBuilder.and("client_id", "=", accountFiletModel.getClientId());
        }
        if (!accountFiletModel.getTypeIntdeterminate()){
            String relation = (accountFiletModel.isTypeSelected()) ? "is null" : "is not null";
            whereBuilder.and("date_close", relation, "");
        }
        JSONObject params = whereBuilder.condition().build();
        return DBUtils.getObjectsListFromDB(Account.class, DB_VEIW_NAME, params);
    }
    
    public static Account getOneFromDB(int id){
        JSONObject params = new ConditionBuilder().where().and("rec_id", "=", id).condition().build();
        Account res = DBUtils.getObjectFromDB(Account.class, DB_VEIW_NAME, params);
        System.out.println("res account: " + res);
        return res;
    }
    
    public static Account saveOneToDB(Account account){
        System.out.println("account save method...");
        return null;
    }
    
    public static boolean deleteOneFromDB(int id){
//        JSONObject params = new ConditionBuilder().where().and("rec_id", "=", id).condition().build();
//        return DBUtils.deleteObjectFromDB(DB_VEIW_NAME, params);
        System.out.println("account delete method...");
        return false;
    }
    
    // Getters:
    public long getAccount(){
        return (accountNumber.get().isEmpty()) ? -1 : Long.parseLong(accountNumber.get());
    }
    
    public String getIso(){
        return iso.get();
    }
    
    public int getbalAccount(){
        return (balAccountObj.isNull().get()) ? -1 : balAccountObj.get().getBalAcc();
    }
    
    public String getDescrip(){
        return descrip.get();
    }
    
    public int getClientId(){
        return (clientObj.isNull().get()) ? clientIdDefaultValue : clientObj.get().getRecId();
    }
    
    public String getClientDescrip(){
        return (clientObj.isNull().get()) ? "" : clientObj.get().getFirstName();
    }
    
    public String getDateOpen(){
        return dateOpenedObj.get().toString();
    }
    
    public String getRemark(){
        return remark.get();
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
        this.balAccountObj.get().setBalAcc(balAcc);
    }
    
    public void setDescrip(String descrip){
        this.descrip.set(descrip);
    }
    
    public void setClientId(int clientId){
        this.clientId.set("" + clientId);
        if (clientObj.isNotNull().get()){
            clientObj.get().setRecId(clientId);
        }
    }
    
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
        System.out.println("----------- copyFrom -------------");
        
        Account otherAccount = (Account)other;
        setAccount(otherAccount.getAccount());
        setIso(otherAccount.getIso());
        setbalAccount(otherAccount.getbalAccount());
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
    public boolean compares(EditorPanelable backup) {
        Account other = (Account)backup;
        
        System.out.println("getbalAccount() == other.getbalAccount(): " + (getbalAccount() == other.getbalAccount()));
        System.out.println("getbalAccount(): " + getbalAccount());
        System.out.println("other.getbalAccount(): " + other.getbalAccount());
        
        return  getAccount() == other.getAccount() &&
                getIso().equals(other.getIso()) &&
                getbalAccount() == other.getbalAccount() &&
                getDescrip().equals(other.getDescrip()) &&
                getClientId() == other.getClientId() &&
                openedProperty().get().equals(other.openedProperty().get()) &&
                getRemark().equals(other.getRemark()) &&
                closedProperty().get().equals(other.closedProperty().get());
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
