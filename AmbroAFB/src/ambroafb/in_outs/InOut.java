/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.in_outs;

import ambro.AFilterableTreeTableView;
import ambro.AView;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.general.interfaces.TableColumnFeatures;
import ambroafb.general.interfaces.TreeItemable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Objects;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author dkobuladze
 */
public class InOut extends EditorPanelable implements TreeItemable {

    @AView.Column(title = "%descrip", width = "360")
    private final StringProperty descrip;
    
    @AView.Column(title = "%amount", width = "70", styleClass = TableColumnFeatures.Style.TEXT_RIGHT)
    private final IntegerProperty balAcc;
    
    @AView.Column(title = "%amount", width = TableColumnFeatures.Width.MONEY, styleClass = TableColumnFeatures.Style.TEXT_RIGHT)
    private final FloatProperty amount;
    
    private int parentRecId, level;
    
    @AFilterableTreeTableView.Children
    private final ObservableList<InOut> children = FXCollections.observableArrayList();
    
    public InOut(){
        descrip = new SimpleStringProperty();
        balAcc = new SimpleIntegerProperty();
        amount = new SimpleFloatProperty();
    }
    
    public StringProperty descripProperty(){
        return descrip;
    }
    
    public IntegerProperty balAccProperty(){
        return balAcc;
    }
    
    public FloatProperty amountProperty(){
        return amount;
    }
    
    
    public String getDescrip(){
        return descrip.get();
    }
    
    public int getBalAcc(){
        return balAcc.get();
    }
    
    public float getAmount(){
        return amount.getValue();
    }
    
    public int getParentRecId(){
        return parentRecId;
    }
    
    public int getLevel(){
        return level;
    }
    
    
    public void setDescrip(String descrip){
        this.descrip.set(descrip);
    }
    
    public void setBalAcc(int balAcc){
        this.balAcc.setValue(balAcc);
    }
    
    public void setAmount(float amount){
        this.amount.setValue(amount);
    }
    
    public void setParentRecId(int parentRecId){
        this.parentRecId = parentRecId;
    }
    
    public void setLevel(int level){
        this.level = level;
    }
    
    
    @Override
    public InOut cloneWithoutID() {
        InOut clone = new InOut();
        clone.copyFrom(this);
        return clone;
    }

    @Override
    public InOut cloneWithID() {
        InOut clone = cloneWithoutID();
        clone.setRecId(this.getRecId());
        return clone;
    }

    @Override
    public void copyFrom(EditorPanelable other) {
        InOut otherInOut = (InOut) other;
        setDescrip(otherInOut.getDescrip());
        setBalAcc(otherInOut.getBalAcc());
        setAmount(otherInOut.getAmount());
        setParentRecId(otherInOut.getParentRecId());
        setLevel(otherInOut.getLevel());
    }

    @Override
    public boolean compares(EditorPanelable backup) {
        InOut other = (InOut) backup;
        return  Objects.equals(getDescrip(), other.getDescrip()) &&
                Objects.equals(getBalAcc(), other.getBalAcc()) &&
                Objects.equals(getAmount(), other.getAmount()) &&
                Objects.equals(getParentRecId(), other.getParentRecId()) &&
                Objects.equals(getLevel(), other.getLevel());
    }

    @Override
    public String toStringForSearch() {
        return "";
    }
    
    
    @Override @JsonIgnore
    public ObservableList<InOut> getChildren(){
        return children;
    }

    @Override @JsonIgnore
    public int getIdentificator() {
        return getRecId();
    }
    
    @Override @JsonIgnore
    public int getParentIdentificator() {
        return getParentRecId();
    }
}
