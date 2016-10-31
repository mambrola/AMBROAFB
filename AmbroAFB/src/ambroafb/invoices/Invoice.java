/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.invoices;

import ambro.AView;
import ambroafb.clients.Client;
import ambroafb.general.AlertMessage;
import ambroafb.general.DBUtils;
import ambroafb.general.DateConverter;
import ambroafb.general.FilterModel;
import ambroafb.general.GeneralConfig;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.general.interfaces.TableColumnWidths;
import ambroafb.invoices.filter.InvoiceFilterModel;
import ambroafb.invoices.helper.InvoiceReissuing;
import ambroafb.invoices.helper.InvoiceStatus;
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
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import org.json.JSONObject;


/**
 *
 * @author mambroladze
 */
public class Invoice extends EditorPanelable { 

// ვინაიდან ეს მხოლოდ ჩვენებაა და თვითო tableView-ს ველში არ ხდება ჩასწორება Property-ები არ გვჭირდება
    
//    public int invoiceId;
    
    
    @AView.Column(title = "%created_date", width = TableColumnWidths.DATE)
    private StringProperty createdDate;
    
    @AView.Column(title = "%invoice_n", width = "100")
    private final SimpleStringProperty invoiceNumber;
    
    
    @AView.Column(title = "%licenses", width = "100")
    private final ObservableList<LicenseShortData> licenses;
    
    private final ObjectProperty<Client> clientObj;
    
    @AView.Column(title = "%begin_date", width = TableColumnWidths.DATE)
    private final StringProperty beginDateDescrip;
    private final ObjectProperty<LocalDate> beginDateObj;

    @AView.Column(title = "%end_date", width = TableColumnWidths.DATE)
    private final StringProperty endDateDescrip;
    private final ObjectProperty<LocalDate> endDateObj;
    
    @AView.Column(title = "%revoked_date", width = TableColumnWidths.DATE)
    private final StringProperty revokedDateDescrip;
    private final ObjectProperty<LocalDate> revokedDateObj;
    
    @AView.Column(title = "%extra_discount", width = "30")
    private final DoubleProperty additionalDiscount;
    
    @AView.Column(title = "%money_to_pay", width = "30")
    private final DoubleProperty moneyToPay;
    
    @AView.Column(title = "%vat", width = "30")
    private final DoubleProperty vat;
    
    @AView.Column(title = "%paid_part", width = "30")
    private final DoubleProperty paidPart;
    
    
    @AView.Column(title = "%invoice_reissuings", width = "50")
    private final ObjectProperty<InvoiceReissuing> reissuingObj;
    
