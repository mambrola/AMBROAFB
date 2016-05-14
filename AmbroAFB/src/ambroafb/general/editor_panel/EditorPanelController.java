/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general.editor_panel;

import ambro.ATableView;
import ambroafb.general.Utils;
import ambroafb.general.interfaces.Dialogable;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.general.Names.EDITOR_BUTTON_TYPE;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;
import javafx.stage.Stage;


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
    private void delete(ActionEvent e) {
        EditorPanelable selected = (EditorPanelable)((ATableView)exit.getScene().lookup("#table")).getSelectionModel().getSelectedItem();
        Class objectClass = Utils.getClassByName(getClassName("objectClass"));
        EditorPanelable real = (EditorPanelable)Utils.getInvokedClassMethod(objectClass, "getOneFromDB", new Class[]{int.class}, null, selected.recId);
        if (real != null) {
            selected.copyFrom(real);
        }
        Class dialogClass = Utils.getClassByName(getClassName("dialogClass"));
        Dialogable dialog = (Dialogable) Utils.getInstanceOfClass(dialogClass, new Class[]{EditorPanelable.class, EDITOR_BUTTON_TYPE.class}, selected, EDITOR_BUTTON_TYPE.DELETE);
        dialog.showAndWait();
        boolean allowToMakeOperation = dialog.allowToMakeOperation();
        
        if (allowToMakeOperation){
            boolean isDeleted = (boolean) Utils.getInvokedClassMethod(objectClass, "deleteOneFromDB", new Class[]{int.class}, null, selected.recId);
            if(isDeleted)
                ((ATableView)exit.getScene().lookup("#table")).getItems().remove(selected);
        }
    }
    
    
    @FXML
    private void edit(ActionEvent e) {
        EditorPanelable selected = (EditorPanelable)((ATableView)exit.getScene().lookup("#table")).getSelectionModel().getSelectedItem();
        EditorPanelable real = (EditorPanelable) Utils.getInvokedClassMethod(Utils.getClassByName(getClassName("objectClass")), "getOneFromDB", new Class[]{int.class}, null, selected.recId);
        if (real != null) {
            selected.copyFrom(real);
        }
        EditorPanelable backup = selected.cloneWithID();
        
        Class dialogClass = Utils.getClassByName(getClassName("dialogClass"));
        Dialogable dialog = (Dialogable) Utils.getInstanceOfClass(dialogClass, new Class[]{EditorPanelable.class, EDITOR_BUTTON_TYPE.class}, selected, EDITOR_BUTTON_TYPE.EDIT);
//        EditorPanelable result = dialog.getResult();
        dialog.showAndWait();
        boolean allowToMakeOperation = dialog.allowToMakeOperation();
        if (!allowToMakeOperation)
            selected.copyFrom(backup);
    }
    
    @FXML
    private void view(ActionEvent e) {
        EditorPanelable selected = (EditorPanelable)((ATableView)exit.getScene().lookup("#table")).getSelectionModel().getSelectedItem();
        EditorPanelable real = (EditorPanelable)Utils.getInvokedClassMethod(Utils.getClassByName(getClassName("objectClass")), "getOneFromDB", new Class[]{int.class}, null, selected.recId);
        if (real != null) {
            selected.copyFrom(real);
        }
        
        Class dialogClass = Utils.getClassByName(getClassName("dialogClass"));
        Dialogable dialog = (Dialogable)Utils.getInstanceOfClass(dialogClass, new Class[]{EditorPanelable.class, EDITOR_BUTTON_TYPE.class}, selected, EDITOR_BUTTON_TYPE.VIEW);
        
        dialog.showAndWait();
    }
    
    @FXML
    private void add(ActionEvent e) {
        EditorPanelable result = (EditorPanelable) ((Dialogable)Utils.getInstanceOfClass(Utils.getClassByName(getClassName("dialogClass")), null)).getResult();
        if (result != null) {
            Class objectClass = Utils.getClassByName(getClassName("objectClass"));
            result = (EditorPanelable) Utils.getInvokedClassMethod(objectClass, "saveOneToDB", new Class[]{objectClass}, null, result); 
            if (result != null) {
                ((ATableView)exit.getScene().lookup("#table")).getItems().add(result);
            }
        }
    }
    
    @FXML
    private void addBySample(ActionEvent e) {
        EditorPanelable selected = ((EditorPanelable)((ATableView)exit.getScene().lookup("#table")).getSelectionModel().getSelectedItem()).cloneWithoutID();
        EditorPanelable result = (EditorPanelable) ((Dialogable)Utils.getInstanceOfClass(Utils.getClassByName(getClassName("dialogClass")), new Class[]{EditorPanelable.class, EDITOR_BUTTON_TYPE.class}, selected, EDITOR_BUTTON_TYPE.ADD)).getResult();
        Class objectClass = Utils.getClassByName(getClassName("objectClass"));
        result = (EditorPanelable) Utils.getInvokedClassMethod(objectClass, "saveOneToDB", new Class[]{objectClass}, null, result); 
        if (result != null) {
            ((ATableView)exit.getScene().lookup("#table")).getItems().add(result);
        }    
    }
    
    @FXML
    private void refresh(ActionEvent e) {
        ATableView table = (ATableView)exit.getScene().lookup("#table");
        EditorPanelable selected = (EditorPanelable)table.getSelectionModel().getSelectedItem();
        table.getItems().clear();
        Class controllerClass = Utils.getClassByName(getClassName("controllerClass"));
        Utils.getInvokedClassMethod(controllerClass, "asignTable", null, outerController);
        //Utils.getInvokedClassMethod(controllerClass, "selectOneAgain", new Class[]{Utils.getClassByName(getClassName("objectClass"))}, outerController, selected);
        selectOneAgain(selected);
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
        delete.disableProperty().bind(table.getSelectionModel().selectedItemProperty().isNull());
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
        switch (className){
            case "Countries":
                rtrn = path + (type.equals("dialogClass") ? "dialog." : "") + "Country" + (type.equals("dialogClass") ? "Dialog" : "");
                break;
            default:
                rtrn = path + (type.equals("dialogClass") ? "dialog." : "") + className.substring(0, className.length() - 1) + (type.equals("dialogClass") ? "Dialog" : "");
        }
        return rtrn;
    }

    private void selectOneAgain(EditorPanelable selected) {
        ATableView table = (ATableView) exit.getScene().lookup("#table");
        int i = table.getItems().size() - 1;
        while (i >= 0 && ((EditorPanelable) table.getItems().get(i)).getRecId() != selected.getRecId()) {
            i--;
        }
        if (i >= 0) {
            table.getSelectionModel().select(i);
        }
    }
}
