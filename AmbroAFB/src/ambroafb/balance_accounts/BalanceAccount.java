/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.balance_accounts;

import ambro.AFilterableTreeTableView;
import ambro.AView;
import ambroafb.general.GeneralConfig;
import ambroafb.general.KFZClient;
import ambroafb.general.TestDataFromDB;
import ambroafb.general.Utils;
import ambroafb.general.interfaces.EditorPanelable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.StringExpression;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeTableColumn;
import javafx.util.Callback;

/**
 *
 * @author dato
 */
public class BalanceAccount extends EditorPanelable {

    private final StringProperty balAcc;
    private final StringProperty descrip_ka;
    private final StringProperty descrip_en;
    private boolean level;
    private boolean isBase;
    
    @AView.Column(title = "%bal_accounts", width = "500")
    private final StringExpression aviewColumnText;
    @AView.Column(width = "50", cellFactory = ActPasValueFactory.class)
    private final BooleanProperty actPas;
    
    private final StringProperty currDescrip;
    
    @AFilterableTreeTableView.Children
    @JsonIgnore
    public final ObservableList<BalanceAccount> childrenAccounts = FXCollections.observableArrayList();
    
    @AView.RowStyles
    @JsonIgnore
    public final ObservableList<String> rowStyle = FXCollections.observableArrayList();
    
    public BalanceAccount(){
        balAcc = new SimpleStringProperty("");
        descrip_ka = new SimpleStringProperty("");
        descrip_en = new SimpleStringProperty("");
        
        currDescrip = (GeneralConfig.getInstance().getCurrentLocal().getLanguage().equals("ka")) ? descrip_ka : descrip_en;
        aviewColumnText = Utils.avoidNull(balAcc).concat(" - ").concat(Utils.avoidNull(currDescrip));
        actPas = new SimpleBooleanProperty();
    }
    
    
    public static ArrayList<BalanceAccount> getAllFromDB(){
        try {
            String data = GeneralConfig.getInstance().getServerClient().get("balAccounts");
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(data, new TypeReference<ArrayList<BalanceAccount>>() {});
        } catch (IOException | KFZClient.KFZServerException ex) {
            Logger.getLogger(BalanceAccount.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public static BalanceAccount getOneFromDB(int recId){
        BalanceAccount result = null;
        Statement stmt = TestDataFromDB.getStatement();
        if (stmt != null){
            try {
                ResultSet set = stmt.executeQuery("select * from bal_accounts where rec_id = " + recId);
                BalanceAccount balAccount = new BalanceAccount();
                while(set.next()){
                    balAccount.setBalAcc(set.getInt(2));
                    balAccount.setDescrip_ka(set.getString(3));
                    balAccount.setDescrip_en(set.getString(4));
                    balAccount.setActPas(set.getBoolean(5));
                }
                result = balAccount;
            } catch (SQLException ex) {
                Logger.getLogger(BalanceAccount.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }
    
    public static void saveOneToDB(BalanceAccount balAccount){
        System.out.println("save balAccount ...???");
    }
    
    // Properties getters:
    public StringProperty codeProperty(){
        return balAcc;
    }
    
    public StringProperty currDescripProperty(){
        return currDescrip;
    }
    
    public BooleanProperty actPasProperty(){
        return actPas;
    }
    

    // Getters:
    public boolean getActPas() {
        return actPas.get();
    }

    public boolean getLevel() {
        return level;
    }

    public boolean getIsBase() {
        return isBase;
    }
    
    public int getBalAcc(){
        return (balAcc.isNotEmpty().get()) ? Integer.parseInt(balAcc.get()) : 0;
    }
    
    /**
     * For JSON
     * @return 
     */
    public String getDescrip_ka(){
        return descrip_ka.get();
    }
    
    /**
     * For JSON
     * @return 
     */
    public String getDescrip_en(){
        return descrip_en.get();
    }
    
    public String getAViewColumText(){
        return aviewColumnText.get();
    }

    
    // Setters:
    public void setActPas(boolean act_pas) {
        this.actPas.set(act_pas);
    }

    public void setLevel(boolean level) {
        this.level = level;
    }

    public void setIsBase(boolean is_base) {
        this.isBase = is_base;
    }
    
    public void setBalAcc(int code){
        this.balAcc.set("" + code);
    }
    
    /**
     * For JSON
     * @param descrip New Description for Georgia.
     */
    public void setDescrip_ka(String descrip){
        this.descrip_ka.set(descrip);
    }
    
    /**
     * For JSON
     * @param descrip New Description for English.
     */
    public void setDescrip_en(String descrip){
        this.descrip_en.set(descrip);
    }
    

    
    @Override
    public BalanceAccount cloneWithoutID() {
        BalanceAccount clone = new BalanceAccount();
        clone.copyFrom(this);
        return clone;
    }

    @Override
    public BalanceAccount cloneWithID() {
        BalanceAccount clone = cloneWithoutID();
        clone.setRecId(this.getRecId());
        return clone;
    }

    @Override
    public void copyFrom(EditorPanelable other) {
        BalanceAccount account = (BalanceAccount) other;
        setDescrip_ka(account.getDescrip_ka());
        setDescrip_en(account.getDescrip_en());
        setBalAcc(account.getBalAcc());
        setActPas(account.getActPas());
//        setLevel(account.getLevel());
//        setIsBase(account.getIsBase());
    }

    @Override
    public String toStringForSearch() {
        return aviewColumnText.get();
    }
    
    @Override
    public String toString(){
        return aviewColumnText.get();
    }

    public boolean compares(BalanceAccount balAccountBackup) {
        return  getActPas() == balAccountBackup.getActPas() &&
                getBalAcc() == balAccountBackup.getBalAcc() && 
                currDescripProperty().get().equals(balAccountBackup.currDescripProperty().get());
    }
    
    
    private class ActPasValueFactory implements Callback<TreeTableColumn.CellDataFeatures<BalanceAccount, String>, ObservableValue<String>> {

        public ActPasValueFactory(){}
        
        @Override
        public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<BalanceAccount, String> param) {
            String result = param.getValue().getValue().getActPas() ? "Act" : "Pas";
            System.out.println("result: " + result + "  actPas: " + (param.getValue().getValue().getActPas()));
            return new ReadOnlyStringWrapper(result);
        }
    }
}
