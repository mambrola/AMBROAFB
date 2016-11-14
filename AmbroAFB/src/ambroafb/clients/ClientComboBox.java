/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.clients;

import static ambroafb.clients.ClientComboBox.clientALL;
import ambroafb.general.filterablecombobox.FilterableWithALLComboBox;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.StringConverter;

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
    
    private ObservableList<Client> items = FXCollections.observableArrayList();
    
    public ClientComboBox(){
        super();
        
        clientALL.setFirstName(ALL);
        clientALL.setRecId(0);
        items.add(clientALL);
        items.addAll(Client.getAllFromDB());
        
        this.setConverter(new CustomConverter());

        Predicate predicate = (Predicate<Client>) (Client client) -> {
               if (getEditor() == null || getEditor().getText() == null || getEditor().getText().isEmpty()){
                   return true;
                }
//                String searchText = client.getFirstName() + separator + client.getLastName() + separator + client.getEmail();
//                return searchText.toLowerCase().contains(getEditor().getText().toLowerCase());
                return client.getEmail().toLowerCase().equals(getEditor().getText().toLowerCase());
        };
        setDataForFilterable(items, predicate);
        
        showCategoryALL(true);
    }
    
    public void selectItem(Client client){
        this.getSelectionModel().select(client);
    }
    
    
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
                if (data.equals(clientALL.getFirstName())){
                    return clientALL;
                }
                List<Client> clients = getItems().stream().filter((Client client) -> client.getEmail().equals(data))
                              .collect(Collectors.toList());
                return (clients.isEmpty()) ? null : clients.get(0);
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
