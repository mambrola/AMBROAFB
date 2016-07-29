/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.products.dialog;

import ambroafb.general.Names;
import ambroafb.general.Names.EDITOR_BUTTON_TYPE;
import ambroafb.general.Utils;
import ambroafb.general.interfaces.Dialogable;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.products.Product;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author dato
 */
public class ProductDialog extends Stage implements Dialogable {
    
    private Product product;
    private final Product productBackup;
    
    private ProductDialogController dialogController;
    
    public ProductDialog(EditorPanelable object, EDITOR_BUTTON_TYPE buttonType, Stage owner){
        String currStagePath = Utils.getPathForStage(owner) + Names.LEVEL_FOR_PATH;
        System.out.println("shevinaxet: path: " + currStagePath + "  class: " + getClass().getSimpleName() + " stage: " + (Stage)this + " owner: " + owner);
        Utils.saveShowingStageByPath(currStagePath, (Stage)this);
        
        Product productObject;
        if (object == null)
            productObject = new Product();
        else
            productObject = (Product) object;
        
        this.product = productObject;
        this.productBackup = productObject.cloneWithID();
        
        Scene currentScene = Utils.createScene("/ambroafb/products/dialog/ProductDialog.fxml", null);
        dialogController = (ProductDialogController) currentScene.getProperties().get("controller");
        dialogController.bindProduct(this.product);
        dialogController.setNextVisibleAndActionParameters(buttonType, "");
        dialogController.setBackupProduct(this.productBackup);
        this.setScene(currentScene);
        this.setResizable(false);
        this.initOwner(owner);
        
        onCloseRequestProperty().set((EventHandler<WindowEvent>) (WindowEvent event) -> {
            dialogController.getOkayCancelController().getCancelButton().getOnAction().handle(null);
            event.consume();
        });
    }

    @Override
    public Product getResult() {
        showAndWait();
        // If doesn't change any field. It become when user close parent stage of this stage (Products).
        if (product != null && product.compares(productBackup)){
            operationCanceled();
        }
        return product;
    }

    @Override
    public void operationCanceled() {
        product = null;
    }
}
