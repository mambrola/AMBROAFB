/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.clients;

import java.util.function.Function;
import java.util.stream.Collectors;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.util.StringConverter;

/**
 * @author dato
 */
public class ClientComboBox extends AnchorPane {
    
    public static final Client clientALL = new Client();
    private static final String separator = ", ";
    
    private final ComboBox<Client> clients = new ComboBox<>();
    private final TextField comboBoxEditor = clients.getEditor();
    private TextField search = new TextField();
    
    private ObservableList<Client> items = FXCollections.observableArrayList();
    private FilteredList<Client> filteredList;
    
    public ClientComboBox(){
        setFeatures();
        
        comboBoxEditor.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            // 1. Remove selected item text from editor (Note: item stay selected in list):
            // 2. Set value null:
            if (newValue == null || newValue.isEmpty()){
                if (!search.isFocused()){ // If searchField has not front and focused yet.
                    search.toFront();
                    search.requestFocus();
                    if (!search.getText().isEmpty()){
                        search.setText("");
                    }
                }
            }
            else { // set new value that is not null and empty:
                search.toBack();
//                search.setText(""); // clear search conntext
            }
        });
        
        // Show clients items list when typed into search field:
        search.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (newValue != null && !newValue.isEmpty()){
                if (!clients.isShowing()){
                    clients.show();
                }
            }
        });
        
        search.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (newValue && clients.getValue() != null){
                clients.requestFocus(); // If ClientComboBox is first element of scene and value is ALL, then search field is focused. So this is needed.
            }
        });
        
        addIntoChildren();
        
        clientALL.setFirstName("ALL");
        clientALL.setRecId(0);
        items.add(clientALL);
        items.addAll(Client.getAllFromDB().stream().filter((Client c) -> c.getEmail() != null && !c.getEmail().isEmpty())
                                                    .collect(Collectors.toList()));
        setItems(items, (Client c) -> c.getShortDescrip(separator).get());
        clients.setValue(clientALL);
    }
    
    public void showCategoryALL(boolean show){
        if (!show && (!getItems().isEmpty() && getItems().get(0).getRecId() == 0)){
            getItems().remove(0);
        }
        
        if (show && (getItems().isEmpty() || getItems().get(0).getRecId() != 0)){
            getItems().add(0, clientALL);
        }
    }
    
    
    private void setFeatures(){
        clients.setEditable(true);
        search.setPromptText("Search"); 
        
        comboBoxEditor.widthProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            search.setMinWidth(newValue.doubleValue());
            search.setMaxWidth(newValue.doubleValue());
        });
        
        comboBoxEditor.heightProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            search.setMinHeight(newValue.doubleValue());
            search.setMaxHeight(newValue.doubleValue());
        });
        
        clients.setConverter(new CustomConverter());
        
        getStyleClass().add("blockAccessToChildrenFocus");
    }
    
    private void addIntoChildren(){
        getChildren().add(clients);
        getChildren().add(search);
    }
    
    // Final keyword is needed for call in constructor. It must not be allowed to override.
    public final void setItems(ObservableList<Client> clientsList, Function<Client, String> clientFilterDataFn){
        filteredList = new FilteredList(clientsList);
        items = clientsList;
        clients.setItems(filteredList);
        
        // Every search text changed, the filteredList must give new predicate for filter:
        search.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            filteredList.setPredicate((Client c) -> {
                String searchText = search.getText();
                if (searchText == null || searchText.isEmpty()) {
                    return true;
                }
                return clientFilterDataFn.apply(c).toLowerCase().contains(searchText.toLowerCase());
            });
        });
        
    }
    
    public final ObservableList<Client> getItems(){
        return items;
    }
    
    public ObjectProperty<Client> valueProperty(){
        return clients.valueProperty();
    }
    
    public Client getValue(){
        return clients.getValue();
    }
    
    public SingleSelectionModel<Client> getSelectionModel(){
        return clients.getSelectionModel();
    }
    
    private class CustomConverter extends StringConverter<Client> {

        @Override
        public String toString(Client c) {
            return (c == null) ? "" : c.getShortDescrip(separator).get();
        }

        @Override
        public Client fromString(String input) {
            if (input.isEmpty()){
                return null;
            }
            int firstSeparatorIndex = input.indexOf(separator);
            String name = input.substring(0, firstSeparatorIndex);
            int secondSeparatorIndex = input.indexOf(separator, firstSeparatorIndex);
            String lastName = input.substring(firstSeparatorIndex + separator.length(), secondSeparatorIndex);
            int emailStartIndex = secondSeparatorIndex + separator.length();
            String email = input.substring(emailStartIndex);
            return getItems().stream().filter((Client c) -> c.getFirstName().equals(name) && c.getLastName().equals(lastName) && c.getEmail().equals(email)).collect(Collectors.toList()).get(0);
        }
        
    }
}
