/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.currencies.dialog;

import ambro.ADatePicker;
import ambroafb.currencies.Currency;
import ambroafb.currencies.IsoComboBox;
import ambroafb.general.Names;
import ambroafb.general.interfaces.Annotations.ContentISO;
import ambroafb.general.interfaces.Annotations.ContentNotEmpty;
import ambroafb.general.interfaces.Annotations.ContentPattern;
import ambroafb.general.interfaces.DialogController;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.general.okay_cancel.DialogOkayCancelController;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author dato
 */
public class CurrencyDialogController extends DialogController {

    @FXML
    private VBox formPane;
    @FXML
    private ADatePicker openDate;
    @FXML @ContentNotEmpty @ContentISO
    private IsoComboBox iso;
    @FXML @ContentNotEmpty
    private TextField descrip;
    @FXML @ContentNotEmpty @ContentPattern(value = "\\p{Sc}", explain = "anot_currency_exp")
    private TextField symbol;
    @FXML
    private DialogOkayCancelController okayCancelController;

    @Override
    protected void componentsInitialize(URL url, ResourceBundle rb) {
    }

    @Override
    protected Parent getSceneRoot() {
        return formPane;
    }
    
    @Override
    protected void bindObjectToSceneComponents(EditorPanelable object) {
        if (object != null){
            Currency currency = (Currency)object;
            openDate.valueProperty().bindBidirectional(currency.dateProperty());
            iso.valueProperty().bindBidirectional(currency.isoProperty());
            descrip.textProperty().bindBidirectional(currency.descripProperty());
            symbol.textProperty().bindBidirectional(currency.symbolProperty());
        }
    }

    @Override
    protected void makeExtraActions(Names.EDITOR_BUTTON_TYPE buttonType) {
        openDate.setDisable(true);
        if (buttonType.equals(Names.EDITOR_BUTTON_TYPE.ADD)){
            iso.setEditable(true);
        }
    }

    @Override
    public DialogOkayCancelController getOkayCancelController() {
        return okayCancelController;
    }
}
