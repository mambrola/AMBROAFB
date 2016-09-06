/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.products;

import ambro.ANodeSlider;
import ambro.AView;
import ambroafb.clients.Client;
import ambroafb.general.AlertMessage;
import ambroafb.general.GeneralConfig;
import ambroafb.general.KFZClient;
import ambroafb.general.TestDataFromDB;
import ambroafb.general.Utils;
import ambroafb.general.interfaces.EditorPanelable;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
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
    
//    public boolean isOnlyGeo;
    
    @AView.Column(width = "35", cellFactory = AliveCellFactory.class)
    private final BooleanProperty isActive;
    
    @AView.Column(title = "%abbreviation", width = "30")
    private final StringProperty abbreviation;
    
    @AView.Column(title = "%product_former", width = "30")
    private final StringProperty former;
    
    @AView.Column(title = "%descrip", width = "200")
    private final SimpleStringProperty descrip;
    
    @AView.Column(title = "%product_specific", width = "120")
    private final StringProperty specific;
    // for specific
    private final StringProperty descrip_default;
    private final StringProperty descrip_first;
    private final StringProperty descrip_second;
    
    @AView.Column(width = "50")
    public double price;
    
    @AView.Column(title = "%discounts", width = "80", cellFactory = DiscountCellFactory.class)
    private ArrayList<ProductDiscount> discounts;
    
//    @AView.Column(title = "%discounts", width = "80", cellFactory = DiscountCellFactory.class)
//    private final StringProperty discount;
    
