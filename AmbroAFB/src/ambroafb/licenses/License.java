/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.licenses;

import ambroafb.licenses.helper.LicenseStatus;
import ambro.AView;
import ambroafb.clients.Client;
import ambroafb.general.DateConverter;
import ambroafb.general.FilterModel;
import ambroafb.general.GeneralConfig;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.general.interfaces.TableColumnWidths;
import ambroafb.invoices.Invoice;
import ambroafb.licenses.filter.LicenseFilterModel;
import ambroafb.products.Product;
import authclient.AuthServerException;
import authclient.db.ConditionBuilder;
import authclient.db.DBClient;
import authclient.db.WhereBuilder;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Dato
 */
public class License extends EditorPanelable {

    public String password;
    
    @AView.Column(title = "%created_date", width = TableColumnWidths.DATE, styleClass = "textCenter")
    private String createdDate;
    
    @AView.Column(title = "%license N", width = "100")
    private final IntegerProperty licenseNumber;
    
    @AView.Column(title = "%client", width = "100")
    @JsonIgnore
    private final StringProperty clientDescrip;
    @JsonIgnore
    private final ObjectProperty<Client> clientObj;
    private int clientId; // for object mapper (case: class to json)
    
    @AView.Column(title = "%product", width = "70")
    @JsonIgnore
    private final StringProperty productDescrip;
    @JsonIgnore
    private final ObjectProperty<Product> productObj;
    private int productId; // for object mapper (case: class to json)
    
    
    @AView.Column(title = "%last_invoice", width = "100")
    private final StringProperty invoiceNumber;
    @JsonIgnore
    private final StringProperty lastInvoiceDescrip;
    @JsonIgnore
    private final ObjectProperty<Invoice> invoiceObj;
    
    @AView.Column(title = "%license_statuses", width = "100")
    @JsonIgnore
    private final StringProperty statusDescrip;
    @JsonIgnore
    private final ObjectProperty<LicenseStatus> statusObj;
    private int status; // for object mapper (case: class to json)
    
    private final StringProperty remark;
    private final StringProperty abbreviationFormer;
    
    @AView.Column(title = "%begin_date", width = TableColumnWidths.DATE, styleClass = "textCenter")
    @JsonIgnore
    private final StringProperty firstDateDescrip;
    @JsonIgnore
    private final ObjectProperty<LocalDate> firstDateObj;
    private String firstDate; // for object mapper (case: class to json)
    
    @AView.Column(title = "%end_date", width = TableColumnWidths.DATE, styleClass = "textCenter")
    @JsonIgnore
    private final StringProperty lastDateDescrip;
    @JsonIgnore
    private final ObjectProperty<LocalDate> lastDateObj;
    private String lastDate; // for object mapper (case: class to json)
    
    @AView.Column(title = "extra_days", width = "30", styleClass = "textRight")
    private final IntegerProperty additionalDays;
    
    private final StringProperty email;
    
    
//    @AView.Column(width = "20", cellFactory = CheckedCellFactory.class)
//    private final BooleanProperty checked;
    
//    @AView.Column(title = "%date", width = "80")
//    private final StringProperty date;
//    private final ObjectProperty<LocalDate> dateObjProperty;
    
    
    
    private static final String DB_VIEW_NAME = "licenses_whole";
    private static final String DB_STATUSES_TABLE_NAME = "license_status_descrips";
    
    public License(){
        clientDescrip = new SimpleStringProperty("");
        clientObj = new SimpleObjectProperty<>();
        productDescrip = new SimpleStringProperty("");
        productObj = new SimpleObjectProperty<>();
        lastInvoiceDescrip = new SimpleStringProperty("");
        invoiceObj = new SimpleObjectProperty<>();
        statusDescrip = new SimpleStringProperty("");
        statusObj = new SimpleObjectProperty<>(new LicenseStatus());
        remark = new SimpleStringProperty("");
        abbreviationFormer = new SimpleStringProperty("");
        additionalDays = new SimpleIntegerProperty(0);
        firstDateDescrip = new SimpleStringProperty("");
        firstDateObj = new SimpleObjectProperty<>();
        lastDateDescrip = new SimpleStringProperty("");
        lastDateObj = new SimpleObjectProperty<>();
        licenseNumber = new SimpleIntegerProperty(0);
        invoiceNumber = new SimpleStringProperty("");
        email = new SimpleStringProperty("");
        
//        checked = new SimpleBooleanProperty();
//        date = new SimpleStringProperty("");
//        dateObjProperty = new SimpleObjectProperty<>();
        
        statusObj.addListener((ObservableValue<? extends LicenseStatus> observable, LicenseStatus oldValue, LicenseStatus newValue) -> {
            if (newValue != null){
                status = newValue.getLicenseStatusId();
                statusDescrip.set(newValue.getDescrip());
                System.out.println("change statusObj. newValue: " + newValue.getLicenseStatusId() + " " + newValue.getDescrip());
            }
        });
        
        firstDateObj.addListener(new DateListener(firstDateDescrip));
        lastDateObj.addListener(new DateListener(lastDateDescrip));
    }
    
