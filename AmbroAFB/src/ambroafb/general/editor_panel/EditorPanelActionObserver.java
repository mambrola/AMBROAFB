/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general.editor_panel;

import ambroafb.general.interfaces.EditorPanelable;

/**
 *
 * @author dkobuladze
 */
public interface EditorPanelActionObserver {
    
    public void notifyDelete(EditorPanelable deleted);
    public void notifyEdit(EditorPanelable edited);
//    public void notifyView(EditorPanelable viewed);
    public void notifyAdd(EditorPanelable added);
    public void notifyAddBySample(EditorPanelable addedBySample);
    
}