    @AView.Column(title = "%invoice_status", width = "50")
    private final ObjectProperty<InvoiceStatus> status;
    
    
    private static final String DB_REISSUINGS_TABLE = "invoice_reissuing_descrips";
    private static final String DB_INVOICES_VIEW = "invoices_whole";
    
    
    public Invoice(){
        invoiceNumber = new SimpleStringProperty("");
        createdDate = new SimpleStringProperty("");
        licenses = FXCollections.observableArrayList();
        clientObj = new SimpleObjectProperty<>(new Client());
        beginDateDescrip = new SimpleStringProperty("");
        beginDateObj = new SimpleObjectProperty<>();
        endDateDescrip = new SimpleStringProperty("");
        endDateObj = new SimpleObjectProperty();
        revokedDateDescrip = new SimpleStringProperty("");
        revokedDateObj = new SimpleObjectProperty<>();
        additionalDiscount = new SimpleDoubleProperty(0);
        moneyToPay = new SimpleDoubleProperty(0);
        vat = new SimpleDoubleProperty(0);
        paidPart = new SimpleDoubleProperty(0);
        reissuingObj = new SimpleObjectProperty<>(new InvoiceReissuing());
        status = new SimpleObjectProperty<>(new InvoiceStatus());
    }
    
    
    // DBService methods:
    public static ArrayList<Invoice> getAllFromDB (){
        try {
            String data = GeneralConfig.getInstance().getAuthClient().get("invoices").getDataAsString();
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(data, new TypeReference<ArrayList<Invoice>>() {});
        } catch (IOException | AuthServerException ex) {
            Logger.getLogger(Invoice.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public static ArrayList<Invoice> getFilteredFromDB(FilterModel model){
        InvoiceFilterModel invoiseFilterModel = (InvoiceFilterModel) model;
        WhereBuilder whereBuilder = new ConditionBuilder().where().and("begin_date", ">=", invoiseFilterModel.getStartDateForDB(true))
                    .and("begin_date", "<=", invoiseFilterModel.getStartDateForDB(false))
                    .and("end_date", ">=", invoiseFilterModel.getEndDateForDB(true))
                    .and("end_date", "<=", invoiseFilterModel.getEndDateForDB(false));
        
        if (invoiseFilterModel.isSelectedConcreteClient()){
            whereBuilder.and("client_id", "=", invoiseFilterModel.getSelectedClient().getRecId());
        }
        if (invoiseFilterModel.hasSelectedReissuings()){
            whereBuilder = whereBuilder.andGroup();
            for(InvoiceReissuing invReissuing : invoiseFilterModel.getCheckedReissuings()){
                whereBuilder.or("reissuing", "=", invReissuing.getInvoiceReissuingId());
            }
            whereBuilder = whereBuilder.closeGroup();
        }
        
        JSONObject params = whereBuilder.condition().build();
        return DBUtils.getObjectsListFromDB(Invoice.class, DB_INVOICES_VIEW, params);
    }
    
    public static ArrayList<InvoiceReissuing> getAllIvoiceReissuingsesFromDB(){
        DBClient dbClient = GeneralConfig.getInstance().getDBClient();
        JSONObject params = new ConditionBuilder().where().and("language", "=", dbClient.getLang()).condition().build();
        return DBUtils.getObjectsListFromDB(InvoiceReissuing.class, DB_REISSUINGS_TABLE, params);
    }
    
    public static Invoice getOneFromDB (int invoiceId){
        try {
            String data = GeneralConfig.getInstance().getAuthClient().get("invoices/" + invoiceId).getDataAsString();
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(data, Invoice.class);
        } catch (IOException | AuthServerException ex) {
            Logger.getLogger(Invoice.class.getName()).log(Level.SEVERE, null, ex);
            new AlertMessage(Alert.AlertType.ERROR, ex, ex.getMessage(), "Invoice").showAlert();
        }
        return null;
    }

    // Properties getters:
    public SimpleStringProperty invoiceNumberProperty(){
        return invoiceNumber;
    }
    
    
    
    
    // Getters:
    public String getCreatedDate(){
        return createdDate.get();
    }
    
    @JsonIgnore
    public LocalDate getLocalDateObj(){
        return DateConverter.getInstance().parseDate(getCreatedDate());
    }
    
    public String getInvoiceNumber(){
        return invoiceNumber.get();
    }
    
    public ObservableList<LicenseShortData> getLicenses(){
        return licenses;
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
    
    public String getBeginDate(){
        return (beginDateObj.get() == null) ? "" : beginDateObj.get().toString();
    }
    
    public String getEndDate(){
        return (endDateObj.get() == null) ? "" : beginDateObj.get().toString();
    }
    
    public String getRevokedDate(){
        return (revokedDateObj.get() == null) ? "" : revokedDateObj.get().toString();
    }
            
    public double getAdditionalDiscount(){
        return additionalDiscount.get();
    }
    
    public double getMoneyToPay(){
        return moneyToPay.get();
    }

    public double getVat(){
        return vat.get();
    }
    
    public double getPaidPart(){
        return paidPart.get();
    }

    public String getReissuingDescrip(){
        return reissuingObj.get().getDescrip();
    }
    
    public int getReissuing(){
        return reissuingObj.get().getInvoiceReissuingId();
    }
    
    public String getStatusDescrip(){
        return status.get().getDescrip();
    }
    
    public int getStatus(){
        return status.get().getInvoiceStatusId();
    }
    
    
    // Setters:
    public void setCreatedDate(String date){
        createdDate.set(date);
    }
    
    public void setInvoiceNumber(String invoiceNumber){
        this.invoiceNumber.set(invoiceNumber);
    }
    
    public void setLicenses(Collection<LicenseShortData> licenses){
        this.licenses.setAll(licenses);
    }
    
    public void setClientId(int recId){
        clientObj.get().setRecId(recId);
    }

    public void setFirstName(String firstName){
        clientObj.get().setFirstName(firstName);
    }
    
    public void setLastName(String lastName){
        clientObj.get().setLastName(lastName);
    }
    
    public void setBeginDate(String date){
        beginDateObj.set(DateConverter.getInstance().parseDate(date));
        beginDateDescrip.set(DateConverter.getInstance().getDayMonthnameYearBySpace(beginDateObj.get()));
    }
    
    public void setEndDate(String date){
        endDateObj.set(DateConverter.getInstance().parseDate(date));
        endDateDescrip.set(DateConverter.getInstance().getDayMonthnameYearBySpace(endDateObj.get()));
    }
    
    public void setRevokedDate(String date){
        revokedDateObj.set(DateConverter.getInstance().parseDate(date));
        revokedDateDescrip.set(DateConverter.getInstance().getDayMonthnameYearBySpace(revokedDateObj.get()));
    }
    
    public void setAdditionalDiscount(double additDisc){
        additionalDiscount.set(additDisc);
    }
    
    public void setMoneyToPay(double money){
        moneyToPay.set(money);
    }
    
    public void setVat(double vat){
        this.vat.set(vat);
    }
    
    public void setPaidPart(double paidPart){
        this.paidPart.set(paidPart);
    }
    
    public void setReissuingDescrip(String descrip){
        reissuingObj.get().setDescrip(descrip);
    }
    
    public void setReissuing(int reissuingId){
        reissuingObj.get().setInvoiceReissuingId(reissuingId);
    }
    
    
    public void setStatusDescrip(String status){
        this.status.get().setDescrip(status);
    }
    
    public void setStatus(int status){
        this.status.get().setInvoiceStatusId(status);
    }
    
    
    // Methods override:
    @Override
    public Invoice cloneWithoutID() {
        Invoice clone = new Invoice();
        clone.copyFrom(this);
        return clone;
    }

    @Override
    public Invoice cloneWithID() {
        Invoice clone = cloneWithoutID();
        clone.setRecId(this.getRecId());
        return clone;
    }

    @Override
    public void copyFrom(EditorPanelable other) {
        Invoice invoice = (Invoice) other;
        setLicenses(invoice.getLicenses());
        setStatus(invoice.getStatus());
        setInvoiceNumber(invoice.getInvoiceNumber());
//        setCreatedDate(invoice.getCreatedDate());
//        setBeginDate(invoice.getBeginDate());
//        setEndDate(invoice.getEndDate());
    }

    @Override
    public String toStringForSearch() {
//        return getInvoiceNumber().concat(getCreatedDate()).concat(getBeginDate()).concat(getEndDate());
        return "";
    }

    @Override
    public boolean compares(EditorPanelable backup) {
        Invoice invoiceBackup = (Invoice) backup;
        return true;
    }

    
    private class LicenseShortData {
        public int recId;
        public int licenseId;
        public int licenseNumber;
    }
}
