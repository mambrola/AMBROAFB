/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.clients;

import authclient.AuthServerException;
import authclient.db.ConditionBuilder;
import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import javafx.application.Platform;
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
import org.json.JSONObject;

/**
 * @author dato
 */
public class ClientComboBox extends AnchorPane {
    
    private final String categoryALL = "ALL";
    private final Client clientALL = new Client();
    private final String separator = ", ";
    
    private final ComboBox<Client> clientsBox = new ComboBox<>();
    private final TextField comboBoxEditor = clientsBox.getEditor();
    private TextField searchField = new TextField();
    
    private int valueSelected = 0;
    private int movedInField = 0;
    private ObservableList<Client> items = FXCollections.observableArrayList();
    private FilteredList<Client> filteredList;
    
    private Consumer<ObservableList<Client>> addCategoryALL;
    
    private ClientDataFetchProvider dataFetchProvider = new ClientDataFetchProvider();
    
    public ClientComboBox(){
        addSceneComponentsToAnchorPane();
        setFeatures();

        // Field width must be equals to comboBox editor width:
//        clientsBox.getEditor().widthProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
//            searchField.setPrefWidth(newValue.doubleValue());
//        });
        
        searchField.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
//            System.out.println("1   field.textProperty oldValue, newValue: " + oldValue + ", " + newValue);
            fieldTextChangeReaction(newValue);
        });
        
        clientsBox.valueProperty().addListener((ObservableValue<? extends Client> observable, Client oldValue, Client newValue) -> {
//            System.out.println("2   box.valueProperty oldValue, newValue: " + oldValue + ", " + newValue);
            valueSelected = 1;
            movedInField = 0;
            if (newValue != null) {
                Platform.runLater(() -> {
                    clientsBox.requestFocus();
                    clientsBox.getEditor().end();
                    clientsBox.toFront();
                });
            }
        });
        
        clientsBox.getEditor().textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
