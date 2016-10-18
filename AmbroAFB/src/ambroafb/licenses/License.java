/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.licenses;

import ambroafb.licenses.helper.LicenseStatus;
import ambro.AView;
import ambroafb.general.DateConverter;
import ambroafb.general.GeneralConfig;
import ambroafb.general.interfaces.EditorPanelable;
import authclient.AuthServerException;
import authclient.db.ConditionBuilder;
import authclient.db.DBClient;
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

/**
 *
 * @author Dato
 */
public class License extends EditorPanelable {

    @AView.Column(width = "20", cellFactory = CheckedCellFactory.class)
    private final BooleanProperty checked;
    
    @AView.Column(title = "%date", width = "80")
    private final StringProperty date;
    private final ObjectProperty<LocalDate> dateObjProperty;
    
    @AView.Column(title = "%license N")
    private final StringProperty licenseNumber;
    
    
    private static final String DB_STATUSES_TABLE_NAME = "license_status_descrips";
    
    public License(){
        checked = new SimpleBooleanProperty();
        date = new SimpleStringProperty("");
        dateObjProperty = new SimpleObjectProperty<>();
        licenseNumber = new SimpleStringProperty("");
    }
    
    // DB methods:
    public static ArrayList<License> getAllFromDB(){
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
