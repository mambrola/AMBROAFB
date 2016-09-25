/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.licenses;

import javafx.stage.Stage;

/**
 *
 * @author Dato
 */
public class Licenses extends Stage {
    
    private LicensesController licensesController;
    
    public  Licenses(Stage owner){
        
    }
    
    public LicensesController getLicensesController(){
        return licensesController;
    }
}
