/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.products;

import ambro.AView;
import ambroafb.general.AlertMessage;
import ambroafb.general.GeneralConfig;
import ambroafb.general.Names;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Alert;

/**
 *
 * @author mambroladze
 */
public class Product { // ვინაიდან ეს მხოლოდ ჩვენებაა და თვითო tableView-ს ველში არ ხდება ჩასწორება Property-ები არ გვჭირდება
    
    public int productId;
    
    @AView.Column(title = "%descrip", width = "250")
    private SimpleStringProperty descrip;
    
    @AView.Column(title = "%remark", width = "550")
    private SimpleStringProperty remark;
        
    public Product(int pi, String d, String r){
        productId = pi;
        descrip = new SimpleStringProperty(d);
        remark = new SimpleStringProperty(r);
    }
    
    @Override
    public String toString(){
        return descrip+" : "+remark+" : "+ productId;
    }
            
    public String getDescrip() {
        return descrip.get();
    }
    
    public static Product dbGetProduct (int productId){
        return dbGetProducts(productId).get(productId);
    }
    
    public static HashMap<Integer,Product> dbGetProducts (int productId){
        HashMap<Integer,Product> products = new HashMap();
        String whereTest = productId == 0 ? "" : " where rec_id = " + Integer.toString(productId);
        try {
            Connection conn = GeneralConfig.getInstance().getConnectionToDB();
            ResultSet resultSet = conn.createStatement().executeQuery("SELECT * FROM products" +  whereTest + " ORDER BY rec_id");
            while (resultSet.next()) {
                int recId = resultSet.getInt("rec_id");
                String descrip = resultSet.getString("descrip");
                String remark = resultSet.getString("remark");
                products.put(recId, new Product(recId, descrip, remark));
            }
        }
        catch (SQLException | NullPointerException ex) {
            Platform.runLater(() -> {
                new AlertMessage(Alert.AlertType.ERROR, ex, Names.SQL_ERROR).showAlert();
            });
        }
        return products;
    }
    
}
