/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.products;

import ambro.AView;
import ambroafb.clients.Client;
import ambroafb.general.GeneralConfig;
import ambroafb.general.KFZClient;
import ambroafb.general.Utils;
import ambroafb.general.interfaces.EditorPanelable;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
//import org.json.JSONArray;
//import org.json.JSONException;

/**
 *
 * @author mambroladze
 */
public class Product extends EditorPanelable {
    
    public int parentId;
    public double price;
    public boolean isOnlyGeo, isActive;
    private ArrayList<ProductDiscount> discounts;
    
    
    @AView.Column(title = "%descrip", width = "250")
    private SimpleStringProperty descrip;
    
    @AView.Column(title = "%remark", width = "550")
    private SimpleStringProperty remark;
        
    public Product(){
        descrip =   new SimpleStringProperty();
        remark =    new SimpleStringProperty();
    }
    
    
    public Product(Object[] values){
        recId = Utils.avoidNullAndReturnInt(   values[0]);
        setDescrip( Utils.avoidNullAndReturnString(values[1]));
        setRemark(  Utils.avoidNullAndReturnString(values[2]));
    }
    
    public Product(int pi, String d, String r){
        recId = pi;
        descrip = new SimpleStringProperty(d);
        remark = new SimpleStringProperty(r);
    }
    
    
    public static Product dbGetProduct (int productId){
        return null; //dbGetProducts(productId).get(productId);
    }
    
    public static ArrayList<Product> dbGetProducts (){
        try {
            String data = GeneralConfig.getInstance().getServerClient().get("products");
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(data, new TypeReference<ArrayList<Product>>() {
            });
        } catch (IOException | KFZClient.KFZServerException ex) {
            Logger.getLogger(Product.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public String toString() {
        return "Product: "+ descrip.get();
    }

    public ArrayList<ProductDiscount> getDiscounts() {
        return discounts;
    }

    public void setDiscounts(ArrayList<ProductDiscount> discounts) {
        this.discounts = discounts;
    }

    public String getDescrip() {
        return descrip.get();
    }

    public String getRemark() {
        return remark.get();
    }
    
    public void setDescrip(String descrip) {
        this.descrip.set(descrip);
    }
    public void setRemark(String remark) {
        this.remark.set(remark);
    }

    @Override
    public EditorPanelable cloneWithoutID() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public EditorPanelable cloneWithID() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void copyFrom(EditorPanelable other) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String toStringForSearch() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    public static class ProductDiscount {
        
        public int months;
        public double discount;
        
    }
}