    // DB methods:
    public static ArrayList<License> getAllFromDB(){
        try {
            String data = GeneralConfig.getInstance().getDBClient().select(DB_VIEW_NAME, new ConditionBuilder().build()).toString();
            
            System.out.println("licenses data: " + data);
            
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(data, new TypeReference<ArrayList<License>>() {});
        } catch (IOException | AuthServerException ex) {
            Logger.getLogger(License.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<>();
    }
    
    public static ArrayList<License> getFilteredFromDB(FilterModel model) {
        WhereBuilder whereBuilder = new ConditionBuilder().where();
        LicenseFilterModel licenseFilterModel = (LicenseFilterModel) model;
        
        int clientId = licenseFilterModel.getSelectedClientId();
        if (clientId > 0){
            whereBuilder = whereBuilder.and("client_id", "=", clientId);
        }
        int productId = licenseFilterModel.getSelectedProductId();
        if (productId > 0){
            whereBuilder = whereBuilder.and("product_id", "=", productId);
        }
        ObservableList<LicenseStatus> statuses = licenseFilterModel.getSelectedStatuses();
        if (!statuses.isEmpty()){
            whereBuilder = whereBuilder.andGroup();
            for (LicenseStatus status : statuses) {
                whereBuilder = whereBuilder.or("status", "=", status.getLicenseStatusId());
            }
            whereBuilder = whereBuilder.closeGroup();
        }
        if (licenseFilterModel.onlyExtraDays()){
            whereBuilder = whereBuilder.and("additional_days", ">", 0);
        }
        else if (licenseFilterModel.withAndWithoutExtraDays()){
            whereBuilder = whereBuilder.and("additional_days", ">=", 0);
        }
        else {
            whereBuilder = whereBuilder.and("additional_days", "=", 0);
        }
        
        try {
            JSONObject params = whereBuilder.condition().build();
            System.out.println("license filtered params: " + params);
            
            String data = GeneralConfig.getInstance().getDBClient().select(DB_VIEW_NAME, params).toString();
            
            System.out.println("license filtered data: " + data);
            
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(data, new TypeReference<ArrayList<License>>() {});
        } catch (IOException | AuthServerException ex) {
            Logger.getLogger(License.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<>();
    }

    
    public static ArrayList<LicenseStatus> getAllLicenseStatusFromDB(){
        try {
            DBClient dbClient = GeneralConfig.getInstance().getDBClient();
//            dbClient.callProcedureAndGetAsJson("general_select", DB_STATUSES_TABLE_NAME, dbClient.getLang(), )
            String data = dbClient.select(DB_STATUSES_TABLE_NAME, new ConditionBuilder().build()).toString();
            System.out.println("license status data: " + data);
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(data, new TypeReference<ArrayList<LicenseStatus>>() {});
        } catch (IOException | AuthServerException ex) {
            Logger.getLogger(LicenseStatus.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<>();
    }
    
    public static LicenseStatus getLicenseStatusFromDB(int licenseStatusId){
        try {
            ConditionBuilder conditionBuilder = new ConditionBuilder().where().and("license_status_id", "=", licenseStatusId).condition();
            JSONArray data = GeneralConfig.getInstance().getDBClient().select(DB_STATUSES_TABLE_NAME, conditionBuilder.build());
            System.out.println("one status data: " + data);
            String statusData = data.opt(0).toString();
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(statusData, LicenseStatus.class);
        } catch (IOException | AuthServerException ex) {
            Logger.getLogger(LicenseStatus.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public static License getOneFromDB(int recId){
        return null;
    }
    
    public static License saveOneToDB(License license){
        return null;
    }
    
    public static boolean deleteFromDB(int recId){
        System.out.println("delete from db...??");
        return false;
    }
    
    // Properties:
//    public BooleanProperty checkedProperty(){
//        return checked;
//    }
//    
//    public ObjectProperty<LocalDate> dateProperty(){
//        return dateObjProperty;
//    }
    
    
    
    
    
    // Getters:
    public String getCreatedDate(){
        return createdDate;
    }
    
    public int getClientId(){
        return (clientObj == null) ? -1 : clientObj.get().getRecId();
    }
    
    public int getProductId(){
        return (productObj == null) ? -1 : productObj.get().getRecId();
    }
    
    public String getStatusDescrip(){
        System.out.println("getStatusDescrip... statusObj: " + statusObj);
        return (statusObj == null) ? null : statusObj.get().getDescrip();
    }
    
    public int getStatus(){
        return (statusObj == null) ? -1 : statusObj.get().getLicenseStatusId();
    }
    
    public String getRemark(){
        return remark.get();
    }
    
    public String getAbbreviationFormer(){
        return abbreviationFormer.get();
    }
    
    public int getAdditionalDays(){
        return additionalDays.get();
    }
    
    
    public String getFirstDate(){
        // Beacouse firstDateDescrip contains user friendly view of date.
        return firstDateDescrip.get();
    }
    
    public int getLicenseNumber(){
        return licenseNumber.get();
    }
    
    public String getEmail(){
        return email.get();
    }
    
    public String getLastDate(){
        // Beacouse lastDateDescrip contains user friendly view of date.
        return lastDateDescrip.get();
    }
    
    public String getInvoiceNumber(){
        return invoiceNumber.get();
    }
    
//    public boolean getChecked(){
//        return checked.get();
//    }
//    
//    public String getDate(){
//        return date.get();
//    }
    
    
    // Setters:
    // This method must not show in other class to avoid createdDate changing.
    private void setCreatedDate(String createdDate){
        LocalDate date = DateConverter.getInstance().parseDate(createdDate);
        this.createdDate = DateConverter.getInstance().getDayMonthnameYearBySpace(date);
    }
    
    public void setClientId(int clienId){
//        clientObj.set(Client.getOneFromDB(clienId));
//        Client client = clientObj.get();
//        clientDescrip.set(client.getFirstName() + ", " + client.getLastName() + ", " + client.getEmail());
    }
    
    public void setProductId(int productId){
//        productObj.set(Product.getOneFromDB(productId));
//        productDescrip.set(productObj.get().getDescrip());
    }
    
    public void setStatusDescrip(String descrip){
        this.statusObj.get().setDescrip(descrip);
    }
    
    public void setStatus(int status){
        this.statusObj.get().setLicenseStatusId(status);
    }
    
    public void setRemark(String remark){
        this.remark.set(remark);
    }
    
    public void setAbbreviationFormer(String abbreviationFormer){
        this.abbreviationFormer.set(abbreviationFormer);
    }
    
    public void setAdditionalDays(int extraDays){
        additionalDays.set(extraDays);
    }
    
    public void setFirstDate(String date){
        firstDateObj.set(DateConverter.getInstance().parseDate(date));
    }
    
    public void setLicenseNumber(int number){
        licenseNumber.set(number);
    }
    
    public void setEmail(String email){
        this.email.set(email);
    }
    
    public void setLastDate(String date){
        lastDateObj.set(DateConverter.getInstance().parseDate(date));
    }
    
    public void setInvoiceNumber(String invoiceNumber){
        this.invoiceNumber.set(invoiceNumber);
    }
    
//    public void setChecked(boolean newValue){
//        this.checked.set(newValue);
//    }
//    
//    public void setDate(String date){
//        dateObjProperty.set(DateConverter.getInstance().parseDate(date));
//        
////        String localDateStr;
////        try {
////            dateObjProperty.set(DateConverter.parseDateWithTimeWithoutMilisecond(date));
////            localDateStr = DateConverter.getDayMonthnameYearBySpace(dateObjProperty.get());
////        } catch(Exception ex) {
////            localDateStr = date;
////        }
////        this.date.set(localDateStr);
//    }
    
    
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
        setCreatedDate(otherLicense.getCreatedDate());
        setLicenseNumber(otherLicense.getLicenseNumber());
        setClientId(otherLicense.getClientId());
        setProductId(otherLicense.getProductId());
        setInvoiceNumber(otherLicense.getInvoiceNumber());
        setStatus(otherLicense.getStatus());
        setStatusDescrip(otherLicense.getStatusDescrip());
        setRemark(otherLicense.getRemark());
        setAbbreviationFormer(otherLicense.getAbbreviationFormer());
        setFirstDate(otherLicense.getFirstDate());
        setLastDate(otherLicense.getLastDate());
        setAdditionalDays(otherLicense.getAdditionalDays());
        setEmail(otherLicense.getEmail());
    }
    
    @Override
    public boolean compares(EditorPanelable backup){
        return true;
    }

    @Override
    public String toStringForSearch() {
        return getLicenseNumber() + " " + getEmail() + getInvoiceNumber();
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
