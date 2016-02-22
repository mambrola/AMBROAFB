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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author mambroladze
 */
public class Client {
    
    public Double fieldNo;
    
    @AView.Column(title = "%in_out_usd_form", width = "700")
    public String descrip;
    
    @AView.Column(title = "%amount_usd", width = "120", styleClass = "amount")
    public String amountString;
    
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
