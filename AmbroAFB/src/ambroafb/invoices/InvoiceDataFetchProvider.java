/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.invoices;

import ambroafb.clients.Client;
import ambroafb.general.DBUtils;
import ambroafb.general.GeneralConfig;
import ambroafb.general.Utils;
import ambroafb.general.countcombobox.Basket;
import ambroafb.general.exceptions.ExceptionsFactory;
import ambroafb.general.interfaces.DataFetchProvider;
import ambroafb.general.interfaces.FilterModel;
import ambroafb.invoices.filter.InvoiceFilterModel;
import ambroafb.invoices.helper.InvoiceFinance;
import ambroafb.invoices.helper.InvoiceReissuing;
import ambroafb.invoices.helper.InvoiceStatusClarify;
import ambroafb.invoices.helper.PartOfLicense;
import ambroafb.licenses.helper.LicenseFinance;
import authclient.AuthServerException;
import authclient.db.ConditionBuilder;
import authclient.db.DBClient;
import authclient.db.WhereBuilder;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author dkobuladze
 */
public class InvoiceDataFetchProvider extends DataFetchProvider {

    private final String INVOICE_FULL_DATA_PROCEDURE = "invoice_get_existed_licenses";
    private final String DB_REISSUINGS_TABLE = "invoice_reissuing_descrips";
    private static final String DB_CLARIFIES_TABLE = "invoice_status_clarify_descrips";
    
    public InvoiceDataFetchProvider(){
        DB_VIEW_NAME = "invoices_whole";
    }
    
    @Override
    public List<Invoice> getFilteredBy(JSONObject params) throws Exception {
        List<Invoice> invoices = getObjectsListFromDBTable(Invoice.class, DB_VIEW_NAME, params);
        return invoices;
    }

    @Override
    public List<Invoice> getFilteredBy(FilterModel model) throws Exception {
        InvoiceFilterModel invoiseFilterModel = (InvoiceFilterModel) model;
        WhereBuilder whereBuilder = new ConditionBuilder().where().and("begin_date", ">=", invoiseFilterModel.getStartDateForDB(true))
                    .and("begin_date", "<=", invoiseFilterModel.getStartDateForDB(false))
                    .and("end_date", ">=", invoiseFilterModel.getEndDateForDB(true))
                    .and("end_date", "<=", invoiseFilterModel.getEndDateForDB(false));
        
        if (invoiseFilterModel.isSelectedConcreteClient()){
            whereBuilder.and("client_id", "=", invoiseFilterModel.getSelectedClientId());
        }
        if (invoiseFilterModel.hasSelectedClarifies()){
            whereBuilder = whereBuilder.andGroup();
            for(InvoiceStatusClarify invClarify : invoiseFilterModel.getCheckedClarifies()){
                whereBuilder = whereBuilder.or("status_clarify", "=", invClarify.getInvoiceStatusClarifyId());
            }
            whereBuilder = whereBuilder.closeGroup();
        }
        if (invoiseFilterModel.hasSelectedReissuings()){
            whereBuilder = whereBuilder.andGroup();
            for(InvoiceReissuing invReissuing : invoiseFilterModel.getCheckedReissuings()){
                whereBuilder = whereBuilder.or("reissuing", "=", invReissuing.getInvoiceReissuingId());
            }
            whereBuilder = whereBuilder.closeGroup();
        }
        return getFilteredBy(whereBuilder.condition().build());
    }

