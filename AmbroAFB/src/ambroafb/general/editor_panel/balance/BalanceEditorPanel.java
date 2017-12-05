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

    private Slider slider;
    private CheckBox nonZero, onlyBalances;
    
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
        
        sliderValue.addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            System.out.println("change: aaaaaa");
        });
    }

    private void setComponentsFeatures(){
        slider.setMin(sliderMin);
        slider.setMax(sliderMax);
        slider.setMajorTickUnit(blockValue);
        slider.setMinorTickCount(0);
        slider.setShowTickMarks(true);
        slider.setSnapToTicks(true);
        slider.valueProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            slider.setValue((newValue.intValue() / blockValue) * blockValue);
        });
        
        nonZero.setText(GeneralConfig.getInstance().getTitleFor("non_zero_acc"));
        onlyBalances.setText(GeneralConfig.getInstance().getTitleFor("only_balances"));
    }
    
    
    public Predicate<EditorPanelable> getPredicate(){
        return (elem) -> {
            Balance balance = (Balance) elem;
            boolean nonZeroCond = nonZero.isSelected() ? balance.getActive() > 0 || balance.getPassive() > 0 : true;
            boolean onlyBalsCond = onlyBalances.isSelected() ? balance.isAccount() : true;
            return nonZeroCond && onlyBalsCond; // შეიძლება Fluent API_ის და Factory Method_ის გაერთიანებით უკეთესი ჩანაწერი გამოვიდეს
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
