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
import ambroafb.general.Utils;
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
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.StringExpression;
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
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Dato
 */
public class License extends EditorPanelable {

    public String password;
    
    @AView.Column(title = "%created_date", width = TableColumnWidths.DATE, styleClass = "textCenter")
    private final StringProperty createdDate;
    
    @AView.Column(title = "%license N", width = "100", styleClass = "textRight")
    private final IntegerProperty licenseNumber;
    
    @AView.Column(title = "%client", width = "150", styleClass = "textCenter")
    @JsonIgnore
    private final StringExpression clientDescrip;
    @JsonIgnore
    private final ObjectProperty<Client> clientObj;
    private int clientId; // for object mapper (case: class to json)
    private String firstName, lastName, email;
    
    @AView.Column(title = "%product", width = "70", styleClass = "textCenter")
    @JsonIgnore
    private final StringExpression productDescrip;
    @JsonIgnore
    private final ObjectProperty<Product> productObj;
    private int productId; // for object mapper (case: class to json)
    private final StringProperty abbreviation, former;
    
    @AView.Column(title = "%last_invoice", width = "100")
    private final StringProperty invoiceNumber;
    @JsonIgnore
    private final StringProperty lastInvoiceDescrip;
    @JsonIgnore
    private final ObjectProperty<Invoice> invoiceObj;
    private final IntegerProperty cfCurrentInvoiceId;
    private final IntegerProperty cfFutureInvoiceId;
    
    @AView.Column(title = "%license_statuses", width = "120")
    private final StringProperty statusDescrip;
    @JsonIgnore
    private final ObjectProperty<LicenseStatus> statusObj;
    private final IntegerProperty status; // for object mapper (case: class to json)
    
    private final StringProperty remark;
    
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
    
    private final StringProperty lastLoginTime;
    private final ObjectProperty<LocalDate> lastLoginDateObj;
    
    
    private static final String DB_VIEW_NAME = "licenses_whole";
    private static final String DB_TABLE_NAME = "licenses";
    private static final String DB_STATUSES_TABLE_NAME = "license_status_descrips";
    
    public License(){
        createdDate = new SimpleStringProperty("");
        clientObj = new SimpleObjectProperty<>(new Client());
        clientDescrip = Utils.avoidNull(clientObj.get().firstNameProperty()).concat(" ").concat(Utils.avoidNull(clientObj.get().lastNameProperty())).concat(" ").concat(Utils.avoidNull(clientObj.get().emailProperty()));
        productObj = new SimpleObjectProperty<>(new Product());
        productDescrip = Utils.avoidNull(productObj.get().abbreviationProperty()).concat(Utils.avoidNull(productObj.get().formerProperty()));
        abbreviation = new SimpleStringProperty("");
        former = new SimpleStringProperty("");
        lastInvoiceDescrip = new SimpleStringProperty("");
        invoiceObj = new SimpleObjectProperty<>(new Invoice());
        status = new SimpleIntegerProperty(0);
        statusDescrip = new SimpleStringProperty("");
        statusObj = new SimpleObjectProperty<>(new LicenseStatus());
        remark = new SimpleStringProperty("");
        additionalDays = new SimpleIntegerProperty(0);
        firstDateDescrip = new SimpleStringProperty("");
        firstDateObj = new SimpleObjectProperty<>();
        lastDateDescrip = new SimpleStringProperty("");
        lastDateObj = new SimpleObjectProperty<>();
        licenseNumber = new SimpleIntegerProperty(0);
        invoiceNumber = new SimpleStringProperty("");
        lastLoginTime = new SimpleStringProperty("");
        lastLoginDateObj = new SimpleObjectProperty<>();
        cfCurrentInvoiceId = new SimpleIntegerProperty(0);
        cfFutureInvoiceId = new SimpleIntegerProperty(0);
        
        statusObj.addListener((ObservableValue<? extends LicenseStatus> observable, LicenseStatus oldValue, LicenseStatus newValue) -> {
//            if (newValue != null){
//                status.set(newValue.getLicenseStatusId());
//                statusDescrip.set(newValue.getDescrip());
//            }
           rebindStatus(); 
        });
        rebindStatus();
        
        firstDateObj.addListener(new DateListener(firstDateDescrip));
        lastDateObj.addListener(new DateListener(lastDateDescrip));
        lastLoginDateObj.addListener(new DateListener(lastLoginTime));
    }
    
    private void rebindStatus(){
        status.unbind();
        statusDescrip.unbind();
        if (statusObj.get() != null){
            status.bind(statusObj.get().statusIdProperty());
            statusDescrip.bind(statusObj.get().descripProperty());
        }
    }
    
