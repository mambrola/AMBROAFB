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
import ambroafb.general.Utils;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.general.interfaces.TableColumnWidths;
import ambroafb.products.helpers.ProductDiscount;
import ambroafb.products.helpers.ProductSpecific;
import authclient.AuthServerException;
import authclient.db.ConditionBuilder;
import authclient.db.DBClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author mambroladze
 */
public class Product extends EditorPanelable {
    
    @AView.Column(width = "30")
    private final SimpleStringProperty abbreviation;
    
    @AView.Column(width = "30")
    private final SimpleStringProperty former;
    
    @AView.Column(title = "%descrip", width = "200")
    private final SimpleStringProperty descrip;
    
    @AView.Column(title = "%remark", width = "200")
    private final SimpleStringProperty remark;
    
    @AView.Column(title = "%product_specific", width = "200")
    private final SimpleStringProperty specificDescrip;
    private final IntegerProperty specific;
    private final ObjectProperty<ProductSpecific> productSpecific;
    
    @AView.Column(width = "50")
    private final SimpleStringProperty price;
    
    @AView.Column(title = "%iso", width = TableColumnWidths.ISO, styleClass = "textCenter")
    private final SimpleStringProperty iso;
    private final ObjectProperty<Currency> currency;
    
    @AView.Column(title = "%discounts", width = "80", cellFactory = DiscountCellFactory.class)
    private final ObservableList<ProductDiscount> discounts;
    
    @AView.Column(width = "35", cellFactory = ActPasCellFactory.class)
    private final SimpleBooleanProperty isActive;
    
    private static final String DB_VIEW_NAME = "products_whole";
    private static final String DB_SPECIFIC_TABLE_NAME = "product_specific_descrips";
    private static final String DB_TABLE_NAME = "products";
    
    
    public Product(){
        abbreviation = new SimpleStringProperty("");
        former = new SimpleStringProperty("");
        descrip = new SimpleStringProperty("");
        remark = new SimpleStringProperty("");
        specific = new SimpleIntegerProperty();
        specificDescrip = new SimpleStringProperty("");
        productSpecific = new SimpleObjectProperty<>();
        price = new SimpleStringProperty("");
        iso = new SimpleStringProperty("");
        currency = new SimpleObjectProperty<>();
        discounts = FXCollections.observableArrayList();
        isActive = new SimpleBooleanProperty();
        
        ProductSpecific spec = new ProductSpecific();
        productSpecific.set(spec);
        productSpecific.addListener((ObservableValue<? extends ProductSpecific> observable, ProductSpecific oldValue, ProductSpecific newValue) -> {
            if (newValue != null){
                specific.set(newValue.getRecId());
                specificDescrip.set(newValue.getDescrip());
            }
        });
        
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
    
    
    public static ArrayList<ProductSpecific> getAllSpecificsFromDB(){
        try {
            String data = GeneralConfig.getInstance().getDBClient().select(DB_SPECIFIC_TABLE_NAME, new ConditionBuilder().build()).toString();
            System.out.println("products specific data: " + data);
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(data, new TypeReference<ArrayList<ProductSpecific>>() {});
        } catch (IOException | AuthServerException ex) {
            Logger.getLogger(Product.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<>();
    }
    
    public static Product getOneFromDB (int productId){
        try {
            ConditionBuilder conditionBuilder = new ConditionBuilder().where().and("rec_id", "=", productId).condition();
            JSONArray data = GeneralConfig.getInstance().getDBClient().select(DB_VIEW_NAME, conditionBuilder.build());
            String productData = data.opt(0).toString();
            ObjectMapper mapper = new ObjectMapper();
            Product product = mapper.readValue(productData, Product.class);
            return product;
        } catch (IOException | AuthServerException ex) {
            Logger.getLogger(Product.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public static Product saveOneToDB(Product product){
        try {
            ObjectMapper mapper = new ObjectMapper();
            ObjectWriter writer = mapper.writer().withDefaultPrettyPrinter();
            JSONObject productJson = new JSONObject(writer.writeValueAsString(product));
            DBClient dbClient = GeneralConfig.getInstance().getDBClient();
            JSONObject newProduct = dbClient.callProcedureAndGetAsJson("general_insert_update", DB_TABLE_NAME, dbClient.getLang(), productJson).getJSONObject(0);
            return mapper.readValue(newProduct.toString(), Product.class);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(Product.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException | AuthServerException | JSONException ex) {
            Logger.getLogger(Product.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
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
    
    public ObjectProperty specificProperty(){
        return productSpecific;
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
    
    public int getSpecific(){
        return productSpecific.get().getRecId();
    }
    
    public String getSpecificDescrip(){
        return productSpecific.get().getDescrip();
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
    
    public void setSpecific(int specific){
        this.productSpecific.get().setRecId(specific);
    }
    
    public void setSpecificDescip(String specificDescrip){
        this.productSpecific.get().setDescrip(specificDescrip);
    }
    
    public void setPrice(double price) {
        this.price.set("" + price);
    }
    
    public void setIso(String iso){
        this.currency.setValue(Currency.getOneFromDB(iso));
    }
    
    public void setDiscounts(Collection<ProductDiscount> discounts) {
        this.discounts.setAll(discounts);
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
        specificProperty().set(product.specificProperty().get());
        //setSpecific(product.getSpecific());
        //setSpecificDescip(product.getSpecificDescrip());
        setPrice(product.getPrice());
        setIso(product.getIso());
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
     * The method equals two products.
     * Note: It is needed for ProductComboBox selectItem method.
     * @param other Product which must compare to this.
     * @return True if they are equals, false - otherwise.
     */
    public boolean equals(Product other){
        return this.compares(other);
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
                this.specificProperty().get().equals(productBackup.specificProperty().get()) &&
                //this.getSpecificDescrip().equals(productBackup.getSpecificDescrip()) &&
                this.getPrice() == productBackup.getPrice() &&
                this.getIso().equals(productBackup.getIso()) &&
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
