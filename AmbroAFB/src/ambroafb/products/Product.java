/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.products;

import ambro.AView;
import ambroafb.clients.Client;
import ambroafb.general.AlertMessage;
import ambroafb.general.GeneralConfig;
import ambroafb.general.KFZClient;
import ambroafb.general.interfaces.EditorPanelable;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Alert;
//import org.json.JSONArray;
//import org.json.JSONException;

/**
 *
 * @author mambroladze
 */
public class Product extends EditorPanelable {
    
    public IntegerProperty parentId = new SimpleIntegerProperty();
    public double price;
    public boolean isOnlyGeo, isActive;
    private ArrayList<ProductDiscount> discounts;
    
    
    @AView.Column(title = "%descrip", width = "250")
    private final SimpleStringProperty descrip = new SimpleStringProperty("");
    
    @AView.Column(title = "%remark", width = "550")
    private final SimpleStringProperty remark = new SimpleStringProperty("");
    
    private final ObjectProperty<Product> parentProperty = new SimpleObjectProperty<>();
    
    public Product(){
    }
    
    
//    public Product(Object[] values){
//        recId = Utils.avoidNullAndReturnInt(values[0]);
//        setDescrip( Utils.avoidNullAndReturnString(values[1]));
//        setRemark(  Utils.avoidNullAndReturnString(values[2]));
//    }
    
    public Product(int pi, String d, String r){
        recId = pi;
        descrip.set(d);
        remark.set(r);
    }
    
    public static Product saveOneToDB(Product product){
        if (product == null) return null; 
        try {
            String resource = "products" + (product.recId > 0 ? "/" + product.recId : "");
            String method = product.recId > 0 ? "PUT" : "POST";
            ObjectMapper mapper = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);
            String product_str = mapper.writeValueAsString(product);
            
            String res_str = GeneralConfig.getInstance().getServerClient().call(resource, method, product_str);
            Product res = mapper.readValue(res_str, Product.class);
            product.copyFrom(res);
            if(product.getRecId() <= 0)
                product.setRecId(res.getRecId());
            return product;
        } catch (IOException | KFZClient.KFZServerException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            new AlertMessage(Alert.AlertType.ERROR, ex, ex.getMessage()).showAlert();
        }
        return null;
    }
    
    public static boolean deleteOneFromDB(int productId){
        try {
            GeneralConfig.getInstance().getServerClient().call("products/" + productId, "DELETE", null);
            return true;
        } catch (IOException | KFZClient.KFZServerException ex) {
            Logger.getLogger(Product.class.getName()).log(Level.SEVERE, null, ex);
            new AlertMessage(Alert.AlertType.ERROR, ex, ex.getMessage()).showAlert();
        }
        return false;
    }
    
    public static Product getOneFromDB (int productId){
        try {
            String data = GeneralConfig.getInstance().getServerClient().get("products/" + productId);
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(data, Product.class);
        } catch (IOException | KFZClient.KFZServerException ex) {
            Logger.getLogger(Product.class.getName()).log(Level.SEVERE, null, ex);
            new AlertMessage(Alert.AlertType.ERROR, ex, ex.getMessage()).showAlert();
        }
        return null;
    }
    
    public static ArrayList<Product> getAllFromDB (){
        try {
            String data = GeneralConfig.getInstance().getServerClient().get("products");
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(data, new TypeReference<ArrayList<Product>>() {});
        } catch (IOException | KFZClient.KFZServerException ex) {
            Logger.getLogger(Product.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    
    // Get properties:
    public SimpleStringProperty descriptionProperty(){
        return descrip;
    }
    
    public SimpleStringProperty remarkProperty(){
        return remark;
    }
    
    public ObjectProperty<Product> getParentProperty() {
        return parentProperty;
    }
    
    public ObjectProperty<Product> parentProperty() {
        return parentProperty;
    }

    
    // Getters:
    public int getParentId() {
        return parentId.get();
    }
    
    public double getPrice() {
        return price;
    }
    
    public boolean getIsOnlyGeo() {
        return isOnlyGeo;
    }
    
    public boolean getIsActive() {
        return isActive;
    }

    public ArrayList<ProductDiscount> getDiscounts() {
        return discounts;
    }
    
    public String getDescrip() {
        return descrip.get();
    }

    public String getRemark() {
        return remark.get();
    }

    
    // Setters:
    public void setParentId(int parentId) {
        this.parentId.set(parentId);
        parentProperty.set(getOneFromDB(parentId));
    }
    
    public void setPrice(double price) {
        this.price = price;
    }

    public void setIsOnlyGeo(boolean isOnlyGeo) {
        this.isOnlyGeo = isOnlyGeo;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public void setDiscounts(ArrayList<ProductDiscount> discounts) {
        this.discounts = discounts;
    }

    public void setDescrip(String descrip) {
        this.descrip.set(descrip);
    }
    public void setRemark(String remark) {
        this.remark.set(remark);
    }

    
    // Overrides:
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
        setParentId(product.getParentId());
        setPrice(product.getPrice());
        setIsOnlyGeo(product.getIsOnlyGeo());
        setIsActive(product.getIsActive());
        setDiscounts(product.getDiscounts());
        setDescrip(product.getDescrip());
        setRemark(product.getRemark());
    }

    @Override
    public String toStringForSearch() {
        return descrip.concat(" " + remark.get()).get();
    }
    
    @Override
    public String toString() {
        return descrip.get();
    }

    
    /**
     * Method compares two products.
     * @param productBackup - other product.
     * @return  - True, if all comparable fields are equals, false otherwise.
     */
    public boolean compares(Product productBackup) {
        System.out.println("this.parentProperty.get(): " + this.parentProperty.get());
        System.out.println("productBackup.parentProperty.get(): " + productBackup.parentProperty.get());
        
        return this.getDescrip().equals(productBackup.getDescrip()) &&
               this.getRemark().equals(productBackup.getRemark());
    }
    
    
    // Private classes:
    public static class ProductDiscount {
        
        public int months;
        public double discount;
        
    }
}
