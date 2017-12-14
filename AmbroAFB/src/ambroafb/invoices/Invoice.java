/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.invoices;

import ambro.AView;
import ambroafb.clients.Client;
import ambroafb.currencies.Currency;
import ambroafb.general.DateConverter;
import ambroafb.general.NumberConverter;
import ambroafb.general.Utils;
import ambroafb.general.countcombobox.CountComboBoxItem;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.general.interfaces.TableColumnFeatures;
import ambroafb.general.monthcountercombobox.MonthCounterItem;
import ambroafb.invoices.helper.InvoiceFinance;
import ambroafb.invoices.helper.InvoiceReissuing;
import ambroafb.invoices.helper.InvoiceStatus;
import ambroafb.invoices.helper.InvoiceStatusClarify;
import ambroafb.licenses.helper.LicenseFinance;
import ambroafb.products.Product;
import ambroafb.products.helpers.ProductDiscount;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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


/**
 *
 * @author mambroladze
 */
public class Invoice extends EditorPanelable { 

    @AView.Column(title = "%created_date", width = TableColumnFeatures.Width.DATE, styleClass = TableColumnFeatures.Style.TEXT_RIGHT)
    private final StringProperty createdDate;
    
    @AView.Column(title = "%invoice_N", width = "100")
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private final SimpleStringProperty invoiceNumber;
    
    private final ObjectProperty<InvoiceStatusClarify> clarifyObj;
    
    @AView.Column(title = "%license", width = TableColumnFeatures.Width.LICENSE)
    private final StringProperty licensesDescrip;
    private final ObservableList<LicenseShortData> licenses;
    
    @AView.Column(title = "%client", width = TableColumnFeatures.Width.CLIENT_MAIL)
    private final StringExpression clientDescrip;
    private final ObjectProperty<Client> clientObj;
    
    @AView.Column(title = "%begin_date", width = TableColumnFeatures.Width.DATE, styleClass = TableColumnFeatures.Style.TEXT_RIGHT)
    private final StringProperty beginDateDescrip;
    private final ObjectProperty<LocalDate> beginDateObj;

    @AView.Column(title = "%end_date", width = TableColumnFeatures.Width.DATE, styleClass = TableColumnFeatures.Style.TEXT_RIGHT)
    private final StringProperty endDateDescrip;
    private final ObjectProperty<LocalDate> endDateObj;
    
    @AView.Column(title = "%revoked_date", width = TableColumnFeatures.Width.DATE, styleClass = TableColumnFeatures.Style.TEXT_RIGHT)
    private final StringProperty revokedDateDescrip;
    private final ObjectProperty<LocalDate> revokedDateObj;
    private final BooleanProperty isRevoked;
    
//    @AView.Column(title = "%extra_discount", width = "100", styleClass = TableColumnFeatures.Style.TEXT_RIGHT)
    private final StringProperty additionalDiscRate;
    
    @AView.Column(title = "%to_pay", width = TableColumnFeatures.Width.MONEY, styleClass = TableColumnFeatures.Style.TEXT_RIGHT )
    private final StringProperty moneyToPay;
    
    
    @AView.Column(title = "%vat", width = TableColumnFeatures.Width.MONEY, styleClass = TableColumnFeatures.Style.TEXT_RIGHT)
    private final StringProperty vat;
    
    @AView.Column(title = "%paid", width = TableColumnFeatures.Width.MONEY, styleClass = TableColumnFeatures.Style.TEXT_RIGHT)
    private final StringProperty moneyPaid;
    
    
    @AView.Column(title = "%invoice_reissuings_min", width = "100")
    private final ObjectProperty<InvoiceReissuing> reissuingObj;
    
    @AView.Column(title = "%invoice_status", width = "100")
    private final ObjectProperty<InvoiceStatus> statusObj;
    
    private final ObjectProperty<MonthCounterItem> months;
    private final BooleanProperty isLogined, isPaid;
    
