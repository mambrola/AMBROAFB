/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general;

import ambroafb.clients.filter.ClientFilter;
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
    
    
    
    public void createTables(){
        createFilterClientTable();
    }

    private void createFilterClientTable() {
        String filterClients = "create table filter_clients ( " +
                                        " id int primary key, " +
                                        " from_date varchar(16)," +
                                        " to_date varchar(16)" +
                                    ")";
        boolean exec = executeQuery(filterClients);
        if (exec){
            addDefaultValuesIntoFilterClients();
        }
    }
    
    private void addDefaultValuesIntoFilterClients(){
        String query = "insert into filter_clients " +
                        " values(1, '" + ClientFilter.dateBigerStr + "', '" + ClientFilter.dateLessStr + "')";
        executeQuery(query);
    }
    
    public JSONObject getFilterClientsDate() {
        JSONObject result = null;
        String query = "select * from filter_clients";
        try {
            Statement statement = connection.createStatement();
            ResultSet set = statement.executeQuery(query);
            set.next();
            String fromDate = set.getString(2);
            String toDate = set.getString(3);
            
            result = new JSONObject();
            result.put("dateBigger", fromDate);
            result.put("dateLess", toDate);
        } catch (SQLException | JSONException ex) {
            Logger.getLogger(UtilsDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public void updateFilterClients(String dateBigger, String dateLess) {
        String query = "update filter_clients " +
                " set from_date = '" + dateBigger + "', " +
                " to_date = '" + dateLess + "' " +
                " where id = 1";

        executeQuery(query);
    }
    
    private boolean executeQuery(String query){
        try {
            try (Statement statement = connection.createStatement()) {
                statement.execute(query);
            }
            return true;
        } catch (SQLException ex) {
            return false;
        }
    }
}
