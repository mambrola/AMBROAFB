/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.clients.editor_panel;

import ambroafb.AmbroAFB;
import ambroafb.general.GeneralConfig;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

/**
 *
 * @author tabramishvili
 */
public class EditorPanel extends Pane{
    private final ObjectProperty<EventHandler<ActionEvent>> onNew = new SimpleObjectProperty<>();
    private final ObjectProperty<EventHandler<ActionEvent>> onNewBySample = new SimpleObjectProperty<>();
    private final ObjectProperty<EventHandler<ActionEvent>> onView = new SimpleObjectProperty<>();
    private final ObjectProperty<EventHandler<ActionEvent>> onEdit = new SimpleObjectProperty<>();
    private final ObjectProperty<EventHandler<ActionEvent>> onDelete = new SimpleObjectProperty<>();
    private final ObjectProperty<EventHandler<ActionEvent>> onRefresh = new SimpleObjectProperty<>();
    private EditorPanelController controller;
    
    public EditorPanel(){
        super();
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setResources(GeneralConfig.getInstance().getBundle());
            getChildren().add(loader.load(AmbroAFB.class.getResource("/ambroafb/clients/editor_panel/EditorPanel.fxml").openStream()));
            controller = loader.getController();
        } catch (IOException ex) {
            Logger.getLogger(EditorPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        onNew.addListener((ObservableValue<? extends EventHandler<ActionEvent>> observable, EventHandler<ActionEvent> oldValue, EventHandler<ActionEvent> newValue) -> {
            controller.setOnNew(newValue);
        });
        onNewBySample.addListener((ObservableValue<? extends EventHandler<ActionEvent>> observable, EventHandler<ActionEvent> oldValue, EventHandler<ActionEvent> newValue) -> {
            controller.setOnNewBySample(newValue);
        });
        onView.addListener((ObservableValue<? extends EventHandler<ActionEvent>> observable, EventHandler<ActionEvent> oldValue, EventHandler<ActionEvent> newValue) -> {
            controller.setOnView(newValue);
        });
        onEdit.addListener((ObservableValue<? extends EventHandler<ActionEvent>> observable, EventHandler<ActionEvent> oldValue, EventHandler<ActionEvent> newValue) -> {
            controller.setOnEdit(newValue);
        });
        onDelete.addListener((ObservableValue<? extends EventHandler<ActionEvent>> observable, EventHandler<ActionEvent> oldValue, EventHandler<ActionEvent> newValue) -> {
            controller.setOnDelete(newValue);
        });
        onRefresh.addListener((ObservableValue<? extends EventHandler<ActionEvent>> observable, EventHandler<ActionEvent> oldValue, EventHandler<ActionEvent> newValue) -> {
            controller.setOnRefresh(newValue);
        });
    }
    
    public ObjectProperty<EventHandler<ActionEvent>> onNewProperty(){
        return onNew;
    }
    
    public void setOnNew(EventHandler<ActionEvent> handler){
        onNew.set(handler);
    }
    
    public EventHandler<ActionEvent> getOnNew(){
        return onNew.get();
    }
    
    public ObjectProperty<EventHandler<ActionEvent>> onNewBySampleProperty(){
        return onNewBySample;
    }
    
    public void setOnNewBySample(EventHandler<ActionEvent> handler){
        onNewBySample.set(handler);
    }
    
    public EventHandler<ActionEvent> getOnNewBySample(){
        return onNewBySample.get();
    }
    
    public ObjectProperty<EventHandler<ActionEvent>> onViewProperty(){
        return onView;
    }
    
    public void setOnView(EventHandler<ActionEvent> handler){
        onView.set(handler);
    }
    
    public EventHandler<ActionEvent> getOnView(){
        return onView.get();
    }
    
    public ObjectProperty<EventHandler<ActionEvent>> onEditProperty(){
        return onEdit;
    }
    
    public void setOnEdit(EventHandler<ActionEvent> handler){
        onEdit.set(handler);
    }
    
    public EventHandler<ActionEvent> getOnEdit(){
        return onEdit.get();
    }
    
    public ObjectProperty<EventHandler<ActionEvent>> onDeleteProperty(){
        return onDelete;
    }
    
    public void setOnDelete(EventHandler<ActionEvent> handler){
        onDelete.set(handler);
    }
    
    public EventHandler<ActionEvent> getOnDelete(){
        return onDelete.get();
    }
    
    public ObjectProperty<EventHandler<ActionEvent>> onRefreshProperty(){
        return onRefresh;
    }
    
    public void setOnRefresh(EventHandler<ActionEvent> handler){
        onRefresh.set(handler);
    }
    
    public EventHandler<ActionEvent> getOnRefresh(){
        return onRefresh.get();
    }
}
