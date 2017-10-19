/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general.countcombobox;

import com.sun.javafx.scene.control.skin.ComboBoxListViewSkin;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author dato
 */
public class CountComboBox extends ComboBox<CountComboBoxItem> {
    
    private static final int defaultWidth = 500;
    
//    private Map<CountComboBoxItem, Integer> data = new HashMap<>();
//    private final Map<String, CountComboBoxContainer> drawItems = new HashMap<>();
    private boolean isDisableState = false;
    private String viewableCSSFile = "/styles/css/countcomboboxviewable.css";
    private final Tooltip tooltip = new Tooltip();;
    
    private final int counter = 1;
    private final ObservableList<CountComboBoxItem> items = FXCollections.observableArrayList();
    private Map<String, CountComboBoxContainer> containers = new HashMap<>();
    private Basket basket = new Basket();
    private final BiConsumer<String, Integer> numberChangeAction;
    
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
        
        this.setItems(items);
        numberChangeAction = (uniqueId, newNumber) -> {
                    if (newNumber <= 0){
                        basket.remove(uniqueId);
                    }
                    else {
                        basket.add(uniqueId, newNumber);
                    }
                    refreshButtonCell();
                };
        
        addKeyListeners();
    }
    
    // +++
    public void fillcomboBox(Supplier<List<CountComboBoxItem>> itemsGenerator, Consumer<ObservableList<CountComboBoxItem>> extraAction){
        new Thread(new FetchDataFromDB(itemsGenerator, extraAction)).start();
    }
    
    private void addKeyListeners(){
        KeyCode plus = KeyCode.ADD;
        KeyCode minus = KeyCode.SUBTRACT;
        setOnKeyPressed((KeyEvent event) -> {
            KeyCode eventKey = event.getCode();
            CountComboBoxItem selectedItem = getSelectionModel().getSelectedItem();
            if (selectedItem == null) return;
            CountComboBoxContainer selectedDrawItem = containers.get(selectedItem.getUniqueIdentifier());
            
            if (eventKey.equals(plus)){
                selectedDrawItem.increaseNumberBy(counter);
            }
            else if (eventKey.equals(minus)){
                selectedDrawItem.decreaseNumberBy(counter);
            }
            refreshButtonCell();
        });
    }
    
//    public void setData(Map<CountComboBoxItem, Integer> data){
////        this.data = data;
//
//        
//        System.out.println("--------------------------------- aaa ---------------------------");
//        data.keySet().forEach((item) -> {
//            // ამ დროს item-ს არ მოყვება დასახელება. ხოლო თუ მაშინ შვქმნით container-ს როცა სია მოგვივიდა ანუ fetchDataFromDB-ში, მაშინ სრული ინფორმაცია გვაქსვ Item-ბზე.
//            System.out.println(item.getDrawableName());
//            
////            CountComboBoxContainer drawItem = convertIntoDrawItem(item);
////            drawItem.numberProperty().set(data.get(item));
////            drawItems.put(item.getUniqueIdentifier(), drawItem);
//        });
//        
//        // ComboBox button cell text will show after any item select in comboBox. 
//        // So we select every item. The last selected item will be zero indexed (only for visually effect).
//        for (int i = getItems().size() - 1; i >= 0; i--){
//            getSelectionModel().select(i);
//        }
//    }
    
    public void setBasket(Basket b){
        if (b == null || b.isEmpty()){
            containers.keySet().stream().forEach((key) -> containers.get(key).numberProperty().set(0));
        }
        else {
            Iterator<String> itr = b.getIterator();
            String uniqueId = "";
            while (itr.hasNext()){
                uniqueId = itr.next();
                if (containers.containsKey(uniqueId)){
                    int count = b.getCountFor(uniqueId);
                    containers.get(uniqueId).numberProperty().set(count);
                }
            }
            // ComboBox button cell text will show after any item select in comboBox.  So select item index that is last in iterator:
            for (CountComboBoxItem item : items) {
                if (item.getUniqueIdentifier().equals(uniqueId)){
                    getSelectionModel().select(items.indexOf(item));
                    break;
                }
            }
        }
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
    
    public Basket getBasket(){
        return basket;
    }
    
    public void changeState(boolean isDisableState){
        this.isDisableState = isDisableState;
        if (isDisableState){
            getStylesheets().add(viewableCSSFile);
        }
        else {
            getStylesheets().remove(viewableCSSFile);
        }
    }
    
//    public boolean nothingIsSelected(){
//        return data.isEmpty();
//    }
    
    
    // Private class
    private class CustomCell extends ListCell<CountComboBoxItem> {

        public CustomCell() {
            super();
        }

        @Override
        public void updateItem(CountComboBoxItem item, boolean empty) {
            super.updateItem(item, empty);
            if (item == null || empty){
                setGraphic(null);
            }
            else {
                CountComboBoxContainer container = containers.get(item.getUniqueIdentifier());
                container.setViewableState(isDisableState);

                setGraphic(container);
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
            if (item == null || empty){ //  +++
                setText(null);
            }
            else {
                String title = "";
                for (CountComboBoxItem boxItem : getItems()) {
                    String identifier = boxItem.getUniqueIdentifier();
                    if (containers.containsKey(identifier)) { // +++
                        CountComboBoxContainer boxDrawItem = containers.get(identifier); // ++
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
    
    
    
    private class FetchDataFromDB implements Runnable {
        
        private final Supplier<List<CountComboBoxItem>> itemsGenerator;
        private final Consumer<ObservableList<CountComboBoxItem>> consumer;
        
        public FetchDataFromDB(Supplier<List<CountComboBoxItem>> itemsGenerator, Consumer<ObservableList<CountComboBoxItem>> consumer){
            this.itemsGenerator = itemsGenerator;
            this.consumer = consumer;
        }
        
        @Override
        public void run() {
            if (itemsGenerator != null){
                List<CountComboBoxItem> itemsList = itemsGenerator.get();
                itemsList.forEach((item) -> {
                    CountComboBoxContainer itemContainer = new CountComboBoxContainer(item);
                    itemContainer.setAction(numberChangeAction);
                    containers.put(item.getUniqueIdentifier(), itemContainer);
                });
                Platform.runLater(() -> {
                    items.setAll(itemsList);
                    if (consumer != null){
                        consumer.accept(items);
                    }
                });
            }
        }
    }
}
