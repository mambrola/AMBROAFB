/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general.editor_panel.doc;

import ambroafb.docs.Doc;
import java.util.ArrayList;

/**
 *
 * @author dkobuladze
 */
public interface DocEditorPanelObserver {
    
    public boolean operationIsAllow();
    public void notify(ArrayList<Doc> docs);
    
}
