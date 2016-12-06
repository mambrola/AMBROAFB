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
import ambroafb.general.monthcountercombobox.MonthCounterItem;
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
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    public ObservableList<License> licensesOnProducts; // ????
    
    @AView.Column(title = "%clients", width = "250")
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
    
    @AView.Column(title = "%extra_discount", width = "100", styleClass = "textRight")
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
    
    private final ObjectProperty<MonthCounterItem> months;
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
        months = new SimpleObjectProperty<>(new MonthCounterItem(""));
        licensesResult = new SimpleObjectProperty<>(new HashMap<>());
        isLogined = new SimpleBooleanProperty(false);
        isPaid = new SimpleBooleanProperty(false);
        
        licenses.addListener((ListChangeListener.Change<? extends LicenseShortData> c) -> {
            rebindLicenses();
        });
        
        beginDateObj.addListener((ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) -> {
            if (months.get() != null && months.get().getMonthCount() != -1){
                rebindEndDate();
            }
        });
        
        months.addListener((ObservableValue<? extends MonthCounterItem> observable, MonthCounterItem oldValue, MonthCounterItem newValue) -> {
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
        long monthValue = months.get().getMonthCount();
        long dayValue = months.get().getDayCount();
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
                whereBuilder = whereBuilder.or("reissuing", "=", invReissuing.getInvoiceReissuingId());
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
            JSONObject json = Utils.getJsonFrom(null, "product_id", product.getRecId());
            productsArray.put(Utils.getJsonFrom(json, "count", productsMap.get(product)));
        });

        JSONArray licensesIds = new JSONArray();
        invoice.getLicenses().stream().forEach((licenseShortData) -> {
            licensesIds.put(Utils.getJsonFrom(null, "license_id", licenseShortData.licenseId));
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
        BigDecimal additoinalDiscount = Utils.getBigDecimalFor(invoice.getAdditionalDiscountRate());
        System.out.println("addintinal disc: " + additoinalDiscount);
        
//        return DBUtils.saveObjectToDBWith("invoice_insert_update_from_afb", invoice, additoinalDiscount);
        return DBUtils.saveInvoice(invoice);
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
    
    public ObjectProperty<MonthCounterItem> monthsProperty(){
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
    @JsonIgnore
    public LocalDate getCreatedDateObj(){
        return DateConverter.getInstance().parseDate(createdDate.get());
    }
    
    
    @JsonIgnore
    public String getInvoiceNumber(){
        return invoiceNumber.get();
    }
    

    public SeperateSaving getSetsForSeparateSaving(){
        return new SeperateSaving(licenses);
    }
    
    @JsonIgnore // we need "sets_for_separate_saving" for DB json before licenses, so use above method for licenses.
    public ObservableList<LicenseShortData> getLicenses(){
        return licenses;
    }
    
//    public ObservableList<Integer> getLicenses(){
//        ObservableList<Integer> licenseIds = FXCollections.observableArrayList();
//        licenses.stream().forEach((shortData) -> {
//            licenseIds.add(shortData.getLicense_id());
//        });
//        return licenseIds;
//    }
    
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
        return (endDateObj.isNull().get()) ? "" : endDateObj.get().toString();
    }
    
    @JsonIgnore
    public String getRevokedDate(){
        return (revokedDateObj.isNull().get()) ? "" : revokedDateObj.get().toString();
    }
    
    public String getAdditionalDiscountRate(){
        return additionalDiscountRate.get();
    }
    
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    public String getMoneyToPay(){
        return moneyToPay.get();
    }
    
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    public String getMoneyPaid(){
        return moneyPaid.get();
    }

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    public String getVat(){
        return vat.get();
    }
    
    @JsonIgnore
    public String getReissuingDescrip(){
        return (reissuingObj.isNull().get()) ? "" : reissuingObj.get().getDescrip();
    }
    
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    public int getReissuing(){
        return (reissuingObj.isNull().get()) ? -1 : reissuingObj.get().getInvoiceReissuingId();
    }
    
    @JsonIgnore
    public String getStatusDescrip(){
        return (statusObj.isNull().get()) ? "" : statusObj.get().getDescrip();
    }
    
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    public int getStatus(){
        return (statusObj.isNull().get()) ? -1 : statusObj.get().getInvoiceStatusId();
    }
    
    @JsonIgnore
    public String getMonths(){
        return "" + months.get().getMonthCount();
    }
    
    
    // Setters:
    private void setCreatedDate(String date){
        LocalDate localDate = DateConverter.getInstance().parseDate(date);
        String userFriendlyDateVisual = DateConverter.getInstance().getDayMonthnameYearBySpace(localDate);
        createdDate.set(userFriendlyDateVisual);
    }
    
    @JsonProperty
    public void setInvoiceNumber(String invoiceNumber){
        this.invoiceNumber.set(invoiceNumber);
    }
    
    @JsonProperty
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

    @JsonProperty
    public void setFirstName(String firstName){
        clientObj.get().setFirstName(firstName);
    }
    
    @JsonProperty
    public void setLastName(String lastName){
        clientObj.get().setLastName(lastName);
    }
    
    @JsonProperty
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
    
    @JsonProperty
    public void setRevokedDate(String date) {
        revokedDateObj.set(DateConverter.getInstance().parseDate(date));
        revokedDateDescrip.set(DateConverter.getInstance().getDayMonthnameYearBySpace(revokedDateObj.get()));
        isRevoked.set(revokedDateObj.get() != null);
    }
    
    @JsonProperty
    public void setAdditionalDiscountRate(String additDisc){
        additionalDiscountRate.set(additDisc);
    }
    
    @JsonProperty // Setter must be available from DB but getter not available to DB
    public void setMoneyToPay(String money){ // the String value give from DB
        moneyToPay.set(money);
    }
    
    @JsonProperty
    public void setMoneyPaid(String money){ // the String value give from DB
        moneyPaid.set(money);
    }
    
    @JsonProperty
    public void setVat(String vat){
        this.vat.set(vat);
    }
    
    @JsonProperty
    public void setReissuingDescrip(String descrip){
        reissuingObj.get().setDescrip(descrip);
    }
    
    public void setReissuing(int reissuingId){
        reissuingObj.get().setInvoiceReissuingId(reissuingId);
    }
    
    @JsonProperty
    public void setStatusDescrip(String status){
        this.statusObj.get().setDescrip(status);
    }
    
    public void setStatus(int status){
        this.statusObj.get().setInvoiceStatusId(status);
    }
    
    @JsonProperty
    public void setMonths(String months){
        this.months.get().setMonth(months);
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
        licenses.addAll(invoice.getLicenses());
        setBeginDate(invoice.getBeginDate());
        setEndDate(invoice.getEndDate());
        setRevokedDate(invoice.getRevokedDate());
        setAdditionalDiscountRate(invoice.getAdditionalDiscountRate());
        setMoneyToPay(invoice.getMoneyToPay());
        setVat(invoice.getVat());
        setMoneyPaid(invoice.getMoneyPaid());
        reissuingObj.get().copyFrom(invoice.reissuingProperty().get());
        statusObj.get().copyFrom(invoice.statusProperty().get());
        setMonths(invoice.getMonths());
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
                Utils.compareListsByElemOrder(licenses, otherInvoice.getLicenses())    &&
                Utils.dateEquals(beginDateProperty().get(), otherInvoice.beginDateProperty().get()) &&
                Utils.dateEquals(endDateProperty().get(), otherInvoice.endDateProperty().get()) &&
                Utils.dateEquals(revokedDateProperty().get(), otherInvoice.revokedDateProperty().get()) &&
//                getBeginDate().equals(otherInvoice.getBeginDate())          &&
//                getEndDate().equals(otherInvoice.getEndDate())      &&
//                getRevokedDate().equals(otherInvoice.getRevokedDate()) &&
                getAdditionalDiscountRate().equals(otherInvoice.getAdditionalDiscountRate()) &&
                getMoneyToPay().equals(otherInvoice.getMoneyToPay()) &&
                getVat().equals(otherInvoice.getVat()) &&
                getMoneyPaid().equals(otherInvoice.getMoneyPaid()) &&
                reissuingObj.get().compares(otherInvoice.reissuingProperty().get()) &&
                statusObj.get().compares(otherInvoice.statusProperty().get()) &&
                getMonths().equals(otherInvoice.getMonths());
    }

    
    @SuppressWarnings("EqualsAndHashcode")
    public static class LicenseShortData {
        
        private int recId;
        private int licenseId;
        private final StringProperty licenseNumber = new SimpleStringProperty("");
        
        public StringProperty licenseNumberProperty(){
            return licenseNumber;
        }
        
        @JsonIgnore
        public int getRecId(){
            return recId;
        }
        
        public int getLicense_id(){
            return licenseId;
        }
        
        @JsonIgnore
        public int getLicenseNumber(){
            return Integer.parseInt(licenseNumber.get());
        }
        
        
        @JsonProperty
        public void setRecId(int recId){
            this.recId = recId;
        }
        
        public void setLicenseId(int licenseId){
            this.licenseId = licenseId;
        }
        
        @JsonProperty
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
    
    public static class SeperateSaving {
        
        private ObservableList<LicenseShortData> licenses;
        
        public SeperateSaving(ObservableList<LicenseShortData> licenses){
            this.licenses = licenses;
        }
        
        public ObservableList<LicenseShortData> getLicenses(){
            return licenses;
        }
    }
}
