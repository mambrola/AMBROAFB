/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.products;

import ambro.ANodeSlider;
import ambro.AView;
import ambroafb.general.TestDataFromDB;
import ambroafb.general.Utils;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.products.helpers.ProductDiscount;
import ambroafb.products.helpers.ProductSpecific;
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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

/**
 *
 * @author mambroladze
 */
public class Product extends EditorPanelable {
    
    @AView.Column(width = "30")
    private final StringProperty abbreviation;
    
    @AView.Column(width = "30")
    private final StringProperty former;
    
    @AView.Column(title = "%descrip", width = "200")
    private final SimpleStringProperty descrip;
    
    @AView.Column(title = "%remark", width = "200")
    private final SimpleStringProperty remark;
    
    @AView.Column(title = "%product_specific", width = "170")
    private final StringProperty specific;
    
    @AView.Column(width = "50")
    private final StringProperty price;
    
    @AView.Column(title = "%currency", width = "50")
    private final StringProperty currency;
    
    @AView.Column(title = "%discounts", width = "80", cellFactory = DiscountCellFactory.class)
    private ObservableList<ProductDiscount> discounts;
    
    @AView.Column(width = "35", cellFactory = AliveCellFactory.class)
    private final BooleanProperty isActive;
    
    public Product(){
        abbreviation = new SimpleStringProperty("");
        former = new SimpleStringProperty("");
        descrip = new SimpleStringProperty("");
        remark = new SimpleStringProperty("");
        specific = new SimpleStringProperty("");
        price = new SimpleStringProperty("");
        currency = new SimpleStringProperty("");
        discounts = FXCollections.observableArrayList();
        isActive = new SimpleBooleanProperty();
        
    }
    
    // DBService methods:
//    public static ArrayList<Product> getAllFromDB (){
//        try {
//            String data = GeneralConfig.getInstance().getServerClient().get("products");
//            ObjectMapper mapper = new ObjectMapper();
//            return mapper.readValue(data, new TypeReference<ArrayList<Product>>() {});
//        } catch (IOException | KFZClient.KFZServerException ex) {
//            Logger.getLogger(Product.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return null;
//    }
    
