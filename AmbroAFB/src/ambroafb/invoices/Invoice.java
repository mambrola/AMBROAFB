/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.invoices;

import ambro.AView;
import ambroafb.clients.Client;
import ambroafb.currencies.Currency;
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
import ambroafb.invoices.helper.InvoiceFinaces;
import ambroafb.licenses.helper.LicenseFinaces;
import ambroafb.products.Product;
import ambroafb.products.helpers.ProductDiscount;
import authclient.db.ConditionBuilder;
import authclient.db.DBClient;
import authclient.db.WhereBuilder;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringExpression;
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
import org.json.JSONObject;


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
//    @JsonIgnore
//    public ObservableList<License> licensesOnProducts; // ????
    
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
    private final IntegerProperty statusClarify;
    
    private final ObjectProperty<MonthCounterItem> months;
    private final BooleanProperty isLogined, isPaid;
    
    @JsonIgnore
    private static final String DB_REISSUINGS_TABLE = "invoice_reissuing_descrips";
    @JsonIgnore
    private static final String DB_INVOICES_VIEW = "invoices_whole";
    
    @JsonIgnore
    public ArrayList<LicenseFinaces> licenseFinanceses = new ArrayList<>();
    @JsonIgnore
    public ArrayList<InvoiceFinaces> invoiceFinaceses = new ArrayList<>();
    
    @JsonIgnore
    private final Map<Product, Integer> productsCounter = new HashMap<>();
    
    public Invoice(){
        invoiceNumber = new SimpleStringProperty("");
        createdDate = new SimpleStringProperty("");
        licensesDescript = new SimpleStringProperty("");
        licenses = FXCollections.observableArrayList();
        clientObj = new SimpleObjectProperty<>(new Client());
        clientDescrip = clientObj.get().getShortDescrip(", ");
        beginDateDescrip = new SimpleStringProperty("");
        beginDateObj = new SimpleObjectProperty<>();
        endDateDescrip = new SimpleStringProperty("");
        endDateObj = new SimpleObjectProperty();
        revokedDateDescrip = new SimpleStringProperty("");
        revokedDateObj = new SimpleObjectProperty<>();
        isRevoked = new SimpleBooleanProperty(false);
        additionalDiscountRate = new SimpleStringProperty("");
        moneyToPay = new SimpleStringProperty("");
        moneyPaid = new SimpleStringProperty("");
        vat = new SimpleStringProperty("");
        reissuingObj = new SimpleObjectProperty<>(new InvoiceReissuing());
        statusObj = new SimpleObjectProperty<>(new InvoiceStatus());
        statusClarify = new SimpleIntegerProperty();
        months = new SimpleObjectProperty<>(new MonthCounterItem(""));
        isLogined = new SimpleBooleanProperty(false);
        isPaid = new SimpleBooleanProperty(false);
        
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
        
        licenses.addListener((ListChangeListener.Change<? extends LicenseShortData> c) -> {
            rebindLicenses();
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
        DBUtils.callInvoiceExistedLicenses(invoiceId);
        ArrayList<LicenseFinaces> licenseFinances = DBUtils.getLicensesFinaces();
        ArrayList<InvoiceFinaces> invoicesFinaceses = DBUtils.getInvoicesFinaces();
        Invoice invoiceFromDB = DBUtils.getObjectFromDB(Invoice.class, DB_INVOICES_VIEW, params);
        
        invoiceFromDB.setLicenseFinances(licenseFinances);
        invoiceFromDB.setInvoiceFinances(invoicesFinaceses);
        
        return invoiceFromDB;
    }
    
    
    public static Invoice saveOneToDB(Invoice invoice) {
        if (invoice == null) return null;
//        Map<Product, Integer> productsMap = invoice.getProductsWithCounts();
//        JSONArray productsArray = new JSONArray();
//        productsMap.keySet().stream().forEach((product) -> {
//            JSONObject json = Utils.getJsonFrom(null, "product_id", product.getRecId());
//            productsArray.put(Utils.getJsonFrom(json, "count", productsMap.get(product)));
//        });
//
//        JSONArray licensesIds = new JSONArray();
//        invoice.getLicenses().stream().forEach((licenseShortData) -> {
//            licensesIds.put(Utils.getJsonFrom(null, "license_id", licenseShortData.licenseId));
//        });
//        DBUtils.callInvoiceSuitedLicenses(null, invoice.getClientId(), invoice.beginDateProperty().get(), invoice.endDateProperty().get(), productsArray, invoice.getAdditionalDiscountRate(), licensesIds);
//        ArrayList<PartOfLicense> licenses = DBUtils.getLicenses();
//        List<LicenseShortData> wholeLicenses = licenses.stream().map((license) -> {
//                                                                        LicenseShortData shortData = new LicenseShortData();
//                                                                        shortData.licenseId = license.invoiceLicenseId;
//                                                                        shortData.setLicenseNumber(license.licenseNumber);
//                                                                        return shortData;
//                                                                }).collect(Collectors.toList());
//        System.out.println("invoice whole license: " + wholeLicenses);
        
//        invoice.setLicenses(wholeLicenses);
//        BigDecimal additoinalDiscount = Utils.getBigDecimalFor(invoice.getAdditionalDiscountRate());
//        System.out.println("addintinal disc: " + additoinalDiscount);
        
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
    
    public StringProperty licensesNumbersProperty(){
        return licensesDescript;
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
    
    public BooleanProperty isLoginedProperty(){
        return isLogined;
    }
    
    public BooleanProperty isPaidProperty(){
        return isPaid;
    }
    
    public IntegerProperty statusClarifyProperty(){
        return statusClarify;
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

    @JsonIgnore
    public ObservableList<LicenseShortData> getLicenses(){
        return licenses;
    }
    
    @JsonIgnore
    public Map<Product, Integer> getProductsWithCounts(){
        return productsCounter;
    }
    
    @JsonIgnore
    public ArrayList<LicenseFinaces> getLicenseFinances(){
        return licenseFinanceses;
    }
    
    @JsonIgnore
    public ArrayList<InvoiceFinaces> getInvoiceFinances(){
        return invoiceFinaceses;
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
        return (endDateObj.isNull().get()) ? "" : endDateObj.get().toString();
    }
    
    @JsonIgnore
    public String getRevokedDate(){
        return (revokedDateObj.isNull().get()) ? "" : revokedDateObj.get().toString();
    }
    
    public String getAdditionalDiscountRate(){
        return (Utils.getDoubleValueFor(additionalDiscountRate.get()) <= 0) ? "" : additionalDiscountRate.get();
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
    
    @JsonIgnore
    public int getStatus(){
        return (statusObj.isNull().get()) ? -1 : statusObj.get().getInvoiceStatusId();
    }
    
    @JsonIgnore
    public String getMonths(){
        return "" + months.get().getMonthCount();
    }
    
    @JsonIgnore
    public int getStatusClarify(){
        return statusClarify.get();
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
    }
    
    public void setLicenseFinances(ArrayList<LicenseFinaces> licensesFinanceses){
        this.licenseFinanceses = licensesFinanceses;
        productsCounter.clear();
        licensesFinanceses.forEach((finance) -> makeAndSaveProductFrom(finance));
    }
    
    private void makeAndSaveProductFrom(LicenseFinaces finance){
        Product p = new Product();
        p.setRecId(finance.productId);
        p.setAbbreviation(finance.articul.substring(0, Product.ABREVIATION_LENGTH));
        p.setFormer(Utils.getIntValueFor(finance.articul.substring(Product.FORMER_LENGTH)));
        p.setDescrip(finance.productDescrip);
        p.setPrice(Utils.getIntValueFor(finance.price));
        Currency currency = new Currency();
        currency.setIso(finance.isoOrigin);
        currency.setSymbol(finance.symbolOrigin);
        p.currencyProperty().set(currency);
        ProductDiscount discount = new ProductDiscount();
        discount.setDays(finance.months);
        discount.setDiscountRate(Utils.getDoubleValueFor(finance.discountRate));
        List<ProductDiscount> discounts = new ArrayList<>();
        discounts.add(discount);
        p.setDiscounts(discounts);
        
        productsCounter.put(p, finance.count);
    }
    
    public void setInvoiceFinances(ArrayList<InvoiceFinaces> invoiceFinances){
        this.invoiceFinaceses = invoiceFinances;
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
    
    @JsonProperty
    public void setIsLogined(int logined){
        isLogined.set(logined == 1);
    }
    
    public void setIsPaid(int paid){
        isPaid.set(paid == 1);
    }
    
    public void setStatusClarify(int clarify){
        statusClarify.set(clarify);
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
        
        licenses.clear();
        invoice.getLicenses().stream().forEach((licenseShortData) -> {
            LicenseShortData license = new LicenseShortData();
            license.copyFrom(licenseShortData);
            licenses.add(license);
        });
        
        licenseFinanceses.clear();
        invoice.getLicenseFinances().stream().forEach((otherFinanceOfLicense) -> {
            LicenseFinaces finance = new LicenseFinaces();
            finance.copyFrom(otherFinanceOfLicense);
            licenseFinanceses.add(finance);
        });
        
        invoiceFinaceses.clear();
        invoice.getInvoiceFinances().stream().forEach((otherFinanceOfInvoice) -> {
            InvoiceFinaces finance = new InvoiceFinaces();
            finance.copyFrom(otherFinanceOfInvoice);
            invoiceFinaceses.add(finance);
        });
        
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
        
        productsCounter.clear();
        invoice.getProductsWithCounts().keySet().stream().forEach((otherInvoiceProduct) -> {
            int count = invoice.getProductsWithCounts().get(otherInvoiceProduct);
            productsCounter.put(otherInvoiceProduct, count);
        });
        
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
        
//        System.out.println("Utils.compareListsByElemOrder(licenses, otherInvoice.getLicenses()): " + (Utils.compareListsByElemOrder(licenses, otherInvoice.getLicenses())));
//        System.out.println("getMoneyToPay().equals(otherInvoice.getMoneyToPay()): " + (getMoneyToPay().equals(otherInvoice.getMoneyToPay())));
//        System.out.println("getVat().equals(otherInvoice.getVat()): " + (getVat().equals(otherInvoice.getVat())));
//        System.out.println("getMoneyPaid().equals(otherInvoice.getMoneyPaid()): " + (getMoneyPaid().equals(otherInvoice.getMoneyPaid())));
//        System.out.println("compareProductsCounter(productsCounter, otherInvoice.getProductsWithCounts()): " + (compareProductsCounter(productsCounter, otherInvoice.getProductsWithCounts())));
        
        return  getFirstName().equals(otherInvoice.getFirstName())  &&
                getLastName().equals(otherInvoice.getLastName())    &&
                getEmail().equals(otherInvoice.getEmail())          &&
                getInvoiceNumber().equals(otherInvoice.getInvoiceNumber())  &&
                Utils.compareListsByElemOrder(licenses, otherInvoice.getLicenses())    &&
                Utils.dateEquals(beginDateProperty().get(), otherInvoice.beginDateProperty().get()) &&
                Utils.dateEquals(endDateProperty().get(), otherInvoice.endDateProperty().get()) &&
                Utils.dateEquals(revokedDateProperty().get(), otherInvoice.revokedDateProperty().get()) &&
                getAdditionalDiscountRate().equals(otherInvoice.getAdditionalDiscountRate()) &&
                
                getMoneyToPay().equals(otherInvoice.getMoneyToPay()) &&
                getVat().equals(otherInvoice.getVat()) &&
                getMoneyPaid().equals(otherInvoice.getMoneyPaid()) &&
                
                reissuingObj.get().compares(otherInvoice.reissuingProperty().get()) &&
                statusObj.get().compares(otherInvoice.statusProperty().get()) &&
                getMonths().equals(otherInvoice.getMonths()) &&
                compareProductsCounter(productsCounter, otherInvoice.getProductsWithCounts());
    }
    
    private boolean compareProductsCounter(Map<Product, Integer> first, Map<Product, Integer> second){
        boolean result = true;
        if (first.keySet().size() != second.keySet().size()) {
            result = false;
        }
        else {
            for(Product p : first.keySet()){
                if (!second.containsKey(p) || first.get(p).intValue() != second.get(p).intValue()){
                    result = false;
                    break;
                }
            }
        }
        return result;
    }

    
    @SuppressWarnings("EqualsAndHashcode")
    public static class LicenseShortData {
        
        private int recId;
        private int licenseId;
        private final StringProperty licenseNumber = new SimpleStringProperty("");
        private int invoiceLicenseId;
        
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
        
        @JsonInclude(JsonInclude.Include.NON_DEFAULT)
        public int getRec_id(){
            return invoiceLicenseId;
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
        
        @JsonProperty
        public void setInvoiceLicense(int invoiceLicenseId){
            this.invoiceLicenseId = invoiceLicenseId;
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
        
        public void copyFrom(LicenseShortData other){
            setLicenseId(other.getLicense_id());
            setLicenseNumber(other.getLicenseNumber());
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
