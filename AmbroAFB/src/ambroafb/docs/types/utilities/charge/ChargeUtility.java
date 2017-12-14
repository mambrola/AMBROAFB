/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.docs.types.utilities.charge;

import ambroafb.docs.Doc;
import ambroafb.docs.DocMerchandise;
import ambroafb.general.DateConverter;
import ambroafb.general.NumberConverter;
import ambroafb.general.interfaces.EditorPanelable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author dkobuladze
 */
public class ChargeUtility extends EditorPanelable {

    private final ObjectProperty<LocalDate> docDate;
    private final ObjectProperty<LocalDate> docInDocDate;
    private final ObjectProperty<DocMerchandise> utility;
    private final StringProperty iso, amount, docCode, descrip, vat;
    private int parentRecId, processId, debitId, creditId, docType, ownerId = -1;

    private final String docCodeValue = "accrual";
    private final float amountDefaultValue = -1;
    private final float vatDefaultValue = -1;

    public ChargeUtility() {
        // By the time "bindBidirectional" the value of brackets object will be set to left side object, of binding. So the default value must set this object.
        docDate = new SimpleObjectProperty<>(LocalDate.now());
        docInDocDate = new SimpleObjectProperty<>(getLastDateOfMonth());

        utility = new SimpleObjectProperty<>(new DocMerchandise());
        iso = new SimpleStringProperty("");
        amount = new SimpleStringProperty("");
        docCode = new SimpleStringProperty(docCodeValue); // when ADD form is open.
        descrip = new SimpleStringProperty("");
        vat = new SimpleStringProperty("");
    }

    private LocalDate getLastDateOfMonth() {
        Calendar c = Calendar.getInstance();
        Date currDate = new Date();
        c.setTime(currDate);
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        Instant instant = Instant.ofEpochMilli(c.getTimeInMillis());
        LocalDateTime dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        return dateTime.toLocalDate();
    }

    public ObjectProperty<DocMerchandise> merchandiseProperty() {
        return utility;
    }

    public ObjectProperty<LocalDate> docDateProperty() {
        return docDate;
    }

    public ObjectProperty<LocalDate> docInDocDateProperty() {
        return docInDocDate;
    }

    public StringProperty isoProperty() {
        return iso;
    }

    public StringProperty amountProperty() {
        return amount;
    }

    public StringProperty docCodeProperty() {
        return docCode;
    }

    public StringProperty descripProperty() {
        return descrip;
    }

    public StringProperty vatProperty() {
        return vat;
    }

    // Getters:
    public String getDocDate() {
        return (docDate.get() == null) ? "" : docDate.get().toString();
    }

    public String getDocInDocDate() {
        return (docInDocDate.get() == null) ? "" : docInDocDate.get().toString();
    }

    public String getIso() {
        return iso.get();
    }

    public Float getAmount() {
        return NumberConverter.stringToFloat(amount.get(), 2, amountDefaultValue);
    }

    public String getDocCode() {
        return docCode.get();
    }

    @JsonIgnore
    public String getDescrip() {
        return descrip.get();
    }

    @JsonIgnore
    public int getParentRectId() {
        return parentRecId;
    }

    @JsonIgnore
    public int getProcessId() {
        return processId;
    }

    @JsonIgnore
    public int getDebitId() {
        return debitId;
    }

    @JsonIgnore
    public int getCreditId() {
        return creditId;
    }

    @JsonIgnore
    public int getDocType() {
        return docType;
    }

    @JsonIgnore
    public int getOwnerId() {
        return ownerId;
    }

    public Float getVat() {
        return NumberConverter.stringToFloat(vat.get(), 2, vatDefaultValue);
    }

    
    // Setters:
    public void setDocDate(String date) {
        this.docDate.set(DateConverter.getInstance().parseDate(date));
    }

    public void setDocInDocDate(String date) {
        this.docInDocDate.set(DateConverter.getInstance().parseDate(date));
    }

    public void setIso(String iso) {
        this.iso.set(iso);
    }

