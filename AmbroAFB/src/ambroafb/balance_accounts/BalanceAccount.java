/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.balance_accounts;

import ambro.ATreeTableView;
import ambro.AView;
import ambroafb.general.GeneralConfig;
import ambroafb.general.interfaces.EditorPanelable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleIntegerProperty;
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

    private final IntegerProperty code;
    private final StringProperty descrip;
    
    @AView.Column(title = "%bal_accouns", width = "800", cellValueFactory = ColumnValueFactory.class)
    private final StringProperty balanceAccounts;
    
    private static Connection con; // for test
    
    @ATreeTableView.Children
    public final ObservableList<EditorPanelable> childrenAccounts = FXCollections.observableArrayList();
    
    
    @AView.RowStyles
    public final ObservableList<String> rowStyle = FXCollections.observableArrayList();
    
    
    public BalanceAccount(){
        code = new SimpleIntegerProperty();
        descrip = new SimpleStringProperty();
        balanceAccounts = new SimpleStringProperty();
        balanceAccounts.bind(Bindings.createStringBinding(() -> {
            return code.get() + " | " + descrip.get();
        }, descrip));
    }
    
    public void addChildAccount(BalanceAccount child){
        childrenAccounts.add(child);
    }
    
    public void removeChildAccount(BalanceAccount child){
        childrenAccounts.remove(child);
    }
    
    public static ArrayList<BalanceAccount> getAllFromDBTest() {
        ArrayList<BalanceAccount> list = new ArrayList<>();
        
        connectToDBTest();
        Statement stm = getStatement();
        try {
            ResultSet set = stm.executeQuery("select * from bal_accounts");
            while(set.next()){
                BalanceAccount account = new BalanceAccount();
                account.setRecId(set.getInt(1));
                account.setCode(set.getInt(2));
                account.setDescrip(set.getString(4));
                list.add(account);
            }
        } catch (SQLException ex) {
            Logger.getLogger(BalanceAccount.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("[getAllFromDBTest] list size: " + list.size());
        return list;
    }
    
    private static void connectToDBTest(){
        try {
            Class.forName(GeneralConfig.classForName);
            String url = "jdbc:mysql://localhost:3307/kfz_db?useUnicode=true&characterEncoding=UTF-8";
            String user = "root";
            String password = "Unabi11liB9leoa*1dh";
            con = DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(BalanceAccount.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private static Statement getStatement(){
        Statement stm = null;
        try {
            stm = con.createStatement();
        } catch (SQLException ex) {
            Logger.getLogger(BalanceAccount.class.getName()).log(Level.SEVERE, null, ex);
        }
        return stm;
    }
    
    
    // Properties getters:
    public IntegerProperty codeProperty(){
        return code;
    }
    
    public StringProperty descripProperty(){
        return descrip;
    }
    
    
    // Getters:
    public int getCode(){
        return code.get();
    }
    
    public String getDescrip(){
        return descrip.get();
    }
    
    
    // Setters:
    public void setCode(int code){
        this.code.set(code);
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
        setCode(account.getCode());
        setDescrip(account.getDescrip());
    }

    @Override
    public String toStringForSearch() {
        return getCode() + getDescrip().toLowerCase();
    }
    
    @Override
    public String toString(){
        return getCode() + "|" + getDescrip();
    }
    
    private class ColumnValueFactory implements Callback<TreeTableColumn.CellDataFeatures<BalanceAccount, String>, ObservableValue<String>> {

        @Override
        public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<BalanceAccount, String> param) {
            return new ReadOnlyStringWrapper(param.getValue().getValue().toString());
        }
    }
}
