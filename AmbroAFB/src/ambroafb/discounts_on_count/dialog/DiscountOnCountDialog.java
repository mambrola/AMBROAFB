/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.discounts_on_count.dialog;

import ambroafb.discounts_on_count.DiscountOnCount;
import ambroafb.general.editor_panel.EditorPanel.EDITOR_BUTTON_TYPE;
import ambroafb.general.interfaces.Dialogable;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.general.interfaces.UserInteractiveDialogStage;
import java.util.function.Consumer;
import javafx.stage.Stage;

/**
 *
 * @author dato
 */
public class DiscountOnCountDialog extends UserInteractiveDialogStage implements Dialogable {
    
    public DiscountOnCount discountOnCount;
    public final DiscountOnCount discountOnCountBackup;
    
    public DiscountOnCountDialog(EditorPanelable object, EDITOR_BUTTON_TYPE buttonType, Stage owner) {
        super(owner, buttonType, "/ambroafb/discounts_on_count/dialog/DiscountOnCountDialog.fxml");
        
        if (object == null)
            discountOnCount = new DiscountOnCount();
        else
            discountOnCount = (DiscountOnCount) object; 
        discountOnCountBackup = discountOnCount.cloneWithID();
        
        dialogController.setSceneData(discountOnCount, discountOnCountBackup, buttonType);
    }
    
    @Override
    public DiscountOnCount getResult() {
        showAndWait();
        return discountOnCount;
    }
    
    @Override
    public void operationCanceled(){
        discountOnCount = null;
    }

    @Override
    protected DiscountOnCount getSceneObject() {
        return discountOnCount;
    }

    @Override
    protected Consumer<Object> getAddSuccessAction() {
        return (obj) -> discountOnCount = (DiscountOnCount)obj;
    }
    
    

}
