/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.invoices;

import ambroafb.general.Utils;
import ambroafb.general.StageUtils;
import ambroafb.general.StagesContainer;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author dato
 */
public class Invoices extends Stage {
    
    private InvoicesController invoicesController;
    
    public Invoices(Stage owner){
        StagesContainer.registerStageByOwner(owner, getClass().getSimpleName(), (Stage)this);
        
        Scene scene = Utils.createScene("/ambroafb/invoices/Invoices.fxml", null);
        invoicesController = (InvoicesController) scene.getProperties().get("controller");
        this.setScene(scene);
        this.initOwner(owner);
        
        onCloseRequestProperty().set((EventHandler<WindowEvent>) (WindowEvent event) -> {
            invoicesController.getEditorPanelController().getExitButton().getOnAction().handle(null);
            if (event != null) event.consume();
        });
        
        StageUtils.centerChildOf(owner, (Stage)this);
        StagesContainer.setSizeFor((Stage)this);
    }
    
    public InvoicesController getInvoicesController(){
        return invoicesController;
    }
    
}
