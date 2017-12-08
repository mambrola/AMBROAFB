/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general.editor_panel.balance;

import ambroafb.balances.Balance;
import ambroafb.general.GeneralConfig;
import ambroafb.general.editor_panel.EditorPanel;
import ambroafb.general.editor_panel.standard.StandardEditorPanel;
import ambroafb.general.interfaces.EditorPanelable;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;

/**
 *
 * @author dkobuladze
 */
public class BalanceEditorPanel extends StandardEditorPanel {

    public static final String NON_ZERO_CHECK_BOX_FXID = "nonZero";
    public static final String ONLY_BALLANCE_CHECK_BOX_FXID = "onlyBalance";
    
    private Slider slider;
    private CheckBox nonZero, onlyBalances;
    private final String nonZeroTextBundleKey = "non_zero_acc", onlyBalancesTextBundleKey = "only_balances";
    
    private final int sliderMin = 25, sliderMax = 100, blockValue = 25;
    
    private IntegerProperty sliderValue;
    
    @Override
    protected void componentsInitialize(URL location, ResourceBundle resources) {
        removeComponents(EditorPanel.DELETE_FXID, EditorPanel.EDIT_FXID, EditorPanel.VIEW_FXID, EditorPanel.ADD_FXID, EditorPanel.SEARCH_FXID);
    
        // Initialize method call first than class instance variables initialization, so initializetion executes here:
        slider = new Slider();
        nonZero = new CheckBox();
        onlyBalances = new CheckBox();

        setComponentsFeatures();
        
        addComponent(1, slider);
        addComponent(2, nonZero);
        addComponent(3, onlyBalances);
        
        sliderValue = new SimpleIntegerProperty();
        sliderValue.bind(Bindings.createIntegerBinding(() -> (int)(slider.getValue() / blockValue), slider.valueProperty()));
        
    }

    private void setComponentsFeatures(){
        slider.setMin(sliderMin);
        slider.setMax(sliderMax);
        slider.setValue(3 * blockValue);
        slider.setMajorTickUnit(blockValue);
        slider.setMinorTickCount(0);
        slider.setShowTickMarks(true);
        slider.setSnapToTicks(true);
        slider.valueProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            slider.setValue((newValue.intValue() / blockValue) * blockValue);
        });
        
        nonZero.setId(NON_ZERO_CHECK_BOX_FXID);
        nonZero.setText(GeneralConfig.getInstance().getTitleFor(nonZeroTextBundleKey));
        nonZero.setSelected(true);
        onlyBalances.setId(ONLY_BALLANCE_CHECK_BOX_FXID);
        onlyBalances.setText(GeneralConfig.getInstance().getTitleFor(onlyBalancesTextBundleKey));
    }
    
    
    public Predicate<EditorPanelable> getPredicate(){
        return (elem) -> {
            Balance balance = (Balance) elem;
            boolean nonZeroCond = true;
            if (nonZero.isSelected()){
                if (balance.isAccount()){
                    nonZeroCond = !balance.hasEmptyAmounts();
                } else if (balance.getActive() == 0 && balance.getPassive() == 0) {
                    for (Balance childBalance : balance.getChildren()) {
                        if (!childBalance.hasEmptyAmounts()) { // May balance amounts are 0, because of its children appropriate amounts sum is 0, but is not 0 seperately (for ex. -10    +10).
                            nonZeroCond = false;
                            break;
                        }
                    }
                }
            }
            boolean onlyBalsCond = onlyBalances.isSelected() ? !balance.isAccount() : true;
            return nonZeroCond && onlyBalsCond;
        };
    }
    
    public Observable[] getChangeableComponents(){
        return new Observable[] {nonZero.selectedProperty(), onlyBalances.selectedProperty()};
    }
    
    
    /**
     *  The method return IntegerProerty for slider value. This s modification of slider real property that is DoubleProperty. So because of bind the IntegerProperty must not be change from outside (avoid to runtime exception).
     * @return IntegerProperty That is read only.
     */
    public IntegerProperty sliderValueProperty(){
        return sliderValue;
    }
    
}