    private List<LicenseFinance> licenseFinanceses = new ArrayList<>();
    private InvoiceFinance invoiceFinace = new InvoiceFinance();
    
    private final Map<CountComboBoxItem, Integer> productsCounter = new HashMap<>();
    private static int clarifyStatus;
    private BooleanProperty isAllowToModify;
    
    private final float additionalDiscRateDefaultValue = 0;
    
    public Invoice(){
        invoiceNumber = new SimpleStringProperty("");
        createdDate = new SimpleStringProperty("");
        licensesDescrip = new SimpleStringProperty("");
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
        additionalDiscRate = new SimpleStringProperty("");
        moneyToPay = new SimpleStringProperty("");
        moneyPaid = new SimpleStringProperty("");
        vat = new SimpleStringProperty("");
        reissuingObj = new SimpleObjectProperty<>(new InvoiceReissuing());
        clarifyObj = new SimpleObjectProperty<>(new InvoiceStatusClarify());
        statusObj = new SimpleObjectProperty<>(new InvoiceStatus());
        months = new SimpleObjectProperty<>(new MonthCounterItem());
        isLogined = new SimpleBooleanProperty(false);
        isPaid = new SimpleBooleanProperty(false);
        isAllowToModify = new SimpleBooleanProperty(true);
        
        beginDateObj.addListener((ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) -> {
            resetEndDate();
        });
        months.addListener((ObservableValue<? extends MonthCounterItem> observable, MonthCounterItem oldValue, MonthCounterItem newValue) -> {
            resetEndDate();
        });
        
        licenses.addListener((ListChangeListener.Change<? extends LicenseShortData> c) -> {
            resetLicenses();
//            rebindLicenses();
        });
        
        moneyPaid.addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            isAllowToModify.set(newValue != null && !newValue.isEmpty() && Utils.getDoubleValueFor(newValue) <= 0 && isLogined.not().get());
        });
    }
    
