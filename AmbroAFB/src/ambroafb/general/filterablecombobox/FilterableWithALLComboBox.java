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

/**
 *
 * @author dato
 * @param <T>
 */
public class FilterableWithALLComboBox<T> extends ComboBox<T> {

    public final String ALL = "ALL";
    
    private boolean hasFilterableData = true;
    private ObservableList<T> items = FXCollections.observableArrayList();
    private FilteredList<T> filteredItems;
    private SingleSelectionModel selectionModelForReal, selectionModelForFilterable;
    private T itemCategoryALL;
//    private Predicate predicate;
    
    
    public FilterableWithALLComboBox() {
//        getEditor().setStyle("-fx-prompt-text-fill: black; -fx-background-radius: 2 0 0 2, 2 0 0 2;");
        

        valueProperty().addListener((ObservableValue<? extends T> observable, T oldValue, T newValue) -> {
            if (oldValue != null){
                
            }
            
            //System.out.println("value change. hasFilterableData: " + hasFilterableData);
//            if (hasFilterableData){
//                hasFilterableData = false;
//                setSelectionModel(selectionModelForReal);
//                setItems(items);
//            }
        });
        
//        getEditor().textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
//            if (newValue != null && !newValue.isEmpty()) {
//                System.out.println("text Change. !hasFilterableData: " + (!hasFilterableData) + " getValue: " + getValue());
////                if (!hasFilterableData && getValue() == null) {
////                    hasFilterableData = true;
////                    System.out.println("text change. daeseteba mnishvnelobebi...");
////                    setItems(filteredItems);
////                    setSelectionModel(selectionModelForFilterable);
////                }
//            }
//        });
        
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
        itemCategoryALL = data.get(0);
        filteredItems = new FilteredList(data);
        filteredItems.predicateProperty().bind(Bindings.createObjectBinding(() -> {
            if (getEditor().getText() == null || getEditor().getText().isEmpty()){
                return null;
            }
            return predicate;
        }, getEditor().textProperty()));
        
        this.setItems(filteredItems);
//        selectionModelForReal = new CustomSelection(items);
        selectionModelForFilterable = new CustomSelection(filteredItems);
        setSelectionModel(selectionModelForFilterable);
    }
    
    /**
     * If 'show' is true -  category 'ALL' will show in comboBox and also selected.
     * If 'show' is false - category 'ALL' will not show in comboBox and comboBox value will be null.
     * @param show The parameter provides show or hide category 'ALL' in comboBox.
     */
    public void showCategoryALL(boolean show){
        if (show){
            if (!getItems().contains(itemCategoryALL)){
                items.add(0, itemCategoryALL);
                setValue(itemCategoryALL);
            }
        }
        else {
            if (getItems().contains(itemCategoryALL)){
                items.remove(0);
                setValue(null);
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
            if (data == null || data.isEmpty() || index < 0) return null;
            return data.get(index);
        }

        @Override
        protected int getItemCount() {
            System.out.println("data size: " + data.size());
            if (data == null || data.isEmpty()) return 0;
            return data.size();
        }
    }
    
}
