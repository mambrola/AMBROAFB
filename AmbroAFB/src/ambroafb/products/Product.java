/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.products;

import ambro.ANodeSlider;
import ambro.AView;
import ambroafb.currencies.Currency;
import ambroafb.general.DBUtils;
import ambroafb.general.GeneralConfig;
import ambroafb.general.Utils;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.general.interfaces.TableColumnWidths;
import ambroafb.general.mapeditor.MapEditorElement;
import ambroafb.products.helpers.ProductDiscount;
import ambroafb.products.helpers.ProductSpecific;
import authclient.db.ConditionBuilder;
import authclient.db.DBClient;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;
import org.json.JSONObject;

/**
 *
 * @author mambroladze
 */
@SuppressWarnings("EqualsAndHashcode")
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
    @JsonIgnore
    private final ObjectProperty<ProductSpecific> productSpecific;
    
    @AView.Column(width = "50")
    private final SimpleStringProperty price;
    
    @AView.Column(title = "%iso", width = TableColumnWidths.ISO, styleClass = "textCenter")
    private final SimpleStringProperty iso;
    @JsonIgnore
    private final ObjectProperty<Currency> currency;
    
    @AView.Column(title = "%discounts", width = "80", cellFactory = DiscountCellFactory.class)
    private final ObservableList<ProductDiscount> discounts;
    private final ObservableList<MapEditorElement> discountsForMapEditor;
    
    @AView.Column(width = "35", cellFactory = ActPasCellFactory.class)
    private final SimpleBooleanProperty isActive;
    
    @JsonIgnore
    private static final String DB_VIEW_NAME = "products_whole";
    @JsonIgnore
    private static final String DB_SPECIFIC_TABLE_NAME = "product_specific_descrips";
    @JsonIgnore
    private static final String DB_TABLE_NAME = "products";
    
    
    public Product(){
        abbreviation = new SimpleStringProperty("");
        former = new SimpleStringProperty("");
        descrip = new SimpleStringProperty("");
        remark = new SimpleStringProperty("");
        specific = new SimpleIntegerProperty();
        specificDescrip = new SimpleStringProperty("");
        productSpecific = new SimpleObjectProperty<>(new ProductSpecific());
        price = new SimpleStringProperty("");
        iso = new SimpleStringProperty("");
        currency = new SimpleObjectProperty<>(new Currency());
        discounts = FXCollections.observableArrayList();
        discountsForMapEditor = FXCollections.observableArrayList();
        isActive = new SimpleBooleanProperty();
        
        productSpecific.addListener((ObservableValue<? extends ProductSpecific> observable, ProductSpecific oldValue, ProductSpecific newValue) -> {
            rebindSpecific();
        });
        rebindSpecific();
        
        discountsForMapEditor.addListener((ListChangeListener.Change<? extends Object> c) -> {
            c.next();
            if (c.wasAdded() ){
                List<? extends Object> adds = c.getAddedSubList();
                adds.stream().forEach((elem) -> {
                    ProductDiscount disc = (ProductDiscount) elem;
                    if (disc != null && !discounts.contains(disc)){
                        discounts.add(disc);
                    }
                });
            }
            else if (c.wasRemoved()){
                List<? extends Object> removes = c.getRemoved();
                removes.stream().forEach((elem) -> {
                    ProductDiscount disc = (ProductDiscount) elem;
                    if (disc != null && discounts.contains(disc)){
                        discounts.remove(disc);
                    }
                });
            }
        });
        
        currency.addListener((ObservableValue<? extends Currency> observable, Currency oldValue, Currency newValue) -> {
            rebindIso();
        });
        rebindIso();
    }
    
    private void rebindIso(){
        iso.unbind();
        if (currency.get() != null){
            iso.bind(currency.get().isoProperty());
        }
    }
    
    private void rebindSpecific(){
        specific.unbind();
        specificDescrip.unbind();
        if (productSpecific.get() != null){
            specific.bind(productSpecific.get().specificProperty());
            specificDescrip.bind(productSpecific.get().descripProperty());
        }
    }
    
    // DBService methods:
    public static ArrayList<Product> getAllFromDB (){
        JSONObject params = new ConditionBuilder().build();
        return DBUtils.getObjectsListFromDB(Product.class, DB_VIEW_NAME, params);
        
//        try {
//            String data = GeneralConfig.getInstance().getDBClient().select(DB_VIEW_NAME, new ConditionBuilder().build()).toString();
//            System.out.println("products data: " + data);
//            ObjectMapper mapper = new ObjectMapper();
//            return mapper.readValue(data, new TypeReference<ArrayList<Product>>() {});
//        } catch (IOException | AuthServerException ex) {
//            Logger.getLogger(Product.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return new ArrayList<>();
    }
    
    
    public static ArrayList<ProductSpecific> getAllSpecificsFromDB(){
        DBClient dbClient = GeneralConfig.getInstance().getDBClient();
        ConditionBuilder condition = new ConditionBuilder().where().and("language", "=", dbClient.getLang()).condition();
        JSONObject params = condition.build();
        return DBUtils.getObjectsListFromDB(ProductSpecific.class, DB_SPECIFIC_TABLE_NAME, params);
        
//        try {
//            DBClient dbClient = GeneralConfig.getInstance().getDBClient();
//            ConditionBuilder condition = new ConditionBuilder().where().and("language", "=", dbClient.getLang()).condition();
//            String data = GeneralConfig.getInstance().getDBClient().select(DB_SPECIFIC_TABLE_NAME, condition.build()).toString();
//            System.out.println("products specific data: " + data);
//            ObjectMapper mapper = new ObjectMapper();
//            return mapper.readValue(data, new TypeReference<ArrayList<ProductSpecific>>() {});
//        } catch (IOException | AuthServerException ex) {
//            Logger.getLogger(Product.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return new ArrayList<>();
    }
    
    public static Product getOneFromDB (int productId){
        JSONObject params = new ConditionBuilder().where().and("rec_id", "=", productId).condition().build();
        return DBUtils.getObjectFromDB(Product.class, DB_VIEW_NAME, params);
        
//        try {
//            ConditionBuilder conditionBuilder = new ConditionBuilder().where().and("rec_id", "=", productId).condition();
//            JSONArray data = GeneralConfig.getInstance().getDBClient().select(DB_VIEW_NAME, conditionBuilder.build());
//            String productData = data.opt(0).toString();
//            ObjectMapper mapper = new ObjectMapper();
//            Product product = mapper.readValue(productData, Product.class);
//            return product;
//        } catch (IOException | AuthServerException ex) {
//            Logger.getLogger(Product.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return null;
    }
    
    public static Product saveOneToDB(Product product){
        if (product == null) return null;
        return DBUtils.saveObjectToDB(product, "product");
        
        
//        try {
//            ObjectMapper mapper = new ObjectMapper();
//            ObjectWriter writer = mapper.writer().withDefaultPrettyPrinter();
//            JSONObject productJson = new JSONObject(writer.writeValueAsString(product));
//            DBClient dbClient = GeneralConfig.getInstance().getDBClient();
//            JSONObject newProduct = dbClient.callProcedureAndGetAsJson("general_insert_update_simpledate", DB_TABLE_NAME, dbClient.getLang(), productJson).getJSONObject(0);
//            return mapper.readValue(newProduct.toString(), Product.class);
//        } catch (JsonProcessingException ex) {
//            Logger.getLogger(Product.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (IOException | AuthServerException | JSONException ex) {
//            Logger.getLogger(Product.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return null;
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
    
    public ObjectProperty<Currency> currencyProperty(){
        return currency;
    }
    
    public ObjectProperty<ProductSpecific> specificProperty(){
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
    
//    public String getRemark() {
//        return remark.get();
//    }
    
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
        return this.currency.get().getIso();
    }
    
    public ObservableList<ProductDiscount> getDiscounts() {
        return discounts;
    }
    
    @JsonIgnore
    public ObservableList<MapEditorElement> getDiscountsForMapEditor(){
        return discountsForMapEditor;
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
    
//    public void setRemark(String remark) {
//        System.out.println("remark: " + remark);
//        this.remark.set(remark);
//    }
    
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
        this.currency.get().setIso(iso);
    }
    
    public void setDiscounts(Collection<ProductDiscount> discounts) {
//        this.discounts.setAll(discounts);
        discounts.stream().forEach((discount) -> {
            discountsForMapEditor.setAll(discounts);
        });
        
        System.out.println("axal discounts: " + this.discounts);
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
        setSpecific(product.getSpecific());
        setSpecificDescip(product.getSpecificDescrip());
        setPrice(product.getPrice());
        setIso(product.getIso());
        setIsActive(product.getIsActive());

        discounts.clear();
        discountsForMapEditor.clear();
        discountsForMapEditor.addAll(product.getDiscountsForMapEditor());
//        discounts.addAll(product.getDiscounts());
    }

    @Override
    public String toStringForSearch() {
        return getAbbreviation() + " " + getDescrip();
    }
    
    @Override
    public String toString() {
        return getAbbreviation() + getFormer();
    }

    /**
     * The method equals two products.
     * Note: It is needed for ProductComboBox selectItem method.
     * @param other Product which must compare to this.
     * @return True if they are equals, false - otherwise.
     */
    @Override
    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    public boolean equals(Object other){
        if (other == null) return false;
        Product otherProduct = (Product) other;
        return  getAbbreviation().equals(otherProduct.getAbbreviation()) &&
                getFormer() == otherProduct.getFormer();
    }
    
    /**
     * Method compares two products.
     * @param backup - other product.
     * @return  - True, if all comparable fields are equals, false otherwise.
     */
    @Override
    public boolean compares(EditorPanelable backup) {
        Product productBackup = (Product) backup;
        
        System.out.println("Utils.compareLists(getDiscounts(), productBackup.getDiscounts()): " + (Utils.compareLists(getDiscounts(), productBackup.getDiscounts()) ));
        System.out.println("getDiscounts(): " + getDiscounts());
        System.out.println("productBackup.getDiscounts(): " + productBackup.getDiscounts());
        
        return  this.getAbbreviation().equals(productBackup.getAbbreviation()) &&
                this.getFormer() == productBackup.getFormer() &&
                this.getDescrip().equals(productBackup.getDescrip()) &&
//                this.getRemark().equals(productBackup.getRemark()) &&
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
