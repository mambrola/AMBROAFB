/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.docs;

import ambroafb.docs.filter.DocFilter;
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
public class DocManager extends EditorPanelableManager {
    
    public DocManager(){
        dataFetchProvider = new DocDataFetchProvider();
    }

    @Override
    public Dialogable getDialogFor(Stage owner, EditorPanel.EDITOR_BUTTON_TYPE type, EditorPanelable object) {
        return null;
    }

    @Override
    public Filterable getFilterFor(Stage owner) {
        DocFilter filter = new DocFilter(owner);
        return filter;
    }
    
}