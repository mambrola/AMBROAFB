/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general.interfaces;

import ambroafb.general.editor_panel.EditorPanelController;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Supplier;
import javafx.fxml.Initializable;

/**
 *
 * @author dkobuladze
 */
public abstract class ListingController implements Initializable {
    
    protected DataFetchProvider dataFetchProvider;
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        componentsInitialize(url, rb);
    }
    
    protected abstract void componentsInitialize(URL url, ResourceBundle rb);
    
    public abstract void reAssignTable(Supplier<List<EditorPanelable>> fetchData);
    public abstract void reAssignTable(FilterModel model);
    public abstract void addListByClass(Class content);
    public abstract EditorPanelController getEditorPanelController();
    public abstract void removeElementsFromEditorPanel(String... componentFXids);
}