    public void setAmount(Float amount) {
        this.amount.set(NumberConverter.convertNumberToStringBySpecificFraction(amount, 2));
    }

    public void setDocCode(String docCode) {
        this.docCode.set(docCode);
    }

    @JsonProperty
    public void setDescrip(String descrip) {
        this.descrip.set(descrip);
    }

    @JsonProperty
    public void setParentRecId(int parentRecId) {
        this.parentRecId = parentRecId;
    }

    @JsonProperty
    public void setProcessId(int processId) {
        this.processId = processId;
    }

    @JsonProperty
    public void setDebitId(int debitId) {
        this.debitId = debitId;
    }

    @JsonProperty
    public void setCreditId(int creditId) {
        this.creditId = creditId;
    }

    @JsonProperty
    public void setDocType(int docType) {
        this.docType = docType;
    }

    @JsonProperty
    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public void setVat(Float vat) {
        this.vat.set(NumberConverter.convertNumberToStringBySpecificFraction(vat, 2));
    }
    
    
    @JsonIgnore
    public List<Doc> convertToDoc(){
        Doc parent = new Doc();
        Doc child = new Doc();
        List<Doc> docs = new ArrayList<>();
        docs.add(parent);
        docs.add(child);
        
        parent.setDocDate(getDocDate());
        parent.setDocInDocDate(getDocInDocDate());
        parent.setIso(getIso());
        parent.setAmount(getAmount());
        parent.setDocCode(getDocCode());
        parent.setDescrip(getDescrip());
        parent.setParentRecId(getParentRectId());
        parent.setProcessId(getProcessId());
        parent.setDebitId(getDebitId());
        parent.setCreditId(getCreditId());
        parent.setDocType(getDocType());
        parent.setOwnerId(getOwnerId());
        child.copyFrom(parent);
        
        parent.setRecId(getRecId());
        child.setParentRecId(parent.getRecId());
        child.setAmount(getVat());
        
        return docs;
    }

    @Override
    public ChargeUtility cloneWithoutID() {
        ChargeUtility clone = new ChargeUtility();
        clone.copyFrom(this);
        return clone;
    }

    @Override
    public ChargeUtility cloneWithID() {
        ChargeUtility clone = cloneWithoutID();
        clone.setRecId(this.getRecId());
        return clone;
    }

    @Override
    public void copyFrom(EditorPanelable other) {
        ChargeUtility otherCharge = (ChargeUtility) other;
        merchandiseProperty().set(otherCharge.merchandiseProperty().get());
        setParentRecId(otherCharge.getParentRectId());
        setProcessId(otherCharge.getProcessId());
        setDocDate(otherCharge.getDocDate());
        setDocInDocDate(otherCharge.getDocInDocDate());
        setIso(otherCharge.getIso());
        setDebitId(otherCharge.getDebitId());
        setCreditId(otherCharge.getCreditId());
        setAmount(otherCharge.getAmount());
        setDocCode(otherCharge.getDocCode());
        setDescrip(otherCharge.getDescrip());
        setDocType(otherCharge.getDocType());
        setOwnerId(otherCharge.getOwnerId());
        setVat(otherCharge.getVat());
    }

    @Override
    public boolean compares(EditorPanelable backup) {
        ChargeUtility other = (ChargeUtility) backup;
        return merchandiseProperty().get().getRecId() == other.merchandiseProperty().get().getRecId()
                && getParentRectId() == other.getParentRectId()
                && getProcessId() == other.getProcessId()
                && docDate.get().equals(other.docDateProperty().get())
                && docInDocDate.get().equals(other.docInDocDateProperty().get())
                && getDebitId() == other.getDebitId()
                && getCreditId() == other.getCreditId()
                && getIso().equals(other.getIso())
                && getAmount().equals(other.getAmount())
                && getDocCode().equals(other.getDocCode())
                && getDescrip().equals(other.getDescrip())
                && getDocType() == other.getDocType()
                && getOwnerId() == other.getOwnerId()
                && getVat().equals(other.getVat());
    }

    @Override
    public String toStringForSearch() {
        return "";
    }

}
