/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general.editor_panel.in_out;

import ambroafb.general.GeneralConfig;
import ambroafb.general.editor_panel.EditorPanel;
import static ambroafb.general.editor_panel.balance.BalanceEditorPanel.NON_ZERO_CHECK_BOX_FXID;
import ambroafb.general.editor_panel.standard.StandardEditorPanel;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.in_outs.InOut;
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
public class InOutEditorPanel extends StandardEditorPanel {
    
    private Slider slider;
    private CheckBox nonZero;
    private final String nonZeroTextBundleKey = "non_zero_bal";

    private final int sliderMin = 25, sliderMax = 75, blockValue = 25;
    
    
    private IntegerProperty sliderValue;
    
    @Override
    protected void componentsInitialize(URL location, ResourceBundle resources) {
        removeComponents(EditorPanel.DELETE_FXID, EditorPanel.EDIT_FXID, EditorPanel.VIEW_FXID, EditorPanel.ADD_FXID, EditorPanel.SEARCH_FXID);
        
        // Initialize method call first than class instance variables initialization, so initializetion executes here:
        slider = new Slider();
        nonZero = new CheckBox();
        
        setComponentsFeatures();
        
        addComponent(1, slider);
        addComponent(2, nonZero);
        
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
    }
    
    public Predicate<EditorPanelable> getPredicate(){
        return (elem) -> nonZero.isSelected() ? ((InOut)elem).getAmount() > 0 : true;
    }
    
    public Observable[] getChangeableComponents(){
        return new Observable[] {nonZero.selectedProperty()};
    }
    
    
    /**
     *  The method return IntegerProerty for slider value. This s modification of slider real property that is DoubleProperty. So because of bind the IntegerProperty must not be change from outside (avoid to runtime exception).
     * @return IntegerProperty That is read only.
     */
    public IntegerProperty sliderValueProperty(){
        return sliderValue;
    }
}
