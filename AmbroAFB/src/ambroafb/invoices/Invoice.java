/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.invoices;

import ambro.AView;
import ambroafb.clients.Client;
import ambroafb.general.DBUtils;
import ambroafb.general.DateConverter;
import ambroafb.general.FilterModel;
import ambroafb.general.GeneralConfig;
import ambroafb.general.Utils;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.general.interfaces.TableColumnWidths;
import ambroafb.invoices.filter.InvoiceFilterModel;
import ambroafb.invoices.helper.InvoiceReissuing;
import ambroafb.invoices.helper.InvoiceStatus;
import ambroafb.licenses.License;
import ambroafb.products.Product;
import authclient.db.ConditionBuilder;
import authclient.db.DBClient;
import authclient.db.WhereBuilder;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringExpression;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;


/**
 *
 * @author mambroladze
 */
public class Invoice extends EditorPanelable { 
    
    @AView.Column(title = "%created_date", width = TableColumnWidths.DATE, styleClass = "textCenter")
    private final StringProperty createdDate;
    
    @AView.Column(title = "%invoice_N", width = "100")
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private final SimpleStringProperty invoiceNumber;
    
    @AView.Column(title = "%licenses", width = "100")
    @JsonIgnore
    private final StringProperty licensesDescript;
    private final ObservableList<LicenseShortData> licenses;
    @JsonIgnore
    public ObservableList<License> licensesOnProducts;
    
    @AView.Column(title = "%clients", width = "100")
    @JsonIgnore
    private final StringExpression clientDescrip;
    @JsonIgnore
    private final ObjectProperty<Client> clientObj;
    
    @AView.Column(title = "%begin_date", width = TableColumnWidths.DATE, styleClass = "textCenter")
    @JsonIgnore
    private final StringProperty beginDateDescrip;
    @JsonIgnore
    private final ObjectProperty<LocalDate> beginDateObj;

    @AView.Column(title = "%end_date", width = TableColumnWidths.DATE, styleClass = "textCenter")
    @JsonIgnore
    private final StringProperty endDateDescrip;
    @JsonIgnore
    private final ObjectProperty<LocalDate> endDateObj;
    
    @AView.Column(title = "%revoked_date", width = TableColumnWidths.DATE, styleClass = "textCenter")
    @JsonIgnore
    private final StringProperty revokedDateDescrip;
    @JsonIgnore
    private final ObjectProperty<LocalDate> revokedDateObj;
    @JsonIgnore
    private final BooleanProperty isRevoked;
    
    @AView.Column(title = "%extra_discount", width = "70", styleClass = "textRight")
    private final StringProperty additionalDiscountRate;
    
    @AView.Column(title = "%money_to_pay", width = "70", styleClass = "textRight" )
    private final StringProperty moneyToPay;
    
    
    @AView.Column(title = "%vat", width = "70", styleClass = "textRight")
    private final StringProperty vat;
    
    @AView.Column(title = "%money_paid", width = "70", styleClass = "textRight")
    private final StringProperty moneyPaid;
    
    
    @AView.Column(title = "%invoice_reissuings", width = "100")
    @JsonIgnore
    private final ObjectProperty<InvoiceReissuing> reissuingObj;
    
    @AView.Column(title = "%invoice_status", width = "100")
    @JsonIgnore
    private final ObjectProperty<InvoiceStatus> statusObj;
    
    private final StringProperty months;
    private final BooleanProperty isLogined, isPaid;
    
    @JsonIgnore
    private final ObjectProperty<Map<Product, Integer>> licensesResult;
    