//    @AView.Column(title = "%product_vendor_code", width = "50", cellFactory = VendorCodeCellFactory.class)
//    public StringProperty vendorCode;
        
    
//    @AView.Column(title = "%remark", width = "500")
    private final SimpleStringProperty remark;
    
    public Product(){
        isActive = new SimpleBooleanProperty();
        abbreviation = new SimpleStringProperty("");
        former = new SimpleStringProperty("0");
//        specific = new SimpleStringProperty();
//        vendorCode = new SimpleStringProperty("");
        descrip_default = new SimpleStringProperty("");
        descrip_first = new SimpleStringProperty("");
        descrip_second = new SimpleStringProperty("");
        String lang = GeneralConfig.getInstance().getCurrentLocal().getLanguage();
        specific = (lang.equals("ka")) ? descrip_first : (lang.equals("en")) ? descrip_second : descrip_default;
        descrip = new SimpleStringProperty("");
        remark = new SimpleStringProperty("");
//        discount = new SimpleStringProperty("");
        discounts = new ArrayList<>();
    }
    
    // DBService methods:
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
    
    public static ArrayList<Product> getAllFromDBTest(){
        ArrayList<Product> result = new ArrayList<>();
        Statement stmt = TestDataFromDB.getStatement();
        
        try {
            ResultSet set = stmt.executeQuery("select products.rec_id, products.abbreviation, products.former, products.descrip, products.remark, products.price, products.is_active,\n" +
"                                                product_discounts.months, product_discounts.discount_rate,\n" +
"                                                product_specifics.descrip_default, product_specifics.descrip_first, product_specifics.descrip_second\n" +
"                                                from products\n" +
"                                                left join product_discounts\n" +
"												on products.rec_id = product_discounts.product_id\n" +
"                                                join product_specifics\n" +
"                                                on products.specific = product_specifics.rec_id;");
            Map<Integer, Product> ids = new HashMap<>();
            while(set.next()) {
                Product pr = new Product();
                int rec_id = set.getInt(1);
                
                if (ids.containsKey(rec_id)) {
                    Product product = ids.get(rec_id);
                    ProductDiscount disc = new ProductDiscount();
                    disc.months = set.getInt(8);
                    System.out.println("aq? 2: " + disc.months);
                    disc.discount = set.getDouble(9);
                    product.getDiscounts().add(disc);
                } else {
                    ids.put(rec_id, pr);
                    pr.setRecId(rec_id);
                    pr.setIsActive(set.getBoolean(7));
                    pr.setAbbreviation(set.getString(2));
                    pr.setFormer(set.getInt(3));
                    pr.setDescrip(set.getString(4));
                    System.out.println("rec_id: " + rec_id + "  "  + pr.getDescrip());
                    pr.setPrice(set.getDouble(6));
                    if (set.getObject(8) != null) {
                        System.out.println("aq? " + set.getInt(8));
                        ProductDiscount disc = new ProductDiscount();
                        disc.months = set.getInt(8);
                        disc.discount = set.getDouble(9);
                        pr.getDiscounts().add(disc);
                    }
//                    pr.setDescrip_default(set.getString(10));
//                    pr.setDescrip_first(set.getString(11));
//                    pr.setDescrip_second(set.getString(12));
                    pr.setSpecific(set.getString(10));
                    result.add(pr);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(Product.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return result;
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
    
    
    // Get properties:
    public BooleanProperty isAliveProperty(){
        return isActive;
    }
    
    public StringProperty abbreviationProperty(){
        return abbreviation;
    }
    
    public StringProperty formerProperty(){
        return former;
    }
    
    public StringProperty specificProperty(){
        return specific;
    }
    
//    public StringProperty vendorCodeProperty(){
//        return vendorCode;
//    }
    
    public SimpleStringProperty descriptionProperty(){
        return descrip;
    }
    
    public SimpleStringProperty remarkProperty(){
        return remark;
    }
    
    // Getters:
    public boolean getIsActive() {
        return isActive.get();
    }
    
    public String getAbbreviation(){
        return abbreviation.get();
    }
    
    public int getFormer(){
        return Utils.getIntValueFor(former.get());
    }
    
    public String getSpecific(){
        return specific.get();
    }
    
    public String getDescrip() {
        return descrip.get();
    }
    
    public String getDescrip_default() {
        return descrip.get();
    }
    
    public String getDescrip_first() {
        return descrip.get();
    }
    
    public String getDescrip_second() {
        return descrip.get();
    }
    
//    public String getVendorCode() {
//        return vendorCode.get();
//    }
    
    public double getPrice() {
        return price;
    }
    
//    public boolean getIsOnlyGeo() {
//        return isOnlyGeo;
//    }

    public ArrayList<ProductDiscount> getDiscounts() {
        return discounts;
    }
    
    public String getRemark() {
        return remark.get();
    }

    
    // Setters:
    public void setIsActive(boolean isActive) {
        this.isActive.set(isActive);
    }

    public void setAbbreviation(String abbreviation){
        this.abbreviation.set(abbreviation);
    }
    
    public void setFormer(int former){
        this.former.set("" + former);
    }
    
    public void setSpecific(String specific){
        this.specific.set(specific);
    }

    public void setDescrip(String descrip) {
        this.descrip.set(descrip);
    }
    
    public void setDescrip_default(String descrip) {
        this.descrip.set(descrip);
    }
    
    public void setDescrip_first(String descrip) {
        this.descrip.set(descrip);
    }
    
    public void setDescrip_second(String descrip) {
        this.descrip.set(descrip);
    }
    
//    public void setVendorCode(String code) {
//        this.vendorCode.set(code);
//    }
    
    public void setPrice(double price) {
        this.price = price;
    }

//    public void setIsOnlyGeo(boolean isOnlyGeo) {
//        this.isOnlyGeo = isOnlyGeo;
//    }
    
    public void setDiscounts(ArrayList<ProductDiscount> discounts) {
        this.discounts = discounts;
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
        setIsActive(product.getIsActive());
        setAbbreviation(product.getAbbreviation());
        setFormer(product.getFormer());
        setDescrip(product.getDescrip());
        setSpecific(product.getSpecific());
        setPrice(product.getPrice());
        setDiscounts(product.getDiscounts());
//        setRemark(product.getRemark());
    }

    @Override
    public String toStringForSearch() {
        return getDescrip().concat(getRemark());
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
        return  this.getIsActive() == productBackup.getIsActive() &&
                this.getAbbreviation().equals(productBackup.getAbbreviation()) &&
                this.getFormer() == productBackup.getFormer() &&
                this.getDescrip().equals(productBackup.getDescrip()) &&
                this.getSpecific().equals(productBackup.getSpecific()) &&
                this.getPrice() == productBackup.getPrice() &&
                // valuta ??
                compareDiscounts(getDiscounts(), productBackup.getDiscounts());
//                this.getRemark().equals(productBackup.getRemark());
    }

    private boolean compareDiscounts(ArrayList<ProductDiscount> discounts, ArrayList<ProductDiscount> backupDiscounts) {
        if (discounts.size() != backupDiscounts.size()) return false;
        for (int i = 0; i < discounts.size(); i++) {
            if (!discounts.get(i).equals(backupDiscounts.get(i))) return false;
        }
        return true;
    }
    
    
    // Private classes:
    public static class ProductDiscount {
        
        public int months;
        public double discount;
        
        public boolean equals(ProductDiscount other){
            return months == other.months && discount == other.discount;
        }
        
        @Override
        public String toString(){
            return months + " : " + discount;
        }
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

    public static class DiscountCellFactory implements Callback<TableColumn<Product, ArrayList<ProductDiscount>>, TableCell<Product, ArrayList<ProductDiscount>>> {

        @Override
        public TableCell<Product, ArrayList<ProductDiscount>> call(TableColumn<Product, ArrayList<ProductDiscount>> param) {
            return new TableCell<Product, ArrayList<ProductDiscount>>() {
                @Override
                public void updateItem(ArrayList<ProductDiscount> discounts, boolean empty) {
                    if (discounts == null || discounts.isEmpty() || empty){
                        setGraphic(null);
                    }
                    else {
                        ANodeSlider<Label> nodeSlider = new ANodeSlider<>();
                        for (ProductDiscount dis : discounts) {
                            Label discLabel = new Label(dis.toString());
                            nodeSlider.getItems().add(discLabel);
                        }
                        setGraphic(nodeSlider);
                        alignmentProperty().set(Pos.CENTER);
                    }
                }
            };
        }
    }
}
