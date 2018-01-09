/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.docs;

import ambro.AView;
import ambroafb.general.DateCellFactory;
import ambroafb.general.DateConverter;
import ambroafb.general.NumberConverter;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.general.interfaces.TableColumnFeatures;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import java.util.Objects;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Paint;
import javafx.util.Callback;

/**
 *
 * @author dkobuladze
 */
public class Doc extends EditorPanelable {
    
    @AView.Column(width = "24", cellFactory = MarkerCellFactory.class, styleClass = TableColumnFeatures.Style.TEXT_CENTER)
    private final IntegerProperty marker;
    
    private final IntegerProperty parentRecId;
    private final IntegerProperty processId;
    
    @AView.Column(title = "%doc_date", width = TableColumnFeatures.Width.DATE, cellFactory = DateCellFactory.LocalDateCell.class)
    private final ObjectProperty<LocalDate> docDate;

    @AView.Column(title = "%doc_in_doc_date", width = TableColumnFeatures.Width.DATE, cellFactory = DateCellFactory.LocalDateCell.class)
    private final ObjectProperty<LocalDate> docInDocDate;
    
    @AView.Column(title = "%debit", width = "260")
    private final StringProperty debitDescrip;
    private final StringProperty debitId;
    private Long debitAccountNumber;
//    private final ObjectProperty<Account> debitObj;
    
    @AView.Column(title = "%credit", width = "260")
    private final StringProperty creditDescrip;
    private final StringProperty creditId;
    private Long creditAccountNumber;
//    private final ObjectProperty<Account> creditObj;
    
    @AView.Column(title = "%iso", width = TableColumnFeatures.Width.ISO, styleClass = TableColumnFeatures.Style.TEXT_CENTER)
    private final StringProperty iso;
    
    @AView.Column(title = "%amount", width = "80", styleClass = TableColumnFeatures.Style.TEXT_RIGHT)
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
        docDate = new SimpleObjectProperty<>(LocalDate.now());
        docInDocDate = new SimpleObjectProperty<>(LocalDate.now());
        
        debitDescrip = new SimpleStringProperty("");
        debitId = new SimpleStringProperty();
//        debitObj = new SimpleObjectProperty<>(new Account());
        
        creditDescrip = new SimpleStringProperty("");
        creditId = new SimpleStringProperty();
//        creditObj = new SimpleObjectProperty<>(new Account());
        
        iso = new SimpleStringProperty(isoDefaultValue);
        amount = new SimpleStringProperty("");
        docCode = new SimpleObjectProperty<>(new DocCode());
        descrip = new SimpleStringProperty("");
        ownerId = new SimpleIntegerProperty(ownerIdDefaultValue);
        
//        debitObj.addListener((ObservableValue<? extends Account> observable, Account oldValue, Account newValue) -> {
//            if (newValue != null){
//                debitDescrip.set(newValue.getDescrip());
//            }
//        });
//        
//        creditObj.addListener((ObservableValue<? extends Account> observable, Account oldValue, Account newValue) -> {
//            if (newValue != null){
//                creditDescrip.set(newValue.getDescrip());
//            }
//        });
//        
//        debitObj.get().setIso(isoDefaultValue);
//        creditObj.get().setIso(isoDefaultValue);
    }
    
    
    // Properties:
    public IntegerProperty parentRecIdProperty(){
        return parentRecId;
    }
    public IntegerProperty processIdProperty(){
        return processId;
    }
    
    public ObjectProperty<LocalDate> docDateProperty(){
        return docDate;
    }
    
    public ObjectProperty<LocalDate> docInDocDateProperty(){
        return docInDocDate;
    }
    
    public StringProperty debitIdProperty(){
        return debitId;
    }
    
    public StringProperty creditIdProperty(){
        return creditId;
    }
    
//    public ObjectProperty<Account> debitProperty(){
//        return debitObj;
//    }
    
