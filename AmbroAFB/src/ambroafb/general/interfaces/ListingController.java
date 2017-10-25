/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general.interfaces;

import ambroafb.general.editor_panel.EditorPanelController;
import java.util.List;
import java.util.function.Supplier;

/**
 *
 * @author dkobuladze
 */
public abstract class ListingController {
    
    public abstract void reAssignTable(Supplier<List<EditorPanelable>> fetchData);
    public abstract void addListByClass(Class content);
    public abstract EditorPanelController getEditorPanelController();
    public abstract void removeElementsFromEditorPanel(String... componentFXids);
}
