/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general.filterablecombobox;

import java.util.function.Predicate;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.SingleSelectionModel;
import javafx.util.StringConverter;

/**
 *
 * @author dato
 * @param <T>
 */
public class FilterableWithALLComboBox<T> extends ComboBox<T> {

    public final String ALL = "ALL";
    
    private boolean hasFilterableData = false;
    private ObservableList<T> items = FXCollections.observableArrayList();
    private FilteredList<T> filteredItems;
    SingleSelectionModel selectionModelForReal, selectionModelForFilterable;
//    private Predicate predicate;
    
    
    public FilterableWithALLComboBox() {
        
        valueProperty().addListener((ObservableValue<? extends T> observable, T oldValue, T newValue) -> {
            System.out.println("value change. hasFilterableData: " + hasFilterableData);
            if (hasFilterableData){
                hasFilterableData = false;
                setSelectionModel(selectionModelForReal);
                setItems(items);
            }
        });
        
        getEditor().textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (newValue != null && !newValue.isEmpty()) {
                System.out.println("text Change. !hasFilterableData: " + (!hasFilterableData) + " getValue: " + getValue());
                if (!hasFilterableData && getValue() == null) {
                    hasFilterableData = true;
                    System.out.println("text change. daeseteba mnishvnelobebi...");
                    setItems(filteredItems);
                    setSelectionModel(selectionModelForFilterable);
                }
            }
        });
        
//        getSelectionModel().selectedIndexProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
//            System.out.println("index selection listener: new Value: " + newValue);
//        });
        
        // show comboBox popup when user enter characters in editable textfield (press 'enter' or esc for hide):
//        addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
//            KeyCode inputKeyCode = event.getCode();
//            if (inputKeyCode.equals(KeyCode.BACK_SPACE) || inputKeyCode.equals(KeyCode.DELETE)){
//                if (getValue() != null){ getSelectionModel().select(-1); }
//            }
//            if (!isShowing()){
//                show();
//            }
//            if (event.getCode().equals(KeyCode.ENTER)){
//                System.out.println("enteeeeer....");
//            }
//        });
        
        this.setEditable(true);
    }
    
    /**
     * The method gives data for filterable. This list will be use for comboBox items.
     * Note: The method show only subclasses.
     * @param data The data that must be filterable. Also it will be comboBox items.
     * @param predicate The boolean logic for filter data. If predicate return true for current item,
     *                      the item will show on filterable list.
     */
    protected final void setDataForFilterable(ObservableList<T> data, Predicate<T> predicate){
        items = data;
        filteredItems = new FilteredList(data);
        filteredItems.predicateProperty().bind(Bindings.createObjectBinding(() -> {
            if (getEditor() == null || getEditor().getText().isEmpty()){
                return null;
            }
            return predicate;
        }, getEditor().textProperty()));
        
        this.setItems(items);
        selectionModelForReal = new CustomSelection(items);
        selectionModelForFilterable = new CustomSelection(filteredItems);
    }
    
    public void showCategoryAll(boolean show, T all){ // ?? filtered list tua getItems ???
        if (!show){
            if (getItems().contains(all)){
                getItems().remove(0);
            }
        }
        else {
            if (!getItems().contains(all)){
                getItems().add(0, all);
            }
        }
    }

    
    private class CustomSelection extends SingleSelectionModel<T> {

        private final ObservableList<T> data;
        
        public CustomSelection(ObservableList<T> items){
            data = items;
        }
        
        @Override
        protected T getModelItem(int index) {
            System.out.println("index: " + index);
            if (data == null || data.isEmpty()) return null;
            return data.get(index);
        }

        @Override
        protected int getItemCount() {
            System.out.println("data size: " + data.size());
            if (data == null || data.isEmpty()) return 0;
            return data.size();
        }
    }
    
    private class DefaultConverter<T> extends StringConverter<T> {

        @Override
        public String toString(T objectInput) {
            return (objectInput == null) ? null : objectInput.toString();
        }

        @Override
        public T fromString(String string) {
            return null;
        }

    }
    
}
