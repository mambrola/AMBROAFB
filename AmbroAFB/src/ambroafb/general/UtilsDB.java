/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general;

import ambroafb.countries.Country;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author murman
 */
public class UtilsDB {
    
         
    private static final String URL = "jdbc:derby:localDB;create=true;user=afb;password=afb";
    private static Connection connection;
    
    public static void initTables(){
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
            connection = DriverManager.getConnection(URL);
            Statement stm = connection.createStatement();
            stm.execute("create table filterDates(ID INTEGER not null primary key)");
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException ex) {
            Logger.getLogger(UtilsDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private static void createTables(String tableName){
        
    }
        
}