    public static ArrayList<Product> getAllFromDBTest(){
        ArrayList<Product> result = new ArrayList<>();
        Statement stmt = TestDataFromDB.getStatement();
        
        try {
            ResultSet set = stmt.executeQuery("select products.rec_id, products.abbreviation, products.former, products.descrip, products.remark, products.price, products.is_active, " +
                                                " product_discounts.months, product_discounts.discount_rate, " +
                                                " product_specifics.descrip_default, product_specifics.descrip_first, product_specifics.descrip_second " +
                                                " from products " +
                                                " left join product_discounts " +
                                                " on products.rec_id = product_discounts.product_id " +
                                                " join product_specifics " +
                                                " on products.specific = product_specifics.rec_id; ");
            Map<Integer, Product> ids = new HashMap<>();
            while(set.next()) {
                Product pr = new Product();
                int rec_id = set.getInt(1);
                
                if (ids.containsKey(rec_id)) {
                    Product product = ids.get(rec_id);
                    ProductDiscount disc = new ProductDiscount();
                    disc.setMonths(set.getInt(8));
                    disc.setDiscount(set.getDouble(9));
                    product.getDiscounts().add(disc);
                } else {
                    ids.put(rec_id, pr);
                    pr.setRecId(rec_id);
                    pr.setIsActive(set.getBoolean(7));
                    pr.setAbbreviation(set.getString(2));
                    pr.setFormer(set.getInt(3));
                    pr.setDescrip(set.getString(4));
                    pr.setRemark(set.getString(5));
                    pr.setPrice(set.getDouble(6));
                    if (set.getObject(8) != null) {
                        ProductDiscount disc = new ProductDiscount();
                        disc.setMonths(set.getInt(8));
                        disc.setDiscount(set.getDouble(9));
                        pr.getDiscounts().add(disc);
                    }
                    ProductSpecific spec = new ProductSpecific();
                    spec.descrip_default = set.getString(10);
                    spec.descrip_first = set.getString(11);
                    spec.descrip_second = set.getString(12);
                    pr.setSpecific(spec.getValue());

                    result.add(pr);
                }
            }
            stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(Product.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return result;
    }
    
//    public static Product getOneFromDB (int productId){
//        try {
//            String data = GeneralConfig.getInstance().getServerClient().get("products/" + productId);
//            ObjectMapper mapper = new ObjectMapper();
//            return mapper.readValue(data, Product.class);
//        } catch (IOException | KFZClient.KFZServerException ex) {
//            Logger.getLogger(Product.class.getName()).log(Level.SEVERE, null, ex);
//            new AlertMessage(Alert.AlertType.ERROR, ex, ex.getMessage(), "Product").showAlert();
//        }
//        return null;
//    }
    
    public static ArrayList<ProductSpecific> getAllSpecifics(){
        ArrayList<ProductSpecific> result = new ArrayList<>();
        Statement stmt = TestDataFromDB.getStatement();
        try {
            ResultSet set = stmt.executeQuery("select * from product_specifics");
            while(set.next()){
                ProductSpecific spec = new ProductSpecific();
                spec.descrip_default = set.getString(2);
                spec.descrip_first = set.getString(3);
                spec.descrip_second = set.getString(4);
                result.add(spec);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Product.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
    
    public static Product getOneFromDB (int productId){
        Product pr = null;
        Statement stmt = TestDataFromDB.getStatement();
        try {
            pr = new Product();
            ResultSet set = stmt.executeQuery("select 	  products.rec_id, products.abbreviation, products.former, products.descrip, products.remark, products.price, products.is_active, " +
                                                        " product_specifics.descrip_default, product_specifics.descrip_first, product_specifics.descrip_second " +
                                                        " from products " +
                                                        " join product_specifics " +
                                                        " on products.specific = product_specifics.rec_id " +
                                                        " where products.rec_id = " + productId);
            while(set.next()){
                pr.setRecId(set.getInt(1));
                pr.setAbbreviation(set.getString(2));
                pr.setFormer(set.getInt(3));
                pr.setDescrip(set.getString(4));
                pr.setRemark(set.getString(5));
                pr.setPrice(set.getDouble(6));
                pr.setIsActive(set.getBoolean(7));
                ProductSpecific spec = new ProductSpecific();
                spec.descrip_default = set.getString(8);
                spec.descrip_first = set.getString(9);
                spec.descrip_second = set.getString(10);
                pr.setSpecific(spec.getValue());
            }

            set = stmt.executeQuery("select * from product_discounts " +
                                " where product_id = " + productId);
            while(set.next()){
                ProductDiscount disc = new ProductDiscount();
                disc.setMonths(set.getInt(3));
                disc.setDiscount(set.getDouble(4));
                pr.getDiscounts().add(disc);
            }
            stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(Product.class.getName()).log(Level.SEVERE, null, ex);
        }
        return pr;
    }
    
//    public static Product saveOneToDB(Product product){
//        if (product == null) return null; 
//        try {
//            String resource = "products" + (product.recId > 0 ? "/" + product.recId : "");
//            String method = product.recId > 0 ? "PUT" : "POST";
//            ObjectMapper mapper = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);
//            String product_str = mapper.writeValueAsString(product);
//            
//            String res_str = GeneralConfig.getInstance().getServerClient().call(resource, method, product_str);
//            Product res = mapper.readValue(res_str, Product.class);
//            product.copyFrom(res);
//            if(product.getRecId() <= 0)
//                product.setRecId(res.getRecId());
//            return product;
//        } catch (IOException | KFZClient.KFZServerException ex) {
//            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
//            new AlertMessage(Alert.AlertType.ERROR, ex, ex.getMessage(), "Product").showAlert();
//        }
//        return null;
//    }
    
    public static Product saveOneToDB(Product product){
        System.out.println("save one to DB... ??");
        return null;
    }
    
//    public static boolean deleteOneFromDB(int productId){
//        try {
//            GeneralConfig.getInstance().getServerClient().call("products/" + productId, "DELETE", null);
//            return true;
//        } catch (IOException | KFZClient.KFZServerException ex) {
//            Logger.getLogger(Product.class.getName()).log(Level.SEVERE, null, ex);
//            new AlertMessage(Alert.AlertType.ERROR, ex, ex.getMessage(), "Product").showAlert();
//        }
//        return false;
//    }
    
    public static boolean deleteOneFromDB(int productId){
        System.out.println("delete from db...??");
        return false;
    }
    
    
    // Get properties:
    public StringProperty abbreviationProperty(){
        return abbreviation;
    }
    
    public StringProperty formerProperty(){
        return former;
    }
    
    public StringProperty descriptionProperty(){
        return descrip;
    }
    
    public StringProperty remarkProperty(){
        return remark;
    }
    
    public StringProperty priceProperty(){
        return price;
    }
    
    public StringProperty currencyProperty(){
        return currency;
    }
    
    public StringProperty specificProperty(){
        return specific;
    }
    
    public BooleanProperty isAliveProperty(){
        return isActive;
    }
    
    
    // Getters:
    public String getAbbreviation(){
        return abbreviation.get();
    }
    
    public int getFormer(){
        return Utils.getIntValueFor(former.get());
    }
    
    public String getDescrip() {
        return descrip.get();
    }
    
    public String getRemark() {
        return remark.get();
    }
    
    public String getSpecific(){
        return specific.get();
    }
    
    public double getPrice() {
        return Utils.getDoubleValueFor(price.get());
    }
    
    public String getCurrency(){
        return currency.get();
    }
    
    public ObservableList<ProductDiscount> getDiscounts() {
        return discounts;
    }
    
    public boolean getIsActive() {
        return isActive.get();
    }

    
    // Setters:
    public void setAbbreviation(String abbreviation){
        this.abbreviation.set(abbreviation);
    }
    
    public void setFormer(int former){
        this.former.set("" + former);
    }

    public void setDescrip(String descrip) {
        this.descrip.set(descrip);
    }
    
    public void setRemark(String remark) {
        this.remark.set(remark);
    }
    
    public void setSpecific(String specific){
        this.specific.set(specific);
    }
    
    public void setPrice(double price) {
        this.price.set("" + price);
    }
    
    public void setCurrency(String currency){
        this.currency.set(currency);
    }
    
    public void setDiscounts(ArrayList<ProductDiscount> discounts) {
        this.discounts = FXCollections.observableArrayList(discounts);
    }

    public void setIsActive(boolean isActive) {
        this.isActive.set(isActive);
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
        setAbbreviation(product.getAbbreviation());
        setFormer(product.getFormer());
        setDescrip(product.getDescrip());
        setRemark(product.getRemark());
        setPrice(product.getPrice());
        setCurrency(product.getCurrency());
        setSpecific(product.getSpecific());
        setIsActive(product.getIsActive());
        
        final ArrayList<ProductDiscount> productDiscounts = new ArrayList<>();
        product.getDiscounts().stream().forEach((disc) -> {
            productDiscounts.add(disc);
        });
        setDiscounts(productDiscounts);
    }

    @Override
    public String toStringForSearch() {
        return getAbbreviation().concat(getDescrip());
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
        return  this.getAbbreviation().equals(productBackup.getAbbreviation()) &&
                this.getFormer() == productBackup.getFormer() &&
                this.getDescrip().equals(productBackup.getDescrip()) &&
                this.getRemark().equals(productBackup.getRemark()) &&
                this.getPrice() == productBackup.getPrice() &&
                this.getCurrency().equals(productBackup.getCurrency()) &&
                this.getSpecific().equals(productBackup.getSpecific()) &&
                this.getIsActive() == productBackup.getIsActive() &&
                Utils.compareLists(getDiscounts(), productBackup.getDiscounts());
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

    public static class DiscountCellFactory implements Callback<TableColumn<Product, ObservableList<ProductDiscount>>, TableCell<Product, ObservableList<ProductDiscount>>> {

        @Override
        public TableCell<Product, ObservableList<ProductDiscount>> call(TableColumn<Product, ObservableList<ProductDiscount>> param) {
            return new TableCell<Product, ObservableList<ProductDiscount>>() {
                @Override
                public void updateItem(ObservableList<ProductDiscount> discounts, boolean empty) {
                    if (discounts == null || discounts.isEmpty() || empty){
                        setGraphic(null);
                    }
                    else {
                        ANodeSlider<Label> nodeSlider = new ANodeSlider<>();
                        discounts.stream().forEach((dis) -> {
                            nodeSlider.getItems().add(new Label(dis.toString()));
                        });
                        System.out.println("nodeSlider size: " + nodeSlider.getItems().size());
                        setGraphic(nodeSlider);
                        
//                        MapEditorComboBox<ProductDiscount> mapEditor = new MapEditorComboBox<>();
//                        mapEditor.setKeyPattern("(1[0-2]|[1-9])?");
//                        mapEditor.setValuePattern("((0|[1-9]{1,2})(\\.[0-9]*)?)?");
//                        mapEditor.setItemsCustom(discounts);
//                        setGraphic(mapEditor);
                    }
                }
            };
        }
    }
}
