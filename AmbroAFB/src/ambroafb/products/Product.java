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
import ambroafb.general.NumberConverter;
import ambroafb.general.Utils;
import ambroafb.general.countcombobox.CountComboBoxItem;
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
public class Product extends EditorPanelable implements CountComboBoxItem {
    
    @AView.Column(width = "30")
    private final SimpleStringProperty abbreviation;
    
    @AView.Column(width = "30")
    private final SimpleStringProperty former;
    
    @AView.Column(title = "%descrip", width = "200")
    private final SimpleStringProperty descrip;
    
    @AView.Column(title = "%product_specific", width = "200")
    private final SimpleStringProperty specificDescrip;
    private final IntegerProperty specific;
    private final ObjectProperty<ProductSpecific> productSpecific;
    
    @AView.Column(title = "%monthly_price", width = "90", styleClass = "textRight")
    private final SimpleStringProperty price;
    
    @AView.Column(title = "%iso", width = TableColumnWidths.ISO, styleClass = "textCenter")
    private final SimpleStringProperty iso;
    private final ObjectProperty<Currency> currency;
    
    @AView.Column(title = "%discounts", width = "90", cellFactory = DiscountCellFactory.class)
    private final ObservableList<ProductDiscount> discounts;
    private final ObservableList<MapEditorElement> discountsForMapEditor;
    
    @AView.Column(width = "35", cellFactory = ActPasCellFactory.class)
    private final SimpleBooleanProperty isActive;
    
    @AView.Column(title = "%max_count", width = "50", styleClass = "textRight")
    private final StringProperty notJurMaxCount;
    
    @AView.Column(title = "%testing_days", width = "100", styleClass = "textRight")
    private final StringProperty testingDays;
    
    private static final String DB_VIEW_NAME = "products_whole";
    private static final String DB_SPECIFIC_TABLE_NAME = "product_specific_descrips";
    private static final String DB_TABLE_NAME = "products";
    private static final String DB_DELETE_PROCEDURE_NAME = "general_delete";
    
    public static final int ABREVIATION_LENGTH = 2;
    public static final int FORMER_LENGTH = 2;
    
    private final float priceDefaultValue = -1;
    
    public Product(){
        abbreviation = new SimpleStringProperty("");
        former = new SimpleStringProperty("");
        descrip = new SimpleStringProperty("");
        specific = new SimpleIntegerProperty();
        specificDescrip = new SimpleStringProperty("");
        productSpecific = new SimpleObjectProperty<>(new ProductSpecific());
        price = new SimpleStringProperty("");
        iso = new SimpleStringProperty("");
        currency = new SimpleObjectProperty<>(new Currency());
        discounts = FXCollections.observableArrayList();
        discountsForMapEditor = FXCollections.observableArrayList();
        isActive = new SimpleBooleanProperty();
        notJurMaxCount = new SimpleStringProperty("");
        testingDays = new SimpleStringProperty("");
        
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
        ArrayList<Product> products = DBUtils.getObjectsListFromDB(Product.class, DB_VIEW_NAME, params);
        products.sort((Product p1, Product p2) -> p2.compareById(p1));
        return products;
    }
    
    
    public static ArrayList<ProductSpecific> getAllSpecificsFromDB(){
        DBClient dbClient = GeneralConfig.getInstance().getDBClient();
        ConditionBuilder condition = new ConditionBuilder().where().and("language", "=", dbClient.getLang()).condition();
        JSONObject params = condition.build();
        return DBUtils.getObjectsListFromDB(ProductSpecific.class, DB_SPECIFIC_TABLE_NAME, params);
    }
    
    public static Product getOneFromDB (int productId){
        JSONObject params = new ConditionBuilder().where().and("rec_id", "=", productId).condition().build();
        Product p = DBUtils.getObjectFromDB(Product.class, DB_VIEW_NAME, params);
        return p;
    }
    
    public static Product saveOneToDB(Product product){
        if (product == null) return null;
        return DBUtils.saveObjectToDB(product, "product");
    }
    
    public static boolean deleteOneFromDB(int productId){
        return DBUtils.deleteObjectFromDB(DB_DELETE_PROCEDURE_NAME, DB_TABLE_NAME, productId);
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
    
    public StringProperty notJurMaxCountProperty(){
        return notJurMaxCount;
    }
    
    public StringProperty testingDaysProperty(){
        return testingDays;
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
    
    public int getSpecific(){
        return productSpecific.get().getRecId();
    }
    
    public String getSpecificDescrip(){
        return productSpecific.get().getDescrip();
    }
    
    public Float getPrice() {
        return NumberConverter.stringToFloat(price.get(), 2, priceDefaultValue);
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
    
    public int getNotJurMaxCount(){
        return Utils.getIntValueFor(notJurMaxCount.get());
    }
    
    public int getTestingDays(){
        return Utils.getIntValueFor(testingDays.get());
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
    
    public void setSpecific(int specific){
        this.productSpecific.get().setProductSpecificId(specific);
    }
    
    public void setSpecificDescrip(String specificDescrip){
        this.productSpecific.get().setDescrip(specificDescrip);
    }
    
    public void setPrice(double price) {
        this.price.set("" + price);
    }
    
    public void setIso(String iso){
        this.currency.get().setIso(iso);
    }
    
    public void setDiscounts(Collection<ProductDiscount> discounts) {
        discountsForMapEditor.setAll(discounts);
    }

    public void setIsActive(boolean isActive) {
        this.isActive.set(isActive);
    }
    
    public void setNotJurMaxCount(int notJurMaxCount){
        this.notJurMaxCount.set("" + notJurMaxCount);
    }

    public void setTestingDays(int testingDays){
        this.testingDays.set("" + testingDays);
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
        productSpecific.get().copyFrom(product.specificProperty().get());
        setPrice(product.getPrice());
        setIso(product.getIso());
        setIsActive(product.getIsActive());
        setNotJurMaxCount(product.getNotJurMaxCount());
        setTestingDays(product.getTestingDays());

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
        return getDescrip(); // getAbbreviation() + getFormer();
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
        return  getAbbreviation().equals(productBackup.getAbbreviation()) &&
                getFormer() == productBackup.getFormer() &&
                getDescrip().equals(productBackup.getDescrip()) &&
                specificProperty().get().compares(productBackup.specificProperty().get()) &&
                getPrice().equals(productBackup.getPrice()) &&
                getIso().equals(productBackup.getIso()) &&
                getIsActive() == productBackup.getIsActive() &&
                getNotJurMaxCount() == productBackup.getNotJurMaxCount() &&
                getTestingDays() == productBackup.getTestingDays() &&
                Utils.compareListsByElemOrder(getDiscounts(), productBackup.getDiscounts());
    }

    @Override
    public String getDrawableName() {
        return getDescrip();
    }

    @Override
    public String getUniqueIdentifier() {
        return "" + getRecId();
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
