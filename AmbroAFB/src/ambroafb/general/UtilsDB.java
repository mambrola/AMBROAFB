/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author murman
 */
public class UtilsDB {
    
    private static UtilsDB instance;
    private static final String DRIVER_NAME = "org.apache.derby.jdbc.EmbeddedDriver";
    private static final String URL = "jdbc:derby:localDB; create=true; user=afb; password=afb";
    private Connection connection;
    private static final String DP_TABLE_NAME = "defaultParameters";
    
    public static UtilsDB getInstance(){
        if (instance == null){
            instance = new UtilsDB();
        }
        return instance;
    }
    
    private UtilsDB(){
        try {
            Class.forName(DRIVER_NAME);
            connection = DriverManager.getConnection(URL);
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(UtilsDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void createLocalUsageTables(){
        String quary = "create table " + DP_TABLE_NAME + " ( " +
                                        " id int GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1) primary key, " +
                                        " target varchar(16)," +
                                        " type varchar(16)," +
                                        " json clob " +
                                    ")";
        boolean exec = executeQuery(quary);
    }

    private void addInitialValuesIntoDefaultParameters(String target, String type, String jsonStr){
        String query = "insert into " + DP_TABLE_NAME + "(target, type, json)" +
                        " values ('" + target + "', '" + type + "', '" + jsonStr + "')";
        executeQuery(query);
    }
    
    public JSONObject getDefaultParametersJson(String target, String type) {
        JSONObject result = null;
        String query = "select * from " + DP_TABLE_NAME +
                        " where target = '" + target + "' and type = '" + type + "'";
        try {
            Statement statement = connection.createStatement();
            ResultSet set = statement.executeQuery(query);
            while (set.next()){
                String jsonString = set.getString(4);
                result = new JSONObject(jsonString);
            }
        } catch (SQLException | JSONException ex) {
            Logger.getLogger(UtilsDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public void updateOrInsertDefaultParameters(String target, String type, JSONObject json) {
        JSONObject jsonValue = getDefaultParametersJson(target, type);
        if (jsonValue == null){
            addInitialValuesIntoDefaultParameters(target, type, json.toString());
        }
        else {
            String query = "update " + DP_TABLE_NAME +
                    " set json = '" + json.toString() + "' " +
                    " where target = '" + target + "' and type = '" + type + "'";

            executeQuery(query);
        }
    }
    
    private boolean executeQuery(String query){
        try {
            try (Statement statement = connection.createStatement()) {
                statement.execute(query);
            }
            return true;
        } catch (SQLException ex) {
//            Logger.getLogger(UtilsDB.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
}
