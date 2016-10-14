/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.products.dialog;

import ambroafb.general.GeneralConfig;
import ambroafb.general.Names;
import ambroafb.general.Names.EDITOR_BUTTON_TYPE;
import ambroafb.general.Utils;
import ambroafb.general.interfaces.Dialogable;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.general.StageUtils;
import ambroafb.general.StagesContainer;
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
        StagesContainer.registerStageByOwner(owner, Names.LEVEL_FOR_PATH, (Stage)this);
        
        if (object == null)
            this.product = new Product();
        else
            this.product = (Product) object;
        this.productBackup = product.cloneWithID();
        
        Scene currentScene = Utils.createScene("/ambroafb/products/dialog/ProductDialog.fxml", null);
        dialogController = (ProductDialogController) currentScene.getProperties().get("controller");
        dialogController.bindProduct(this.product);
        dialogController.setNextVisibleAndActionParameters(buttonType);
        dialogController.setBackupProduct(this.productBackup);
        this.setScene(currentScene);
        this.setResizable(false);
        this.initOwner(owner);
        this.setTitle(GeneralConfig.getInstance().getTitleFor("product_dialog_title"));
        
        onCloseRequestProperty().set((EventHandler<WindowEvent>) (WindowEvent event) -> {
            dialogController.getOkayCancelController().getCancelButton().getOnAction().handle(null);
            if (event != null) event.consume();
        });
        
        StageUtils.centerChildOf(owner, (Stage)this);
        StageUtils.followChildTo(owner, (Stage)this);
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
