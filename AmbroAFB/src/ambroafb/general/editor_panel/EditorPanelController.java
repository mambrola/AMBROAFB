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
    
    private enum CLASS_TYPE {OBJECT, DIALOG, CONTROLLER};
    
    @FXML
    private void delete(ActionEvent e) {
        EditorPanelable selected = (EditorPanelable)((ATableView)exit.getScene().lookup("#table")).getSelectionModel().getSelectedItem();
        Class objectClass = Utils.getClassByName(getClassName(CLASS_TYPE.OBJECT));
        EditorPanelable real = (EditorPanelable)Utils.getInvokedClassMethod(objectClass, "getOneFromDB", new Class[]{int.class}, null, selected.recId);
        if (real != null) {
            selected.copyFrom(real);
        }
        Class dialogClass = Utils.getClassByName(getClassName(CLASS_TYPE.DIALOG));
        Dialogable dialog = (Dialogable) Utils.getInstanceOfClass(dialogClass, new Class[]{EditorPanelable.class, EDITOR_BUTTON_TYPE.class}, selected, EDITOR_BUTTON_TYPE.DELETE);
        if (dialog.getResult() != null){
            boolean isDeleted = (boolean) Utils.getInvokedClassMethod(objectClass, "deleteOneFromDB", new Class[]{int.class}, null, selected.recId);
            if(isDeleted)
                ((ATableView)exit.getScene().lookup("#table")).getItems().remove(selected);
        }
    }
    
    @FXML
    private void edit(ActionEvent e) {
        EditorPanelable selected = (EditorPanelable)((ATableView)exit.getScene().lookup("#table")).getSelectionModel().getSelectedItem();
        Class objectClass = Utils.getClassByName(getClassName(CLASS_TYPE.OBJECT));
        EditorPanelable real = (EditorPanelable) Utils.getInvokedClassMethod(objectClass, "getOneFromDB", new Class[]{int.class}, null, selected.recId);
        if (real != null) {
            selected.copyFrom(real);
        }
        EditorPanelable backup = selected.cloneWithID();
        Class dialogClass = Utils.getClassByName(getClassName(CLASS_TYPE.DIALOG));
        Dialogable dialog = (Dialogable) Utils.getInstanceOfClass(dialogClass, new Class[]{EditorPanelable.class, EDITOR_BUTTON_TYPE.class}, selected, EDITOR_BUTTON_TYPE.EDIT);
        EditorPanelable result = dialog.getResult();
        if (result == null){
            selected.copyFrom(backup);
        } else {
             Utils.getInvokedClassMethod(objectClass, "saveOneToDB", new Class[]{objectClass}, null, result);
        }
    }
    
    @FXML
    private void view(ActionEvent e) {
        EditorPanelable selected = (EditorPanelable)((ATableView)exit.getScene().lookup("#table")).getSelectionModel().getSelectedItem();
        EditorPanelable real = (EditorPanelable)Utils.getInvokedClassMethod(Utils.getClassByName(getClassName(CLASS_TYPE.OBJECT)), "getOneFromDB", new Class[]{int.class}, null, selected.recId);
        if (real != null) {
            selected.copyFrom(real);
        }
        Class dialogClass = Utils.getClassByName(getClassName(CLASS_TYPE.DIALOG));
        Dialogable dialog = (Dialogable)Utils.getInstanceOfClass(dialogClass, new Class[]{EditorPanelable.class, EDITOR_BUTTON_TYPE.class}, selected, EDITOR_BUTTON_TYPE.VIEW);
        dialog.showAndWait();
    }
    
    @FXML
    private void add(ActionEvent e) {
        EditorPanelable result = (EditorPanelable) ((Dialogable)Utils.getInstanceOfClass(Utils.getClassByName(getClassName(CLASS_TYPE.DIALOG)), null)).getResult();
        if (result != null) {
            Class objectClass = Utils.getClassByName(getClassName(CLASS_TYPE.OBJECT));
            result = (EditorPanelable) Utils.getInvokedClassMethod(objectClass, "saveOneToDB", new Class[]{objectClass}, null, result); 
            if (result != null) {
                ((ATableView)exit.getScene().lookup("#table")).getItems().add(result);
            }
        }
    }
    
    @FXML
    private void addBySample(ActionEvent e) {
        EditorPanelable selected = ((EditorPanelable)((ATableView)exit.getScene().lookup("#table")).getSelectionModel().getSelectedItem()).cloneWithoutID();
        EditorPanelable result = (EditorPanelable) ((Dialogable)Utils.getInstanceOfClass(Utils.getClassByName(getClassName(CLASS_TYPE.DIALOG)), new Class[]{EditorPanelable.class, EDITOR_BUTTON_TYPE.class}, selected, EDITOR_BUTTON_TYPE.ADD)).getResult();
        Class objectClass = Utils.getClassByName(getClassName(CLASS_TYPE.OBJECT));
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
        Class controllerClass = Utils.getClassByName(getClassName(CLASS_TYPE.CONTROLLER));
        Utils.getInvokedClassMethod(controllerClass, "asignTable", null, outerController);
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
    
    private String getClassName(CLASS_TYPE type){
        String rtrn = exit.getScene().getProperties().get("controller").toString();
        if(type.equals(CLASS_TYPE.CONTROLLER))
            return rtrn.substring(0, rtrn.indexOf("@"));
        String path = rtrn.substring(0, rtrn.lastIndexOf(".") + 1);
        String className = rtrn.substring(path.length(), rtrn.lastIndexOf("Controller"));
        switch (className){
            case "Countries":
                rtrn = path + (type.equals(CLASS_TYPE.DIALOG) ? "dialog." : "") + "Country" + (type.equals(CLASS_TYPE.DIALOG) ? "Dialog" : "");
                break;
            default:
                rtrn = path + (type.equals(CLASS_TYPE.DIALOG) ? "dialog." : "") + className.substring(0, className.length() - 1) + (type.equals(CLASS_TYPE.DIALOG) ? "Dialog" : "");
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
