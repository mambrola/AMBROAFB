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
import com.fasterxml.jackson.annotation.JsonIgnore;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.text.Font;
import javafx.util.Callback;

/**
 *
 * @author dkobuladze
 */
public class Balance extends EditorPanelable {

    private int accountId, balAcc, parentRecId, level;
    
    @AView.Column(title = "%descrip", width = "360")
    private final StringProperty descrip;
    
    @AView.Column(title = "%account", width = "100", styleClass = TableColumnFeatures.Style.TEXT_RIGHT)
    private final StringProperty accountIso;
    
    @AView.Column(title = "%active", width = "100", styleClass = TableColumnFeatures.Style.TEXT_RIGHT)
    private final FloatProperty active;
    
    @AView.Column(title = "%passive", width = "100", styleClass = TableColumnFeatures.Style.TEXT_RIGHT)
    private final FloatProperty passive;
    
    private boolean activeIsRed, passiveIsRed;
    
    @AFilterableTreeTableView.Children
    private final ObservableList<Balance> children = FXCollections.observableArrayList();
    
    @AView.RowStyles
    private final ObservableList<String> rowStyleClasses = FXCollections.observableArrayList();
    
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
    
    @JsonIgnore
    public ObservableList<Balance> getChildren(){
        return children;
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

    @JsonIgnore
    public boolean isAccount() {
        return getLevel() == 1;
    }
    
    @JsonIgnore
    public ObservableList<String> getRowStyles(){
        return rowStyleClasses;
    }
    
    private Font defaultFont;
    
    @JsonIgnore
    public Font getDefaultFont(){
        return defaultFont;
    }
    
    @JsonIgnore
    public void saveDefaultFont(Font font){
        defaultFont = font;
    
    }

    
    /**
     * 
     */
    public static class MoneyCellFactory implements Callback<TreeTableColumn<Balance, Float>, TreeTableCell<Balance, Float>> {

        @Override
        public TreeTableCell<Balance, Float> call(TreeTableColumn<Balance, Float> param) {
            return new TreeTableCell<Balance, Float>(){
                @Override
                protected void updateItem(Float money, boolean empty) {
                    super.updateItem(money, empty);
                    if (money == null || empty){
                        // Removes add style classes if exists (it is implemented in reomve method). In here appropriate balance object is null.
//                        getTreeTableRow().getStyleClass().remove(lightBackground);
//                        getTreeTableRow().getStyleClass().remove(mediumBackground);
//                        getTreeTableRow().getStyleClass().remove(darkBackground);
//                        setGraphic(null);

                        setText(null);
                    }
                    else {
                        Balance b = getTreeTableRow().getItem();
                        if (b != null) {
                            int level = b.getLevel();
//                            String styleClassForColor = getBackgroundColorClassBy(level);
//                            b.getRowStyles().add(styleClassForColor);
                            String moneyText = "" + money;
//                            for (int i = 0; i < level; i++) {
//                                monyText += " ";
//                            }
//                            Label label = new Label(moneyText);
//                            System.out.println("label default font: " + label.getFont());
//                            double fontSize = getCoeficientFor(b.getLevel()) * label.getFont().getSize();
//                            label.setFont(Font.font(fontSize));
//                            System.out.println("label change font: " + label.getFont() + "\n");
//                            setGraphic(label);

                            setText(moneyText);
                            if (b.getDefaultFont() == null){
                                b.saveDefaultFont(getFont());
                            }
                            double fontSize = getCoeficientFor(b.getLevel()) * b.getDefaultFont().getSize();
                            setFont(Font.font(fontSize));
                        }
                    }
                }
              
            };
        }
        
        private String getBackgroundColorClassBy(int level){
            String color;
            switch(level){
                case 1:
                    color = lightBackground;
                    break;
                case 2:
                    color = mediumBackground;
                    break;
                case 3:
                    color = darkBackground;
                    break;
                default:
                    color = paleBackground;
            }
            return color;
        }
        
        private double getCoeficientFor(int level){
            switch(level){
                case 1:
                    return 1.1;
                case 2:
                    return 1.3;
                case 3:
                    return 1.5;
                default:
                    return 1;
            }
        }
    }
}