//    private void rebindLicenses(){
//        licensesDescrip.unbind();
//        licensesDescrip.bind(
//                licenses.stream()
//                                    .map(LicenseShortData::licenseNumberProperty)
//                                    .reduce(new SimpleStringProperty(""), (StringProperty total, StringProperty unary) -> {
//                                        StringProperty temp = new SimpleStringProperty("");
//                                        StringExpression exp = total.concat(Bindings.createStringBinding(() -> {
//                                                                    return (total.get().isEmpty()) ? "" : ", ";
//                                                                }, total.isEmpty())).concat(unary);
//                                        temp.set(exp.get());
//                                        return temp;
//                                    })
//                            );
//    }
    
    private void resetLicenses(){
        String licensesNumersDescrip = "";
        if (!licenses.isEmpty()){
            licensesNumersDescrip = licenses.stream().map(LicenseShortData::licenseNumberProperty).reduce(new SimpleStringProperty(""), (StringProperty total, StringProperty unary) -> {
                String totalValue = (total.isEmpty().get()) ? unary.get() : total.get() + ", " + unary.get();
                total.set(totalValue);
                return total;
            }).get();
        }
        licensesDescrip.set(licensesNumersDescrip);
    }
    
    private void resetEndDate(){
        if (beginDateObj.isNull().get() || months.isNull().get()) {
            endDateObj.set(null);
        } else {
            long monthValue = months.get().getMonthCount();
            long dayValue = months.get().getDayCount();
            endDateObj.set(beginDateObj.get().plusMonths(monthValue).plusDays(dayValue));
        }
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
        return additionalDiscRate;
    }
    
    public StringProperty licensesNumbersProperty(){
        return licensesDescrip;
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
    
    public ObjectProperty<InvoiceStatusClarify> clarifyProperty(){
        return clarifyObj;
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
    
    
    
    // Getters:
    @JsonIgnore
    public LocalDate getCreatedDateObj(){
        return DateConverter.getInstance().parseDate(createdDate.get());
    }
    
    @JsonIgnore
    public InvoiceStatus getInvoiceStatus(){
        return statusObj.get();
    }
    
    @JsonIgnore
    public String getInvoiceNumber(){
        return invoiceNumber.get();
    }

    public ObservableList<LicenseShortData> getLicenses(){
        return licenses;
    }
    
    @JsonIgnore
    public Map<CountComboBoxItem, Integer> getProductsWithCounts(){
        return productsCounter;
    }
    
    @JsonIgnore
    public List<LicenseFinance> getLicenseFinances(){
        return licenseFinanceses;
    }
    
    @JsonIgnore
    public InvoiceFinance getInvoiceFinance(){
        return invoiceFinace;
    }
    
    public Integer getClientId(){
        return (clientObj.isNull().get()) ? null : clientObj.get().getRecId();
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
    
    public Float getAdditionalDiscountRate(){
        return NumberConverter.stringToFloat(additionalDiscRate.get(), 2, additionalDiscRateDefaultValue);
    }
    
    @JsonIgnore
    public String getMoneyToPay(){
        return moneyToPay.get();
    }
    
    @JsonIgnore
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
    
    public int getReissuing(){
        return (reissuingObj.isNull().get()) ? -1 : reissuingObj.get().getInvoiceReissuingId();
    }
    
    @JsonIgnore
    public int getStatusClarify(){
        return (clarifyObj.isNull().get()) ? -1 : clarifyObj.get().getInvoiceStatusClarifyId();
    }
    
    @JsonIgnore
    public String getStatusClarifyDescrip(){
        return (clarifyObj.isNull().get()) ? "" : clarifyObj.get().getDescrip();
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
        return (months.isNull().get()) ? "" : "" + months.get().getMonthCount();
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
    
    @JsonProperty
    public void setLicenseFinances(List<LicenseFinance> licensesFinanceses){
        this.licenseFinanceses = licensesFinanceses;
        productsCounter.clear();
        licensesFinanceses.forEach((finance) -> makeAndSaveProductFrom(finance));
    }
    
    private void makeAndSaveProductFrom(LicenseFinance finance){
        Product p = new Product();
        p.setRecId(finance.productId);
        p.setAbbreviation(finance.articul.substring(0, Product.ABREVIATION_LENGTH));
        p.setFormer(Utils.getIntValueFor(finance.articul.substring(Product.FORMER_LENGTH)));
        p.setDescrip(finance.productDescrip);
        p.setPrice(Float.parseFloat(finance.price));
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
    
    @JsonProperty
    public void setInvoiceFinances(InvoiceFinance invoiceFinances){
        this.invoiceFinace = invoiceFinances;
        if (invoiceFinances != null){
            additionalDiscRate.set(invoiceFinances.additionalDiscountRate);
        }
    }
    
    public void setClientId(Integer recId){
        clientObj.get().setRecId(recId);
    }

    @JsonProperty
    public void setFirstName(String firstName){
        if (clientObj.get() != null){
            clientObj.get().setFirstName(firstName);
        }
    }
    
    @JsonProperty
    public void setLastName(String lastName){
        if (clientObj.get() != null){
            clientObj.get().setLastName(lastName);
        }
    }
    
    @JsonProperty
    public void setEmail(String email){
        if (clientObj.get() != null){
            clientObj.get().setEmail(email);
        }
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
    public void setAdditionalDiscountRate(Float additDisc){
        additionalDiscRate.set(NumberConverter.convertNumberToStringBySpecificFraction(additDisc, 2));
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
    
    @JsonProperty
    public void setStatusClarify(int clarify){
        clarifyObj.get().setInvoiceStatusClarifyId(clarify);
    }
    
    @JsonProperty
    public void setStatusClarifyDescrip(String descrip){
        clarifyObj.get().setDescrip(descrip);
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
        //  ?? კლიენტის ინფოს მეთოდების ცალ-ცალკე კოპირებას clientObj დავსეტოთ other_ის clientObj-ის clone-ით, ხომ არ აჯობებს... თუმცა ბევრი ისეთი მნიშვნელობის copy მოგვიწევს რომლებიც ცარიელები იქნებიან..
        setClientId(invoice.getClientId());
        setFirstName(invoice.getFirstName());
        setLastName(invoice.getLastName());
        setEmail(invoice.getEmail());
        
        clientObj.set((invoice.clientProperty().isNull().get()) ? null : invoice.clientProperty().get().cloneWithID());
        
        setInvoiceNumber(invoice.getInvoiceNumber());
        
        licenses.clear();
        invoice.getLicenses().stream().forEach((licenseShortData) -> {
            LicenseShortData license = new LicenseShortData();
            license.copyFrom(licenseShortData);
            licenses.add(license);
        });
        
        licenseFinanceses.clear();
        invoice.getLicenseFinances().stream().forEach((otherFinanceOfLicense) -> {
            LicenseFinance finance = new LicenseFinance();
            finance.copyFrom(otherFinanceOfLicense);
            licenseFinanceses.add(finance);
        });
        
        invoiceFinace.copyFrom(invoice.getInvoiceFinance());
        
        setBeginDate(invoice.getBeginDate());
        setEndDate(invoice.getEndDate());
        setRevokedDate(invoice.getRevokedDate());
        setAdditionalDiscountRate(invoice.getAdditionalDiscountRate());
        setMoneyToPay(invoice.getMoneyToPay());
        setVat(invoice.getVat());
        setMoneyPaid(invoice.getMoneyPaid());
        reissuingObj.get().copyFrom(invoice.reissuingProperty().get());
        clarifyObj.get().copyFrom(invoice.clarifyProperty().get());
        statusObj.get().copyFrom(invoice.getInvoiceStatus());
        setMonths(invoice.getMonths());
        
        productsCounter.clear();
        invoice.getProductsWithCounts().keySet().stream().forEach((otherInvoiceProduct) -> {
            Product thisInvoiceProduct = ((Product)otherInvoiceProduct).cloneWithID();
            int count = invoice.getProductsWithCounts().get(otherInvoiceProduct);
            productsCounter.put(thisInvoiceProduct, count);
        });
        
    }

    @Override
    public String toStringForSearch() {
        String searchString = "";
        if (clientObj.isNotNull().get() && licensesDescrip.isNotNull().get()){
            searchString = clientObj.get().getShortDescrip(" ").get() + " " + licensesDescrip.get();
        }
        return searchString;
    }

    @Override
    public boolean compares(EditorPanelable backup) {
        Invoice otherInvoice = (Invoice) backup;
        
        //  ?? კლიენტის ინფოს მეთოდების შედარებას clientObj შევადაროთ ხომ არ აჯობებს, თუმცა ბევრი ისეთი მნიშვნელობა ექნება რომელსაც ინვოისი საერთოდ არ ცვლის.
        
        System.out.println("getClientId().equals(otherInvoice.getClientId()): " + (getClientId().equals(otherInvoice.getClientId())));
        System.out.println("getFirstName().equals(otherInvoice.getFirstName()): " + (getFirstName().equals(otherInvoice.getFirstName())));
        System.out.println("getLastName().equals(otherInvoice.getLastName()): " + (getLastName().equals(otherInvoice.getLastName())));
        System.out.println("getEmail().equals(otherInvoice.getEmail()): " + (getEmail().equals(otherInvoice.getEmail())));
        System.out.println("getInvoiceNumber().equals(otherInvoice.getInvoiceNumber()): " + (getInvoiceNumber().equals(otherInvoice.getInvoiceNumber())));
        System.out.println("Utils.dateEquals(beginDateProperty().get(), otherInvoice.beginDateProperty().get()): " + (Utils.objectEquals(beginDateProperty().get(), otherInvoice.beginDateProperty().get())));
        System.out.println("Utils.dateEquals(endDateProperty().get(), otherInvoice.endDateProperty().get()): " + (Utils.objectEquals(endDateProperty().get(), otherInvoice.endDateProperty().get())));
        System.out.println("Utils.dateEquals(revokedDateProperty().get(), otherInvoice.revokedDateProperty().get()): " + (Utils.objectEquals(revokedDateProperty().get(), otherInvoice.revokedDateProperty().get())));
        System.out.println("getAdditionalDiscountRate().equals(otherInvoice.getAdditionalDiscountRate()): " + (getAdditionalDiscountRate().equals(otherInvoice.getAdditionalDiscountRate())));
        System.out.println("reissuingObj.get().compares(otherInvoice.reissuingProperty().get()): " + (reissuingObj.get().compares(otherInvoice.reissuingProperty().get())));
        System.out.println("statusObj.get().compares(otherInvoice.getInvoiceStatus()): " + (statusObj.get().compares(otherInvoice.getInvoiceStatus())));
        System.out.println("getMonths().equals(otherInvoice.getMonths()): " + (getMonths().equals(otherInvoice.getMonths())));
        System.out.println("Utils.compareListsByElemOrder(licenses, otherInvoice.getLicenses()): " + (Utils.compareListsByElemOrder(licenses, otherInvoice.getLicenses())));
        System.out.println("compareProductsCounter(productsCounter, otherInvoice.getProductsWithCounts()): " + (Utils.compareProductsCounter(productsCounter, otherInvoice.getProductsWithCounts())));
        
        
        return  Utils.objectEquals(clientObj.get(), otherInvoice.clientProperty().get()) &&

                getInvoiceNumber().equals(otherInvoice.getInvoiceNumber())  &&
                Utils.compareListsByElemOrder(licenses, otherInvoice.getLicenses())    &&
                Utils.objectEquals(beginDateProperty().get(), otherInvoice.beginDateProperty().get()) &&
                Utils.objectEquals(endDateProperty().get(), otherInvoice.endDateProperty().get()) &&
                Utils.objectEquals(revokedDateProperty().get(), otherInvoice.revokedDateProperty().get()) &&
                getAdditionalDiscountRate().equals(otherInvoice.getAdditionalDiscountRate()) &&
                
                getMoneyToPay().equals(otherInvoice.getMoneyToPay()) &&
                getVat().equals(otherInvoice.getVat()) &&
                getMoneyPaid().equals(otherInvoice.getMoneyPaid()) &&
                
                reissuingObj.get().compares(otherInvoice.reissuingProperty().get()) &&
                clarifyObj.get().compares(otherInvoice.clarifyProperty().get()) &&
                statusObj.get().compares(otherInvoice.getInvoiceStatus()) &&
                getMonths().equals(otherInvoice.getMonths()) &&
                Utils.compareProductsCounter(productsCounter, otherInvoice.getProductsWithCounts());
    }
    
    @Override
    public BooleanProperty isAllowToModify(){
        return isAllowToModify;
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
            return licenseNumber.get().isEmpty() ? 0 : Integer.parseInt(licenseNumber.get());
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
            return "license_id: " + licenseId + " license_number: " + licenseNumber.get();
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
    
//    private static Color getClarifyColor(){
//        switch(clarifyStatus){
//            case 1:
//                return Color.GREEN;
//            case 2:
//                return Color.ORANGE;
//            case 3:
//                return Color.RED;
//            case 4:
//                return Color.LIGHTGRAY;
//            default:
//                return Color.BLUE;
//        }
//    }
    
//    public static class TextColorCellFactory implements Callback<TableColumn<Invoice, String>, TableCell<Invoice, String>> {
//
//        @Override
//        public TableCell<Invoice, String> call(TableColumnFeatures<Invoice, String> param) {
//            TableCell<Invoice, String> cell = new TableCell<Invoice, String>() {
//
//                @Override
//                public void updateItem(String date, boolean empty) {
//                    if (empty) {
//                        setGraphic(null);
//                    } else {
//                        Label label = new Label(date);
//                        label.setTextFill(getClarifyColor());
//                        setGraphic(label); 
//                    }
//                }
//            };
//            return cell;
//        }
//    }
}
