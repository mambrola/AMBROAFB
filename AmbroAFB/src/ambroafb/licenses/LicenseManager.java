/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.licenses;

import ambroafb.general.editor_panel.EditorPanel;
import ambroafb.general.interfaces.Dialogable;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.general.interfaces.EditorPanelableManager;
import ambroafb.general.interfaces.Filterable;
import ambroafb.licenses.filter.LicenseFilter;
import javafx.stage.Stage;

/**
 *
 * @author dkobuladze
 */
public class LicenseManager extends EditorPanelableManager {

    public LicenseManager(){
        dataFetchProvider = new LicenseDataFetchProvider();
        dataChangeProvider = null;
    }
    
    @Override
    public Dialogable getDialogFor(Stage owner, EditorPanel.EDITOR_BUTTON_TYPE type, EditorPanelable object) {
        return null;
    }

    @Override
    public Filterable getFilterFor(Stage owner) {
        return new LicenseFilter(owner);
    }
    
}
