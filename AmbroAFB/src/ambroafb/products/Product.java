/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.products;

import ambro.ANodeSlider;
import ambro.AView;
import ambroafb.general.GeneralConfig;
import ambroafb.general.NumberConverter;
import ambroafb.general.Utils;
import ambroafb.general.countcombobox.CountComboBoxItem;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.general.interfaces.TableColumnFeatures;
import ambroafb.products.helpers.ProductDiscount;
import ambroafb.products.helpers.ProductSpecific;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
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
@SuppressWarnings("EqualsAndHashcode")
public class Product extends EditorPanelable implements CountComboBoxItem {
    
    @AView.Column(width = "30")
    private final SimpleStringProperty abbreviation;
    
    @AView.Column(width = "30")
    private final SimpleStringProperty former;
    
    @AView.Column(title = "%descrip", width = "200")
    private final SimpleStringProperty descrip;
    
//    @AView.Column(title = "%product_specific", width = "200", cellFactory = SpecificCellFactory.class)
    private final StringProperty specificId;
    
    @AView.Column(title = "%monthly_price", width = TableColumnFeatures.Width.MONEY, styleClass = TableColumnFeatures.Style.TEXT_RIGHT)
    private final SimpleStringProperty price;
    
    @AView.Column(title = "%iso", width = TableColumnFeatures.Width.ISO, styleClass = TableColumnFeatures.Style.TEXT_CENTER)
    private final StringProperty iso;
    
    @AView.Column(title = "%discounts", width = "90", cellFactory = DiscountCellFactory.class)
    private final ObjectProperty<ObservableList<ProductDiscount>> discountsObj; // List may is not Observable. The discounts runtime update result is the same.
//    private final ObjectProperty<List<ProductDiscount>> discountsObj;
    
    @AView.Column(width = "35", cellFactory = ActPasCellFactory.class)
    private final SimpleBooleanProperty isActive;
    
    @AView.Column(title = "%max_count", width = "50", styleClass = TableColumnFeatures.Style.TEXT_RIGHT)
    private final StringProperty notJurMaxCount;
    
    @AView.Column(title = "%testing_days", width = "100", styleClass = TableColumnFeatures.Style.TEXT_RIGHT)
    private final StringProperty testingDays;
    
    public static final int ABREVIATION_LENGTH = 2;
    public static final int FORMER_LENGTH = 2;
    
    private final float priceDefaultValue = -1;
    
