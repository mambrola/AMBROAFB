/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.currency_rates;

import ambroafb.currency_rates.dialog.CurrencyRateDialog;
import ambroafb.currency_rates.filter.CurrencyRateFilter;
import ambroafb.general.editor_panel.EditorPanel;
import ambroafb.general.interfaces.Dialogable;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.general.interfaces.EditorPanelableManager;
import ambroafb.general.interfaces.Filterable;
import javafx.stage.Stage;

/**
 *
 * @author dkobuladze
 */
public class CurrencyRateManager extends EditorPanelableManager {

    public CurrencyRateManager() {
        dataFetchProvider = new CurrencyRateDataFetchProvider();
        dataChangeProvider = new CurrencyRateDataChangeProvider();
    }
    
    @Override
    public Dialogable getDialogFor(Stage owner, EditorPanel.EDITOR_BUTTON_TYPE type, EditorPanelable object) {
        CurrencyRateDialog dialog = new CurrencyRateDialog(object, type, owner);
        dialog.setDataChangeProvider(dataChangeProvider);
        dialog.setFrameFeatures(type, "currency_rate_dialog_title");
        return dialog;
    }

    @Override
    public Filterable getFilterFor(Stage owner) {
        return new CurrencyRateFilter(owner);
    }
    
}
