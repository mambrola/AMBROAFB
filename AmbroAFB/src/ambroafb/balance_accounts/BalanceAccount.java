/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.balance_accounts;

import ambro.ATreeTableView;
import ambro.AView;
import ambroafb.general.GeneralConfig;
import ambroafb.general.KFZClient;
import ambroafb.general.interfaces.EditorPanelable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
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
    private final IntegerProperty balAcc; // This place is because of balAccountDescrip binding.
    
    @AView.Column(title = "%bal_accouns", width = "800")
    private final StringProperty balAccountDescrip;
    
    @ATreeTableView.Children
    @JsonIgnore
    public final ObservableList<EditorPanelable> childrenAccounts = FXCollections.observableArrayList();
    
    @AView.RowStyles
    @JsonIgnore
    public final ObservableList<String> rowStyle = FXCollections.observableArrayList();
    
    
    public BalanceAccount(){
        balAcc = new SimpleIntegerProperty();
        descrip_ka = new SimpleStringProperty();
        descrip_en = new SimpleStringProperty();
        balAccountDescrip = new SimpleStringProperty();
        balAccountDescrip.bind(Bindings.createStringBinding(() -> {
            String langId = GeneralConfig.getInstance().getCurrentLocal().getLanguage();
            return balAcc.get() + " - " + ((langId.equals("ka")) ? descrip_ka.get() : descrip_en.get());
        }, balAcc));
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
    
    
    // Properties getters:
    public IntegerProperty codeProperty(){
        return balAcc;
    }
    
    public StringProperty descripKaProperty(){
        return descrip_ka;
    }

    public StringProperty descripEnProperty() {
        return descrip_en;
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
        return balAcc.get();
    }
    
    public String getDescrip_ka(){
        return descrip_ka.get();
    }
    
    public String getDescrip_en(){
        return descrip_en.get();
    }
    
    public String getDescrip(){
        return balAccountDescrip.get();
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
        this.balAcc.set(code);
    }
    
    public void setDescrip_ka(String descrip){
        this.descrip_ka.set(descrip);
    }
    
    public void setDescrip_en(String descrip){
        this.descrip_en.set(descrip);
    }
    
    public void setDescrip(String descrip){
        this.balAccountDescrip.set(descrip);
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
        setDescrip(account.getDescrip());
        setActPas(account.getActPas());
        setLevel(account.getLevel());
        setIsBase(account.getIsBase());
    }

    @Override
    public String toStringForSearch() {
        return getBalAcc() + getDescrip().toLowerCase();
    }
    
    @Override
    public String toString(){
        return getBalAcc() + " - " + getDescrip();
    }
    
}
