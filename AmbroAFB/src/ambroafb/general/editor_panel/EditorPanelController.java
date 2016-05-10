/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general.editor_panel;

import ambro.ATableView;
import ambroafb.clients.ClientsController;
import ambroafb.general.Utils;
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
    private Initializable outerController;
    
    @FXML
    private void edit(ActionEvent e) {
        EditorPanelable selected = (EditorPanelable)((ATableView)exit.getScene().lookup("#table")).getSelectionModel().getSelectedItem();
        try {
            EditorPanelable real = (EditorPanelable)Class.forName(getClassName("objectClass")).getMethod("getOneFromDB", int.class).invoke(null, selected.recId);
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
    
    @FXML
    private void view(ActionEvent e) {
        EditorPanelable selected = (EditorPanelable)((ATableView)exit.getScene().lookup("#table")).getSelectionModel().getSelectedItem();
        try {
            EditorPanelable real = (EditorPanelable)Class.forName(getClassName("objectClass")).getMethod("getOneFromDB", int.class).invoke(null, selected.recId);
            if (real != null) {
                selected.copyFrom(real);
            }    
        } catch (ClassNotFoundException | NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) { Logger.getLogger(EditorPanelController.class.getName()).log(Level.SEVERE, null, ex); }
        
        try {
            try {
                Dialogable dialog = (Dialogable)Class.forName(getClassName("dialogClass")).getConstructor(EditorPanelable.class).newInstance(selected);
                dialog.setDisabled();
                dialog.askClose(false);
                dialog.showAndWait();
            } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) { Logger.getLogger(EditorPanelController.class.getName()).log(Level.SEVERE, null, ex); }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(EditorPanelController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void fackeView(){
        EditorPanelable selected = (EditorPanelable)((ATableView)exit.getScene().lookup("#table")).getSelectionModel().getSelectedItem();
            Class objectClass = Utils.getClassByName(getClassName("objectClass"));
            System.out.println("object class: " + objectClass);
            EditorPanelable real = null; //(EditorPanelable)Utils.getInvokedClassMethod("getOneFromDB", int.class, null, selected.recId);
            if (real != null) {
                selected.copyFrom(real);
            }    
        
        try {
            try {
                Dialogable dialog = (Dialogable)Class.forName(getClassName("dialogClass")).getConstructor(EditorPanelable.class).newInstance(selected);
                dialog.setDisabled();
                dialog.askClose(false);
                dialog.showAndWait();
            } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) { Logger.getLogger(EditorPanelController.class.getName()).log(Level.SEVERE, null, ex); }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(EditorPanelController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    private void add(ActionEvent e) {
        EditorPanelable result = null;
        Class dialogClass = Utils.getClassByName(getClassName("dialogClass"));
        Dialogable dialogable = (Dialogable)Utils.getInstanceOfClass(dialogClass, null);
        result = (EditorPanelable) dialogable.getResult();

        if (result == null) {
            System.out.println("dialog is cancelled addClient");
        } else {
            System.out.println("changed client: " + result);
            Class objectClass = Utils.getClassByName(getClassName("objectClass"));
            result = (EditorPanelable) Utils.getInvokedClassMethod("saveOneToDB", new Class[]{objectClass}, objectClass, result); 
            if (result != null) {
                ((ATableView)exit.getScene().lookup("#table")).getItems().add(result);
            }
        }
    }
    
    @FXML
    private void refresh(ActionEvent e) {
        ATableView table = (ATableView)exit.getScene().lookup("#table");
        EditorPanelable selected = (EditorPanelable)table.getSelectionModel().getSelectedItem();
        table.getItems().clear();
        try {
            Class.forName(getClassName("controllerClass")).getMethod("asignTable").invoke(outerController);
            if(selected != null)
                Class.forName(getClassName("controllerClass")).getMethod("selectOneAgain", Class.forName(getClassName("objectClass"))).invoke(outerController, selected);
        } catch (SecurityException | IllegalArgumentException | NoSuchMethodException | IllegalAccessException | InvocationTargetException | ClassNotFoundException ex) { Logger.getLogger(EditorPanelController.class.getName()).log(Level.SEVERE, null, ex); }
    }
       
    /**
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }
  
    public void buttonsMainPropertysBinder (TableView table){
        edit.disableProperty().bind(table.getSelectionModel().selectedItemProperty().isNull());
        view.disableProperty().bind(table.getSelectionModel().selectedItemProperty().isNull());
        addBySample.disableProperty().bind(table.getSelectionModel().selectedItemProperty().isNull());
    }

    public void setOuterController(Initializable controller){
        outerController = controller;
    }
    
    private String getClassName(String type){
        String rtrn = exit.getScene().getProperties().get("controller").toString();
        if(type.equals("controllerClass"))
            return rtrn.substring(0, rtrn.indexOf("@"));
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
