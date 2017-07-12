/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.docs;

import ambro.AView;
import ambroafb.currencies.Currency;
import ambroafb.general.DBUtils;
import ambroafb.general.DateConverter;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.licenses.License;
import authclient.db.ConditionBuilder;
import java.time.LocalDate;
import java.util.ArrayList;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import org.json.JSONObject;

/**
 *
 * @author dkobuladze
 */
public class Doc extends EditorPanelable {
    
    @AView.Column(title = "par_id", width = "60")
    private final IntegerProperty parentRecId;
    
    @AView.Column(title = "proc_id", width = "60")
    private final IntegerProperty processId;
    
    @AView.Column(title = "docDate", width = "100")
    private final StringProperty docDate;
    private final ObjectProperty<LocalDate> docDateObj;

    @AView.Column(title = "%pocInDocDate", width = "100")
    private final StringProperty docInDocDate;
    private final ObjectProperty<LocalDate> docInDocDateObj;
    
    @AView.Column(title = "%iso", width = "50")
    private final StringProperty iso;
    private final IntegerProperty debitId;
    private final IntegerProperty creditId;
    
    @AView.Column(title = "amount", width = "80")
    private final StringProperty amount;
    
    @AView.Column(title = "%docCode", width = "70")
    private final StringProperty docCode;
    
    @AView.Column(title = "%descrip", width = "120")
    private final StringProperty descrip;
    private final IntegerProperty ownerId;
    
    private static final String DB_TABLE_NAME = "docs";
    
    public Doc(){
        parentRecId = new SimpleIntegerProperty();
        processId = new SimpleIntegerProperty();
        docDate = new SimpleStringProperty("");
        docDateObj = new SimpleObjectProperty<>();
        docInDocDate = new SimpleStringProperty("");
        docInDocDateObj = new SimpleObjectProperty<>();
        iso = new SimpleStringProperty();
        debitId = new SimpleIntegerProperty();
        creditId = new SimpleIntegerProperty();
        amount = new SimpleStringProperty();
        docCode = new SimpleStringProperty();
        descrip = new SimpleStringProperty();
        ownerId = new SimpleIntegerProperty();
        
        docDateObj.addListener((ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) -> {
            docDate.set(convertDateToString(newValue));
        });
        
        docInDocDateObj.addListener((ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) -> {
            docInDocDate.set(convertDateToString(newValue));
        });
    }
    
    private String convertDateToString(LocalDate date){
        String dateStr = "";
        if (date != null){
            dateStr = DateConverter.getInstance().getDayMonthnameYearBySpace(date);
        }
        return dateStr;
    }
    
    // DB methods:
    public static ArrayList<License> getAllFromDB() {
        JSONObject params = new ConditionBuilder().build();
        return DBUtils.getObjectsListFromDB(Doc.class, DB_TABLE_NAME, params);
    }
    
    public static Doc getOneFromDB (int recId){
        JSONObject params = new ConditionBuilder().where().and("rec_id", "=", recId).condition().build();
        return DBUtils.getObjectFromDB(Doc.class, DB_TABLE_NAME, params);
    }
    
    public static Currency saveOneToDB(Doc doc){
        if (doc == null) return null;
//        return DBUtils.saveObjectToDBSimple(doc, DB_TABLE_NAME);
        return null;
    }
    
    public static boolean deleteOneFromDB(int productId){
        System.out.println("delete from db...??");
        return false;
    }
    
    // Properties:
    public IntegerProperty parentRecIdProperty(){
        return parentRecId;
    }
    public IntegerProperty processIdProperty(){
        return processId;
    }
    
    public ObjectProperty<LocalDate> docDateProperty(){
        return docDateObj;
    }
    
    public ObjectProperty<LocalDate> docInDocDateProperty(){
        return docInDocDateObj;
    }
    
    public StringProperty isoProperty(){
        return iso;
    }
    
    public IntegerProperty debitId(){
        return debitId;
    }
    