    public Product(){
        abbreviation = new SimpleStringProperty("");
        former = new SimpleStringProperty("");
        descrip = new SimpleStringProperty("");
        specificId = new SimpleStringProperty();
        price = new SimpleStringProperty("");
        iso = new SimpleStringProperty();
        discountsObj = new SimpleObjectProperty<>(FXCollections.observableArrayList());
        isActive = new SimpleBooleanProperty();
        notJurMaxCount = new SimpleStringProperty("");
        testingDays = new SimpleStringProperty("");
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
    
    public StringProperty isoProperty(){
        return iso;
    }
    
    public StringProperty specificIdProperty(){
        return specificId;
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
    
    /**
     * @return The DB id of product specific.
     */
    public Integer getSpecific(){
        return NumberConverter.stringToInteger(specificId.get(), null);
    }
    
    public Float getPrice() {
        return NumberConverter.stringToFloat(price.get(), 2, priceDefaultValue);
    }
    
    public String getIso(){
        return iso.get();
    }
    
    @JsonIgnore // Discounts must need "sets_separate_saving" parameter. So ProductDataChangeProvider provides to send accounts to DB.
    public List<ProductDiscount> getDiscounts() {
        return discountsObj.get();
    }
    
    public boolean getIsActive() {
        return isActive.get();
    }
    
    public Integer getNotJurMaxCount(){
        return NumberConverter.stringToInteger(notJurMaxCount.get(), null);
    }
    
    public Integer getTestingDays(){
        return NumberConverter.stringToInteger(testingDays.get(), null);
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
    
    public void setSpecific(Integer specific){
        this.specificId.set((specific == null) ? null : specific.toString());
    }
    
    public void setPrice(Float price) {
        this.price.set(NumberConverter.convertNumberToStringBySpecificFraction(price, 2));
    }
    
    public void setIso(String iso){
        this.iso.set(iso);
    }
    
    @JsonProperty
    public void setDiscounts(Collection<ProductDiscount> discounts) {
        this.discountsObj.set(FXCollections.observableArrayList(discounts));
    }

    public void setIsActive(boolean isActive) {
        this.isActive.set(isActive);
    }
    
    public void setNotJurMaxCount(int notJurMaxCount){
        this.notJurMaxCount.set((notJurMaxCount == 0) ? "" : "" + notJurMaxCount);
    }

    public void setTestingDays(int testingDays){
        this.testingDays.set((testingDays == 0) ? "" : "" + testingDays);
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
        
        setPrice(product.getPrice());
        setIso(product.getIso());
        setIsActive(product.getIsActive());
        setNotJurMaxCount(product.getNotJurMaxCount());
        setTestingDays(product.getTestingDays());
        setDiscounts(product.getDiscounts());
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
                Objects.equals(getSpecific(), productBackup.getSpecific()) &&
                getPrice().equals(productBackup.getPrice()) &&
                Objects.equals(getIso(), productBackup.getIso()) &&
                getIsActive() == productBackup.getIsActive() &&
                Objects.equals(getNotJurMaxCount(), productBackup.getNotJurMaxCount()) &&
                Objects.equals(getTestingDays(), productBackup.getTestingDays()) &&
                Utils.compareListsByElemOrder(getDiscounts(), productBackup.getDiscounts());
    }

    @Override @JsonIgnore
    public String getDrawableName() {
        return getDescrip();
    }

    @Override @JsonIgnore
    public String getUniqueIdentifier() {
        return "" + getRecId();
    }
    
    
    /**
     * The class provides to change visual  for table discount cell.  
     * Discounts will show in {@link ambro.ANodeSlider  ANodeSlider} for table appropriate cell.
     */
    public static class DiscountCellFactory implements Callback<TableColumn<Product, ObservableList<ProductDiscount>>, TableCell<Product, ObservableList<ProductDiscount>>> {

        @Override
        public TableCell<Product, ObservableList<ProductDiscount>> call(TableColumn<Product, ObservableList<ProductDiscount>> param) {
            return new TableCell<Product, ObservableList<ProductDiscount>>() {
                @Override
                public void updateItem(ObservableList<ProductDiscount> discounts, boolean empty) {
                    super.updateItem(discounts, empty);
                    setEditable(false);
                    if (discounts == null || discounts.isEmpty() || empty){
                        setGraphic(null);
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
    
    /**
     *  The class provides to show "act" or "pas" strings in table appropriate cell.
     */
    public static class ActPasCellFactory implements Callback<TableColumn<Product, Boolean>, TableCell<Product, Boolean>> {

        @Override
        public TableCell<Product, Boolean> call(TableColumn<Product, Boolean> param) {
            return new TableCell<Product, Boolean>() {
                @Override
                public void updateItem(Boolean isAct, boolean empty) {
                    super.updateItem(isAct, true);
                    if (isAct == null || empty) {
                        setText(null);
                    } else {
                        String actPasKey = (isAct ? "act" : "pas");
                        setText(GeneralConfig.getInstance().getTitleFor(actPasKey));
                    }
                }
            };
        }
    }
    
    /**
     * The class provides to show product specific in table appropriate cell.
     */
    public static class SpecificCellFactory implements Callback<TableColumn<Product, ProductSpecific>, TableCell<Product, ProductSpecific>> {

        @Override
        public TableCell<Product, ProductSpecific> call(TableColumn<Product, ProductSpecific> param) {
            return new TableCell<Product, ProductSpecific>(){
                @Override
                protected void updateItem(ProductSpecific item, boolean empty) {
                    super.updateItem(item, empty);
                    setText((item == null || empty) ? null : item.getDescrip());
                }
            };
        }
    }
    
}