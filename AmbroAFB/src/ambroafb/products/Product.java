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
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;
//import org.json.JSONArray;
//import org.json.JSONException;

/**
 *
 * @author mambroladze
 */
public class Product extends EditorPanelable {
    
    public double price;
    public boolean isOnlyGeo;
    private ArrayList<ProductDiscount> discounts;
    
    @AView.Column(width = "35", cellFactory = AliveCellFactory.class)
    private final BooleanProperty isActive;
    
    @AView.Column(title = "%product_vendor_code", width = "50", cellFactory = VendorCodeCellFactory.class)
    public StringProperty vendorCode;
        
    @AView.Column(title = "%descrip", width = "250")
    private final SimpleStringProperty descrip;
    
    @AView.Column(title = "%remark", width = "500")
    private final SimpleStringProperty remark;
    
    public Product(){
        vendorCode = new SimpleStringProperty("");
        isActive = new SimpleBooleanProperty();
        descrip = new SimpleStringProperty("");
        remark = new SimpleStringProperty("");
    }
    
    // DBService methods:
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
            new AlertMessage(Alert.AlertType.ERROR, ex, ex.getMessage(), "Product").showAlert();
        }
        return null;
    }
    
    public static boolean deleteOneFromDB(int productId){
        try {
            GeneralConfig.getInstance().getServerClient().call("products/" + productId, "DELETE", null);
            return true;
        } catch (IOException | KFZClient.KFZServerException ex) {
            Logger.getLogger(Product.class.getName()).log(Level.SEVERE, null, ex);
            new AlertMessage(Alert.AlertType.ERROR, ex, ex.getMessage(), "Product").showAlert();
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
            new AlertMessage(Alert.AlertType.ERROR, ex, ex.getMessage(), "Product").showAlert();
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
    public StringProperty vendorCodeProperty(){
        return vendorCode;
    }
    
    public SimpleStringProperty descriptionProperty(){
        return descrip;
    }
    
    public SimpleStringProperty remarkProperty(){
        return remark;
    }

    public BooleanProperty isAliveProperty(){
        return isActive;
    }
    
    // Getters:
    public String getVendorCode() {
        return vendorCode.get();
    }
    
    public double getPrice() {
        return price;
    }
    
    public boolean getIsOnlyGeo() {
        return isOnlyGeo;
    }
    
    public boolean getIsActive() {
        return isActive.get();
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
    public void setVendorCode(String code) {
        this.vendorCode.set(code);
    }
    
    public void setPrice(double price) {
        this.price = price;
    }

    public void setIsOnlyGeo(boolean isOnlyGeo) {
        this.isOnlyGeo = isOnlyGeo;
    }

    public void setIsActive(boolean isActive) {
        this.isActive.set(isActive);
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
        setVendorCode(product.getVendorCode());
        setPrice(product.getPrice());
        setIsOnlyGeo(product.getIsOnlyGeo());
        setIsActive(product.getIsActive());
        setDiscounts(product.getDiscounts());
        setDescrip(product.getDescrip());
        setRemark(product.getRemark());
    }

    @Override
    public String toStringForSearch() {
        return descrip.concat(" " + remark.get()).get().toLowerCase();
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
        return this.getIsActive() == productBackup.getIsActive() &&
               this.getDescrip().equals(productBackup.getDescrip()) &&
               this.getVendorCode().equals(productBackup.getVendorCode()) &&
               this.getRemark().equals(productBackup.getRemark());
    }
    
    
    // Private classes:
    public static class ProductDiscount {
        
        public int months;
        public double discount;
        
    }
    
    public static class AliveCellFactory implements Callback<TableColumn<Product, Boolean>, TableCell<Product, Boolean>> {

        @Override
        public TableCell<Product, Boolean> call(TableColumn<Product, Boolean> param) {
            return new TableCell<Product, Boolean>() {
                @Override
                public void updateItem(Boolean isAlive, boolean empty) {
                    setText(empty ? null : (isAlive ? "Al" : null));
                    alignmentProperty().set(Pos.CENTER);
                }
            };
        }
    }

    public static class VendorCodeCellFactory implements Callback<TableColumn<Product, String>, TableCell<Product, String>> {

        @Override
        public TableCell<Product, String> call(TableColumn<Product, String> param) {
            return new TableCell<Product, String>() {
                @Override
                public void updateItem(String code, boolean empty) {
                    setText(empty ? null : code);
                    alignmentProperty().set(Pos.CENTER);
                }
            };
        }
    }
}
