/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.products;

import ambro.ANodeSlider;
import ambro.AView;
import ambroafb.currencies.Currency;
import ambroafb.general.GeneralConfig;
import ambroafb.general.TestDataFromDB;
import ambroafb.general.Utils;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.general.interfaces.TableColumnWidths;
import ambroafb.products.helpers.ProductDiscount;
import ambroafb.products.helpers.ProductSpecific;
import authclient.AuthServerException;
import authclient.db.ConditionBuilder;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
    
    @AView.Column(title = "specDescrip", width = "200")
    private final SimpleStringProperty specificDescrip;
    
    @AView.Column(width = "50")
    private final StringProperty price;
    
    @AView.Column(title = "%iso", width = TableColumnWidths.ISO, styleClass = "textCenter")
    private final StringProperty iso;
    private final ObjectProperty<Currency> currency;
    
    @AView.Column(title = "%discounts", width = "80", cellFactory = DiscountCellFactory.class)
    private ObservableList<ProductDiscount> discounts;
    
    @AView.Column(width = "35", cellFactory = ActPasCellFactory.class)
    private final BooleanProperty isActive;
    
    private static final String DB_VIEW_NAME = "products_whole";
    
    public Product(){
        abbreviation = new SimpleStringProperty("");
        former = new SimpleStringProperty("");
        descrip = new SimpleStringProperty("");
        remark = new SimpleStringProperty("");
        specific = new SimpleStringProperty("");
        specificDescrip = new SimpleStringProperty("");
        price = new SimpleStringProperty("");
        iso = new SimpleStringProperty("");
        currency = new SimpleObjectProperty<>();
        discounts = FXCollections.observableArrayList();
        isActive = new SimpleBooleanProperty();
        
        currency.addListener((ObservableValue<? extends Currency> observable, Currency oldValue, Currency newValue) -> {
            if (newValue != null) {
                iso.set(newValue.getIso());
            }
        });
    }
    
    // DBService methods:
    public static ArrayList<Product> getAllFromDB (){
        try {
            String data = GeneralConfig.getInstance().getDBClient().select(DB_VIEW_NAME, new ConditionBuilder().build()).toString();
            System.out.println("products data: " + data);
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(data, new TypeReference<ArrayList<Product>>() {});
        } catch (IOException | AuthServerException ex) {
            Logger.getLogger(Product.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<>();
    }
    
    
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
                disc.setDays(set.getInt(3));
                disc.setDiscountRate(set.getDouble(4));
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
    
    public ObjectProperty currencyProperty(){
        return currency;
    }
    
    public StringProperty specificProperty(){
        return specific;
    }
    
    public StringProperty specificDescripProperty(){
        return specificDescrip;
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
    
    public String getSpecificDescrip(){
        return specificDescrip.get();
    }
    
    public double getPrice() {
        return Utils.getDoubleValueFor(price.get());
    }
    
    public String getIso(){
        return iso.get();
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
    
    public void setSpecificDescip(String specificDescrip){
        this.specificDescrip.set(specificDescrip);
    }
    
    public void setPrice(double price) {
        this.price.set("" + price);
    }
    
    public void setIso(String iso){
        this.currency.setValue(Currency.getOneFromDB(iso));
    }
    
    public void setDiscounts(Collection<ProductDiscount> discounts) {
        this.discounts.addAll(discounts);
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
        setIso(product.getIso());
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
                this.getIso().equals(productBackup.getIso()) &&
                this.getSpecific().equals(productBackup.getSpecific()) &&
                this.getIsActive() == productBackup.getIsActive() &&
                Utils.compareLists(getDiscounts(), productBackup.getDiscounts());
    }
    
    public static class DiscountCellFactory implements Callback<TableColumn<Product, ObservableList<ProductDiscount>>, TableCell<Product, ObservableList<ProductDiscount>>> {

        @Override
        public TableCell<Product, ObservableList<ProductDiscount>> call(TableColumn<Product, ObservableList<ProductDiscount>> param) {
            return new TableCell<Product, ObservableList<ProductDiscount>>() {
                @Override
                public void updateItem(ObservableList<ProductDiscount> discounts, boolean empty) {
                    setEditable(false);
                    if (discounts == null || discounts.isEmpty() || empty){
                        setText(null);
                    }
                    else {
                        ANodeSlider<Label> nodeSlider = new ANodeSlider<>();
                        discounts.stream().forEach((dis) -> {
                            nodeSlider.getItems().add(new Label(dis.toString()));
                        });
                        setGraphic(nodeSlider);
                    }
                }
            };
        }
    }
    
    public static class ActPasCellFactory implements Callback<TableColumn<Product, Boolean>, TableCell<Product, Boolean>> {

        @Override
        public TableCell<Product, Boolean> call(TableColumn<Product, Boolean> param) {
            return new TableCell<Product, Boolean>() {
                @Override
                public void updateItem(Boolean isAct, boolean empty) {
                    setText(empty ? null : (isAct ? "Act" : "Pas"));
                }
            };
        }
    }
}
