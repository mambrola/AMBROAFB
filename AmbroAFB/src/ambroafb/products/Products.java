/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.products;

import ambroafb.general.SceneUtils;
import ambroafb.general.stages.ListingStage;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author dato
 */
public class Products extends ListingStage {
    
    private ProductsController productsController;
    
    public Products(Stage owner){
        super(owner, StringUtils.substringAfterLast(Products.class.toString(), "."), "products", "/images/list.png");
        
        Scene scene = SceneUtils.createScene("/ambroafb/products/Products.fxml", null);
        productsController = (ProductsController) scene.getProperties().get("controller");
        this.setScene(scene);
        
        onCloseRequestProperty().set((EventHandler<WindowEvent>) (WindowEvent event) -> {
            productsController.getEditorPanelController().getExitButton().getOnAction().handle(null);
            if (event != null) event.consume();
        });
        
        super.setFeatures(() -> productsController.getEditorPanelController().getPanelMinWidth());
    }
    
    public ProductsController getProductsController(){
        return productsController;
    }
}
