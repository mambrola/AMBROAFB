/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general.interfaces;

import ambroafb.general.editor_panel.EditorPanel;
import ambroafb.general.editor_panel.custom.CustomEditorPanel;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Supplier;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;

/**
 *
 * @author dkobuladze
 */
public abstract class ListingController implements Initializable {
    
    @FXML
    protected BorderPane formPane;
    
    protected DataFetchProvider dataFetchProvider;
    protected EditorPanel editorPanel = new CustomEditorPanel();
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        formPane.topProperty().setValue(editorPanel);
        componentsInitialize(url, rb);
    }
    
    protected abstract void componentsInitialize(URL url, ResourceBundle rb);
    
    public abstract void reAssignTable(Supplier<List<EditorPanelable>> fetchData);
    public abstract void reAssignTable(FilterModel model);
    public abstract void addListByClass(Class content);
    public abstract void removeElementsFromEditorPanel(String... componentFXids);
    
    public void setEditorPanel(EditorPanel editorPanel){
        if (editorPanel != null){
            this.editorPanel = editorPanel;
            formPane.topProperty().setValue(editorPanel);
        }
    }
    
    public EditorPanel getEditorPanel(){
        return editorPanel;
    }
}