//            System.out.println("3   box.getEditor().textProperty oldValue, newValue: " + oldValue + ", " + newValue);
            if (valueSelected != 1 && movedInField != 1) {
//                System.out.println("ვწერ");

                Platform.runLater(() -> {
                    int currCaret = clientsBox.getEditor().getCaretPosition();
                    if (searchField.getText().equals(newValue)) {
                        fieldTextChangeReaction(newValue);
                    } else {
                        searchField.setText(newValue == null || newValue.equals("") ? "" : newValue);
                    }
                    searchField.requestFocus();
                    searchField.positionCaret(currCaret);
                    searchField.toFront();
                });
                movedInField = 1;
            }
            valueSelected = 0;
        });
        
        clientALL.setFirstName(categoryALL);
        setItems(items);
        
        addCategoryALL = (clientsList) -> {
            clientsList.add(0, clientALL);
            clientsBox.setValue(clientALL);
        };
    }
    
    private void addSceneComponentsToAnchorPane(){
        this.getChildren().add(clientsBox);
        this.getChildren().add(searchField);
    }
    
    private void fieldTextChangeReaction(String value) {
        filteredList.setPredicate((Client elem) -> {
            return elem.getShortDescrip(separator).get().toLowerCase().contains(value.toLowerCase());
        });
        Platform.runLater(() -> {
            clientsBox.hide();
            clientsBox.show();
        });
    }
    
    /**
     * The method fills comboBox only clients data, without ALL category and partners data.
     * @param extraAction The action that will execute after filling the comboBox. If there is no extra action exists, gives null value. 
     */
    public void fillComboBoxOnlyClients(Consumer<ObservableList<Client>> extraAction){
        JSONObject params = new ConditionBuilder().where().and("email", "is not null", "").condition().build();
        new Thread(new FetchDataFromDB(extraAction, params)).start();
    }
    
    /**
     * The method fills comboBox with clients data and ALL category, but without partners data.
     * @param extraAction The action that will execute after filling the comboBox. If there is no extra action exists, gives null value. 
     */
    public void fillComboBoxOnlyClientsWithALL(Consumer<ObservableList<Client>> extraAction){
        Consumer<ObservableList<Client>> consumer = (extraAction == null) ? addCategoryALL : addCategoryALL.andThen(extraAction);
        fillComboBoxOnlyClients(consumer);
    }
    
    /**
     * The method fills comboBox only partners data, without ALL category and clients data.
     * @param extraAction The action that will execute after filling the comboBox. If there is no extra action exists, gives null value. 
     */
    public void fillComboBoxOnlyPartners(Consumer<ObservableList<Client>> extraAction){
        JSONObject params = new ConditionBuilder().where().and("email", "is null", "").condition().build();
        new Thread(new FetchDataFromDB(extraAction, params)).start();
    }
    
    /**
     * The method fills comboBox with partners data and ALL category, but without clients data.
     * @param extraAction The action that will execute after filling the comboBox. If there is no extra action exists, gives null value. 
     */
    public void fillComboBoxOnlyPartnersWithALL(Consumer<ObservableList<Client>> extraAction){
        Consumer<ObservableList<Client>> consumer = (extraAction == null) ? addCategoryALL : addCategoryALL.andThen(extraAction);
        fillComboBoxOnlyPartners(consumer);
    }
    
    /**
     * The method fills comboBox with clients and partners, but without category ALL.
     * @param extraAction The action that will execute after filling the comboBox. If there is no extra action exists, gives null value. 
     */
    public void fillComboBoxWithClientsAndPartners(Consumer<ObservableList<Client>> extraAction){
        new Thread(new FetchDataFromDB(extraAction)).start();
    }
    
    /**
     * The method fills comboBox with clients, partners and  category ALL.
     * @param extraAction The action that will execute after filling the comboBox. If there is no extra action exists, gives null value. 
     */
    public void fillComboBoxWithClientsAndPartnersWithALL(Consumer<ObservableList<Client>> extraAction){
        Consumer<ObservableList<Client>> consumer = (extraAction == null) ? addCategoryALL : addCategoryALL.andThen(extraAction);
        fillComboBoxWithClientsAndPartners(consumer);
    }
    
    public Client getClientWithDescripALL(){
        Client result = null;
        if (!getItems().isEmpty() && getItems().get(0).getRecId() == 0){
            result = items.get(0);
        }
        return result;
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
        clientsBox.setEditable(true);
        searchField.setPromptText("Search"); 
        
        widthProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            searchField.setMinWidth(newValue.doubleValue());
            searchField.setMaxWidth(newValue.doubleValue());
            
            clientsBox.setMinWidth(newValue.doubleValue());
            clientsBox.setMaxWidth(newValue.doubleValue());
        });
        
        clientsBox.setConverter(new CustomConverter());
        
        getStyleClass().add("blockAccessToChildrenFocus");
    }
    
    // Final keyword is needed for call in constructor. It must not be allowed to override.
    public final void setItems(ObservableList<Client> clientsList){ // Function<Client, String> clientFilterDataFn
        filteredList = new FilteredList(clientsList);
        items = clientsList;
        clientsBox.setItems(filteredList);
    }
    
    public final ObservableList<Client> getItems(){
        return items;
    }
    
    public ObjectProperty<Client> valueProperty(){
        return clientsBox.valueProperty();
    }
    
    public Client getValue(){
        return clientsBox.getValue();
    }
    
    public SingleSelectionModel<Client> getSelectionModel(){
        return clientsBox.getSelectionModel();
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
    
    private class FetchDataFromDB implements Runnable {

        private final Consumer<ObservableList<Client>> consumer;
        private final JSONObject params;
        
        public FetchDataFromDB(Consumer<ObservableList<Client>> consumer){
            this(consumer, new ConditionBuilder().build());
        }
        
        public FetchDataFromDB(Consumer<ObservableList<Client>> consumer, JSONObject params){
            this.consumer = consumer;
            this.params = params;
        }
        
        @Override
        public void run() {
            try {
                List<Client> clientsList = dataFetchProvider.getFilteredBy(params);
                Platform.runLater(() -> {
                    items.setAll(clientsList);
                    if (consumer != null){
                        consumer.accept(items);
                    }
                    decreasePopUpWidth();
                });
            } catch (Exception ex) {
            }
        }
    
        /**
         * The method sets anchorPane width to popUp listView width. If user clicks on the comboBox arrow  and shows popUp, this width is not equal for size by this method.
         * Because of at this time popUp width calculate again and becomes equal to the longest item width.
         */
        private void decreasePopUpWidth(){
            System.out.println("clientsBox.getWidth(): " + getWidth());
            String popCss = ".combo-box-popup > .list-view {"
                                    + " -fx-min-width: " + getWidth() + ";"
                                    + " -fx-max-width: " + getWidth() + ";"
                                + "}";
            clientsBox.setStyle(popCss);
        }
    
    }
}
