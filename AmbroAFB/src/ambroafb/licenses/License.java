/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.licenses;

import ambro.AView;
import ambroafb.clients.Client;
import ambroafb.general.DateConverter;
import ambroafb.general.Utils;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.general.interfaces.TableColumnFeatures;
import ambroafb.licenses.helper.LicenseStatus;
import ambroafb.products.Product;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javafx.beans.binding.StringExpression;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

/**
 *
 * @author Dato
 */
public class License extends EditorPanelable {

    public String password;
    
    @AView.Column(title = "%created_date", width = TableColumnFeatures.Width.DATETIME, styleClass = TableColumnFeatures.Style.TEXT_RIGHT)
    private final StringProperty createdTime;
    private final ObjectProperty<LocalDateTime> createdTimeObj;
    
    @AView.Column(title = "%license_N", width = TableColumnFeatures.Width.LICENSE, styleClass = TableColumnFeatures.Style.TEXT_RIGHT)
    private final IntegerProperty licenseNumber;
    
    @AView.Column(title = "%client", width = TableColumnFeatures.Width.CLIENT_MAIL)
    private final StringExpression clientDescrip;
    private final ObjectProperty<Client> clientObj;
    private int clientId; // for object mapper (case: class to json)
    private String firstName, lastName, email;
    
    @AView.Column(title = "%product", width = TableColumnFeatures.Width.PRODUCT, styleClass = TableColumnFeatures.Style.TEXT_CENTER)
    private final StringProperty productDescrip;
    private final ObjectProperty<Product> productObj;
    private int productId; // for object mapper (case: class to json)
    private String abbreviation, former;
    
    @AView.Column(title = "%last_invoice", width = TableColumnFeatures.Width.INVOICE)
    private final StringProperty invoiceNumber;
//    @JsonIgnore
//    private final ObjectProperty<Invoice> invoiceObj;
    private final IntegerProperty cfCurrentInvoiceId;
    private final IntegerProperty cfFutureInvoiceId;
    
    @AView.Column(title = "%license_status", width = "120")
    private final StringProperty statusDescrip;
    private final ObjectProperty<LicenseStatus> statusObj;
    private final IntegerProperty status; // for object mapper (case: class to json)
    
    private final StringProperty remark;
    
    @AView.Column(title = "%begin_date", width = TableColumnFeatures.Width.DATE, styleClass = TableColumnFeatures.Style.TEXT_RIGHT)
    private final StringProperty firstDateDescrip;
    private final ObjectProperty<LocalDate> firstDateObj;
    private String firstDate; // for object mapper (case: class to json)
    
    @AView.Column(title = "%end_date", width = TableColumnFeatures.Width.DATE, styleClass = TableColumnFeatures.Style.TEXT_RIGHT)
    private final StringProperty lastDateDescrip;
    private final ObjectProperty<LocalDate> lastDateObj;
    private String lastDate; // for object mapper (case: class to json)
    
    @AView.Column(title = "%extra_days", width = "64", styleClass = TableColumnFeatures.Style.TEXT_RIGHT)
    private final IntegerProperty additionalDays;
    
    private final StringProperty lastLoginTime;
    private final ObjectProperty<LocalDate> lastLoginTimeObj;
    
    private final BooleanProperty isNew;
    
    private static final String DB_VIEW_NAME = "licenses_whole";
    private static final String DB_TABLE_NAME = "licenses";
    private static final String DB_STATUSES_TABLE_NAME = "license_status_descrips";
    
