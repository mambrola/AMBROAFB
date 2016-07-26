/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.products;

import ambroafb.general.Utils;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author dato
 */
public class Products extends Stage {
    
    private ProductsController productsController;
    
    public Products(Stage owner){
        String productsPath = Utils.getPathForStage(owner) + "/" + getClass().getSimpleName();
        Utils.saveShowingStageByPath(productsPath, (Stage)this);
        
        Scene scene = Utils.createScene("/ambroafb/products/Products.fxml", null);
        productsController = (ProductsController) scene.getProperties().get("controller");
        this.setScene(scene);
        this.initOwner(owner);
        
        Utils.setSizeFor((Stage)this);
    }
    
    public ProductsController getProductsController(){
        return productsController;
    }
}
