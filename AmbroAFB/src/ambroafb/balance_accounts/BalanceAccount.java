/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.balance_accounts;

import ambro.AFilterableTreeTableView;
import ambro.AView;
import ambroafb.general.Names;
import ambroafb.general.interfaces.EditorPanelable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    private final StringExpression actPasExpression;
    
    private final BooleanProperty indeterminateProperty;
    private final BooleanProperty activeProperty;
    private final IntegerProperty level;
    private int parentRecId;
    
    
    @AFilterableTreeTableView.Children
    @JsonIgnore
    public final ObservableList<BalanceAccount> childrenAccounts = FXCollections.observableArrayList();
    
    @AView.RowStyles
    @JsonIgnore
    public final ObservableList<String> rowStyle = FXCollections.observableArrayList();
    
    @JsonIgnore
    public static final String DESCRIP_DELIMITER = " - ";
    
    private static final int ACT = 1, PAS = 2, INDETERMINATE = 3;
    
    public BalanceAccount(){
        balAcc = new SimpleStringProperty("");
        descrip = new SimpleStringProperty("");
        actPas = new SimpleIntegerProperty(0);
        indeterminateProperty = new SimpleBooleanProperty();
        activeProperty = new SimpleBooleanProperty();
        level = new SimpleIntegerProperty();
        
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
    
    public IntegerProperty levelProperty(){
        return level;
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
    
    @JsonIgnore
    public int getLevel(){
        return level.get();
    }
    
    @JsonIgnore
    public int getParentRecId(){
        return parentRecId;
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
    
    @JsonProperty
    public void setLevel(int level){
        this.level.set(level);
    }
    
    @JsonProperty
    public void setParentRecId(int parentRecId){
        this.parentRecId = parentRecId;
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
    
    
    @JsonIgnore
    public boolean isLeaf(){
        return getLevel() == 0;
    }
    

}
