/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general;

import ambroafb.clients.filter.ClientFilter;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
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
    private static final String URL = "jdbc:derby:localDB;create=true;user=afb;password=afb";
    private Connection connection;
    private static final String TABLE_NAME = "filters";
    
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
    
    
    
    public void createLocalUsageTable(){
//        createFilterClientTable();
        String filterClients = "create table filters ( " +
                                        " id int primary key, " +
                                        " target varchar(16)," +
                                        " type varchar(16)," +
                                        " json clob " +
                                    ")";
        boolean exec = executeQuery(filterClients);
        if (exec){
            addDefaultValuesIntoFilters();
        }
    }

    private void addDefaultValuesIntoFilters(){
        String query = "insert into filters " +
                        " values (1, 'clients', 'filter', '{}')";
        executeQuery(query);
    }
    
    public JSONObject getFilterJson(String target, String type) {
        JSONObject result = null;
        String query = "select * from filters " +
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

    public void updateFilters(String target, String type, JSONObject json) {
        String query = "update filters " +
                " set json = '" + json.toString() + "' " +
                " where target = '" + target + "' and type = '" + type + "'";

        executeQuery(query);
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
