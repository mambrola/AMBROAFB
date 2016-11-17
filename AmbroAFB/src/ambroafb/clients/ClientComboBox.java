/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.clients;

import static ambroafb.clients.ClientComboBox.clientALL;
import ambroafb.general.filterablecombobox.FilterableWithALLComboBox;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.StringConverter;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Class provides to change behaviour whether its items must be filterable.
 * When user set any value, comboBox will not filterable.
 * When user want to make comboBox a filterable, he/she must remove value from it.
 * Note: 
 * To remove value means - textEditor must contain empty string and press 'enter'.
 * If user want to see a filter effect, he/she must show comboBox elements.
 * @author dato
 */
public class ClientComboBox extends FilterableWithALLComboBox<Client> {
    
    public static final Client clientALL = new Client();
    private static final String separator = ",  ";
    
    private final ObservableList<Client> items = FXCollections.observableArrayList();
    
    public ClientComboBox(){
        super();
        super.initializerClass(Client.class);
        getStyleClass().add("combo-box");
        
        clientALL.setFirstName(ALL);
        clientALL.setRecId(0);
        items.add(clientALL);
        items.addAll(Client.getAllFromDB().stream().filter((Client c) -> c.getEmail() != null && !c.getEmail().isEmpty())
                                                    .collect(Collectors.toList()));
        super.registerCategoryALL(clientALL);
        
        setDataItems(items, (Client c) -> c.getShortDescrip("").get());
        showCategoryALL(true);
    }
    
    private JSONObject getColumnsJSON(){
        JSONObject json = new JSONObject();
        try {
            json.put("0", 0);
            json.put("1", 0);
//            json.put("2", 50);
            json.put("3", 0);
//            json.put("4", 50);
            json.put("5", 0);
            json.put("6", 0);
            json.put("7", 0);
            json.put("8", 0);
            json.put("9", 0);
            json.put("10", 0);
            
        } catch (JSONException ex) {
            Logger.getLogger(ClientComboBox.class.getName()).log(Level.SEVERE, null, ex);
        }
        return json;
    }
    
    public Client getValue(){
        return super.selectedItemPrpoerty().get();
    }
    
    public ObjectProperty<Client> valueProperty(){
        return super.selectedItemPrpoerty();
    }
    
    /**
     * 
     * @param rb 
     */
    public void registerBundle(ResourceBundle rb){
        super.initializerClass(Client.class);
        super.setBundle(rb);
        JSONObject columnsJSON = getColumnsJSON();
        setColunWidthes(columnsJSON);
    }
    
//    public void selectItem(Client client){
//        this.getSelectionModel().select(client);
//    }
    
    
    private class CustomConverter extends StringConverter<Client> {

        @Override
            public String toString(Client client) {
                System.out.println("input client: " + client);
                String result = "";
                if (client != null){
                    result = (client.equals(clientALL)) ? client.getFirstName() 
                                                        //: client.getFirstName() + separator + client.getLastName() + separator + client.getEmail();
                                                        : client.getEmail();
                }
                return result;
            }
            
            @Override
            public Client fromString(String data) {
                System.out.println("input data: " + data);
                if (data == null || data.isEmpty()){
                    return null;
                }
                Client c = new Client();
                c.setEmail(data);
                return c;
//                if (data.equals(clientALL.getFirstName())){
//                    return clientALL;
//                }
//                List<Client> clients = getItems().stream().filter((Client client) -> client.getEmail().equals(data))
//                              .collect(Collectors.toList());
//                return (clients.isEmpty()) ? null : clients.get(0);
            }

//            @Override
//            public Client fromString(String data) {
//                List<Client> clients = items.stream().filter((Client client) -> {
//                                            String enteredFirstName = StringUtils.substringBefore(data, separator);
//                                            String enteredLastName = StringUtils.substringBefore(StringUtils.substringAfter(data, separator), separator);
//                                            String enteredEmail = StringUtils.substringAfterLast(data, separator);
//
//                                            return  client.getFirstName().equals(enteredFirstName) &&
//                                                    client.getLastName().equals(enteredLastName) &&
//                                                    client.getEmail().equals(enteredEmail);
//                }).collect(Collectors.toList());
//                return (clients.isEmpty()) ? null : clients.get(0);
//            }
        
    }
}
