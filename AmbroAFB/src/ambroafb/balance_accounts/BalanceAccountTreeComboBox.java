/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.balance_accounts;

import java.util.ArrayList;
import java.util.function.Consumer;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.ComboBox;

/**
 *
 * @author dato
 */
public class BalanceAccountTreeComboBox extends ComboBox<BalanceAccount> {
    
    private final String categoryALL = "ALL";
    private final BalanceAccount balAccountALL = new BalanceAccount();
    private final ObservableList<BalanceAccount> items = FXCollections.observableArrayList();
    private final FilteredList<BalanceAccount> filteredList = new FilteredList<>(items);
    
    public BalanceAccountTreeComboBox(){
        super();
        setItems(items);
        
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
//        items.setAll(BalanceAccount.getAllFromDB());
    }
    
    
    private class FetchDataFromDB implements Runnable {

        private final Consumer<ObservableList<BalanceAccount>> consumer;
        
        public FetchDataFromDB(Consumer<ObservableList<BalanceAccount>> consumer){
            this.consumer = consumer;
        }
        
        @Override
        public void run() {
            ArrayList<BalanceAccount> balAccs = BalanceAccount.getAllFromDB();
            Platform.runLater(() -> {
                items.setAll(balAccs);
                if (consumer != null){
                    consumer.accept(items);
                }
            });
        }
    }
}
