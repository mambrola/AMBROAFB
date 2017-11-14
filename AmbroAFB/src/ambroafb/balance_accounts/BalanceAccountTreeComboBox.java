/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.balance_accounts;

import ambroafb.general.interfaces.DataFetchProvider;
import java.util.List;
import java.util.function.Consumer;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.paint.Color;
import javafx.util.Callback;

/**
 *
 * @author dato
 */
public class BalanceAccountTreeComboBox extends ComboBox<BalanceAccount> {
    
    private final String categoryALL = "ALL";
    private final BalanceAccount balAccountALL = new BalanceAccount();
    private final ObservableList<BalanceAccount> items = FXCollections.observableArrayList();
    private final FilteredList<BalanceAccount> filteredList = new FilteredList<>(items);
    private BalAccountDataFetchProvider dataFetchProvider = new BalAccountDataFetchProvider();
    
    public BalanceAccountTreeComboBox(){
        super();
        setItems(items);
        setCellFactory(new CustomCellFactory());
        
        balAccountALL.setDescrip(categoryALL);
    }
    
    public void fillComboBoxWithALL(Consumer<ObservableList<BalanceAccount>> extraAction){
        Consumer<ObservableList<BalanceAccount>> addCategoryALL = (balAccList) -> {
            if (!balAccList.contains(balAccountALL)){
                balAccList.add(0, balAccountALL);
                setValue(balAccountALL);
            }
        };
        Consumer<ObservableList<BalanceAccount>> consumer = (extraAction == null) ? addCategoryALL : addCategoryALL.andThen(extraAction);
        new Thread(new FetchDataFromDB(consumer)).start();
    }
    
    public void fillComboBoxWithoutALL(Consumer<ObservableList<BalanceAccount>> extraAction){
        new Thread(new FetchDataFromDB(extraAction)).start();
    }
    
    
    private class FetchDataFromDB implements Runnable {

        private final Consumer<ObservableList<BalanceAccount>> consumer;
        
        public FetchDataFromDB(Consumer<ObservableList<BalanceAccount>> consumer){
            this.consumer = consumer;
        }
        
        @Override
        public void run() {
            try {
                List<BalanceAccount> balAccs = dataFetchProvider.getFilteredBy(DataFetchProvider.PARAM_FOR_ALL);
                Platform.runLater(() -> {
                    items.setAll(balAccs);
                    if (consumer != null){
                        consumer.accept(items);
                    }
                });
            } catch (Exception ex) {
            }
        }
    }
    
    /**
     * The class provides to disable non leaf items in list.
     * If item is null or empty cell - do nothing;
     * If item is not leaf disabled;
     * If item is leaf set text color to BLACK;
     * Use  BalanceAccount toString method for show item name;
     */
    public class CustomCellFactory implements Callback<ListView<BalanceAccount>, ListCell<BalanceAccount>> {

        @Override
        public ListCell<BalanceAccount> call(ListView<BalanceAccount> param) {
            final ListCell<BalanceAccount> cell = new ListCell<BalanceAccount>() {
                    
                    @Override 
                    public void updateItem(BalanceAccount item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty){ // This is need for CellFactory specification
                            setText(null);
                        }
                        else {
                            setText(item.toString());
                            if (item.isLeaf()){
                                setTextFill(Color.BLACK);
                                setDisable(false); // This is need for enable leaf items. Without it the items numeration will disable leaf items too.
                            }
                            else {
                                setDisable(true); // This is need for disable leaf items.
                            }
                        }
                    }
            };
            return cell;
        }
    
    }
}
