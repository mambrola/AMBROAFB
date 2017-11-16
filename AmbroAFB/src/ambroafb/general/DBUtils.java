/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general;

import ambroafb.docs.Doc;
import ambroafb.general.countcombobox.Basket;
import ambroafb.invoices.Invoice;
import ambroafb.invoices.helper.InvoiceFinance;
import ambroafb.invoices.helper.PartOfLicense;
import ambroafb.licenses.helper.LicenseFinance;
import authclient.AuthServerException;
import authclient.db.DBClient;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author dato
 */
public class DBUtils {
    
    /**
     * The static function saves element into DB.
     * Note: If this element is new for DB, then it has not DB id. 
     * So after this function the element will has every old value and a DB id too.
     * @param <T>
     * @param source The element which must save to DB.
     * @param tableName The DB table name. (Specific for clients and products: client, product - singular form)
     *                                  
     * @return 
     */
    @Deprecated
    public static <T> T saveObjectToDB(Object source, String tableName){
        try {
            return saveObjectSample(source, tableName);
        } 
        catch (Exception ex) {
            Logger.getLogger(DBUtils.class.getName()).log(Level.SEVERE, null, ex);
            new AlertMessage(Alert.AlertType.ERROR, ex, ex.getLocalizedMessage(), "").showAndWait();
        }
        return null;
    }
    
    private static <T> T saveObjectSample(Object source, String tableName) throws IOException, AuthServerException{
        JSONObject targetJson = Utils.getJSONFromClass(source);

        System.out.println("data for simple table to server: " + targetJson);

        DBClient dbClient = GeneralConfig.getInstance().getDBClient();
        JSONObject newSourceFromDB;
        newSourceFromDB = dbClient.insertUpdate(tableName, targetJson);

        System.out.println("data for simple table from server: " + newSourceFromDB);

        return Utils.getClassFromJSON(source.getClass(), newSourceFromDB);
    }
    
    /**
     * The method analyzes DB exception and show message on alert.
     * @param ex 
     */
    private static void analyzeDBException(Exception ex){
        String docDBErorTitle = GeneralConfig.getInstance().getTitleFor("doc_db_error");
        String message = ex.getLocalizedMessage();
        if (ex instanceof AuthServerException){
            try {
                JSONObject errorInfo = new JSONObject(ex.getMessage());
                message = errorInfo.optString("message");
            } catch (JSONException ex1) {
                Logger.getLogger(DBUtils.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        new AlertMessage(Alert.AlertType.ERROR, ex, message, docDBErorTitle).showAndWait();
    }
    
    @Deprecated
    public static Doc saveCustomDoc(Doc doc){
        DBClient dbClient = GeneralConfig.getInstance().getDBClient();
        Integer id = (doc.getRecId() == 0) ? null : doc.getRecId();
        try {
            JSONArray data = dbClient.callProcedureAndGetAsJson("doc_memorial_insert_update", 
                                                            id,
                                                            doc.docDateProperty().get(),
                                                            doc.docInDocDateProperty().get(),
                                                            doc.getIso(),
                                                            doc.getDebitId(),
                                                            doc.getCreditId(),
                                                            doc.getAmount(),
                                                            doc.getDocCode(),
                                                            doc.getDescrip(),
                                                            doc.getOwnerId());
            System.out.println("doc_memorial_insert_update data from DB: " + data);
            return Utils.getClassFromJSON(Doc.class, data.getJSONObject(0));
        } catch (Exception ex) {
            analyzeDBException(ex);
        }
        return null;
    }
    
    @Deprecated
    public static void callInvoiceSuitedLicenses(Invoice invoice, Basket basket,
                                                    Consumer<List<PartOfLicense>> licensesCnsm,
                                                    Consumer<List<LicenseFinance>> licenseFinancesCnsm,
                                                    Consumer<InvoiceFinance> invoiceFinanceDataCnsm) throws AuthServerException{
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
            
            JSONArray licensesArray = dbClient.callProcedureAndGetAsJson("invoice_get_suited_licenses",
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
            
        } catch (IOException | JSONException ex) {
            Logger.getLogger(DBUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
