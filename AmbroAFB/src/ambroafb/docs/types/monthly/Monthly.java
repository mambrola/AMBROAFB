/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.docs.types.monthly;

import ambroafb.docs.Doc;
import ambroafb.general.DateConverter;
import ambroafb.general.Utils;
import ambroafb.general.interfaces.EditorPanelable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 *
 * @author dkobuladze
 */
public class Monthly extends EditorPanelable {

    private final ObjectProperty<LocalDate> docDate;
    private List<Doc> docs;
    
    public Monthly(){
        docDate = new SimpleObjectProperty<>(LocalDate.now());
        docs = new ArrayList<>();
    }
    
    public ObjectProperty<LocalDate> docDateProperty(){
        return docDate;
    }
    
    // Getters:
    public String getDocDate(){
        return (docDate.get() == null) ? "" : docDate.get().toString();
    }
    
    @JsonIgnore
    public List<Doc> getDocs(){
        return docs;
    }
    
    // Setters:
    public void setDocDate(String docDate){
        this.docDate.set(DateConverter.getInstance().parseDate(docDate));
    }
    
    @JsonIgnore
    public void setDocs(List<Doc> docList){
        docs.clear();
        docs.addAll(docList);
    }
    
    
    @Override
    public Monthly cloneWithoutID() {
        Monthly clone = new Monthly();
        clone.copyFrom(this);
        return clone;
    }

    @Override
    public Monthly cloneWithID() {
        Monthly clone = cloneWithoutID();
        clone.setRecId(this.getRecId());
        return clone;
    }

    @Override
    public void copyFrom(EditorPanelable other) {
        Monthly otherMonthly = (Monthly) other;
        setDocDate(otherMonthly.getDocDate());
        setDocs(otherMonthly.getDocs());
    }

    @Override
    public boolean compares(EditorPanelable backup) {
        Monthly other = (Monthly) backup;
        return  docDate.get().equals(other.docDateProperty().get()) &&
                Utils.compareDocs(docs, other.getDocs());
    }

    @Override
    public String toStringForSearch() {
        return "";
    }

    
}