    // DB methods:
    public static ArrayList<License> getAllFromDB(){
        try {
            JSONObject params = new ConditionBuilder().build();
            String data = GeneralConfig.getInstance().getDBClient().select(DB_VIEW_NAME, params).toString();
            
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
            JSONObject params = new ConditionBuilder().where().and("license_status_id", "=", licenseStatusId).condition().build();
            JSONArray data = GeneralConfig.getInstance().getDBClient().select(DB_STATUSES_TABLE_NAME, params);
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
        try {
            JSONObject params = new ConditionBuilder().where().and("rec_id", "=", recId).condition().build();
            JSONArray clientResult = GeneralConfig.getInstance().getDBClient().select(DB_VIEW_NAME, params);
            String data = clientResult.optJSONObject(0).toString();
            
            System.out.println("one license data: " + data);
            
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(data, License.class);    
        } catch (IOException | AuthServerException ex) {
            Logger.getLogger(License.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public static License saveOneToDB(License license){
        if (license == null) return null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            ObjectWriter writer = mapper.writer().withDefaultPrettyPrinter();
            JSONObject clientJson = new JSONObject(writer.writeValueAsString(license));
            DBClient dbLicense = GeneralConfig.getInstance().getDBClient();
            JSONObject newLicense = dbLicense.callProcedureAndGetAsJson("general_insert_update", DB_TABLE_NAME, dbLicense.getLang(), clientJson).getJSONObject(0);
            
            System.out.println("save license data: " + newLicense.toString());
            
            return mapper.readValue(newLicense.toString(), License.class);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException | AuthServerException | JSONException ex) {
            Logger.getLogger(License.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public static boolean deleteFromDB(int recId){
        System.out.println("delete from db...??");
        return false;
    }
    
    
    // properties:
    
    
    
    // Getters:
    public String getCreatedDate(){
        return createdDate.get();
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
    
    public String getStatusDescrip(){
        return statusObj.get().getDescrip();
    }
    
    public int getStatus(){
        return statusObj.get().getLicenseStatusId();
    }
    
    public String getRemark(){
        return remark.get();
    }
    
    public String getAbbreviation(){
        return productObj.get().getAbbreviation();
    }
    
    public int getFormer(){
        return productObj.get().getFormer();
    }
    
    public int getAdditionalDays(){
        return additionalDays.get();
    }
    
    public String getFirstDate(){
        // Beacause firstDateDescrip contains user friendly view of date.
        return firstDateDescrip.get();
    }
    
    public int getLicenseNumber(){
        return licenseNumber.get();
    }
    
    public String getLastDate(){
        // Beacause lastDateDescrip contains user friendly view of date.
        return lastDateDescrip.get();
    }
    
    public int getCfCurrentInvoiceId(){
        return cfCurrentInvoiceId.get();
    }
    
    public int getCfFutureInvoiceId(){
        return cfFutureInvoiceId.get();
    }
    
    public String getInvoiceNumber(){
        return invoiceObj.get().getInvoiceNumber();
    }
    
    public String getLastLoginDate(){
        return lastLoginTime.get();
    }
    
    public String getLastLoginTime(){
        return lastLoginTime.get();
    }
    
    
    
    // Setters:
    // This method must not show in other class to avoid createdDate changing.
    private void setCreatedDate(String createdDate){
        LocalDate date = DateConverter.getInstance().parseDate(createdDate);
        this.createdDate.set(DateConverter.getInstance().getDayMonthnameYearBySpace(date));
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
    
    public void setStatusDescrip(String statusDescrip){
        this.statusObj.get().setDescrip(statusDescrip);
    }
    
    public void setStatus(int status){
        this.statusObj.get().setLicenseStatusId(status);
    }
    
    public void setRemark(String remark){
        this.remark.set(remark);
    }
    
    public void setAbbreviation(String abbreviation){
        productObj.get().setAbbreviation(abbreviation);
    }
    
    public void setFormer(int former){
        productObj.get().setFormer(former);
    }
    
    public void setAdditionalDays(int extraDays){
        additionalDays.set(extraDays);
    }
    
    public void setLicenseNumber(int number){
        licenseNumber.set(number);
    }
    
    public void setFirstDate(String date){
        firstDateObj.set(DateConverter.getInstance().parseDate(date));
    }
    
    public void setLastDate(String date){
        lastDateObj.set(DateConverter.getInstance().parseDate(date));
    }
    
    public void setCfCurrentInvoiceId(int id){
        this.cfCurrentInvoiceId.set(id);
    }
    
    public void setCfFutureInvoiceId(int id){
        this.cfFutureInvoiceId.set(id);
    }
    
    public void setInvoiceNumber(String invoiceNumber){
        this.invoiceObj.get().setInvoiceNumber(invoiceNumber);
    }
    
    public void setLastLoginTime(String date){
        lastLoginDateObj.set(DateConverter.getInstance().parseDate(date));
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
        setCreatedDate(otherLicense.getCreatedDate());
        setCreatedDate(otherLicense.getCreatedDate());
        setLicenseNumber(otherLicense.getLicenseNumber());
        setClientId(otherLicense.getClientId());
        setProductId(otherLicense.getProductId());
        setInvoiceNumber(otherLicense.getInvoiceNumber());
        setStatus(otherLicense.getStatus());
        setStatusDescrip(otherLicense.getStatusDescrip());
        setRemark(otherLicense.getRemark());
        setAbbreviation(otherLicense.getAbbreviation());
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
