/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.products.dialog;

import ambroafb.general.Names;
import ambroafb.general.interfaces.Dialogable;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.general.interfaces.UserInteractiveDialogStage;
import ambroafb.products.Product;
import javafx.stage.Stage;

/**
 *
 * @author dato
 */
public class ProductDialog extends UserInteractiveDialogStage implements Dialogable {
    
    private Product product;
    private final Product productBackup;
    
    public ProductDialog(EditorPanelable object, Names.EDITOR_BUTTON_TYPE buttonType, Stage owner){
        super(owner, "/ambroafb/products/dialog/ProductDialog.fxml", "product_dialog_title");
        
        if (object == null)
            product = new Product();
        else
            product = (Product) object;
        productBackup = product.cloneWithID();
        
        dialogController.setSceneData(product, productBackup, buttonType);
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
