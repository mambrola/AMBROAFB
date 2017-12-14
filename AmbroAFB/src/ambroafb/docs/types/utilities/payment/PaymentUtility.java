/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.docs.types.utilities.payment;

import ambroafb.docs.Doc;
import ambroafb.docs.DocMerchandise;
import ambroafb.general.DateConverter;
import ambroafb.general.NumberConverter;
import ambroafb.general.interfaces.EditorPanelable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author dkobuladze
 */
public class PaymentUtility extends EditorPanelable {

    private final ObjectProperty<LocalDate> docDate;
    private final ObjectProperty<LocalDate> docInDocDate;
    private final ObjectProperty<DocMerchandise> utility;
    private final StringProperty iso, amount, docCode, descrip;
    private int parentRecId, processId, debitId, creditId, docType, ownerId;
    
    private final String docCodeValue = "payment";
    private final float amountDefaultValue = -1;
    
    
    public PaymentUtility(){
        // By the time "bindBidirectional" the value of brackets object will be set to left side object, of binding. So the default value must set this object.
        docDate = new SimpleObjectProperty<>(LocalDate.now()); 
        docInDocDate = new SimpleObjectProperty<>(LocalDate.now());
        
        utility = new SimpleObjectProperty<>(new DocMerchandise());
        iso = new SimpleStringProperty("");
        amount = new SimpleStringProperty("");
        docCode = new SimpleStringProperty(docCodeValue); // when ADD form is open.
        descrip = new SimpleStringProperty("");
    }
    
    public ObjectProperty<DocMerchandise> utilityProperty() {
        return utility;
    }
    
    public ObjectProperty<LocalDate> docDateProperty(){
        return docDate;
    }

    public ObjectProperty<LocalDate> docInDocDateProperty(){
        return docInDocDate;
    }
    
    public StringProperty isoProperty() {
        return iso;
    }
    
    public StringProperty amountProperty(){
        return amount;
    }
    
    public StringProperty docCodeProperty() {
        return docCode;
    }
    
    public StringProperty descripProperty() {
        return descrip;
    }
    
    
    // Getters:
    public String getDocDate(){
        return (docDate.get() == null) ? "" : docDate.get().toString();
    }
    
    public String getDocInDocDate(){
        return (docInDocDate.get() == null) ? "" : docInDocDate.get().toString();
    }
    
    public String getIso(){
        return iso.get();
    }
    
    public Float getAmount(){
        return NumberConverter.stringToFloat(amount.get(), 2, amountDefaultValue);
    }
    
    public String getDocCode(){
        return docCode.get();
    }
    
    @JsonIgnore
    public String getDescrip(){
        return descrip.get();
    }
    
    @JsonIgnore
    public int getParentRectId(){
        return parentRecId;
    }
    
    @JsonIgnore
    public int getProcessId(){
        return processId;
    }
    
    @JsonIgnore
    public int getDebitId(){
        return debitId;
    }
    
    @JsonIgnore
    public int getCreditId(){
        return creditId;
    }
    
    @JsonIgnore
    public int getDocType(){
        return docType;
    }
    
    @JsonIgnore
    public int getOwnerId(){
        return ownerId;
    }
    
    
    // Setters:
    public void setDocDate(String date){
        this.docDate.set(DateConverter.getInstance().parseDate(date));
    }
    
    public void setDocInDocDate(String date){
        this.docInDocDate.set(DateConverter.getInstance().parseDate(date));
    }
    
    public void setIso(String iso){
        this.iso.set(iso);
    }
    
    public void setAmount(Float amount){
        this.amount.set(NumberConverter.convertNumberToStringBySpecificFraction(amount, 2));
    }
    
    public void setDocCode(String docCode){
        this.docCode.set(docCode);
    }
    
    @JsonProperty
    public void setDescrip(String descrip){
        this.descrip.set(descrip);
    }
    
    @JsonProperty
    public void setParentRecId(int parentRecId){
        this.parentRecId = parentRecId;
    }
    
    @JsonProperty
    public void setProcessId(int processId){
        this.processId = processId;
    }
    
    @JsonProperty
    public void setDebitId(int debitId){
        this.debitId = debitId;
    }

    @JsonProperty
    public void setCreditId(int creditId){
        this.creditId = creditId;
    }

    @JsonProperty
    public void setDocType(int docType){
        this.docType = docType;
    }
    
    @JsonProperty
    public void setOwnerId(int ownerId){
        this.ownerId = ownerId;
    }

    @JsonIgnore
    public Doc convertToDoc(){
        Doc result = new Doc();
        result.setDocDate(getDocDate());
        result.setDocInDocDate(getDocInDocDate());
        result.setIso(getIso());
        result.setAmount(getAmount());
        result.setDocCode(getDocCode());
        result.setDescrip(getDescrip());
        result.setParentRecId(getParentRectId());
        result.setProcessId(getProcessId());
        result.setDebitId(getDebitId());
        result.setCreditId(getCreditId());
        result.setDocType(getDocType());
        result.setOwnerId(getOwnerId());
        return result;
    }
    
    
    @Override
    public PaymentUtility cloneWithoutID() {
        PaymentUtility clone = new PaymentUtility();
        clone.copyFrom(this);
        return clone;
    }

    @Override
    public PaymentUtility cloneWithID() {
        PaymentUtility clone = cloneWithoutID();
        clone.setRecId(this.getRecId());
        return clone;
    }

    @Override
    public void copyFrom(EditorPanelable other) {
        PaymentUtility otherPayment = (PaymentUtility) other;
        utilityProperty().set(otherPayment.utilityProperty().get());
        setParentRecId(otherPayment.getParentRectId());
        setProcessId(otherPayment.getProcessId());
        setDocDate(otherPayment.getDocDate());
        setDocInDocDate(otherPayment.getDocInDocDate());
        setIso(otherPayment.getIso());
        setDebitId(otherPayment.getDebitId());
        setCreditId(otherPayment.getCreditId());
        setAmount(otherPayment.getAmount());
        setDocCode(otherPayment.getDocCode());
        setDescrip(otherPayment.getDescrip());
        setDocType(otherPayment.getDocType());
        setOwnerId(otherPayment.getOwnerId());
    }

    @Override
    public boolean compares(EditorPanelable backup) {
        PaymentUtility other = (PaymentUtility) backup;
        return  utilityProperty().get().getRecId() == other.utilityProperty().get().getRecId() &&
                getParentRectId() == other.getParentRectId() &&
                getProcessId() == other.getProcessId() &&
                docDate.get().equals(other.docDateProperty().get()) &&
                docInDocDate.get().equals(other.docInDocDateProperty().get()) &&
                getIso().equals(other.getIso()) &&
                getDebitId() == other.getDebitId() &&
                getCreditId() == other.getCreditId() &&
                getAmount().equals(other.getAmount()) &&
                getDescrip().equals(other.getDescrip()) &&
                getDocCode().equals(other.getDocCode()) &&
                getDocType() == other.getDocType() &&
                getOwnerId() == other.getOwnerId();
    }
    
    @Override
    public String toStringForSearch() {
        return ""; // This object is not in table list, so the search string value does not metter.
    }

    
}
