/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general.filterablecombobox;

import ambro.AFilterableTableViewComboBox;
import java.util.function.Function;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.json.JSONObject;

/**
 *
 * @author dato
 * @param <T>
 */
public class FilterableWithALLComboBox<T> extends AFilterableTableViewComboBox<T> {

    public final String ALL = "ALL";
    
    private ObservableList<T> items = FXCollections.observableArrayList();
    private T itemCategoryALL;
    
    public FilterableWithALLComboBox() {
        super();
    }
    
    public void registerCategoryALL(T appropObj){
        itemCategoryALL = appropObj;
    }
    
    public void setDataItems(ObservableList<T> items, Function<T, String> filterableContent){
        this.items = items;
        super.setItems(items, filterableContent);
    }
    
    public ObservableList<T> getDataItems(){
        return items;
    }
    
    public void setColumnWidthes(JSONObject json) {
        super.setColumnsWidth(json);
    }
    
    
    /**
     * If 'show' is true -  category 'ALL' will show in comboBox and also selected.
     * If 'show' is false - category 'ALL' will not show in comboBox and comboBox value will be null.
     * @param show The parameter provides show or hide category 'ALL' in comboBox.
     */
    public void showCategoryALL(boolean show){
        if (show){
            if (!items.contains(itemCategoryALL)){
                items.add(0, itemCategoryALL);
                super.selectItem(itemCategoryALL);
                //setValue(itemCategoryALL);
            }
        }
        else {
            if (items.contains(itemCategoryALL)){
                items.remove(0);
                super.selectItem(null);
                //setValue(null);
            }
        }
    }
    
    /**
     * The method gives data for filterable. This list will be use for comboBox items.
     * Note: The method show only subclasses.
     * @param data The data that must be filterable. Also it will be comboBox items.
     * @param predicate The boolean logic for filter data. If predicate return true for current item,
     *                      the item will show on filterable list.
     */
//    protected final void setDataForFilterable(ObservableList<T> data, Predicate<T> predicate){
//        items = data;
//        itemCategoryALL = data.get(0);
//        filteredItems = new FilteredList(data);
//        filteredItems.predicateProperty().bind(Bindings.createObjectBinding(() -> {
//            if (getEditor().getText() == null || getEditor().getText().isEmpty()){
//                return null;
//            }
//            return predicate;
//        }, getEditor().textProperty()));
//        
//        this.setItems(filteredItems);
////        selectionModelForReal = new CustomSelection(items);
//        selectionModelForFilterable = new CustomSelection(filteredItems);
//        setSelectionModel(selectionModelForFilterable);
//    }
    
    
//    private class CustomSelection extends SingleSelectionModel<T> {
//
//        private final ObservableList<T> data;
//        
//        public CustomSelection(ObservableList<T> items){
//            data = items;
//        }
//        
//        @Override
//        protected T getModelItem(int index) {
//            System.out.println("index: " + index);
//            if (data == null || data.isEmpty() || index < 0) return null;
//            return data.get(index);
//        }
//
//        @Override
//        protected int getItemCount() {
//            System.out.println("data size: " + data.size());
//            if (data == null || data.isEmpty()) return 0;
//            return data.size();
//        }
//    }
    
}