    public License(){
        createdTime = new SimpleStringProperty("");
        createdTimeObj = new SimpleObjectProperty<>(LocalDateTime.now());
        clientObj = new SimpleObjectProperty<>(new Client());
        clientDescrip = clientObj.get().getShortDescrip(", ");
        productObj = new SimpleObjectProperty<>(new Product());
        productDescrip = new SimpleStringProperty("");
//        productDescrip = Utils.avoidNull(productObj.get().abbreviationProperty()).concat(Utils.avoidNull(productObj.get().formerProperty()));
//        invoiceObj = new SimpleObjectProperty<>(new Invoice());
        invoiceNumber = new SimpleStringProperty("");
        cfCurrentInvoiceId = new SimpleIntegerProperty(0);
        cfFutureInvoiceId = new SimpleIntegerProperty(0);
        status = new SimpleIntegerProperty(0);
        statusDescrip = new SimpleStringProperty("");
        statusObj = new SimpleObjectProperty<>(new LicenseStatus());
        remark = new SimpleStringProperty("");
        additionalDays = new SimpleIntegerProperty(0);
        licenseNumber = new SimpleIntegerProperty(0);
        firstDateDescrip = new SimpleStringProperty("");
        firstDateObj = new SimpleObjectProperty<>();
        lastDateDescrip = new SimpleStringProperty("");
        lastDateObj = new SimpleObjectProperty<>();
        lastLoginTime = new SimpleStringProperty("");
        lastLoginTimeObj = new SimpleObjectProperty<>();
        isNew = new SimpleBooleanProperty(false);
        
        statusObj.addListener((ObservableValue<? extends LicenseStatus> observable, LicenseStatus oldValue, LicenseStatus newValue) -> {
            rebindStatus(); 
        });
        rebindStatus();
        
        firstDateObj.addListener(new DateListener(firstDateDescrip));
        lastDateObj.addListener(new DateListener(lastDateDescrip));
        lastLoginTimeObj.addListener(new DateListener(lastLoginTime));
    }
    
    private void rebindStatus(){
        status.unbind();
        statusDescrip.unbind();
        if (statusObj.get() != null){
            status.bind(statusObj.get().statusIdProperty());
            statusDescrip.bind(statusObj.get().descripProperty());
        }
    }
    
    // properties:
    public ObjectProperty<LocalDateTime> createdTimeProperty(){
        return createdTimeObj;
    }
    
    public ObjectProperty<Client> clientProperty(){
        return clientObj;
    }
    
    public StringExpression clientDescripExpression(){
        return clientDescrip;
    }
    
    public ObjectProperty<Product> productProperty(){
        return productObj;
    }
    
    public StringExpression productDescripExpression(){
        return productDescrip;
    }

    public ObjectProperty<LicenseStatus> statusProperty(){
        return statusObj;
    }
    
    public ObjectProperty<LocalDate> firstDateProperty(){
        return firstDateObj;
    }
    
    public ObjectProperty<LocalDate> lastDateProperty(){
        return lastDateObj;
    }
    
    public ObjectProperty<LocalDate> lastLoginTimeProperty(){
        return lastLoginTimeObj;
    }
    
    public StringProperty invoiceNumberProperty(){
        return invoiceNumber;
    }
    
    public IntegerProperty cfCurrentInvoiceIdProperty(){
        return cfCurrentInvoiceId;
    }
    
    public IntegerProperty cfFutureInvoiceIdProperty(){
        return cfFutureInvoiceId;
    }
    
    public StringProperty remarkProperty(){
        return remark;
    }
    
    public IntegerProperty additionalDaysProperty(){
        return additionalDays;
    }
    
    public IntegerProperty licenseNumberProperty(){
        return licenseNumber;
    }
    
    
    // Getters:
    @JsonIgnore
    public String getCreatedDateStr(){
        return createdTime.get();
    }
    
    public int getClientId(){
        return clientObj.get().getRecId();
    }
    
    public String getFirstName(){
        return clientObj.get().getFirstName();
    }
    
    public String getLastName(){
        return clientObj.get().getLastName();
    }
    
    public String getEmail(){
        return clientObj.get().getEmail();
    }
    
    public int getProductId(){
        return productObj.get().getRecId();
    }
    
    public String getAbbreviation(){
        return productObj.get().getAbbreviation();
    }
    
    public int getFormer(){
        return productObj.get().getFormer();
    }
    
