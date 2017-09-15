/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.docs;

import ambro.AView;
import ambroafb.accounts.Account;
import ambroafb.general.DBUtils;
import ambroafb.general.DateConverter;
import ambroafb.general.Utils;
import ambroafb.general.interfaces.EditorPanelable;
import authclient.db.ConditionBuilder;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import java.util.ArrayList;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Paint;
import javafx.util.Callback;
import org.json.JSONObject;

/**
 *
 * @author dkobuladze
 */
public class Doc extends EditorPanelable {
    
    @AView.Column(width = "24", cellFactory = MarkerCellFactory.class)
    private final IntegerProperty marker;
    
    private final IntegerProperty parentRecId;
    private final IntegerProperty processId;
    
    @AView.Column(title = "%doc_date", width = "120", styleClass = "textCenter")
    private final ObjectProperty<LocalDate> docDateObj;

    @AView.Column(title = "%doc_in_doc_date", width = "100", styleClass = "textCenter")
    private final ObjectProperty<LocalDate> docInDocDateObj;
    
    @AView.Column(title = "%debit", width = "230")
    private final StringProperty debitDescrip;
    private final ObjectProperty<Account> debitObj;
    
    @AView.Column(title = "%credit", width = "230")
    private final StringProperty creditDescrip;
    private final ObjectProperty<Account> creditObj;
    
    @AView.Column(title = "%iso", width = "50", styleClass = "textCenter")
    private final StringProperty iso;
    
    @AView.Column(title = "%amount", width = "80", styleClass = "textRight")
    private final StringProperty amount;
    
    @AView.Column(title = "%doc_code", width = "130")
    private final ObjectProperty<DocCode> docCode;
    
    @AView.Column(title = "%doc_descrip", width = "270")
    private final StringProperty descrip;
    private final IntegerProperty ownerId;
    
    private int docType;
    
    private static final String DB_VIEW_NAME = "docs_whole";
    
    private final int markerDefaultValue = 0;
    private final String isoDefaultValue = "GEL";
    private final float amountDefaultValue = -1; // for ADD dialog, that clone method does not throw exception.
    private final int ownerIdDefaultValue = -1;      // for ADD dialog, that clone method does not throw exception.
    
    public Doc(){
        marker = new SimpleIntegerProperty(markerDefaultValue);
        parentRecId = new SimpleIntegerProperty(-1);
        processId = new SimpleIntegerProperty();
        docDateObj = new SimpleObjectProperty<>(LocalDate.now());
        docInDocDateObj = new SimpleObjectProperty<>(LocalDate.now());
        
        debitDescrip = new SimpleStringProperty("");
        debitObj = new SimpleObjectProperty<>(new Account());
        
        creditDescrip = new SimpleStringProperty("");
        creditObj = new SimpleObjectProperty<>(new Account());
        
        iso = new SimpleStringProperty(isoDefaultValue);
        amount = new SimpleStringProperty("" + amountDefaultValue);
        docCode = new SimpleObjectProperty<>(new DocCode());
        descrip = new SimpleStringProperty("");
        ownerId = new SimpleIntegerProperty(ownerIdDefaultValue);
        
        debitObj.addListener((ObservableValue<? extends Account> observable, Account oldValue, Account newValue) -> {
            if (newValue != null){
                debitDescrip.set(newValue.getDescrip());
            }
        });
        
        creditObj.addListener((ObservableValue<? extends Account> observable, Account oldValue, Account newValue) -> {
            if (newValue != null){
                creditDescrip.set(newValue.getDescrip());
            }
        });
        
        debitObj.get().setIso(isoDefaultValue);
        creditObj.get().setIso(isoDefaultValue);
    }
    
    private String convertDateToString(LocalDate date){
        String dateStr = "";
        if (date != null){
            dateStr = DateConverter.getInstance().getDayMonthnameYearBySpace(date);
        }
        return dateStr;
    }
    
