/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.products.dialog;

import ambroafb.general.Names;
import ambroafb.general.SceneUtils;
import ambroafb.general.interfaces.Dialogable;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.general.interfaces.UserInteractiveStage;
import ambroafb.products.Product;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author dato
 */
public class ProductDialog extends UserInteractiveStage implements Dialogable {
    
    private Product product;
    private final Product productBackup;
    
    private ProductDialogController dialogController;
    
    public ProductDialog(EditorPanelable object, Names.EDITOR_BUTTON_TYPE buttonType, Stage owner){
        super(owner, Names.LEVEL_FOR_PATH, "product_dialog_title", "/images/dialog.png");
        
        if (object == null)
            this.product = new Product();
        else
            this.product = (Product) object;
        this.productBackup = product.cloneWithID();
        
        Scene currentScene = SceneUtils.createScene("/ambroafb/products/dialog/ProductDialog.fxml", null);
        dialogController = (ProductDialogController) currentScene.getProperties().get("controller");
        dialogController.bindProduct(this.product);
        dialogController.setNextVisibleAndActionParameters(buttonType);
        dialogController.setBackupProduct(this.productBackup);
        this.setScene(currentScene);
        
        onCloseRequestProperty().set((EventHandler<WindowEvent>) (WindowEvent event) -> {
            dialogController.getOkayCancelController().getCancelButton().getOnAction().handle(null);
            if (event != null) event.consume();
        });
        
    }

    @Override
    public Product getResult() {
        showAndWait();
        return product;
    }

    @Override
    public void operationCanceled() {
        product = null;
    }
}