//    public ObjectProperty<Account> creditProperty(){
//        return creditObj;
//    }
    
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
        return (docDate.isNull().get()) ? "" : docDate.get().toString();
    }
    
    public String getDocInDocDate(){
        return (docInDocDate.isNull().get()) ? "" : docInDocDate.get().toString();
    }
    
    public String getIso(){
        return iso.get();
    }
    
    public Integer getDebitId(){
        return NumberConverter.stringToInteger(debitId.get(), null);
//        return debitObj.get().getRecId();
    }
    
    @JsonIgnore
    public Long getDebitAccount(){
        return debitAccountNumber;
    }
    
    @JsonIgnore
    public String getDebitDescrip(){
        return debitDescrip.get();
    }
    
    public Integer getCreditId(){
        return NumberConverter.stringToInteger(creditId.get(), null);
//        return creditObj.get().getRecId();
    }
    
    @JsonIgnore
    public Long getCreditAccount(){
        return creditAccountNumber;
    }
    
    @JsonIgnore
    public String getCreditDescrip(){
        return creditDescrip.get();
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
        this.docDate.set(DateConverter.getInstance().parseDate(docDate));
    }
    
    public void setDocInDocDate(String docInDocDate){
        this.docInDocDate.set(DateConverter.getInstance().parseDate(docInDocDate));
    }
    
    public void setDebitId(Integer debitId){
        this.debitId.set((debitId == null) ? null : debitId.toString());
//        debitObj.get().setRecId(debitId);
    }
    
    @JsonProperty
    public void setDebitAccount(Long accountNumber){
        debitAccountNumber = accountNumber;
//        debitObj.get().setAccount(accountNumber);
    }
    
    @JsonProperty
    public void setDebitDescrip(String descrip){
//        debitObj.get().setDescrip(descrip);
        debitDescrip.set(descrip);
    }
    
    public void setCreditId(Integer creditId){
        this.creditId.set((creditId == null) ? null : creditId.toString());
//        creditObj.get().setRecId(creditId);
    }
    
    @JsonProperty
    public void setCreditAccount(Long accountNumber){
        creditAccountNumber = accountNumber;
//        creditObj.get().setAccount(accountNumber);
    }
    
    @JsonProperty
    public void setCreditDescrip(String descrip){
//        creditObj.get().setDescrip(descrip);
        creditDescrip.set(descrip);
    }
    
    public void setIso(String iso){
        this.iso.set(iso);
//        debitObj.get().setIso(iso);
//        creditObj.get().setIso(iso);
    }
    
    public void setAmount(Float amount){
        this.amount.set(NumberConverter.convertNumberToStringBySpecificFraction(amount, 2));
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
//        debitObj.set(otherDoc.debitProperty().get().cloneWithID());
//        debitDescrip.set(otherDoc.debitProperty().get().getDescrip());
//        creditObj.set(otherDoc.creditProperty().get().cloneWithID());
//        creditDescrip.set(otherDoc.creditProperty().get().getDescrip());

        setDebitId(otherDoc.getDebitId());
        setDebitDescrip(otherDoc.getDebitDescrip());
        setDebitAccount(otherDoc.getDebitAccount());
        setCreditId(otherDoc.getCreditId());
        setCreditDescrip(otherDoc.getCreditDescrip());
        setCreditAccount(otherDoc.getCreditAccount());
        
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
//        Account debitAcc = Utils.avoidNullAndReturnEmpty(debitObj.get());
//        Account creditAcc = Utils.avoidNullAndReturnEmpty(creditObj.get());
        
        return  getParentRecId() == docBackup.getParentRecId() &&
                getProcessId() == docBackup.getProcessId() &&
                getDocDate().equals(docBackup.getDocDate()) &&
                getDocInDocDate().equals(docBackup.getDocInDocDate()) &&
                getIso().equals(docBackup.getIso()) &&
//                debitAcc.partlyCompare(docBackup.debitProperty().get()) &&
//                creditAcc.partlyCompare(docBackup.creditProperty().get()) &&
                Objects.equals(getDebitId(), docBackup.getDebitId()) &&
                Objects.equals(getDebitDescrip(), docBackup.getDebitDescrip()) &&
                Objects.equals(getDebitAccount(), docBackup.getDebitAccount()) &&
                Objects.equals(getCreditId(), docBackup.getCreditId()) &&
                Objects.equals(getCreditDescrip(), docBackup.getCreditDescrip()) &&
                Objects.equals(getCreditAccount(), docBackup.getCreditAccount()) &&
                
                getAmount().equals(docBackup.getAmount()) &&
                getDocCode().equals(docBackup.getDocCode()) &&
                getDescrip().equals(docBackup.getDescrip()) &&
                getOwnerId() == docBackup.getOwnerId() &&
                getDocType() == docBackup.getDocType() &&
                getMarker() == docBackup.getMarker();
    }

    @Override
    public String toStringForSearch() {
        return  getDebitDescrip() + " " + getCreditDescrip()+ " " + getDescrip();
    }

    @Override
    public String toString() {
        return "Doc{" + "recId= " + getRecId() + 
                        ", marker=" + marker.get() + ", parentRecId=" + parentRecId.get() + 
                        ", processId=" + processId.get() + 
                        ", docDateObj=" + docDate.get().toString() + 
                        ", docInDocDateObj=" + docInDocDate.get().toString() + 
                        ", debitObj=" + getDebitId() + 
                        ", creditObj=" + getCreditId() + 
                        ", iso=" + iso.get() + ", amount=" + amount.get() + ", docCode=" + docCode.get() + 
                        ", descrip=" + descrip.get() + ", ownerId=" + ownerId.get() + 
                        ", docType=" + docType + ", markerDefaultValue=" + markerDefaultValue + '}';
    }

    
    
    private static final String DOC_NO_CHILD_SYMBOL = "\u26AB", DOC_PARENT_SYMBOL = "\u29ED", DOC_CHILD_SYMBOL = "\u25B4";
    
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
                        String symbol;
                        switch(marker){
                            case -1:
                                symbol = DOC_PARENT_SYMBOL;
                                break;
                            case 1:
                                symbol = DOC_CHILD_SYMBOL;
                                break;
                            default:
                                symbol = DOC_NO_CHILD_SYMBOL;
                                break;
                        }
                        setText(symbol);
                        setTextFill(Paint.valueOf("black"));
                    }
                }
            };
            return cell;
        }
    }
}