    public String getStatusDescrip(){
        return statusObj.get().getDescrip();
    }
    
    public int getStatus(){
        return statusObj.get().getLicenseStatusId();
    }
    
    public String getRemark(){
        return remark.get();
    }
    
    public int getAdditionalDays(){
        return additionalDays.get();
    }
    
    public int getLicenseNumber(){
        return licenseNumber.get();
    }
    
    public String getInvoiceNumber(){
        return invoiceNumber.get();
    }
    
    public int getCfCurrentInvoiceId(){
        return cfCurrentInvoiceId.get();
    }
    
    public int getCfFutureInvoiceId(){
        return cfFutureInvoiceId.get();
    }
    
    public String getFirstDate(){
        // Beacause firstDateDescrip contains user friendly view of date.
        return firstDateDescrip.get();
    }
    
    public String getLastDate(){
        // Beacause lastDateDescrip contains user friendly view of date.
        return lastDateDescrip.get();
    }
    
    public String getLastLoginTime(){
        return lastLoginTime.get();
    }
    
    public String getProductDescrip(){
        return productDescrip.get();
    }
    
    
    
    // Setters:
    // This method must not show in other class to avoid createdDate changing.
    private void setCreatedTime(String createdTime){
        this.createdTimeObj.set(DateConverter.getInstance().parseDateTime(createdTime));
        this.createdTime.set(DateConverter.getInstance().getDayMonthnameYearBySpace(createdTimeObj.get()));
        
    }
    
    public void setClientId(int clienId){
        this.clientObj.get().setRecId(recId);
    }
    
    public void setFirstName(String name){
        this.clientObj.get().setFirstName(name);
    }
    
    public void setLastName(String lastName){
        this.clientObj.get().setLastName(lastName);
    }
    
    public void setEmail(String email){
        clientObj.get().setEmail(email);
    }
    
    public void setProductId(int productId){
        this.productObj.get().setRecId(recId);
    }
    
    public void setAbbreviation(String abbreviation){
        productObj.get().setAbbreviation(abbreviation);
    }
    
    public void setFormer(int former){
        productObj.get().setFormer(former);
    }
    
    public void setStatusDescrip(String statusDescrip){
        this.statusObj.get().setDescrip(statusDescrip);
    }
    
    public void setStatus(int status){
        this.statusObj.get().setLicenseStatusId(status);
    }
    
    public void setRemark(String remark){
        this.remark.set(remark);
    }
    
    public void setAdditionalDays(int extraDays){
        additionalDays.set(extraDays);
    }
    
    public void setLicenseNumber(int number){
        licenseNumber.set(number);
    }
    
    public void setInvoiceNumber(String invoiceNumber){
        this.invoiceNumber.set(invoiceNumber);
    }
    
    public void setCfCurrentInvoiceId(int id){
        this.cfCurrentInvoiceId.set(id);
    }
    
    public void setCfFutureInvoiceId(int id){
        this.cfFutureInvoiceId.set(id);
    }
    
    public void setFirstDate(String date){
        firstDateObj.set(DateConverter.getInstance().parseDate(date));
    }
    
    public void setLastDate(String date){
        lastDateObj.set(DateConverter.getInstance().parseDate(date));
    }
    
    public void setLastLoginTime(String date){
        lastLoginTimeObj.set(DateConverter.getInstance().parseDate(date));
    }
    
    // The method is need for license that will create when invoice select some products and 
    // ask DB the new license info, licenses finances and invoice finances.
    public void setIsNew(int isNew){
        this.isNew.set(isNew == 1);
    }
    
    public void setProductDescrip(String productDescrip){
        this.productDescrip.set(productDescrip);
    }
    
    /**
     *  The metho compares to licenses created time parameters.
     * @param other Other license object.
     * @return Negative number if this object is less, positive number if this object is greater. If they are equals -  0.
     */
    public int compareTo(License other){
        return createdTimeObj.get().compareTo(other.createdTimeProperty().get());
    }
    
    
    @Override
    public License cloneWithoutID() {
        License clone = new License();
        clone.copyFrom(this);
        return clone;
    }

