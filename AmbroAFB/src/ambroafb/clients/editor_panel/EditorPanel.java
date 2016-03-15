/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.clients.editor_panel;

import ambroafb.AmbroAFB;
import ambroafb.general.GeneralConfig;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;

/**
 *
 * @author tabramishvili
 */
public class EditorPanel extends HBox implements Initializable {

    @FXML
    private Button view, edit, delete;
    @FXML
    private ToggleButton refresh;
    @FXML
    private SplitMenuButton add;
    @FXML
    private MenuItem addBySample;

    public EditorPanel() {
        super();

        FXMLLoader loader = new FXMLLoader(AmbroAFB.class.getResource("/ambroafb/clients/editor_panel/EditorPanel.fxml"));
        loader.setResources(GeneralConfig.getInstance().getBundle());
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException ex) {
            Logger.getLogger(EditorPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public SplitMenuButton getAddButton() {
        return add;
    }

    public MenuItem getAddBySample() {
        return addBySample;
    }

    public Button getViewButton() {
        return view;
    }

    public Button getEditButton() {
        return edit;
    }

    public Button getDeleteButton() {
        return delete;
    }

    public ToggleButton getRefreshButton() {
        return refresh;
    }

    /*
     * *************************************************************************
     * Event Handlers *
     * *************************************************************************
     */
    
    public ObjectProperty<EventHandler<ActionEvent>> onNewProperty() {
        return add.onActionProperty();
    }

    public void setOnNew(EventHandler<ActionEvent> handler) {
        add.setOnAction(handler);
    }

    public EventHandler<ActionEvent> getOnNew() {
        return add.getOnAction();
    }

    public ObjectProperty<EventHandler<ActionEvent>> onNewBySampleProperty() {
        return addBySample.onActionProperty();
    }

    public void setOnNewBySample(EventHandler<ActionEvent> handler) {
        addBySample.setOnAction(handler);
    }

    public EventHandler<ActionEvent> getOnNewBySample() {
        return addBySample.getOnAction();
    }

    public ObjectProperty<EventHandler<ActionEvent>> onViewProperty() {
        return view.onActionProperty();
    }

    public void setOnView(EventHandler<ActionEvent> handler) {
        view.setOnAction(handler);
    }

    public EventHandler<ActionEvent> getOnView() {
        return view.getOnAction();
    }

    public ObjectProperty<EventHandler<ActionEvent>> onEditProperty() {
        return edit.onActionProperty();
    }

    public void setOnEdit(EventHandler<ActionEvent> handler) {
        edit.setOnAction(handler);
    }

    public EventHandler<ActionEvent> getOnEdit() {
        return edit.getOnAction();
    }

    public ObjectProperty<EventHandler<ActionEvent>> onDeleteProperty() {
        return delete.onActionProperty();
    }

    public void setOnDelete(EventHandler<ActionEvent> handler) {
        delete.setOnAction(handler);
    }

    public EventHandler<ActionEvent> getOnDelete() {
        return delete.getOnAction();
    }

    public ObjectProperty<EventHandler<ActionEvent>> onRefreshProperty() {
        return refresh.onActionProperty();
    }

    public void setOnRefresh(EventHandler<ActionEvent> handler) {
        refresh.setOnAction(handler);
    }

    public EventHandler<ActionEvent> getOnRefresh() {
        return refresh.getOnAction();
    }
}
