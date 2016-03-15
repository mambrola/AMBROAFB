/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.clients.editor_panel;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

/**
 * FXML Controller class
 *
 * @author tabramishvili
 */
public class EditorPanelController implements Initializable {

    private EventHandler<ActionEvent> onNew, onNewBySample, onView, onEdit, onDelete, onRefresh;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    
    @FXML
    public void delete(ActionEvent event){
        System.out.println("delete");
        if (onDelete != null){
            onDelete.handle(event);
        }
    }
    
    @FXML
    public void edit(ActionEvent event){
        System.out.println("edit");
        if (onEdit != null){
            onEdit.handle(event);
        }
    }
    
    
    @FXML
    public void view(ActionEvent event){
        System.out.println("view");
        if (onView != null){
            onView.handle(event);
        }
    }
    
    
    @FXML
    public void add(ActionEvent event){
        System.out.println("add");
        if (onNew != null){
            onNew.handle(event);
        }
    }
    
    
    @FXML
    public void addBySample(ActionEvent event){
        System.out.println("add by sample");
        if (onNewBySample != null){
            onNewBySample.handle(event);
        }
    }
    
    
    @FXML
    public void refresh(ActionEvent event){
        System.out.println("refresh");
        if (onRefresh != null){
            onRefresh.handle(event);
        }
    }
    
    public void setOnNew(EventHandler<ActionEvent> handler){
        onNew = handler;
    }
    
    public void setOnNewBySample(EventHandler<ActionEvent> handler){
        onNewBySample = handler;
    }
    
    public void setOnView(EventHandler<ActionEvent> handler){
        onView = handler;
    }
    
    public void setOnEdit(EventHandler<ActionEvent> handler){
        onEdit = handler;
    }
    
    public void setOnDelete(EventHandler<ActionEvent> handler){
        onDelete = handler;
    }
    
    public void setOnRefresh(EventHandler<ActionEvent> handler){
        onRefresh = handler;
    }
    
}