    @Override
    public License cloneWithID() {
        License clone = cloneWithoutID();
        clone.setRecId(this.getRecId());
        return clone;
    }

    @Override
    public void copyFrom(EditorPanelable other) {
        License otherLicense = (License) other;
//        setCreatedDate(otherLicense.getCreatedDateStr());
        // clientObj.set(otherLicense.clientObj.get()) -> copy reference and they always be equals! So copy every field.
        setClientId(otherLicense.getClientId());
        setFirstName(otherLicense.getFirstName());
        setLastName(otherLicense.getLastName());
        setEmail(otherLicense.getEmail());
        
        setProductId(otherLicense.getProductId());
        setAbbreviation(otherLicense.getAbbreviation());
        setFormer(otherLicense.getFormer());
        
        setInvoiceNumber(otherLicense.getInvoiceNumber());
        setCfCurrentInvoiceId(otherLicense.getCfCurrentInvoiceId());
        setCfFutureInvoiceId(otherLicense.getCfFutureInvoiceId());
        
        setStatus(otherLicense.getStatus());
        setStatusDescrip(otherLicense.getStatusDescrip());
        
        setRemark(otherLicense.getRemark());
        setAdditionalDays(otherLicense.getAdditionalDays());
        setLicenseNumber(otherLicense.getLicenseNumber());
        
        setFirstDate(otherLicense.getFirstDate());
        setLastDate(otherLicense.getLastDate());
        setLastLoginTime(otherLicense.getLastLoginTime());
    }
    
    @Override
    public boolean compares(EditorPanelable backup){
        License otherLicense = (License) backup;
//        getCreatedDateStr().equals(otherLicense.getCreatedDateStr())  &&
        return  getClientId() == otherLicense.getClientId() &&
                clientDescripExpression().equals(otherLicense.clientDescripExpression()) &&
                getProductId() == otherLicense.getProductId()           &&
                productDescripExpression().equals(otherLicense.productDescripExpression()) &&
                getInvoiceNumber().equals(otherLicense.getInvoiceNumber()) &&
                getCfCurrentInvoiceId() == otherLicense.getCfCurrentInvoiceId() &&
                getCfFutureInvoiceId() == otherLicense.getCfFutureInvoiceId() &&
                statusProperty().get().compares(otherLicense.statusProperty().get()) &&
                getRemark().equals(otherLicense.getRemark()) &&
                getAdditionalDays() == otherLicense.getAdditionalDays() &&
                getLicenseNumber() == otherLicense.getLicenseNumber()   &&
                
                // The dialog buttons does not allow for licenses, so they don't be null:
Utils.objectEquals(firstDateProperty().get(), otherLicense.firstDateProperty().get()) &&
                Utils.objectEquals(lastDateProperty().get(), otherLicense.lastDateProperty().get()) &&
                Utils.objectEquals(lastLoginTimeProperty().get(), otherLicense.lastLoginTimeProperty().get());
//                firstDateProperty().get().equals(otherLicense.firstDateProperty().get()) &&
//                lastDateProperty().get().equals(otherLicense.lastDateProperty().get()) &&
//                lastLoginTimeProperty().get().equals(lastLoginTimeProperty().get());
    }
    

    @Override
    public String toStringForSearch() {
        return getLicenseNumber() + " " + getEmail() + " " + getInvoiceNumber();
    }
    
    private class DateListener implements ChangeListener<LocalDate> {

        private final StringProperty target;
        
        public DateListener(StringProperty targetProperty){
            target = targetProperty;
        }
        
        @Override
        public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
            String dateStr = "";
            if (newValue != null){
                dateStr = DateConverter.getInstance().getDayMonthnameYearBySpace(newValue);
            }
            target.set(dateStr);
        }
        
    }
}
