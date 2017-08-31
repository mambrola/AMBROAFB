/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.docs.types.utilities.payment;

import ambroafb.docs.DocMerchandise;
import ambroafb.general.DateConverter;
import ambroafb.general.interfaces.EditorPanelable;
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

//    private final String default_iso = "GEL";
    
    private final ObjectProperty<LocalDate> docDate;
    private final ObjectProperty<LocalDate> docInDocDate;
    private final ObjectProperty<DocMerchandise> utility;
    private final StringProperty iso, amount, descrip;
    
    
    public PaymentUtility(){
        // By the time "bindBidirectional" the value of brackets object will be set to left side object, of binding. So the default value must set this object.
        docDate = new SimpleObjectProperty<>(LocalDate.now()); 
        docInDocDate = new SimpleObjectProperty<>(LocalDate.now());
        
        utility = new SimpleObjectProperty<>();
        iso = new SimpleStringProperty("");
        amount = new SimpleStringProperty("");
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
    
    public StringProperty descripProperty() {
        return descrip;
    }
    
    // Getters:
    public String getUtility(){
        return (utility.get() == null) ? "" : utility.get().getDescrip();
    }
    
    public String getDocDate(){
        return (docDate.get() == null) ? "" : docDate.get().toString();
    }
    
    public String getDocInDocDate(){
        return (docInDocDate.get() == null) ? "" : docInDocDate.get().toString();
    }
    
    public String getIso(){
        return iso.get();
    }
    
    public String getAmount(){
        return amount.get();
    }
    
    public String getDescrip(){
        return descrip.get();
    }
    
    // Setters:
    public void setUtility(String utility){
        if (this.utility.get() != null){
            this.utility.get().setDescrip(utility);
        }
    }
    
    public void setDocDate(String date){
        this.docDate.set(DateConverter.getInstance().parseDate(date));
    }
    
    public void setDocInDocDate(String date){
        this.docInDocDate.set(DateConverter.getInstance().parseDate(date));
    }
    
    public void setIso(String iso){
        this.iso.set(iso);
    }
    
    public void setAmount(String amount){
        this.amount.set(amount);
    }
    
    public void setDescrip(String assign){
        this.descrip.set(assign);
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
        setUtility(otherPayment.getUtility());
        setDocDate(otherPayment.getDocDate());
        setDocInDocDate(otherPayment.getDocInDocDate());
        setIso(otherPayment.getIso());
        setAmount(otherPayment.getAmount());
        setDescrip(otherPayment.getDescrip());
    }

    @Override
    public boolean compares(EditorPanelable backup) {
        PaymentUtility other = (PaymentUtility) backup;
        return  getUtility().equals(other.getUtility()) &&
                getDocDate().equals(other.getDocDate()) &&
                getDocInDocDate().equals(other.getDocInDocDate()) &&
                getIso().equals(other.getIso()) &&
                getAmount().equals(other.getAmount()) &&
                getDescrip().equals(other.getDescrip());
    }

    @Override
    public String toStringForSearch() {
        return ""; // This object is not in table list, so the search string value does not metter.
    }



    
}