    public IntegerProperty creditId(){
        return creditId;
    }
    
    public StringProperty amountProperty(){
        return amount;
    }
    
    public IntegerProperty ownerIdProperty(){
        return ownerId;
    }
            
    
    
    // Getters:
    public int getParentRecId(){
        return parentRecId.get();
    }
    
    public int getProcessId(){
        return processId.get();
    }
    
    public String getDocDate(){
        return docDate.get();
    }
    
    public String getDocInDocDate(){
        return docInDocDate.get();
    }
    
    public String getIso(){
        return iso.get();
    }
    
    public int getDebitId(){
        return debitId.get();
    }
    
    public int getCreditId(){
        return creditId.get();
    }
    
    public Float getAmount(){
        return Float.valueOf(amount.get());
    }
    
    public String getDocCode(){
        return docCode.get();
    }
    
    public String getDescrip(){
        return descrip.get();
    }
    
    public int getOwnerId(){
        return ownerId.get();
    }
    

    // Setters:
    public void setParentRecId(int parentId){
        this.parentRecId.set(parentId);
    }
    
    public void setProcessId(int processID){
        this.processId.set(processID);
    }
    
    public void setDocDate(String docDate){
        this.docCode.set(docDate);
    }
    
    public void setDocInDocDate(String docInDocDate){
        this.docInDocDate.set(docInDocDate);
    }
    
    public void setIso(String iso){
        this.iso.set(iso);
    }
    
    public void setDebitId(int debitId){
        this.debitId.set(debitId);
    }
    
    public void setCreditId(int creditID){
        this.creditId.set(creditID);
    }
    
    public void setAmount(Float amount){
        this.amount.set("" + amount);
    }
    
    public void setDocCode(String docCode){
        this.docCode.set(docCode);
    }
    
    public void setDescrip(String descrip){
        this.descrip.set(descrip);
    }
    
    public void setOwnerId(int ownerId){
        this.ownerId.set(ownerId);
    }
    
    
    @Override
    public Doc cloneWithoutID() {
        Doc clone = new Doc();
        clone.copyFrom(this);
        return clone;
    }

    @Override
    public Doc cloneWithID() {
        Doc clone = cloneWithoutID();
        clone.setRecId(getRecId());
        return clone;
    }

    @Override
    public void copyFrom(EditorPanelable other) {
        Doc otherDoc = (Doc) other;
        setParentRecId(otherDoc.getParentRecId());
        setProcessId(otherDoc.getProcessId());
        setDocDate(otherDoc.getDocDate());
        setDocInDocDate(otherDoc.getDocInDocDate());
        setIso(otherDoc.getIso());
        setDebitId(otherDoc.getDebitId());
        setCreditId(otherDoc.getCreditId());
        setAmount(otherDoc.getAmount());
        setDocCode(otherDoc.getDocCode());
        setDescrip(otherDoc.getDescrip());
        setOwnerId(otherDoc.getOwnerId());
    }

    @Override
    public boolean compares(EditorPanelable backup) {
        Doc docBackup = (Doc) backup;
        return  getParentRecId() == docBackup.getParentRecId() &&
                getProcessId() == docBackup.getProcessId() &&
                getDocDate().equals(docBackup.getDocDate()) &&
                getDocInDocDate().equals(docBackup.getDocInDocDate()) &&
                getIso().equals(docBackup.getIso()) &&
                getDebitId() == docBackup.getDebitId() &&
                getCreditId() == docBackup.getCreditId() &&
                getAmount().equals(docBackup.getAmount()) &&
                getDocCode().equals(docBackup.getDocCode()) &&
                getDescrip().equals(docBackup.getDescrip()) &&
                getOwnerId() == docBackup.getOwnerId();
    }

    @Override
    public String toStringForSearch() {
        return  getDocDate()+ " " + getDocInDocDate() + " " + getIso() + " " + getAmount() + " " +
                getDocCode() + " " + getDescrip();
    }
}
