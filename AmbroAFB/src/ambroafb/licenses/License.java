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
import ambroafb.invoices.Invoice;
import ambroafb.licenses.filter.LicenseFilterModel;
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
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Dato
 */
public class License extends EditorPanelable {

    public String createdDate;
    
    @AView.Column(title = "%license N")
    private final StringProperty licenseNumber;
    
    @AView.Column(title = "%client")
    @JsonIgnore
    private final StringProperty clientDescrip;
    private final ObjectProperty<Client> client;
    
    @AView.Column(title = "%product")
    private final StringProperty productDescrip;
    
    @AView.Column(title = "%last_invoice")
    private final StringProperty lastInvoiceDescrip;
    private final ObjectProperty<Invoice> invoice;
    
    @AView.Column(title = "%invoice_status")
    private final StringProperty statusDescrip;
    private final ObjectProperty<LicenseStatus> status;
    
    @AView.Column(title = "%begin_date", width = "100")
    private final StringProperty beginDateDescrip;
    private final ObjectProperty<LocalDate> beginDate;
    
    @AView.Column(title = "%end_date", width = "100")
    private final StringProperty endDateDescrip;
    private final ObjectProperty<LocalDate> endDate;
    
    @AView.Column(width = "20", cellFactory = CheckedCellFactory.class)
    private final BooleanProperty checked;
    
    @AView.Column(title = "%date", width = "80")
    private final StringProperty date;
    private final ObjectProperty<LocalDate> dateObjProperty;
    
    
    
    private static final String DB_VIEW_NAME = "licenses_whole";
    private static final String DB_STATUSES_TABLE_NAME = "license_status_descrips";
    
    public License(){
        licenseNumber = new SimpleStringProperty("");
        clientDescrip = new SimpleStringProperty("");
        client = new SimpleObjectProperty<>();
        productDescrip = new SimpleStringProperty("");
        lastInvoiceDescrip = new SimpleStringProperty("");
        invoice = new SimpleObjectProperty<>();
        statusDescrip = new SimpleStringProperty("");
        status = new SimpleObjectProperty<>();
        beginDateDescrip = new SimpleStringProperty("");
        beginDate = new SimpleObjectProperty<>();
        endDateDescrip = new SimpleStringProperty("");
        endDate = new SimpleObjectProperty<>();
        
        checked = new SimpleBooleanProperty();
        date = new SimpleStringProperty("");
        dateObjProperty = new SimpleObjectProperty<>();
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
//        if ()
//        whereBuilder.and("client_id", "=", licenseFilterModel.getClient().getRecId())
//                    .and("product_id", "=", licenseFilterModel.getProduct().getRecId())
//                    .and("status", "=", licenseFilterModel.getStatuses());
        
        
        try {
            JSONObject params = whereBuilder.condition().build();
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
    public BooleanProperty checkedProperty(){
        return checked;
    }
    
    public ObjectProperty<LocalDate> dateProperty(){
        return dateObjProperty;
    }
    
    
    // Getters:
    public boolean getChecked(){
        return checked.get();
    }
    
    public String getDate(){
        return date.get();
    }
    
    
    // Setters:
    public void setChecked(boolean newValue){
        this.checked.set(newValue);
    }
    
    public void setDate(String date){
        dateObjProperty.set(DateConverter.getInstance().parseDate(date));
        
//        String localDateStr;
//        try {
//            dateObjProperty.set(DateConverter.parseDateWithTimeWithoutMilisecond(date));
//            localDateStr = DateConverter.getDayMonthnameYearBySpace(dateObjProperty.get());
//        } catch(Exception ex) {
//            localDateStr = date;
//        }
//        this.date.set(localDateStr);
    }
    
    
    @Override
    public EditorPanelable cloneWithoutID() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public EditorPanelable cloneWithID() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void copyFrom(EditorPanelable other) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String toStringForSearch() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    public static class CheckedCellFactory implements Callback<TableColumn<License, Boolean>, TableCell<License, Boolean>> {

        @Override
        public TableCell<License, Boolean> call(TableColumn<License, Boolean> param) {
            return new TableCell<License, Boolean>() {
                @Override
                public void updateItem(Boolean isChecked, boolean empty) {
                    CheckBox checker = new CheckBox();
                    checker.setSelected(isChecked);
                    setGraphic(empty ? null : checker);
                }
            };
        }
    }
}
