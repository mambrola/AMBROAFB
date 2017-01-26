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

/**
 *
 * @author dato
 */
public class Invoices extends ListingStage {
    
    private InvoicesController invoicesController;
    
    public Invoices(Stage owner){
        super(owner, Invoices.class.getSimpleName(), "invoices");
        
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
