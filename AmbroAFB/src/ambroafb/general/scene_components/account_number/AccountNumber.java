/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general.scene_components.account_number;

import ambroafb.general.GeneralConfig;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

/**
 *
 * @author dkobuladze
 */
public class AccountNumber extends HBox {
    
//    @FXML
//    private TextField accountNumber;
//    @FXML
//    private Button key;
//    @FXML
//    private Button next;
    
    private final TextField accountNumber = new TextField();
    private final Button key = new Button();
    private final Button next = new Button();
    
    private final String contentPattern = "[\\d-]*";
    
    private final double fieldDefaultPrefWidth = 10;
    private final String keyButtonSymbol = "\uD83D\uDD11";
    private final String nextButtonSymbol = "\u21D2";
    
    public AccountNumber(){
        super();
        assignLoader();
        initComponents();
        
        accountNumber.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (newValue != null && !newValue.isEmpty() && !Pattern.matches(contentPattern, newValue)){
                accountNumber.setText(oldValue);
            }
        });
        
    }
    
    private void assignLoader(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ambroafb/general/scene_components/account_number/AccountNumber.fxml"));
        loader.setResources(GeneralConfig.getInstance().getBundle());
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException ex) {
            Logger.getLogger(AccountNumber.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void initComponents(){
        key.setText(keyButtonSymbol);
        next.setText("  " + nextButtonSymbol + "  ");

        // The pref width setter is nesesary for execution Priorities.
        accountNumber.setPrefWidth(fieldDefaultPrefWidth);
        setHgrow(accountNumber, Priority.SOMETIMES);
        setHgrow(key, Priority.ALWAYS);
        setHgrow(next, Priority.ALWAYS);
        
        getChildren().addAll(accountNumber, key, next);
    }
    
    public Button getKey(){
        return key;
    }
    
    public Button getNext(){
        return next;
    }
    
    public TextField getField(){
        return accountNumber;
    }
    
    /**
     * Field value property.
     * @return 
     */
    public StringProperty valueProperty(){
        return accountNumber.textProperty();
    }
    
    /**
     * The method sets the new value into field.
     * @param number 
     */
    public void setText(String number){
        accountNumber.setText(number);
    }
    
    /**
     * Returns account number String value.
     * @return The value of  field. It also contains slashes.
     */
    public String getText(){
        return accountNumber.getText();
    }
}
