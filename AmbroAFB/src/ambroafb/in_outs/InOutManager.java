/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.in_outs;

import ambroafb.general.editor_panel.EditorPanel;
import ambroafb.general.interfaces.Dialogable;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.general.interfaces.EditorPanelableManager;
import ambroafb.general.interfaces.Filterable;
import ambroafb.in_outs.filter.InOutFilter;
import javafx.stage.Stage;

/**
 *
 * @author dkobuladze
 */
public class InOutManager extends EditorPanelableManager {
    
    public InOutManager(){
        dataFetchProvider = new InOutDataFetchProvider();
    }

    @Override
    public Dialogable getDialogFor(Stage owner, EditorPanel.EDITOR_BUTTON_TYPE type, EditorPanelable object) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Filterable getFilterFor(Stage owner) {
        return new InOutFilter(owner);
    }
    
    
}
