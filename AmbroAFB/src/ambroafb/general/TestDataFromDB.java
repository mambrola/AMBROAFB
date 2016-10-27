/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dato
 */
public class TestDataFromDB {

    public static Statement getStatement() {
        try {
            Class.forName(GeneralConfig.classForName);
            String url = "jdbc:mysql://localhost:3307/db_kfz";
            String user = "root";
            String password = "Unabi11liB9leoa*1dh";
            Connection conn = DriverManager.getConnection(url, user, password);
            Statement stm = conn.createStatement();
            return stm;
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(TestDataFromDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