    @JsonIgnore
    private static final String DB_REISSUINGS_TABLE = "invoice_reissuing_descrips";
    @JsonIgnore
    private static final String DB_INVOICES_VIEW = "invoices_whole";
    
    
    public Invoice(){
        invoiceNumber = new SimpleStringProperty("");
        createdDate = new SimpleStringProperty("");
        licensesDescript = new SimpleStringProperty("");
        licenses = FXCollections.observableArrayList();
        licensesOnProducts = FXCollections.observableArrayList();
        clientObj = new SimpleObjectProperty<>(new Client());
        clientDescrip = clientObj.get().getShortDescrip(", ");
        beginDateDescrip = new SimpleStringProperty("");
        beginDateObj = new SimpleObjectProperty<>();
        endDateDescrip = new SimpleStringProperty("");
        endDateObj = new SimpleObjectProperty();
        revokedDateDescrip = new SimpleStringProperty("");
        revokedDateObj = new SimpleObjectProperty<>();
        isRevoked = new SimpleBooleanProperty(false);
        additionalDiscountRate = new SimpleStringProperty("0");
        moneyToPay = new SimpleStringProperty("");
        moneyPaid = new SimpleStringProperty("");
        vat = new SimpleStringProperty("");
        reissuingObj = new SimpleObjectProperty<>(new InvoiceReissuing());
        statusObj = new SimpleObjectProperty<>(new InvoiceStatus());
        months = new SimpleStringProperty("");
        licensesResult = new SimpleObjectProperty<>(new HashMap<>());
        isLogined = new SimpleBooleanProperty(false);
        isPaid = new SimpleBooleanProperty(false);
        
        licenses.addListener((ListChangeListener.Change<? extends LicenseShortData> c) -> {
            rebindLicenses();
        });
        
        beginDateObj.addListener((ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) -> {
            if (months.isNotEmpty().get()){
                rebindEndDate();
            }
        });
        
        months.addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (beginDateObj.get() != null){
                rebindEndDate();
            }
        });
        
    }
    
    private void rebindLicenses(){
        licensesDescript.unbind();
        licensesDescript.bind(licenses.stream()
                                    .map(LicenseShortData::licenseNumberProperty)
                                    .reduce(new SimpleStringProperty(""), (StringProperty total, StringProperty unary) -> {
                                        StringProperty temp = new SimpleStringProperty("");
                                        temp.bind(total.concat(Bindings.createStringBinding(() -> {
                                            return (total.get().isEmpty()) ? "" : ", ";
                                        }, total.isEmpty())).concat(unary));
                                        return temp;
                                    })
                            );
    }
    
    private void rebindEndDate(){
        Double monthCount = Utils.getDoubleValueFor(months.get());
        long monthValue = monthCount.longValue();
        long dayValue = (long)((monthCount - monthValue) * 30);
        endDateObj.set(beginDateObj.get().plusMonths(monthValue).plusDays(dayValue));
    }
    
    // DBService methods:
    public static ArrayList<Invoice> getAllFromDB (){
        JSONObject params = new ConditionBuilder().build();
        return DBUtils.getObjectsListFromDB(Invoice.class, DB_INVOICES_VIEW, params);
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
        JSONObject params = new ConditionBuilder().where().and("rec_id", "=", invoiceId).condition().build();
        return DBUtils.getObjectFromDB(Invoice.class, DB_INVOICES_VIEW, params);
    }
    
    public static Invoice saveOneToDB(Invoice invoice) {
        if (invoice == null) return null;
        Map<Product, Integer> productsMap = invoice.licensesResultProperty().get();        
        JSONArray productsArray = new JSONArray();
        productsMap.keySet().stream().forEach((product) -> {
            JSONObject json = getJsonFrom(null, "product_id", product.getRecId());
            productsArray.put(getJsonFrom(json, "count", productsMap.get(product)));
        });

//        System.out.println("productsArray: " + productsArray);
        
        JSONArray licensesIds = new JSONArray();
        invoice.getLicensesShortData().stream().forEach((licenseShortData) -> {
            licensesIds.put(getJsonFrom(null, "license_id", licenseShortData.licenseId));
        });
        DBUtils.callInvoiceSuitedLicenses(null, invoice.getClientId(), invoice.beginDateProperty().get(), invoice.endDateProperty().get(), productsArray, invoice.getAdditionalDiscountRate(), licensesIds);
        ArrayList<License> licenses = DBUtils.getLicenses();
        List<LicenseShortData> wholeLicenses = licenses.stream().map((License license) -> {
                                                                        LicenseShortData shortData = new LicenseShortData();
                                                                        shortData.licenseId = license.getRecId();
                                                                        shortData.setLicenseNumber(license.getLicenseNumber());
                                                                        return shortData;
                                                                }).collect(Collectors.toList());
        System.out.println("invoice whole license: " + wholeLicenses);
        
        invoice.setLicenses(wholeLicenses);
        return DBUtils.saveObjectToDB(invoice, "invoice");
    }

    /** Returns JSON object for given key and value */
    private static JSONObject getJsonFrom(JSONObject jsonObj, String key, Object value){
        JSONObject json = (jsonObj == null) ? new JSONObject() : jsonObj;
        try {
            json.put(key, value);
        } catch (JSONException ex) {
            Logger.getLogger(Invoice.class.getName()).log(Level.SEVERE, null, ex);
        }
        return json;
    }
    
    public static boolean deleteOneFromDB(int id) {
        JSONObject params = new ConditionBuilder().where().and("rec_id", "=", id).condition().build();
        return DBUtils.deleteObjectFromDB("invoice_delete", params);
    }
    

    // Properties getters:
    public ObjectProperty<Client> clientProperty(){
        return clientObj;
    }
    
    public SimpleStringProperty invoiceNumberProperty(){
        return invoiceNumber;
    }
    
    public ObjectProperty<LocalDate> beginDateProperty(){
        return beginDateObj;
    }
    
    public ObjectProperty<LocalDate> endDateProperty(){
        return endDateObj;
    }
    
    public ObjectProperty<LocalDate> revokedDateProperty(){
        return revokedDateObj;
    }
    
    public BooleanProperty revokedProperty(){
        return isRevoked;
    }
    
    public StringProperty additionaldiscountProperty(){
        return additionalDiscountRate;
    }

    public StringProperty moneyToPayProperty(){
        return moneyToPay;
    }
    
    public StringProperty vatProperty(){
        return vat;
    }

    public StringProperty moneyPaidProperty(){
        return moneyPaid;
    }
    
    public ObjectProperty<InvoiceReissuing> reissuingProperty(){
        return reissuingObj;
    }
    
    public ObjectProperty<InvoiceStatus> statusProperty(){
        return statusObj;
    }
    
    public StringProperty monthsProperty(){
        return months;
    }
    
    public ObjectProperty<Map<Product, Integer>> licensesResultProperty(){
        return licensesResult;
    }
    
    public BooleanProperty isLoginedProperty(){
        return isLogined;
    }
    
    public BooleanProperty isPaidProperty(){
        return isPaid;
    }
    
    
    
    // Getters:
//    @JsonIgnore
//    public LocalDate getLocalDateObj(){
//        return DateConverter.getInstance().parseDate(createdDate.get());
//    }
//    
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    @JsonIgnore
    public String getInvoiceNumber(){
        return invoiceNumber.get();
    }
    

    @JsonIgnore
    public ObservableList<LicenseShortData> getLicensesShortData(){
        return licenses;
    }
    
    public ObservableList<Integer> getLicenses(){
        ObservableList<Integer> licenseIds = FXCollections.observableArrayList();
        licenses.stream().forEach((shortData) -> {
            licenseIds.add(shortData.getLicenseId());
        });
        return licenseIds;
    }
    
    public int getClientId(){
        return (clientObj.isNull().get()) ? -1 : clientObj.get().getRecId();
    }
    
    @JsonIgnore
    public String getFirstName(){
        return (clientObj.isNull().get()) ? "" : clientObj.get().getFirstName();
    }
    
    @JsonIgnore
    public String getLastName(){
        return (clientObj.isNull().get()) ? "" : clientObj.get().getLastName();
    }
    
    @JsonIgnore
    public String getEmail(){
        return (clientObj.isNull().get()) ? "" : clientObj.get().getEmail();
    }
    
    public String getBeginDate(){
        return (beginDateObj.isNull().get()) ? "" : beginDateObj.get().toString();
    }
    
    public String getEndDate(){
        return (endDateObj.isNull().get()) ? "" : beginDateObj.get().toString();
    }
    
    @JsonIgnore
    public String getRevokedDate(){
        return (revokedDateObj.isNull().get()) ? "" : revokedDateObj.get().toString();
    }
    
    @JsonIgnore
    public double getAdditionalDiscountRate(){
        return Utils.getDoubleValueFor(additionalDiscountRate.get());
    }
    
    @JsonIgnore
    public double getMoneyToPay(){
        return Utils.getDoubleValueFor(moneyToPay.get());
    }
    
    @JsonIgnore
    public double getMoneyPaid(){
        return Utils.getDoubleValueFor(moneyPaid.get());
    }

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    @JsonIgnore
    public double getVat(){
        return Utils.getDoubleValueFor(vat.get());
    }
    
    @JsonIgnore
    public String getReissuingDescrip(){
        return (reissuingObj.isNull().get()) ? "" : reissuingObj.get().getDescrip();
    }
    
    @JsonIgnore
    public int getReissuing(){
        return (reissuingObj.isNull().get()) ? -1 : reissuingObj.get().getInvoiceReissuingId();
    }
    
    @JsonIgnore
    public String getStatusDescrip(){
        return (statusObj.isNull().get()) ? "" : statusObj.get().getDescrip();
    }
    
    @JsonIgnore
    public int getStatus(){
        return (statusObj.isNull().get()) ? -1 : statusObj.get().getInvoiceStatusId();
    }
    
    @JsonIgnore
    public String getMonths(){
        return months.get();
    }
    
    
    // Setters:
    private void setCreatedDate(String date){
        LocalDate localDate = DateConverter.getInstance().parseDate(date);
        String userFriendlyDateVisual = DateConverter.getInstance().getDayMonthnameYearBySpace(localDate);
        createdDate.set(userFriendlyDateVisual);
    }
    
    public void setInvoiceNumber(String invoiceNumber){
        this.invoiceNumber.set(invoiceNumber);
    }
    
    public void setLicenses(Collection<LicenseShortData> licenses){
        this.licenses.setAll(licenses);

        licensesOnProducts.clear();
        licenses.stream().forEach((shortData) -> {
            License license = new License();
            license.setRecId(shortData.licenseId);
            license.setLicenseNumber(shortData.getLicenseNumber());
            licensesOnProducts.add(license);
        });
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
    
    public void setEmail(String email){
        clientObj.get().setEmail(email);
    }
    
    public void setBeginDate(String date){
        beginDateObj.set(DateConverter.getInstance().parseDate(date));
        beginDateDescrip.set(DateConverter.getInstance().getDayMonthnameYearBySpace(beginDateObj.get()));
    }
    
    public void setEndDate(String date){
        endDateObj.set(DateConverter.getInstance().parseDate(date));
        endDateDescrip.set(DateConverter.getInstance().getDayMonthnameYearBySpace(endDateObj.get()));
    }
    
    public void setRevokedDate(String date) {
        revokedDateObj.set(DateConverter.getInstance().parseDate(date));
        revokedDateDescrip.set(DateConverter.getInstance().getDayMonthnameYearBySpace(revokedDateObj.get()));
        isRevoked.set(revokedDateObj.get() != null);
    }
    
    public void setAdditionalDiscountRate(double additDisc){
        additionalDiscountRate.set("" + additDisc);
    }
    
    public void setMoneyToPay(double money){
        moneyToPay.set("" + money);
    }
    
    public void setMoneyPaid(double money){
        moneyPaid.set("" + money);
    }
    
    public void setVat(double vat){
        this.vat.set("" + vat);
    }
    
    public void setReissuingDescrip(String descrip){
        reissuingObj.get().setDescrip(descrip);
    }
    
    public void setReissuing(int reissuingId){
        reissuingObj.get().setInvoiceReissuingId(reissuingId);
    }
    
    
    public void setStatusDescrip(String status){
        this.statusObj.get().setDescrip(status);
    }
    
    public void setStatus(int status){
        this.statusObj.get().setInvoiceStatusId(status);
    }
    
    public void setMonths(String months){
        this.months.set(months);
    }
    
    public void setIsLogined(int logined){
        isLogined.set(logined == 1);
    }
    
    public void setIsPaid(int paid){
        isPaid.set(paid == 1);
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
        setFirstName(invoice.getFirstName());
        setLastName(invoice.getLastName());
        setEmail(invoice.getEmail());
        setInvoiceNumber(invoice.getInvoiceNumber());
        licenses.clear(); // Avoid to add twise licenses in tableView
        licenses.addAll(invoice.getLicensesShortData());
        setBeginDate(invoice.getBeginDate());
        setEndDate(invoice.getEndDate());
        setRevokedDate(invoice.getRevokedDate());
        setAdditionalDiscountRate(invoice.getAdditionalDiscountRate());
        setMoneyToPay(invoice.getMoneyToPay());
        setVat(invoice.getVat());
        setMoneyPaid(invoice.getMoneyPaid());
        reissuingObj.get().copyFrom(invoice.reissuingProperty().get());
        statusObj.get().copyFrom(invoice.statusProperty().get());
    }

    @Override
    public String toStringForSearch() {
        String searchString = "";
        if (clientObj.isNotNull().get() && licensesDescript.isNotNull().get()){
            searchString = clientObj.get().getShortDescrip(" ").get() + " " + licensesDescript.get();
        }
        return searchString;
    }

    @Override
    public boolean compares(EditorPanelable backup) {
        Invoice otherInvoice = (Invoice) backup;
        return  getFirstName().equals(otherInvoice.getFirstName())  &&
                getLastName().equals(otherInvoice.getLastName())    &&
                getEmail().equals(otherInvoice.getEmail())          &&
                getInvoiceNumber().equals(otherInvoice.getInvoiceNumber())  &&
                Utils.compareListsByElemOrder(licenses, otherInvoice.getLicensesShortData())    &&
                Utils.dateEquals(beginDateProperty().get(), otherInvoice.beginDateProperty().get()) &&
                Utils.dateEquals(endDateProperty().get(), otherInvoice.endDateProperty().get()) &&
                Utils.dateEquals(revokedDateProperty().get(), otherInvoice.revokedDateProperty().get()) &&
//                getBeginDate().equals(otherInvoice.getBeginDate())          &&
//                getEndDate().equals(otherInvoice.getEndDate())      &&
//                getRevokedDate().equals(otherInvoice.getRevokedDate()) &&
                getAdditionalDiscountRate() == otherInvoice.getAdditionalDiscountRate() &&
                getMoneyToPay() == otherInvoice.getMoneyToPay() &&
                getVat() == otherInvoice.getVat() &&
                getMoneyPaid() == otherInvoice.getMoneyPaid() &&
                reissuingObj.get().compares(otherInvoice.reissuingProperty().get()) &&
                statusObj.get().compares(otherInvoice.statusProperty().get());
    }

    
    @SuppressWarnings("EqualsAndHashcode")
    public static class LicenseShortData {
        
        private int recId;
        private int licenseId;
        public StringProperty licenseNumber = new SimpleStringProperty("");
        
        public StringProperty licenseNumberProperty(){
            return licenseNumber;
        }
        
        public int getRecId(){
            return recId;
        }
        
        public int getLicenseId(){
            return licenseId;
        }
        
        public int getLicenseNumber(){
            return Integer.parseInt(licenseNumber.get());
        }
        
        public void setRecId(int recId){
            this.recId = recId;
        }
        
        public void setLicenseId(int licenseId){
            this.licenseId = licenseId;
        }
        
        public void setLicenseNumber(int number){
            licenseNumber.set("" + number);
        }
        
        @Override
        public String toString(){
            return licenseNumber.get();
        }
        
        @Override
        @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
        public boolean equals(Object other){
            if (other == null) return false;
            LicenseShortData otherLicense = (LicenseShortData) other;
            return  licenseId == otherLicense.licenseId &&
                    licenseNumber.get().equals(otherLicense.licenseNumber.get());
        }
    }
}
