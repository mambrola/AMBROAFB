/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.docs;

import ambro.AView;
import ambroafb.accounts.Account;
import ambroafb.docs.filter.DocFilterModel;
import ambroafb.general.DBUtils;
import ambroafb.general.DateConverter;
import ambroafb.general.NumberConverter;
import ambroafb.general.Utils;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.general.interfaces.FilterModel;
import ambroafb.general.interfaces.TableColumnWidths;
import authclient.db.ConditionBuilder;
import authclient.db.WhereBuilder;
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
    
    @AView.Column(width = "24", cellFactory = MarkerCellFactory.class, styleClass = "textCenter")
    private final IntegerProperty marker;
    
    private final IntegerProperty parentRecId;
    private final IntegerProperty processId;
    
    @AView.Column(title = "%doc_date", width = TableColumnWidths.DATE)
    private final StringProperty docDateDescrip;
    private final ObjectProperty<LocalDate> docDateObj;

    @AView.Column(title = "%doc_in_doc_date", width = TableColumnWidths.DATE)
    private final StringProperty docInDocDateDescrip;
    private final ObjectProperty<LocalDate> docInDocDateObj;
    
    @AView.Column(title = "%debit", width = "260")
    private final StringProperty debitDescrip;
    private final ObjectProperty<Account> debitObj;
    
    @AView.Column(title = "%credit", width = "260")
    private final StringProperty creditDescrip;
    private final ObjectProperty<Account> creditObj;
    
    @AView.Column(title = "%iso", width = TableColumnWidths.ISO, styleClass = "textCenter")
    private final StringProperty iso;
    
    @AView.Column(title = "%amount", width = "80", styleClass = "textRight")
    private final StringProperty amount;
    
    @AView.Column(title = "%doc_code", width = "90")
    private final ObjectProperty<DocCode> docCode;
    
    @AView.Column(title = "%doc_descrip", width = "380")
    private final StringProperty descrip;
    private final IntegerProperty ownerId;
    
    private int docType = 10; // by default
    
    private static final String DB_VIEW_NAME = "docs_whole";
    
    private final int markerDefaultValue = 0;
    private final String isoDefaultValue = "GEL";
    private final float amountDefaultValue = -1; // for ADD dialog, that clone method does not throw exception.
    private final int ownerIdDefaultValue = -1;      // for ADD dialog, that clone method does not throw exception.
    private final int parentRecIdDefaultValue = -1;      // for ADD dialog, that clone method does not throw exception.
    
    public Doc(){
        marker = new SimpleIntegerProperty(markerDefaultValue);
        parentRecId = new SimpleIntegerProperty(parentRecIdDefaultValue);
        processId = new SimpleIntegerProperty();
        docDateDescrip = new SimpleStringProperty("");
        docDateObj = new SimpleObjectProperty<>(LocalDate.now());
        docInDocDateDescrip = new SimpleStringProperty("");
        docInDocDateObj = new SimpleObjectProperty<>(LocalDate.now());
        
        debitDescrip = new SimpleStringProperty("");
        debitObj = new SimpleObjectProperty<>(new Account());
        
        creditDescrip = new SimpleStringProperty("");
        creditObj = new SimpleObjectProperty<>(new Account());
        
        iso = new SimpleStringProperty(isoDefaultValue);
        amount = new SimpleStringProperty("");
        docCode = new SimpleObjectProperty<>(new DocCode());
        descrip = new SimpleStringProperty("");
        ownerId = new SimpleIntegerProperty(ownerIdDefaultValue);
        
        docDateObj.addListener((ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) -> {
            docDateDescrip.set(newValue == null ? "" : DateConverter.getInstance().getDayMonthnameYearBySpace(newValue));
        });
        docInDocDateObj.addListener((ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) -> {
            docInDocDateDescrip.set(newValue == null ? "" : DateConverter.getInstance().getDayMonthnameYearBySpace(newValue));
        });
        
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
    
    public static ArrayList<Doc> getFilteredFromDB(FilterModel model) {
        DocFilterModel docFilterModel = (DocFilterModel) model;
        WhereBuilder whereBuilder = new ConditionBuilder().where().
                        and("doc_date", ">=", docFilterModel.getDocDateForDB(true)).
                        and("doc_date", "<=", docFilterModel.getDocDateForDB(false)).
                        and("doc_in_doc_date", ">=", docFilterModel.getDocInDocDateForDB(true)).
                        and("doc_in_doc_date", "<=", docFilterModel.getDocInDocDateForDB(false));
        if (docFilterModel.isSelectedConcreteAccount()){
            whereBuilder.andGroup().or("debit_id", "=", docFilterModel.getSelectedAccountId()).
                                    or("credit_id", "=", docFilterModel.getSelectedAccountId()).closeGroup();
        }
        if (docFilterModel.isSelectedConcreteCurrency()){
            whereBuilder.and("iso", "=", docFilterModel.getSelectedCurrencyIso());
        }
        if (docFilterModel.isSelectedConcreteDocCode()){
            whereBuilder.and("doc_code", "=", docFilterModel.getSelectedDocCode());
        }
        JSONObject params = whereBuilder.condition().build();
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
        return (docDateObj.isNull().get()) ? "" : docDateObj.get().toString();
    }
    
    public String getDocInDocDate(){
        return (docInDocDateObj.isNull().get()) ? "" : docInDocDateObj.get().toString();
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
        return NumberConverter.stringToFloat(amount.get(), 2, amountDefaultValue);
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
        docDateDescrip.set(DateConverter.getInstance().getDayMonthnameYearBySpace(docDateObj.get()));
    }
    
    public void setDocInDocDate(String docInDocDate){
        docInDocDateObj.set(DateConverter.getInstance().parseDate(docInDocDate));
        docInDocDateDescrip.set(DateConverter.getInstance().getDayMonthnameYearBySpace(docInDocDateObj.get()));
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
        debitObj.get().setIso(iso);
        creditObj.get().setIso(iso);
    }
    
    public void setAmount(Float amount){
        this.amount.set(NumberConverter.makeFloatStringBySpecificFraction(amount, 2));
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
        debitObj.set(otherDoc.debitProperty().get().cloneWithID());
        debitDescrip.set(otherDoc.debitProperty().get().getDescrip());
        creditObj.set(otherDoc.creditProperty().get().cloneWithID());
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
        return  debitProperty().get().getDescrip() + " " + creditProperty().get().getDescrip() + " " + getDescrip();
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
                        String imgPath = "\u26AB";
                        switch(marker){
                            case -1:
                                imgPath = "\u29ED";
                                break;
                            case 1:
                                imgPath = "\u25B4";
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
