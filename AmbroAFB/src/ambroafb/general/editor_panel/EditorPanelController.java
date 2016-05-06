/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general.editor_panel;

import ambro.ATableView;
import ambroafb.general.interfaces.Dialogable;
import ambroafb.general.interfaces.EditorPanelable;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;

/**
 * FXML Controller class
 *
 * @author mambroladze
 */
public class EditorPanelController implements Initializable {

    @FXML
    private Button exit, delete, edit, view;
    
    @FXML
    private MenuItem addBySample;
    

    @FXML
    private void edit(ActionEvent e) {
        EditorPanelable selected = (EditorPanelable)((ATableView)exit.getScene().lookup("#table")).getSelectionModel().getSelectedItem();
        try {
            EditorPanelable real = (EditorPanelable)Class.forName(getClassName("")).getMethod("getOneFromDB", int.class).invoke(null, selected.recId);
            if (real != null) {
                selected.copyFrom(real);
            }    
        } catch (ClassNotFoundException | NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) { Logger.getLogger(EditorPanelController.class.getName()).log(Level.SEVERE, null, ex); }
        EditorPanelable backup = selected.cloneWithID();
        EditorPanelable result = null;
        try {
            result = (EditorPanelable)((Dialogable)Class.forName(getClassName("dialogClass")).getConstructor(EditorPanelable.class).newInstance(selected)).getResult();
        } catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) { Logger.getLogger(EditorPanelController.class.getName()).log(Level.SEVERE, null, ex); }
        if (result == null)
            selected.copyFrom(backup);
    }

    
       
    /**
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }
  
    public void disablePropertyBinder (TableView table){
        edit.disableProperty().bind(table.getSelectionModel().selectedItemProperty().isNull());
        view.disableProperty().bind(table.getSelectionModel().selectedItemProperty().isNull());
        addBySample.disableProperty().bind(table.getSelectionModel().selectedItemProperty().isNull());
    
    
    }
    
    private String getClassName(String type){
        String rtrn = (String)exit.getScene().getProperties().get("controller").toString();
        String path = rtrn.substring(0, rtrn.lastIndexOf(".") + 1);
        String className = rtrn.substring(path.length(), rtrn.lastIndexOf("Controller"));
        System.out.println("<-------> " + rtrn + " : " + path + " : " + className);
        switch (className){
            case "Countries":
                rtrn = path + (type.equals("dialogClass") ? "dialog." : "") + "Country" + (type.equals("dialogClass") ? "Dialog" : "");
                break;
            default:
                rtrn = path + (type.equals("dialogClass") ? "dialog." : "") + className.substring(0, className.length() - 1) + (type.equals("dialogClass") ? "Dialog" : "");
        }
        return rtrn;
    }
}