    // DB methods:
    /**
     * Fetch Docs from DB.
     * @return 
     */
    public static ArrayList<Doc> getAllFromDB() {
        JSONObject params = new ConditionBuilder().build();
        ArrayList<Doc> docs = DBUtils.getObjectsListFromDB(Doc.class, DB_VIEW_NAME, params);
        return docs;
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
    
    public ObjectProperty<Account> debitProperty(){
        return debitObj;
    }
    
    public ObjectProperty<Account> creditProperty(){
        return creditObj;
    }
    
    public StringProperty isoProperty(){
        return iso;
    }
    
    public StringProperty amountProperty(){
        return amount;
    }
    
    public ObjectProperty<DocCode> docCodeProperty(){
        return docCode;
    }
    
    public StringProperty descripProperty(){
        return descrip;
    }
    
    public IntegerProperty ownerIdProperty(){
        return ownerId;
    }
            
    
    
    // Getters:
    public int getDocType(){
        return docType;
    }
    
    public int getParentRecId(){
        return parentRecId.get();
    }
    
    public int getProcessId(){
        return processId.get();
    }
    
    public String getDocDate(){
        return docDateObj.get().toString();
    }
    
    public String getDocInDocDate(){
        return docInDocDateObj.get().toString();
    }
    
    public String getIso(){
        return iso.get();
    }
    
    public int getDebitId(){
        return debitObj.get().getRecId();
    }
    
    public int getCreditId(){
        return creditObj.get().getRecId();
    }
    
    public Float getAmount(){
        return amount.get().isEmpty() ? amountDefaultValue : Float.valueOf(amount.get());
    }
    
    public String getDocCode(){
        return docCode.get().getDocCode();
    }
    
    public String getDescrip(){
        return descrip.get();
    }
    
    public int getOwnerId(){
        return ownerId.get();
    }
    
    @JsonIgnore
    public int getMarker(){
        return marker.get();
    }
    
    @JsonIgnore
    public boolean isChildDoc(){
        return marker.get() == 1;
    }
    
    @JsonIgnore
    public boolean isParentDoc(){
        return marker.get() == -1;
    }
    
    
    // Setters:
    public void setDocType(int docType){
        this.docType = docType;
    }
    
    public void setParentRecId(int parentId){
        this.parentRecId.set(parentId);
    }
    
    public void setProcessId(int processID){
        this.processId.set(processID);
    }
    
    public void setDocDate(String docDate){
        docDateObj.set(DateConverter.getInstance().parseDate(docDate));
    }
    
    public void setDocInDocDate(String docInDocDate){
        docInDocDateObj.set(DateConverter.getInstance().parseDate(docInDocDate));
    }
    
    public void setDebitId(int debitId){
        debitObj.get().setRecId(debitId);
    }
    
    public void setDebitAccount(int accountNumber){
        debitObj.get().setAccount(accountNumber);
    }
    
    public void setDebitDescrip(String descrip){
        debitObj.get().setDescrip(descrip);
        debitDescrip.set(descrip);
    }
    
    public void setCreditId(int creditId){
        creditObj.get().setRecId(creditId);
    }
    
    public void setCreditAccount(int accountNumber){
        creditObj.get().setAccount(accountNumber);
    }
    
    public void setCreditDescrip(String descrip){
        creditObj.get().setDescrip(descrip);
        creditDescrip.set(descrip);
    }
    
    public void setIso(String iso){
        this.iso.set(iso);
    }
    
    public void setAmount(Float amount){
        this.amount.set("" + amount);
    }
    
    public void setDocCode(String docCode){
        this.docCode.get().setDocCode(docCode);
    }
    
    public void setDescrip(String descrip){
        this.descrip.set(descrip);
    }
    
    public void setOwnerId(int ownerId){
        this.ownerId.set(ownerId);
    }
    
    @JsonProperty
    public void setMarker(int marker){
        this.marker.set(marker);
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
        debitObj.get().copyFrom(otherDoc.debitProperty().get());
        debitDescrip.set(otherDoc.debitProperty().get().getDescrip());
        creditObj.get().copyFrom(otherDoc.creditProperty().get());
        creditDescrip.set(otherDoc.creditProperty().get().getDescrip());
        setAmount(otherDoc.getAmount());
        setDocCode(otherDoc.getDocCode());
        setDescrip(otherDoc.getDescrip());
        setOwnerId(otherDoc.getOwnerId());
        setDocType(otherDoc.getDocType());
        setMarker(otherDoc.getMarker());
    }

    @Override
    public boolean compares(EditorPanelable backup) {
        Doc docBackup = (Doc) backup;
        Account debitAcc = Utils.avoidNullAndReturnEmpty(debitObj.get());
        Account creditAcc = Utils.avoidNullAndReturnEmpty(creditObj.get());
        return  getParentRecId() == docBackup.getParentRecId() &&
                getProcessId() == docBackup.getProcessId() &&
                getDocDate().equals(docBackup.getDocDate()) &&
                getDocInDocDate().equals(docBackup.getDocInDocDate()) &&
                getIso().equals(docBackup.getIso()) &&
                debitAcc.partlyCompare(docBackup.debitProperty().get()) &&
                creditAcc.partlyCompare(docBackup.creditProperty().get()) &&
                getAmount().equals(docBackup.getAmount()) &&
                getDocCode().equals(docBackup.getDocCode()) &&
                getDescrip().equals(docBackup.getDescrip()) &&
                getOwnerId() == docBackup.getOwnerId() &&
                getDocType() == docBackup.getDocType() &&
                getMarker() == docBackup.getMarker();
    }

    @Override
    public String toStringForSearch() {
        return  getDocDate() + " " + getDocInDocDate() + " " + debitProperty().get().getDescrip() + " "  + 
                creditProperty().get().getDescrip() + " " + getIso() + " " + getDocCode() + " " + getDescrip();
    }

    @Override
    public String toString() {
        return "Doc{" + "marker=" + marker.get() + ", parentRecId=" + parentRecId.get() + 
                        ", processId=" + processId.get() + 
                        ", docDateObj=" + docDateObj.get().toString() + 
                        ", docInDocDateObj=" + docInDocDateObj.get().toString() + 
                        ", debitObj=" + debitObj.get().toString() + 
                        ", creditObj=" + creditObj.get().toString() + 
                        ", iso=" + iso.get() + ", amount=" + amount.get() + ", docCode=" + docCode.get() + 
                        ", descrip=" + descrip.get() + ", ownerId=" + ownerId.get() + 
                        ", docType=" + docType + ", markerDefaultValue=" + markerDefaultValue + '}';
    }

    
    
//    private static final String DOC_NO_CHILD_IMG_URL = "/images/doc_hasnot_child.png", DOC_PARENT_IMG_URL = "/images/doc_parent.png", DOC_CHILD_IMG_URL = "/images/doc_child.png";
    
    public static class MarkerCellFactory implements Callback<TableColumn<Doc, Integer>, TableCell<Doc, Integer>> {

        @Override
        public TableCell<Doc, Integer> call(TableColumn<Doc, Integer> param) {
            TableCell<Doc, Integer> cell = new TableCell<Doc, Integer>(){
                private final ImageView view = new ImageView();

                @Override
                public void updateItem(Integer marker, boolean empty) {
                    if (empty) {
                        setText(null);
                    } else {
                        String imgPath = "\u25EF";
                        switch(marker){
                            case -1:
                                imgPath = "\u2A00";
                                break;
                            case 1:
                                imgPath = "\u29CA";
                                break;
                            default:
                                break;
                        }
                        setText(imgPath);
                        setTextFill(Paint.valueOf("black"));
                    }
                }
            };
            return cell;
        }
        
    }
}
