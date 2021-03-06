/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.loggings;

import ambroafb.general.editor_panel.EditorPanel;
import ambroafb.general.interfaces.Dialogable;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.general.interfaces.EditorPanelableManager;
import ambroafb.general.interfaces.Filterable;
import ambroafb.loggings.filter.LoggingFilter;
import javafx.stage.Stage;

/**
 *
 * @author dkobuladze
 */
public class LoggingManager extends EditorPanelableManager {

    public LoggingManager(){
        dataFetchProvider = new LoggingDataFetchProvider();
        dataChangeProvider = null;
    }
    
    @Override
    public Dialogable getDialogFor(Stage owner, EditorPanel.EDITOR_BUTTON_TYPE type, EditorPanelable object) {
        return null;
    }

    @Override
    public Filterable getFilterFor(Stage owner) {
        return new LoggingFilter(owner);
    }
    
}
