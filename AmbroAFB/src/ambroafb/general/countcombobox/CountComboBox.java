/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general.countcombobox;

import com.sun.javafx.scene.control.skin.ComboBoxListViewSkin;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author dato
 */
public class CountComboBox extends ComboBox<CountComboBoxItem> {
    
    private static final int defaultWidth = 500;
    
    private Map<CountComboBoxItem, Integer> data = new HashMap<>();
    private final Map<String, CountComboBoxDrawItem> drawItems = new HashMap<>();
    private boolean isViewableState = false;
    private String viewableCSSFile = "/styles/css/countcomboboxviewable.css";
    private final Tooltip tooltip = new Tooltip();;
    
    public CountComboBox(){
        this.setPrefWidth(defaultWidth);
        this.setTooltip(tooltip);
        
        setCellFactory((ListView<CountComboBoxItem> param) -> new CustomCell());
        setButtonCell(new CustomButtonCell());
        
        // Never hide comboBox items listView:
        this.setSkin(new ComboBoxListViewSkin(this){
            @Override
            protected boolean isHideOnClickEnabled(){
                return false;
            }
        });
        
        addKeyListeners();
    }
    
    private void addKeyListeners(){
        KeyCode plus = KeyCode.ADD;
        KeyCode minus = KeyCode.SUBTRACT;
        setOnKeyPressed((KeyEvent event) -> {
            KeyCode eventKey = event.getCode();
            CountComboBoxItem selectedItem = getSelectionModel().getSelectedItem();
            if (selectedItem == null) return;
            CountComboBoxDrawItem selectedDrawItem = drawItems.get(selectedItem.getUniqueIdentifier());
            
            if (eventKey.equals(plus)){
                selectedDrawItem.increaseNumberBy(1);
            }
            else if (eventKey.equals(minus)){
                selectedDrawItem.decreaseNumberBy(1);
            }
            refreshButtonCell();
        });
    }
    
    public void setData(Map<CountComboBoxItem, Integer> data){
        this.data = data;
        
        data.keySet().forEach((item) -> {
            CountComboBoxDrawItem drawItem = convertIntoDrawItem(item);
            drawItem.numberProperty().set(data.get(item));
            drawItems.put(item.getUniqueIdentifier(), drawItem);
        });
        
        // ComboBox button cell text will show after any item select in comboBox. 
        // So we select every item. The last selected item will be zero indexed (only for visually effect).
        for (int i = getItems().size() - 1; i >= 0; i--){
            getSelectionModel().select(i);
        }
    }
    
    private CountComboBoxDrawItem convertIntoDrawItem(CountComboBoxItem item) {
        String name = item.getDrawableName();
        CountComboBoxDrawItem drawItem = new CountComboBoxDrawItem(name);
        return drawItem;
    }
    
    private void refreshButtonCell() {
        int selected = getSelectionModel().getSelectedIndex();
        if (selected == getItems().size() - 1) {
            getSelectionModel().selectPrevious();
            getSelectionModel().selectNext();
        } else {
            getSelectionModel().selectNext();
            getSelectionModel().selectPrevious();
        }
    }
    
    // Returns items that count != 0.
    public Map<CountComboBoxItem, Integer> getData(){
        return data;
    }
    
    public void changeState(boolean isViewable){
        isViewableState = isViewable;
        if (isViewable){
            getStylesheets().add(viewableCSSFile);
        }
        else {
            getStylesheets().remove(viewableCSSFile);
        }
    }
    
    public boolean nothingIsSelected(){
        return data.isEmpty();
    }
    
    
    private void printData() {
        data.keySet().stream().forEach((itm) -> {
            System.out.println("name: " + itm.getDrawableName());
        });
        System.out.println("");
    }
    
    // Private class
    private class CustomCell extends ListCell<CountComboBoxItem> {

        public CustomCell() {
            super();
        }

        @Override
        public void updateItem(CountComboBoxItem item, boolean empty) {
            super.updateItem(item, empty);
            if (item != null) {
                // make all draw items newly (to avoid difference width for draw items):
                CountComboBoxDrawItem drawItem = convertIntoDrawItem(item);
                if (drawItems.containsKey(item.getUniqueIdentifier())){
                    int itemCounter = drawItems.get(item.getUniqueIdentifier()).numberProperty().get();
                    drawItem.numberProperty().set(itemCounter);
                }
                drawItem.setViewableState(isViewableState);
                drawItems.put(item.getUniqueIdentifier(), drawItem);
                
                drawItem.numberProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
                    List<CountComboBoxItem> itemsInData = data.keySet().stream().filter((CountComboBoxItem itm) -> itm.getUniqueIdentifier().equals(item.getUniqueIdentifier())).collect(Collectors.toList());
                    CountComboBoxItem currItem = (itemsInData.isEmpty()) ? item : itemsInData.get(0);
                    
                    if (newValue.intValue() <= 0){
                        data.remove(currItem);
                    }
                    else {
                        data.put(currItem, newValue.intValue());
                    }
                });
                
                // This is need for immediately change button cell when counter is changed:
                drawItem.addEventHandler(MouseEvent.MOUSE_RELEASED, (MouseEvent event) -> {
                    refreshButtonCell();
                });
                setGraphic(drawItem);
            }
        }
        
    }
    
    
    // private button cell
    private class CustomButtonCell extends ListCell<CountComboBoxItem> {
        
        private final String delimiter = ", ";
        
        public CustomButtonCell() {
            super();
        }

        @Override
        public void updateItem(CountComboBoxItem item, boolean empty) {
            super.updateItem(item, empty);
            if (item != null) {
                String title = "";
                for (CountComboBoxItem boxItem : getItems()) {
                    String identifier = boxItem.getUniqueIdentifier();
                    if (drawItems.containsKey(identifier)) {
                        CountComboBoxDrawItem boxDrawItem = drawItems.get(identifier);
                        title = title.concat(boxDrawItem.nameExpression().getValueSafe());
                        if (!boxDrawItem.nameExpression().getValueSafe().isEmpty()){
                            title = title.concat(delimiter);
                        }
                    }
                }
                title = StringUtils.substringBeforeLast(title, delimiter);
                setText(title);
                tooltip.setText(title);
            }
        }
    }
}
