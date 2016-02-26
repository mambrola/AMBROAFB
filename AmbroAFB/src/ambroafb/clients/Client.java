/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.clients;

import ambroafb.in_out.*;
import ambro.ATreeTableView;
import ambro.AView;
import java.text.DecimalFormat;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author mambroladze
 */
public class Client {
    
    public int clientId;
    
    @AView.Column(title = "%is_jur", width = "24")
    public SimpleBooleanProperty isJur;
    
    @AView.Column(title = "%first_name", width = "700")
    public SimpleStringProperty first_name;
    
    @AView.Column(title = "%last_name", width = "700")
    public SimpleStringProperty last_name;
    
    @AView.Column(title = "%last_name", width = "700")
    public SimpleStringProperty alast_name;
    
    
    
    /*
    `clients`.`email`,
    `clients`.`password`,
    `clients`.`address`,
    `clients`.`zip_code`,
    `clients`.`city`,
    `clients`.`country_code`,
    `clients`.`is_rezident`,
    `clients`.`pass_number`
    */
    
    public Double amount;
    
    @AView.RowStyles
    public ObservableList<String> styles;
    
    @ATreeTableView.Children
    public ObservableList<Client> children;
    
    public Client(Double fn, String d, Double a){
        fieldNo = fn;
        descrip = d;
        amount = a;
        amountString = new DecimalFormat("##,##0.00").format(a);
        children = FXCollections.observableArrayList();
        styles = FXCollections.observableArrayList();
    }
    
    
    
    @Override
    public String toString(){
        return fieldNo+" : "+descrip+" : "+amount+" : "+styles.toString();
    }
            
//ამის მაგალითია customTable-ში Person
    
}
