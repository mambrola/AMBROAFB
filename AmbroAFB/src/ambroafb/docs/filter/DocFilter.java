/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.docs.filter;

import ambro.ADatePicker;
import ambroafb.accounts.Account;
import ambroafb.accounts.AccountComboBox;
import ambroafb.currencies.CurrencyComboBox;
import ambroafb.docs.DocCode;
import ambroafb.docs.DocCodeComboBox;
import ambroafb.general.Names;
import ambroafb.general.SceneUtils;
import ambroafb.general.interfaces.FilterModel;
import ambroafb.general.interfaces.Filterable;
import ambroafb.general.interfaces.UserInteractiveStage;
import ambroafb.general.okay_cancel.FilterOkayCancelController;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author dkobuladze
 */
public class DocFilter extends UserInteractiveStage implements Filterable, Initializable {

    @FXML
    private ADatePicker docDateFrom, docDateTo, docInDocDateFrom, docInDocDateTo;
    @FXML
    private AccountComboBox accounts;
    @FXML
    private CurrencyComboBox currencies;
    @FXML
    private DocCodeComboBox docCodes;
    @FXML
    private FilterOkayCancelController okayCancelController;
    
    private final DocFilterModel docFilterModel = new DocFilterModel();
    
    public static final String DATE_BIGGER = "1970-01-01";
    public static final String DATE_LESS = "9999-01-01";
    
    public DocFilter(Stage owner){
        super(owner, Names.LEVEL_FOR_PATH, "docs", "/images/filter.png");
        Scene scene = SceneUtils.createScene("/ambroafb/docs/filter/DocFilter.fxml", (DocFilter)this);
        this.setScene(scene);
        
        onCloseRequestProperty().set((EventHandler<WindowEvent>) (WindowEvent event) -> {
            okayCancelController.cancel(null);
            if (event != null) event.consume();
        });
    }
    
    @Override
    public FilterModel getResult() {
        showAndWait();
        return docFilterModel;
    }

    @Override
    public void setResult(boolean isOk) {
        if (!isOk){
            docFilterModel.changeModelAsEmpty();
        }
        else {
            docDateFrom.setEditingValue();
            docDateTo.setEditingValue();
            docInDocDateFrom.setEditingValue();
            docInDocDateTo.setEditingValue();
            
            docFilterModel.setDocDate(true, docDateFrom.getValue());
            docFilterModel.setDocDate(false, docDateTo.getValue());
            docFilterModel.setDocInDocDate(true, docInDocDateFrom.getValue());
            docFilterModel.setDocInDocDate(false, docInDocDateTo.getValue());
            docFilterModel.setSelectedAccount(accounts.getValue());
            docFilterModel.setSelectedCurrency(currencies.getValue().getDescrip());
            docFilterModel.setSelectedDocCode(docCodes.getValue());
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        docDateFrom.setValue(docFilterModel.getDocDate(true));
        docDateTo.setValue(docFilterModel.getDocDate(false));
        docInDocDateFrom.setValue(docFilterModel.getDocInDocDate(true));
        docInDocDateTo.setValue(docFilterModel.getDocInDocDate(false));
        
        Consumer<List<Account>> accountConsumer = (accountList) -> {
            int accId = docFilterModel.getSelectedAccountId();
            Optional<Account> optAccount = accountList.stream().filter((acc) -> acc.getRecId() == accId).findFirst();
            if (optAccount.isPresent()){
                accounts.setValue(optAccount.get());
            }
        };
        accounts.fillComboBox(accountConsumer);
        
        currencies.fillComboBoxWithALL(null);
        
        Consumer<List<DocCode>> docCodeConsumer = (accountList) -> {
            String selectedDocCode = docFilterModel.getSelectedDocCode();
            Optional<DocCode> optAccount = accountList.stream().filter((dc) -> dc.getDocCode().equals(selectedDocCode)).findFirst();
            if (optAccount.isPresent()){
                docCodes.setValue(optAccount.get());
            }
        };
        docCodes.fillComboBox(docCodeConsumer);
        
    }
    
}
