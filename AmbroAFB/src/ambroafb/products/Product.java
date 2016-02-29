/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.products;

import ambro.AView;
import ambroafb.general.Utils;
import java.util.HashMap;
import javafx.beans.property.SimpleStringProperty;

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
    
    public static Product dbGetProduct (int productId){
        return dbGetProducts(productId).get(productId);
    }
    
    public static HashMap<Integer,Product> dbGetProducts (int productId){
        HashMap<Integer,Product> products = new HashMap();
        String whereText = productId == 0 ? "" : " where rec_id = " + Integer.toString(productId);
        Utils.getArrayListsFromDB("SELECT * FROM products" +  whereText + " ORDER BY rec_id", new String[]{"rec_id", "descrip", "remark"}).stream().forEach((row) -> {
            products.put((int) row[0], new Product((int) row[0], (String) row[1],(String) row[2]));
        });
        return products;
    }
    
}