    @Override
    public Invoice getOneFromDB(int recId) throws Exception {
        JSONObject params = new ConditionBuilder().where().and(DB_ID, "=", recId).condition().build();
        Invoice invoiceFromDB = getObjectFromDB(Invoice.class, DB_VIEW_NAME, params);
        
        Consumer<List<LicenseFinance>> licensesFianceConsumer = (List<LicenseFinance> licenseFinances) -> {
            invoiceFromDB.setLicenseFinances(licenseFinances);
        };
        
        Consumer<InvoiceFinance> invoiceFianceConsumer = (InvoiceFinance invoiceFinanaceData) -> {
            invoiceFromDB.setInvoiceFinances(invoiceFinanaceData);
        };
        
        try {
            // licenses and client data gives from invoice_whole, so method does not need their consumers:
            callInvoiceExistedLicenses(recId, null, licensesFianceConsumer, invoiceFianceConsumer, null);
        } catch (JSONException ex) {
            Logger.getLogger(InvoiceDataFetchProvider.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return invoiceFromDB;
    }
    
    private void callInvoiceExistedLicenses(int invoiceId, 
                                                    Consumer<List<PartOfLicense>> licensesCnsm,
                                                    Consumer<List<LicenseFinance>> licenseFinancesCnsm,
                                                    Consumer<InvoiceFinance> invoiceFinanceDataCnsm,
                                                    Consumer<Client> clientCnsm) throws IOException, AuthServerException, JSONException {
        DBClient dbClient = GeneralConfig.getInstance().getDBClient();
        JSONArray financesData = dbClient.callProcedureAndGetAsJson(INVOICE_FULL_DATA_PROCEDURE, invoiceId, dbClient.getLang());

        if (licensesCnsm != null){
            licensesCnsm.accept(Utils.getListFromJSONArray(PartOfLicense.class, financesData.getJSONArray(0)));
        }
        if (licenseFinancesCnsm != null){
            licenseFinancesCnsm.accept(Utils.getListFromJSONArray(LicenseFinance.class, financesData.getJSONArray(1)));
        }
        if (invoiceFinanceDataCnsm != null){
            ArrayList<InvoiceFinance> invFinance = Utils.getListFromJSONArray(InvoiceFinance.class, financesData.getJSONArray(2));
            invoiceFinanceDataCnsm.accept(invFinance.get(0)); // There is always one entry in it.
        }
        if (clientCnsm != null){
            ArrayList<Client> clients = Utils.getListFromJSONArray(Client.class, financesData.getJSONArray(3));
            clientCnsm.accept(clients.get(0)); // There is always one entry in it.
        }
    }
    
    public List<InvoiceReissuing> getAllIvoiceReissuingsesFromDB() throws Exception {
        DBClient dbClient = GeneralConfig.getInstance().getDBClient();
        JSONObject params = new ConditionBuilder().where().and("language", "=", dbClient.getLang()).condition().build();
        return getObjectsListFromDBTable(InvoiceReissuing.class, DB_REISSUINGS_TABLE, params);
    }
    
    public List<InvoiceStatusClarify> getAllIvoiceClarifiesFromDB() throws Exception {
        DBClient dbClient = GeneralConfig.getInstance().getDBClient();
        JSONObject params = new ConditionBuilder().where().and("language", "=", dbClient.getLang()).condition().build();
        return getObjectsListFromDBTable(InvoiceStatusClarify.class, DB_CLARIFIES_TABLE, params);
    }
    
    public void callInvoiceSuitedLicenses(Invoice invoice, Basket basket,
                                                    Consumer<List<PartOfLicense>> licensesCnsm,
                                                    Consumer<List<LicenseFinance>> licenseFinancesCnsm,
                                                    Consumer<InvoiceFinance> invoiceFinanceDataCnsm) throws Exception {
        DBClient dbClient = GeneralConfig.getInstance().getDBClient();
        try {
            Integer invoiceId = (invoice.getRecId() == 0) ? null : invoice.getRecId();
            
            JSONArray productsArray = new JSONArray();
            Iterator<String> itr = basket.getIterator();
            while(itr.hasNext()){
                String uniqueId = itr.next();
                JSONObject json = Utils.getJsonFrom(null, "product_id", uniqueId);
                productsArray.put(Utils.getJsonFrom(json, "count", basket.getCountFor(uniqueId)));
            }
            
            JSONArray licensesIds = new JSONArray();
            invoice.getLicenses().forEach((licenseShortData) -> {
                licensesIds.put(Utils.getJsonFrom(null, "license_id", licenseShortData.getLicense_id()));
            });
            
            JSONArray licensesArray;
            try {
                licensesArray = dbClient.callProcedureAndGetAsJson("invoice_get_suited_licenses",
                                                                                dbClient.getLang(),
                                                                                invoiceId, invoice.getClientId(),
                                                                                invoice.beginDateProperty().get(),
                                                                                invoice.endDateProperty().get(),
                                                                                productsArray, invoice.getAdditionalDiscountRate(),
                                                                                licensesIds);
                    System.out.println("licenses array from DB: " + licensesArray);
                    if (licensesCnsm != null){
                        licensesCnsm.accept(Utils.getListFromJSONArray(PartOfLicense.class, licensesArray.getJSONArray(0)));
                    }
                    if (licenseFinancesCnsm != null){
                        licenseFinancesCnsm.accept(Utils.getListFromJSONArray(LicenseFinance.class, licensesArray.getJSONArray(1)));
                    }
                    if (invoiceFinanceDataCnsm != null){
                        ArrayList<InvoiceFinance> invFinances = Utils.getListFromJSONArray(InvoiceFinance.class, licensesArray.getJSONArray(2));
                        invoiceFinanceDataCnsm.accept(invFinances.get(0)); // There is always one entry in it.
                    }
            } catch (AuthServerException ex) {
                throw ExceptionsFactory.getAppropriateException(ex);
            }
            
            
        } catch (IOException | JSONException ex) {
            Logger.getLogger(DBUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
