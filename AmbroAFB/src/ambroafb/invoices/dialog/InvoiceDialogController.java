/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.invoices.dialog;

import ambroafb.general.Names;
import ambroafb.general.Utils;
import ambroafb.general.okay_cancel.DialogOkayCancelController;
import ambroafb.invoices.Invoice;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.VBox;

/**
 *
 * @author dato
 */
public class InvoiceDialogController implements Initializable {

    @FXML
    private VBox formPane;
    
    @FXML
    private DialogOkayCancelController okayCancelController;
    
    private ArrayList<Node> focusTraversableNodes;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        focusTraversableNodes = Utils.getFocusTraversableBottomChildren(formPane);
    }

    public void bindInvoice(Invoice invoice) {
    }

    public void setNextVisibleAndActionParameters(Names.EDITOR_BUTTON_TYPE buttonType, String string) {
    }

    public void setBackupInvoice(Invoice invoiceBackup) {
    }

    public DialogOkayCancelController getOkayCancelController() {
        return okayCancelController;
    }
    
    
    
}
