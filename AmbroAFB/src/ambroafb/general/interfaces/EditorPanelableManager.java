/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general.interfaces;

import ambroafb.general.Names;
import javafx.stage.Stage;

/**
 *
 * @author dkobuladze
 */
public abstract class EditorPanelableManager {
    
    protected DataFetchProvider dataFetchProvider;
    protected DataChangeProvider dataChangeProvider;
    
    public DataFetchProvider getDataFetchProvider(){
        return dataFetchProvider;
    }
    
    public DataChangeProvider getDataChangeProvider(){
        return dataChangeProvider;
    }
    
    public abstract Dialogable getDialogFor(Stage owner, Names.EDITOR_BUTTON_TYPE type, EditorPanelable object);
    public abstract Filterable getFilterFor(Stage owner);
    
}
