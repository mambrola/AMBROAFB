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

    private final ObjectProperty<EventHandler<ActionEvent>> onNew = new SimpleObjectProperty<>();
    private final ObjectProperty<EventHandler<ActionEvent>> onNewBySample = new SimpleObjectProperty<>();
    private final ObjectProperty<EventHandler<ActionEvent>> onView = new SimpleObjectProperty<>();
    private final ObjectProperty<EventHandler<ActionEvent>> onEdit = new SimpleObjectProperty<>();
    private final ObjectProperty<EventHandler<ActionEvent>> onDelete = new SimpleObjectProperty<>();
    private final ObjectProperty<EventHandler<ActionEvent>> onRefresh = new SimpleObjectProperty<>();

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

    @FXML
    private void delete(ActionEvent event) {
        System.out.println("delete");
        if (onDelete.get() != null) {
            onDelete.get().handle(event);
        }
    }

    @FXML
    private void edit(ActionEvent event) {
        System.out.println("edit");
        if (onEdit.get() != null) {
            onEdit.get().handle(event);
        }
    }

    @FXML
    private void view(ActionEvent event) {
        System.out.println("view");
        if (onView.get() != null) {
            onView.get().handle(event);
        }
    }

    @FXML
    private void add(ActionEvent event) {
        System.out.println("add");
        if (onNew.get() != null) {
            onNew.get().handle(event);
        }
    }

    @FXML
    private void addBySample(ActionEvent event) {
        System.out.println("add by sample");
        if (onNewBySample.get() != null) {
            onNewBySample.get().handle(event);
        }
    }

    @FXML
    private void refresh(ActionEvent event) {
        System.out.println("refresh");
        if (onRefresh.get() != null) {
            onRefresh.get().handle(event);
        }
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
        return onNew;
    }

    public void setOnNew(EventHandler<ActionEvent> handler) {
        onNew.set(handler);
    }

    public EventHandler<ActionEvent> getOnNew() {
        return onNew.get();
    }

    public ObjectProperty<EventHandler<ActionEvent>> onNewBySampleProperty() {
        return onNewBySample;
    }

    public void setOnNewBySample(EventHandler<ActionEvent> handler) {
        onNewBySample.set(handler);
    }

    public EventHandler<ActionEvent> getOnNewBySample() {
        return onNewBySample.get();
    }

    public ObjectProperty<EventHandler<ActionEvent>> onViewProperty() {
        return onView;
    }

    public void setOnView(EventHandler<ActionEvent> handler) {
        onView.set(handler);
    }

    public EventHandler<ActionEvent> getOnView() {
        return onView.get();
    }

    public ObjectProperty<EventHandler<ActionEvent>> onEditProperty() {
        return onEdit;
    }

    public void setOnEdit(EventHandler<ActionEvent> handler) {
        onEdit.set(handler);
    }

    public EventHandler<ActionEvent> getOnEdit() {
        return onEdit.get();
    }

    public ObjectProperty<EventHandler<ActionEvent>> onDeleteProperty() {
        return onDelete;
    }

    public void setOnDelete(EventHandler<ActionEvent> handler) {
        onDelete.set(handler);
    }

    public EventHandler<ActionEvent> getOnDelete() {
        return onDelete.get();
    }

    public ObjectProperty<EventHandler<ActionEvent>> onRefreshProperty() {
        return onRefresh;
    }

    public void setOnRefresh(EventHandler<ActionEvent> handler) {
        onRefresh.set(handler);
    }

    public EventHandler<ActionEvent> getOnRefresh() {
        return onRefresh.get();
    }
}