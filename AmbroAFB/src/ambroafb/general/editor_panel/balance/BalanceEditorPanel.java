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
import javafx.beans.value.ObservableValue;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;

/**
 *
 * @author dkobuladze
 */
public class BalanceEditorPanel extends StandardEditorPanel {

    private Slider depthSlider;
    private CheckBox nonZero, onlyBalances;
//    private ADatePicker date;
//    private CurrencyComboBox currencies;
    private TextField search;
    
    private final int sliderMin = 25, sliderMax = 100, sliderValue = 50;
    
    @Override
    protected void componentsInitialize(URL location, ResourceBundle resources) {
        removeComponents(EditorPanel.DELETE_FXID, EditorPanel.EDIT_FXID, EditorPanel.VIEW_FXID, EditorPanel.ADD_FXID, EditorPanel.SEARCH_FXID);
    
        // Initialize method call first than class instance variables initialization, so initializetion executes here:
        depthSlider = new Slider();
        nonZero = new CheckBox();
        onlyBalances = new CheckBox();
//        date = new ADatePicker();
//        currencies = new CurrencyComboBox();
        search = new TextField();

        setComponentsFeatures();
        
        addComponent(2, depthSlider);
        addComponent(3, nonZero);
        addComponent(4, onlyBalances);
//        addComponent(5, date);
//        addComponent(6, currencies);
        addComponent(getChildren().size(), search); // adds after 'region' component.
    }

    private void setComponentsFeatures(){
        depthSlider.setMin(sliderMin);
        depthSlider.setMax(sliderMax);
        depthSlider.setValue(sliderValue);
        depthSlider.setMajorTickUnit(25);
        depthSlider.setMinorTickCount(0);
        depthSlider.setShowTickMarks(true);
        depthSlider.setSnapToTicks(true);
        depthSlider.valueProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            depthSlider.setValue((newValue.intValue() / 25) * 25);
        });
        
        nonZero.setText(GeneralConfig.getInstance().getTitleFor("non_zero_acc"));
        onlyBalances.setText(GeneralConfig.getInstance().getTitleFor("only_balances"));
        
//        currencies.fillComboBoxWithoutALL(null);
        
        search.setPromptText(GeneralConfig.getInstance().getTitleFor("search"));
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
}
