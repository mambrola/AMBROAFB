/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.balances;

import ambro.AFilterableTreeTableView;
import ambro.AView;
import ambroafb.general.Utils;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.general.interfaces.TableColumnFeatures;
import ambroafb.general.interfaces.TreeItemable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.paint.Color;
import javafx.util.Callback;

/**
 *
 * @author dkobuladze
 */
public class Balance extends EditorPanelable implements TreeItemable {

    private int accountId, balAcc, parentRecId, level;
    
    @AView.Column(title = "%descrip", width = "360")
    private final StringProperty descrip;
    
    @AView.Column(title = "%account", width = "100", styleClass = TableColumnFeatures.Style.TEXT_RIGHT)
    private final StringProperty accountIso;
    
    @AView.Column(title = "%active", width = TableColumnFeatures.Width.MONEY, styleClass = TableColumnFeatures.Style.TEXT_RIGHT, cellFactory = ActiveCellFactory.class)
    private final FloatProperty active;
    
    @AView.Column(title = "%passive", width = TableColumnFeatures.Width.MONEY, styleClass = TableColumnFeatures.Style.TEXT_RIGHT, cellFactory = PassiveCellFactory.class)
    private final FloatProperty passive;
    
    private boolean activeIsRed, passiveIsRed;
    
    @AFilterableTreeTableView.Children
    private final ObservableList<Balance> children = FXCollections.observableArrayList();
    
    @AView.RowStyles
    private final ObservableList<String> rowStyleClasses = FXCollections.observableArrayList();
    
    // StyleClasses are in TreeTableList.css file
    private static final String paleBackground = "pale_background";
    private static final String lightBackground = "light_background";
    private static final String mediumBackground = "medium_background";
    private static final String darkBackground = "dark_background";
    
    public Balance(){
        accountIso = new SimpleStringProperty("");
        descrip = new SimpleStringProperty("");
        active = new SimpleFloatProperty();
        passive = new SimpleFloatProperty();
    }
    
    // Properties getters:
    public StringProperty balAccountIsoProperty(){
        return accountIso;
    }
    
    public StringProperty descripProperty(){
        return descrip;
    }
    
    public FloatProperty activeProperty(){
        return active;
    }

    public FloatProperty passiveProperty(){
        return passive;
    }

    
    // Getters:
    public int getAccountId() {
        return accountId;
    }
    
    public Float getActive() {
        return active.get();
    }
    
    public boolean isActiveRed() {
        return activeIsRed;
    }

    public int getBalAcc() {
        return balAcc;
    }
    
    public String getAccountIso() {
        return accountIso.get();
    }
    
    public String getDescrip() {
        return descrip.get();
    }
    
    public Float getPassive() {
        return passive.get();
    }
    
    public boolean isPassiveRed() {
        return passiveIsRed;
    }

    public int getParentRecId() {
        return parentRecId;
    }

