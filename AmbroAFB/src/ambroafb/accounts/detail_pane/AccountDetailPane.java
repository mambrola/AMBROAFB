/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.accounts.detail_pane;

import ambro.ADatePicker;
import ambro.ATableView;
import ambroafb.general.GeneralConfig;
import ambroafb.general.Printer;
import ambroafb.general.interfaces.EditorPanelable;
import java.io.IOException;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 *
 * @author dkobuladze
 */
public class AccountDetailPane extends VBox  {
    
    @FXML
    private Label accountInfo, balAccInfo, clientInfo, currentDebitInfo, currentCreditInfo, startingDebitInfo, startingCreditInfo;
    
    @FXML
    private ADatePicker endDate, beginDate;
    
    @FXML
    private Button compute;
    
    @FXML
    private ATableView<AccountEntry> accountEntries;
    
    
    public AccountDetailPane(){
        assignLoader();
    }
    
    private void assignLoader(){
        FXMLLoader loader = new FXMLLoader(AccountDetailPane.class.getResource("/ambroafb/accounts/detail_pane/AccountDetailPane.fxml"));
        loader.setResources(GeneralConfig.getInstance().getBundle());
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException ex) {
            Logger.getLogger(AccountDetailPane.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public Consumer<EditorPanelable> getDetailPaneAction(){
        return (obj) -> {
            Printer.printInfo("AccountDetailPane", "getDetailPaneAction", "");
        };
    }
}
