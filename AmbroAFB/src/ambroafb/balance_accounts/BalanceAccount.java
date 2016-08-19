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
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author dato
 */
public class BalanceAccount extends EditorPanelable {

    private final StringProperty descrip_ka;
    private final StringProperty descrip_en;
    private boolean actPas;
    private boolean level;
    private boolean isBase;
    private final StringProperty balAcc; // This place is because of balAccountDescrip binding.
    
    @AView.Column(title = "%bal_accouns", width = "800")
    private final StringProperty aviewColumnText;
    
    private final StringProperty currDescrip;
    
    @AFilterableTreeTableView.Children
    @JsonIgnore
    public final ObservableList<EditorPanelable> childrenAccounts = FXCollections.observableArrayList();
    
    @AView.RowStyles
    @JsonIgnore
    public final ObservableList<String> rowStyle = FXCollections.observableArrayList();
    
    private String langId;
    
    public BalanceAccount(){
        balAcc = new SimpleStringProperty();
        descrip_ka = new SimpleStringProperty();
        descrip_en = new SimpleStringProperty();
        aviewColumnText = new SimpleStringProperty();
        currDescrip = new SimpleStringProperty();
        langId = GeneralConfig.getInstance().getCurrentLocal().getLanguage();
        
//        currDescrip.bind(Bindings.createStringBinding(() -> {
//            return ((langId.equals("ka")) ? descrip_ka.get() : descrip_en.get());
//        }, balAcc));
        aviewColumnText.bind(Bindings.createStringBinding(() -> {
            return balAcc.get() + " - " + currDescripProperty().get();
        }, balAcc));
//        aviewColumnText.bind(Bindings.createStringBinding(() -> {
//            return balAcc.get() + " - " + descrip_ka.get();
//        }, descrip_ka));
//        aviewColumnText.bind(Bindings.createStringBinding(() -> {
//            return balAcc.get() + " - " + descrip_en.get();
//        }, descrip_en));
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
    
    
    // Properties getters:
    public StringProperty codeProperty(){
        return balAcc;
    }
    
    public StringProperty currDescripProperty(){
        return ((langId.equals("ka")) ? descrip_ka : descrip_en);
    }

    public boolean getActPas() {
        return actPas;
    }

    public boolean getLevel() {
        return level;
    }

    public boolean getIsBase() {
        return isBase;
    }
    
    
    // Getters:
    public int getBalAcc(){
        return Integer.parseInt(balAcc.get());
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

    public void setActPas(boolean act_pas) {
        this.actPas = act_pas;
    }

    public void setLevel(boolean level) {
        this.level = level;
    }

    public void setIsBase(boolean is_base) {
        this.isBase = is_base;
    }
    
    
    // Setters:
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
    
    public void setAViewColumnText(String descrip){
        this.aviewColumnText.set(descrip);
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
//        setAViewColumnText(account.getAViewColumText());
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
        System.out.println("curr bal_code: " + getBalAcc() + "  backup code: " + balAccountBackup.getBalAcc());
        System.out.println("curr column: " + currDescripProperty().get() + "  backup column: " + balAccountBackup.currDescripProperty().get());
        System.out.println("curr act_pas: " + getActPas() + "  backup column: " + balAccountBackup.getActPas());
        
        return  getBalAcc() == balAccountBackup.getBalAcc() && 
                currDescripProperty().get().equals(balAccountBackup.currDescripProperty().get());
    }
    
}
