/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.balance_accounts;

import ambro.AFilterableTreeTableView;
import ambro.AView;
import ambroafb.general.GeneralConfig;
import ambroafb.general.Utils;
import ambroafb.general.interfaces.EditorPanelable;
import authclient.AuthServerException;
import authclient.db.ConditionBuilder;
import authclient.db.DBClient;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringExpression;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author dato
 */
public class BalanceAccount extends EditorPanelable {

    private final StringProperty balAcc;
    private final StringProperty descrip_ka;
    private final StringProperty descrip_en;
    private final IntegerProperty actPas;
    private boolean level;  // for old DB
    private boolean isBase; // for old DB
    
    @AView.Column(title = "%bal_accounts", width = "500")
    private final StringExpression descripExpression;
    @AView.Column(width = "60")
    private final StringExpression actPasExpression;
    
    private final StringProperty currDescrip;
    private final BooleanProperty indeterminateProperty;
    private final BooleanProperty actPasProperty;
    
    
    @AFilterableTreeTableView.Children
    @JsonIgnore
    public final ObservableList<BalanceAccount> childrenAccounts = FXCollections.observableArrayList();
    
    @AView.RowStyles
    @JsonIgnore
    public final ObservableList<String> rowStyle = FXCollections.observableArrayList();
    
    private static final String DB_TABLE_NAME = "bal_accounts";
    
    public BalanceAccount(){
        balAcc = new SimpleStringProperty("");
        descrip_ka = new SimpleStringProperty("");
        descrip_en = new SimpleStringProperty("");
        
        currDescrip = (GeneralConfig.getInstance().getCurrentLocal().getLanguage().equals("ka")) ? descrip_ka : descrip_en;
        descripExpression = Utils.avoidNull(balAcc).concat(" - ").concat(Utils.avoidNull(currDescrip));
        actPas = new SimpleIntegerProperty();
        indeterminateProperty = new SimpleBooleanProperty();
        actPasProperty = new SimpleBooleanProperty();
        
        actPas.addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            indeterminateProperty.set(newValue.intValue() == 3);
            actPasProperty.set(newValue.intValue() == 1);
        });
        actPasExpression = Bindings.when(indeterminateProperty).then("Act_Pas").
                            otherwise(Bindings.when(actPasProperty).then("Act").otherwise("Pas"));
    }
    
    
    public static ArrayList<BalanceAccount> getAllFromDB(){
        try {
            DBClient dbClient = GeneralConfig.getInstance().getDBClient();
            String allClientsData = dbClient.select(DB_TABLE_NAME, new ConditionBuilder().build()).toString();
//            String allClientsData = dbClient.callProcedureAndGetAsJson("general_select", DB_TABLE_NAME, dbClient.getLang(), new ConditionBuilder().build()).toString();
            
            System.out.println("ball acc all data: " + allClientsData);
            
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(allClientsData, new TypeReference<ArrayList<BalanceAccount>>() {});
        } 
        catch (IOException | AuthServerException ex) {
            Logger.getLogger(BalanceAccount.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public static BalanceAccount getOneFromDB(int recId){
        try {
            ConditionBuilder conditionBuilder = new ConditionBuilder().where().and("rec_id", "=", recId).condition();
            JSONArray data = GeneralConfig.getInstance().getDBClient().select(DB_TABLE_NAME, conditionBuilder.build());
            
            String currencyData = data.opt(0).toString();
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(currencyData, BalanceAccount.class);
        } catch (IOException | AuthServerException ex) {
            Logger.getLogger(BalanceAccount.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
        
//        BalanceAccount result = null;
//        Statement stmt = TestDataFromDB.getStatement();
//        if (stmt != null){
//            try {
//                ResultSet set = stmt.executeQuery("select * from bal_accounts where rec_id = " + recId);
//                BalanceAccount balAccount = new BalanceAccount();
//                while(set.next()){
//                    balAccount.setBalAcc(set.getInt(2));
//                    balAccount.setDescrip_ka(set.getString(3));
//                    balAccount.setDescrip_en(set.getString(4));
//                    balAccount.setActPas(set.getInt(5));
//                }
//                result = balAccount;
//            } catch (SQLException ex) {
//                Logger.getLogger(BalanceAccount.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//        return result;
    }
    
    public static BalanceAccount saveOneToDB(BalanceAccount balAccount){
        try {
            ObjectMapper mapper = new ObjectMapper();
            ObjectWriter writer = mapper.writer().withDefaultPrettyPrinter();
            JSONObject balAccountJson = new JSONObject(writer.writeValueAsString(balAccount));
            DBClient dbClient = GeneralConfig.getInstance().getDBClient();
            JSONObject newBalAccount = dbClient.callProcedureAndGetAsJson("general_insert_update", DB_TABLE_NAME, dbClient.getLang(), balAccountJson).getJSONObject(0);
            return mapper.readValue(newBalAccount.toString(), BalanceAccount.class);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(BalanceAccount.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException | AuthServerException | JSONException ex) {
            Logger.getLogger(BalanceAccount.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public static boolean deleteOneFromDB(int productId){
        System.out.println("delete balAccount ...???");
        return false;
    }
    
    
    // Properties getters:
    public StringProperty balAccProperty(){
        return balAcc;
    }
    
    public StringProperty currDescripProperty(){
        return currDescrip;
    }
    
    public BooleanProperty indeterminateProperty() {
        return indeterminateProperty;
    }

    public BooleanProperty actPasProperty() {
        return actPasProperty;
    }
    

    // Getters:
    public int getActPas() {
        return actPas.get();
    }

    public boolean getLevel() {
        return level;
    }

    public boolean getIsBase() {
        return isBase;
    }
    
    public int getBalAcc(){
        return Utils.getIntValueFor(balAcc.get());
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
        return descripExpression.get();
    }

    
    // Setters:
    public void setActPas(int act_pas) {
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
    }

    @Override
    public String toStringForSearch() {
        return descripExpression.get();
    }
    
    @Override
    public String toString(){
        return descripExpression.get();
    }

    public boolean compares(BalanceAccount balAccountBackup) {
        return  getBalAcc() == balAccountBackup.getBalAcc() && 
                currDescripProperty().get().equals(balAccountBackup.currDescripProperty().get()) &&
                getActPas() == balAccountBackup.getActPas();
    }
    
}
