/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.car_fines;

import ambro.ATreeTableView.Children;
import ambro.AView.Column;
import ambro.AView.RowStyles;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ambro.AView.Column;

/**
 *
 * @author Administrator
 */
public class CarNumber {
    
    @Column(title = "number", width = "100")
    public String carNumber;
    
    
    public CarNumber(String numberOfCar){
        carNumber = numberOfCar;
    }
    
    @Override
    public String toString(){
        return "Number of car: " + carNumber;
    }
}
