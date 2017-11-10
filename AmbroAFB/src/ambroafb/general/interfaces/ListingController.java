/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general.interfaces;

import ambroafb.general.editor_panel.EditorPanel;
import java.net.URL;
import java.util.ResourceBundle;
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
    protected EditorPanel editorPanel;
    protected ResourceBundle bundle;
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        bundle = rb;
        componentsInitialize(url, rb);
    }
    
    protected abstract void componentsInitialize(URL url, ResourceBundle rb);
    
//    public abstract void reAssignTable(Supplier<List<EditorPanelable>> fetchData);
    public abstract void reAssignTable(FilterModel model);
    public abstract void addListWith(Class content);
    
    public void setEditorPanel(EditorPanel editorPanel){
        this.editorPanel = editorPanel;
        formPane.topProperty().setValue(editorPanel);
    }
    
    public EditorPanel getEditorPanel(){
        return editorPanel;
    }
}
