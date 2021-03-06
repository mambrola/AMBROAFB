/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.currencies.dialog;

import ambro.ADatePicker;
import ambroafb.currencies.Currency;
import ambroafb.currencies.IsoComboBox;
import ambroafb.general.editor_panel.EditorPanel;
import ambroafb.general.interfaces.Annotations.ContentISO;
import ambroafb.general.interfaces.Annotations.ContentNotEmpty;
import ambroafb.general.interfaces.DialogController;
import ambroafb.general.interfaces.EditorPanelable;
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
    @FXML @ContentNotEmpty // @ContentPattern(value = "\\p{Sc}", explain = "anot_currency_exp")
    private TextField symbol;

    @Override
    protected void componentsInitialize(URL url, ResourceBundle rb) {
        iso.fillComboBox(null);
    }

    @Override
    protected Parent getSceneRoot() {
        return formPane;
    }
    
    @Override
    protected void bindObjectToSceneComponents(EditorPanelable object) {
        if (object != null){
            Currency currency = (Currency)object;
            openDate.valueProperty().bind(currency.createdDateProperty());
            iso.valueProperty().bindBidirectional(currency.isoProperty());
            descrip.textProperty().bindBidirectional(currency.descripProperty());
            symbol.textProperty().bindBidirectional(currency.symbolProperty());
        }
    }

    @Override
    protected void makeExtraActions(EditorPanel.EDITOR_BUTTON_TYPE buttonType) {
        if (buttonType.equals(EditorPanel.EDITOR_BUTTON_TYPE.ADD)){
            iso.setEditable(true);
        }
    }

}
