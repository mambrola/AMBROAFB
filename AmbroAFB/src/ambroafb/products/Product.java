/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.products;

import ambro.AView;
import ambroafb.general.GeneralConfig;
import ambroafb.general.KFZClient;
import ambroafb.general.Utils;
import ambroafb.general.interfaces.EditorPanelable;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
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
    
    public static Product saveOneToDB(Product product){
        return product;
    }
    
    public static boolean deleteOneFromDB(int productId){
//        try {
//            GeneralConfig.getInstance().getServerClient().call("clients/" + productId, "DELETE", null);
            return true;
//        } catch (IOException | KFZClient.KFZServerException ex) {
//            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
//            new AlertMessage(Alert.AlertType.ERROR, ex, ex.getMessage()).showAlert();
//        }
//        return false;
    }
    
    public static Product getOneFromDB (int productId){
        return null;
    }
    
    public static ArrayList<Product> getAllFromDB (){
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

    public SimpleStringProperty descriptionProperty(){
        return descrip;
    }
    
    public SimpleStringProperty remarkProperty(){
        return remark;
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
    public Product cloneWithoutID() {
        Product clone = new Product();
        clone.copyFrom(this);
        return clone;
    }

    @Override
    public Product cloneWithID() {
        Product clone = cloneWithoutID();
        clone.setRecId(this.getRecId());
        return clone;
    }

    @Override
    public void copyFrom(EditorPanelable other) {
        Product product = (Product) other;
        setDescrip(product.getDescrip());
        setRemark(product.getRemark());
    }

    @Override
    public String toStringForSearch() {
        return descrip.concat(" " + remark.get()).get();
    }

    public boolean compares(Product productBackup) {
        return this.getDescrip().equals(productBackup.getDescrip()) && this.getRemark().equals(productBackup.getRemark());
    }
    
    
    public static class ProductDiscount {
        
        public int months;
        public double discount;
        
    }
}
