/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general.interfaces;

import ambroafb.general.editor_panel.EditorPanel;
import ambroafb.general_scene.SelectionObserver;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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
    protected List<SelectionObserver> observers = new ArrayList<>();
    
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
    
    public abstract void reAssignTable(FilterModel model);
    public abstract void addListWith(Class content);
    
    public void setEditorPanel(EditorPanel editorPanel){
        this.editorPanel = editorPanel;
        formPane.topProperty().setValue(editorPanel);
    }
    
    public EditorPanel getEditorPanel(){
        return editorPanel;
    }
    
    public void registerObserver(SelectionObserver observer){
        if (observer != null) observers.add(observer);
    }
}
