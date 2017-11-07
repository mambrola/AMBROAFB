/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general.editor_panel.doc;

import ambroafb.AmbroAFB;
import ambroafb.docs.Doc;
import ambroafb.docs.types.conversion.ConversionManager;
import ambroafb.docs.types.doc_in_order.DocOrderComponent;
import ambroafb.docs.types.monthly.MonthlyManager;
import ambroafb.docs.types.utilities.charge.ChargeUtilityManager;
import ambroafb.docs.types.utilities.payment.PaymentUtilityManager;
import ambroafb.general.GeneralConfig;
import ambroafb.general.editor_panel.EditorPanel;
import ambroafb.general.interfaces.Dialogable;
import ambroafb.general.interfaces.EditorPanelableManager;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

/**
 *
 * @author dkobuladze
 */
public class DocsAddButton extends MenuButton implements Initializable {
    
    @FXML
    private MenuItem addConvert, addPaymentUtility, addChargeUtility, addMonthlyAccrual;
    
    private DocEditorPanelObserver observer;
    
    public DocsAddButton(){
        super();
        assignLoader();
    }
    
    private void assignLoader(){
        FXMLLoader loader = new FXMLLoader(AmbroAFB.class.getResource("/ambroafb/general/editor_panel/doc/DocsAddButton.fxml"));
        loader.setResources(GeneralConfig.getInstance().getBundle());
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException ex) {
            Logger.getLogger(DocOrderComponent.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        buttonsActions();
    }
    
    private void buttonsActions(){
        addConvert.setOnAction(this::addConversion);
        addPaymentUtility.setOnAction(this::addPaymentUtility);
        addChargeUtility.setOnAction(this::addChargeUtility);
        addChargeUtility.setOnAction(this::addChargeUtility);
        addMonthlyAccrual.setOnAction(this::addMonthlyAccrual);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
    
    public void registerObserver(DocEditorPanelObserver observer){
        this.observer = observer;
    }
    
    public void addConversion(ActionEvent e){
        if (observer.operationIsAllow()) {
            EditorPanelableManager manager = new ConversionManager();
            Dialogable dialog = manager.getDialogFor((Stage)getScene().getWindow(), EditorPanel.EDITOR_BUTTON_TYPE.ADD, null);
            List<Doc> docsFromDialog = dialog.getResult();
            if (docsFromDialog != null){
                observer.notify(docsFromDialog);
            }
        }
    }
    
    public void addPaymentUtility(ActionEvent e) {
        if (observer.operationIsAllow()) {
            makeAddAction(new PaymentUtilityManager());
        }
    }
    
    public void addChargeUtility(ActionEvent e) {
        if (observer.operationIsAllow()) {
            makeAddAction(new ChargeUtilityManager());
        }
    }
    
    public void addMonthlyAccrual(ActionEvent e){
        if (observer.operationIsAllow()) {
            makeAddAction(new MonthlyManager());
        }
    }
    
    private void makeAddAction(EditorPanelableManager manager){
        Dialogable dialog = manager.getDialogFor((Stage)getScene().getWindow(), EditorPanel.EDITOR_BUTTON_TYPE.ADD, null);
        List<Doc> docsFromDialog = dialog.getResult();
        if (docsFromDialog != null){
            observer.notify(docsFromDialog);
        }
    }
}
