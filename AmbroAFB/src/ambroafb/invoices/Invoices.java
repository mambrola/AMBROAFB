/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.invoices;

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
public class Invoices extends ListingStage {
    
    private InvoicesController invoicesController;
    
    public Invoices(Stage owner){
        super(owner, StringUtils.substringAfterLast(Invoices.class.toString(), "."), "invoices", "/images/list.png");
        
        Scene scene = SceneUtils.createScene("/ambroafb/invoices/Invoices.fxml", null);
        invoicesController = (InvoicesController) scene.getProperties().get("controller");
        this.setScene(scene);
        
        onCloseRequestProperty().set((EventHandler<WindowEvent>) (WindowEvent event) -> {
            invoicesController.getEditorPanelController().getExitButton().getOnAction().handle(null);
            if (event != null) event.consume();
        });
        
        super.setFeatures(() -> invoicesController.getEditorPanelController().getPanelMinWidth());
    }
    
    public InvoicesController getInvoicesController(){
        return invoicesController;
    }
    
}
