/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.balance_accounts;

import ambro.AFilterableTreeTableView;
import ambro.AView;
import ambroafb.general.DBUtils;
import ambroafb.general.Names;
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
import javafx.beans.value.ObservableValue;
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
    private final BooleanProperty activeProperty;
    
    
    @AFilterableTreeTableView.Children
    @JsonIgnore
    public final ObservableList<BalanceAccount> childrenAccounts = FXCollections.observableArrayList();
    
    @AView.RowStyles
    @JsonIgnore
    public final ObservableList<String> rowStyle = FXCollections.observableArrayList();
    
    @JsonIgnore
    public static final String DB_TABLE_NAME = "bal_accounts";
    public static final String DB_DELETE_PROC_NAME = "general_delete";
    public static final String DESCRIP_DELIMITER = " - ";
    
    @JsonIgnore 
    private static final int ACT = 1, PAS = 2, INDETERMINATE = 3;
    
    public BalanceAccount(){
        balAcc = new SimpleStringProperty("");
        descrip = new SimpleStringProperty("");
        actPas = new SimpleIntegerProperty(0);
        indeterminateProperty = new SimpleBooleanProperty();
        activeProperty = new SimpleBooleanProperty();
        
        actPasExpression = Bindings.when(indeterminateProperty).then(Names.BAL_ACCOUNT_ACT_PAS).
                            otherwise(Bindings.when(activeProperty).then(Names.BAL_ACCOUNT_ACT)
                                                                    .otherwise(Names.BAL_ACCOUNT_PAS));
    
        indeterminateProperty.addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (newValue){
                actPas.set(INDETERMINATE);
            }
        });
        
        activeProperty.addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            actPas.set((newValue) ? ACT : PAS);
        });
    }
    
    
    public static ArrayList<BalanceAccount> getAllFromDB(){
        JSONObject params = new ConditionBuilder().build();
        System.out.println("params: " + params);
        return DBUtils.getObjectsListFromDB(BalanceAccount.class, DB_TABLE_NAME, params);
    }
    
    public static BalanceAccount getOneFromDB(int recId){
        JSONObject params = new ConditionBuilder().where().and("rec_id", "=", recId).condition().build();
        return DBUtils.getObjectFromDB(BalanceAccount.class, DB_TABLE_NAME, params);
    }
    
    public static BalanceAccount saveOneToDB(BalanceAccount balAccount){
        if (balAccount == null) return null;
        return DBUtils.saveObjectToDBSimple(balAccount, DB_TABLE_NAME);
    }
    
    public static boolean deleteOneFromDB(int id){
        return DBUtils.deleteObjectFromDB(DB_DELETE_PROC_NAME, DB_TABLE_NAME, id);
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
        return activeProperty;
    }
    

    // Getters:
    public int getActPas() {
        return actPas.get();
    }

    public int getBalAcc(){
        return balAcc.get().isEmpty() ? 0 : Integer.parseInt(balAcc.get());
    }
    
    public String getDescrip(){
        return descrip.get();
    }
    
    
    // Setters:
    public void setActPas(int act_pas) {
        if (act_pas == INDETERMINATE){
            indeterminateProperty.set(true);
        }
        else {
            activeProperty.set(act_pas == ACT);
        }
        actPas.set(act_pas);
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
        return getShortDescirp(DESCRIP_DELIMITER).get();
    }

    public StringExpression getShortDescirp(String delimiter){
        return Bindings.when(balAccProperty().isEmpty()).
                            then(getDescrip()).
                            otherwise(getBalAcc() + delimiter + getDescrip());
    }
    
    @Override
    public boolean equals(Object other){
        if (other == null) return false;
        BalanceAccount otherBalAcc = (BalanceAccount)other;
        return  getRecId() == otherBalAcc.getRecId() || 
                getBalAcc() == otherBalAcc.getBalAcc();
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
