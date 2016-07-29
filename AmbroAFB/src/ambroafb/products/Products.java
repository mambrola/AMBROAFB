/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.products;

import ambroafb.general.Utils;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

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
        
        onCloseRequestProperty().set((EventHandler<WindowEvent>) (WindowEvent event) -> {
            productsController.getEditorPanelController().getExitButton().getOnAction().handle(null);
            event.consume();
        });
        
        Utils.setSizeFor((Stage)this);
    }
    
    public ProductsController getProductsController(){
        return productsController;
    }
}
