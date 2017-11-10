/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general_scene.table_master_detail;

import ambroafb.general.interfaces.EditorPanelable;

/**
 *
 * @author dkobuladze
 */
public interface MasterObserver {
    
    /**
     *  The method should use when observer need selected object data for simple update (not go out from RAM).
     * @param selected The selected object on master pane.
     * @see #update(ambroafb.general.interfaces.EditorPanelable) 
     */
    public void notify(EditorPanelable selected);
    
    /**
     *  The method should use when observer need selected object data for hard update (go to DataBase,  file  etc.).
     * @param selected The selected object on master pane.
     * @see #notify(ambroafb.general.interfaces.EditorPanelable) 
     */
    public void update(EditorPanelable selected);
    
}
