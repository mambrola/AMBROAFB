/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.balance_accounts;

import ambro.AFilterableTreeTableView;
import ambro.AView;
import ambroafb.general.DBUtils;
import ambroafb.general.Utils;
import ambroafb.general.interfaces.EditorPanelable;
import authclient.db.ConditionBuilder;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringExpression;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.json.JSONObject;

/**
 *
 * @author dato
 */
public class BalanceAccount extends EditorPanelable {

    private final StringProperty balAcc;
    private final IntegerProperty actPas;
    
    @AView.Column(title = "%bal_accounts", width = "500")
    private final StringProperty descrip;
    @AView.Column(width = "60")
    @JsonIgnore
    private final StringExpression actPasExpression;
    
    @JsonIgnore
    private final BooleanProperty indeterminateProperty;
    @JsonIgnore
    private final BooleanProperty actPasProperty;
    
    
    @AFilterableTreeTableView.Children
    @JsonIgnore
    public final ObservableList<BalanceAccount> childrenAccounts = FXCollections.observableArrayList();
    
    @AView.RowStyles
    @JsonIgnore
    public final ObservableList<String> rowStyle = FXCollections.observableArrayList();
    
    @JsonIgnore
    private static final String DB_TABLE_NAME = "bal_accounts";
    
    public BalanceAccount(){
        balAcc = new SimpleStringProperty("");
        descrip = new SimpleStringProperty("");
        actPas = new SimpleIntegerProperty(0);
        indeterminateProperty = new SimpleBooleanProperty();
        actPasProperty = new SimpleBooleanProperty();
        
        actPasExpression = Bindings.when(indeterminateProperty).then("Act_Pas").
                            otherwise(Bindings.when(actPasProperty).then("Act").otherwise("Pas"));
    }
    
    
    public static ArrayList<BalanceAccount> getAllFromDB(){
        JSONObject params = new ConditionBuilder().build();
        return DBUtils.getObjectsListFromDB(BalanceAccount.class, DB_TABLE_NAME, params);
    }
    
    public static BalanceAccount getOneFromDB(int recId){
        JSONObject params = new ConditionBuilder().where().and("rec_id", "=", recId).condition().build();
        return DBUtils.getObjectFromDB(BalanceAccount.class, DB_TABLE_NAME, params);
    }
    
    public static BalanceAccount saveOneToDB(BalanceAccount balAccount){
        if (balAccount == null) return null;
        return DBUtils.saveObjectToDBSimple(balAccount, DB_TABLE_NAME);
        
//        if (balAccount == null) return null;
//        try {
//            ObjectMapper mapper = new ObjectMapper();
//            ObjectWriter writer = mapper.writer().withDefaultPrettyPrinter();
//            JSONObject balAccountJson = new JSONObject(writer.writeValueAsString(balAccount));
//            DBClient dbClient = GeneralConfig.getInstance().getDBClient();
//            JSONObject newBalAccount = dbClient.callProcedureAndGetAsJson("general_insert_update_simpledate", DB_TABLE_NAME, dbClient.getLang(), balAccountJson).getJSONObject(0);
//            return mapper.readValue(newBalAccount.toString(), BalanceAccount.class);
//        } catch (JsonProcessingException ex) {
//            Logger.getLogger(BalanceAccount.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (IOException | AuthServerException | JSONException ex) {
//            Logger.getLogger(BalanceAccount.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return null;
    }
    
    public static boolean deleteOneFromDB(int productId){
        System.out.println("delete balAccount ...???");
        return false;
    }
    
    
    // Properties getters:
    public StringProperty balAccProperty(){
        return balAcc;
    }
    
    public StringProperty descripProperty(){
        return descrip;
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

    public int getBalAcc(){
        return Utils.getIntValueFor(balAcc.get());
    }
    
    public String getDescrip(){
        return descrip.get();
    }
    
    
    // Setters:
    public void setActPas(int act_pas) {
        this.actPas.set(act_pas);
    }

    public void setBalAcc(int code){
        this.balAcc.set("" + code);
    }
    
    public void setDescrip(String descrip){
        this.descrip.set(descrip);
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
        setBalAcc(account.getBalAcc());
        setActPas(account.getActPas());
        setDescrip(account.getDescrip());
    }

    @Override
    public String toStringForSearch() {
        return getDescrip();
    }
    
    @Override
    public String toString(){
        return getDescrip();
    }

    /**
     *
     * @param backup
     * @return
     */
    @Override
    public boolean compares(EditorPanelable backup) {
        BalanceAccount balAccountBackup = (BalanceAccount) backup;
        return  getBalAcc() == balAccountBackup.getBalAcc() && 
                getActPas() == balAccountBackup.getActPas() &&
                getDescrip().equals(balAccountBackup.getDescrip());
    }
    
}
