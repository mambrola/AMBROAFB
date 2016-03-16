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
public class Product {
    
    public int productId;
    
    @AView.Column(title = "%descrip", width = "250")
    private SimpleStringProperty descrip;
    
    @AView.Column(title = "%remark", width = "550")
    private SimpleStringProperty remark;
        
    public Product(){
        descrip =   new SimpleStringProperty();
        remark =    new SimpleStringProperty();
    }
    
    
    public Product(Object[] values){
        productId = Utils.avoidNullAndReturnInt(   values[0]);
        setDescrip( Utils.avoidNullAndReturnString(values[1]));
        setRemark(  Utils.avoidNullAndReturnString(values[2]));
    }
    
    public Product(int pi, String d, String r){
        productId = pi;
        descrip = new SimpleStringProperty(d);
        remark = new SimpleStringProperty(r);
    }
    
    
    public static Product dbGetProduct (int productId){
        return dbGetProducts(productId).get(productId);
    }
    
    public static HashMap<Integer,Product> dbGetProducts (int productId){
        HashMap<Integer,Product> products = new HashMap();
        Utils.getArrayListsByQueryFromDB("SELECT * FROM products" +  (productId == 0 ? "" : " where rec_id = " + Integer.toString(productId)) + " ORDER BY rec_id", new String[]{"rec_id", "descrip", "remark"}).stream().forEach((row) -> {
            products.put((int) row[0], new Product(row));
        });
        return products;
    }
    
    public final void setDescrip(String descrip) {
        this.descrip.set(descrip);
    }
    public final void setRemark(String remark) {
        this.remark.set(remark);
    }
}