    public int getLevel() {
        return level;
    }
    
    
    // Setters:
    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }
    
    public void setActive(Float active){
        this.active.set(active);
    }
    
    public void setActiveIsRed(boolean activeIsRed) {
        this.activeIsRed = activeIsRed;
    }

    public void setBalAcc(int balAcc) {
        this.balAcc = balAcc;
    }
    
    public void setAccountIso(String balAccountIso){
        this.accountIso.set(balAccountIso);
    }
    
    public void setDescrip(String descrip){
        this.descrip.set(descrip);
    }

    public void setPassive(Float passive){
        this.passive.set(passive);
    }
    
    public void setPassiveIsRed(boolean passiveIsRed) {
        this.passiveIsRed = passiveIsRed;
    }
    
    public void setParentRecId(int parentRecId) {
        this.parentRecId = parentRecId;
    }

    public void setLevel(int level) {
        this.level = level;
        if (level == 1){
            rowStyleClasses.add(lightBackground);
        }
    }
    
    
    @Override
    public Balance cloneWithoutID() {
        Balance clone = new Balance();
        clone.copyFrom(this);
        return clone;
    }

    @Override
    public Balance cloneWithID() {
        Balance clone = cloneWithoutID();
        clone.setRecId(this.getRecId());
        return clone;
    }

    @Override
    public void copyFrom(EditorPanelable other) {
        Balance otherBalance = (Balance)other;
        setAccountId(otherBalance.getAccountId());
        setActive(otherBalance.getActive());
        setActiveIsRed(otherBalance.isActiveRed());
        setBalAcc(otherBalance.getBalAcc());
        setAccountIso(otherBalance.getAccountIso());
        setDescrip(otherBalance.getDescrip());
        setPassive(otherBalance.getPassive());
        setPassiveIsRed(otherBalance.isPassiveRed());
        setParentRecId(otherBalance.getParentRecId());
        setLevel(otherBalance.getLevel());
    }

    @Override
    public boolean compares(EditorPanelable backup) {
        Balance other = (Balance)backup;
        return  getAccountId() == other.getAccountId() &&
                Utils.avoidNullAndReturnFloat(getActive()).equals(Utils.avoidNullAndReturnFloat(other.getActive())) &&
                isActiveRed() == other.isActiveRed() &&
                getBalAcc() == other.getBalAcc() &&
                Utils.avoidNullAndReturnString(getAccountIso()).equals(Utils.avoidNullAndReturnString(other.getAccountIso())) &&
                Utils.avoidNullAndReturnString(getDescrip()).equals(Utils.avoidNullAndReturnString(other.getDescrip())) &&
                Utils.avoidNullAndReturnFloat(getPassive()).equals(other.getPassive()) &&
                isPassiveRed() == other.isPassiveRed() &&
                getParentRecId() == other.getParentRecId() &&
                getLevel() == other.getLevel();
    }

    @Override
    public String toStringForSearch() {
        return getDescrip() + " " + getBalAcc();
    }

    @Override
    public String toString() {
        return "Balance{" + "id: " + getRecId() + ", accountId=" + accountId + ", balAcc=" + balAcc + ", parentRecId=" + parentRecId + ", level=" + level + ", descrip=" + descrip.get() + ", accountIso=" + accountIso.get() + ", active=" + active.get() + ", passive=" + passive.get() + ", activeIsRed=" + activeIsRed + ", passiveIsRed=" + passiveIsRed + '}';
    }
    
    @Override @JsonIgnore
    public ObservableList<Balance> getChildren(){
        return children;
    }

    @Override
    public int getIdentificator() {
        return getRecId();
    }

    @Override
    public int getParentIdentificator() {
        return getParentRecId();
    }
    
    

    @JsonIgnore
    public boolean isAccount() {
        return getLevel() == 1;
    }
    
    @JsonIgnore
    public ObservableList<String> getRowStyles(){
        return rowStyleClasses;
    }
    
    
    public static class ActiveCellFactory implements Callback<TreeTableColumn<Balance, Float>, TreeTableCell<Balance, Float>> {

        @Override
        public TreeTableCell<Balance, Float> call(TreeTableColumn<Balance, Float> param) {
            return new TreeTableCell<Balance, Float>() {
                @Override
                protected void updateItem(Float item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty){
                        setGraphic(null);
                    }
                    else {
                        Balance b = getTreeTableRow().getItem();
                        if (b != null){
                            Label active = new Label("" + item.toString());
                            if (b.isActiveRed()) active.setTextFill(Color.RED);
                            setGraphic(active);
                        }
                    }
                }
                
            };
        }
        
    }
    
    
    public static class PassiveCellFactory implements Callback<TreeTableColumn<Balance, Float>, TreeTableCell<Balance, Float>> {

        @Override
        public TreeTableCell<Balance, Float> call(TreeTableColumn<Balance, Float> param) {
            return new TreeTableCell<Balance, Float>() {
                @Override
                protected void updateItem(Float item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty){
                        setGraphic(null);
                    }
                    else {
                        Balance b = getTreeTableRow().getItem();
                        if (b != null){
                            Label passive = new Label("" + item.toString());
                            if (b.isPassiveRed()) passive.setTextFill(Color.RED);
                            setGraphic(passive);
                        }
                    }
                }
                
            };
        }
        
    }
    
    
}
