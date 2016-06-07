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
import ambroafb.general.interfaces.Filterable;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
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
    private ToggleButton refresh;
    
    @FXML
    private Initializable outerController;
    
    @FXML
    private TextField search;

    private enum CLASS_TYPE {OBJECT, DIALOG, CONTROLLER};
    
    private ObservableList<EditorPanelable> tableData;
    
    @FXML
    private void delete(ActionEvent e) {
        Stage editorPanelSceneStage = (Stage) exit.getScene().getWindow();
        Stage dialogStage = Utils.getStageFor(editorPanelSceneStage, Dialogable.LOCAL_NAME);
        if (dialogStage == null || !dialogStage.isShowing()){
            EditorPanelable selected = (EditorPanelable)((ATableView)exit.getScene().lookup("#table")).getSelectionModel().getSelectedItem();
            Class objectClass = Utils.getClassByName(getClassName(CLASS_TYPE.OBJECT));
            EditorPanelable real = (EditorPanelable)Utils.getInvokedClassMethod(objectClass, "getOneFromDB", new Class[]{int.class}, null, selected.recId);
            if (real != null) {
                selected.copyFrom(real);
            }
            Class dialogClass = Utils.getClassByName(getClassName(CLASS_TYPE.DIALOG));
            Stage ownerStage = (Stage) exit.getScene().getWindow();
            Dialogable dialog = (Dialogable) Utils.getInstanceOfClass(dialogClass, new Class[]{EditorPanelable.class, EDITOR_BUTTON_TYPE.class, Stage.class}, selected, EDITOR_BUTTON_TYPE.DELETE, ownerStage);

            EditorPanelable result = dialog.getResult();
            if (result != null){
                boolean isDeleted = (boolean) Utils.getInvokedClassMethod(objectClass, "deleteOneFromDB", new Class[]{int.class}, null, selected.recId);
                if(isDeleted)
                    tableData.remove(selected);
            }
        }
        else {
            dialogStage.requestFocus();
        }
    }
    
    @FXML
    private void edit(ActionEvent e) {
        Stage editorPanelSceneStage = (Stage) exit.getScene().getWindow();
        Stage dialogStage = Utils.getStageFor(editorPanelSceneStage, Dialogable.LOCAL_NAME);
        if (dialogStage == null || !dialogStage.isShowing()){
            EditorPanelable selected = (EditorPanelable)((ATableView)exit.getScene().lookup("#table")).getSelectionModel().getSelectedItem();
            Class objectClass = Utils.getClassByName(getClassName(CLASS_TYPE.OBJECT));
            EditorPanelable real = (EditorPanelable) Utils.getInvokedClassMethod(objectClass, "getOneFromDB", new Class[]{int.class}, null, selected.recId);
            if (real != null) {
                selected.copyFrom(real);
            }
            EditorPanelable backup = selected.cloneWithID();
            Class dialogClass = Utils.getClassByName(getClassName(CLASS_TYPE.DIALOG));
            Stage ownerStage = (Stage) exit.getScene().getWindow();
            Dialogable dialog = (Dialogable) Utils.getInstanceOfClass(dialogClass, new Class[]{EditorPanelable.class, EDITOR_BUTTON_TYPE.class, Stage.class}, selected, EDITOR_BUTTON_TYPE.EDIT, ownerStage);

            EditorPanelable result = dialog.getResult();
            if (result == null){
                selected.copyFrom(backup);
            } else {
                 Utils.getInvokedClassMethod(objectClass, "saveOneToDB", new Class[]{objectClass}, null, result);
            }
        }
        else {
            dialogStage.requestFocus();
        }
    }
    
    @FXML
    private void view(ActionEvent e) {
        Stage editorPanelSceneStage = (Stage) exit.getScene().getWindow();
        Stage dialogStage = Utils.getStageFor(editorPanelSceneStage, Dialogable.LOCAL_NAME);
        if(dialogStage == null || !dialogStage.isShowing()){
            System.out.println("dialog stage: " + dialogStage);
            EditorPanelable selected = (EditorPanelable)((ATableView)exit.getScene().lookup("#table")).getSelectionModel().getSelectedItem();
            EditorPanelable real = (EditorPanelable)Utils.getInvokedClassMethod(Utils.getClassByName(getClassName(CLASS_TYPE.OBJECT)), "getOneFromDB", new Class[]{int.class}, null, selected.recId);
            if (real != null) {
                selected.copyFrom(real);
            }
            Class dialogClass = Utils.getClassByName(getClassName(CLASS_TYPE.DIALOG));
            Stage ownerStage = (Stage) exit.getScene().getWindow();
            Dialogable dialog = (Dialogable)Utils.getInstanceOfClass(dialogClass, new Class[]{EditorPanelable.class, EDITOR_BUTTON_TYPE.class, Stage.class}, selected, EDITOR_BUTTON_TYPE.VIEW, ownerStage);

            dialog.showAndWait();
        }
        else {
            dialogStage.requestFocus();
        }
    }
    
    @FXML
    private void add(ActionEvent e) {
        Stage editorPanelSceneStage = (Stage) exit.getScene().getWindow();
        Stage dialogStage = Utils.getStageFor(editorPanelSceneStage, Dialogable.LOCAL_NAME);
        if(dialogStage == null || !dialogStage.isShowing()){
            Dialogable dialog = (Dialogable)Utils.getInstanceOfClass(Utils.getClassByName(getClassName(CLASS_TYPE.DIALOG)), new Class[]{EditorPanelable.class, EDITOR_BUTTON_TYPE.class, Stage.class}, null, EDITOR_BUTTON_TYPE.ADD, (Stage) exit.getScene().getWindow());

            EditorPanelable result = (EditorPanelable)dialog.getResult();
            if (result != null) {
                Class objectClass = Utils.getClassByName(getClassName(CLASS_TYPE.OBJECT));
                result = (EditorPanelable) Utils.getInvokedClassMethod(objectClass, "saveOneToDB", new Class[]{objectClass}, null, result); 

                if (result != null) {
                    tableData.add(result);
                }
            }
        }
        else {
            dialogStage.requestFocus();
        }
    }
    
    @FXML
    private void addBySample(ActionEvent e) {
        Stage editorPanelSceneStage = (Stage) exit.getScene().getWindow();
        Stage dialogStage = Utils.getStageFor(editorPanelSceneStage, Dialogable.LOCAL_NAME);
        if(dialogStage == null || !dialogStage.isShowing()){
            EditorPanelable selected = ((EditorPanelable)((ATableView)exit.getScene().lookup("#table")).getSelectionModel().getSelectedItem()).cloneWithoutID();
            Stage ownerStage = (Stage) exit.getScene().getWindow();
            Dialogable dialog = (Dialogable) Utils.getInstanceOfClass(Utils.getClassByName(getClassName(CLASS_TYPE.DIALOG)), new Class[]{EditorPanelable.class, EDITOR_BUTTON_TYPE.class, Stage.class}, selected, EDITOR_BUTTON_TYPE.ADD, ownerStage);

            EditorPanelable result = (EditorPanelable) dialog.getResult();
            Class objectClass = Utils.getClassByName(getClassName(CLASS_TYPE.OBJECT));
            result = (EditorPanelable) Utils.getInvokedClassMethod(objectClass, "saveOneToDB", new Class[]{objectClass}, null, result); 
            if (result != null) {
                tableData.add(result);
            }
        }
        else {
            dialogStage.requestFocus();
        }
    }
    
    @FXML
    private void refresh(ActionEvent e) {
        Stage editorPanelSceneStage = (Stage) exit.getScene().getWindow();
        Stage filterStage = Utils.getStageFor(editorPanelSceneStage, Filterable.LOCAL_NAME);
        if (filterStage == null || !filterStage.isShowing()){
            ATableView table = (ATableView)exit.getScene().lookup("#table");
            EditorPanelable selected = (EditorPanelable)table.getSelectionModel().getSelectedItem();
            Class controllerClass = Utils.getClassByName(getClassName(CLASS_TYPE.CONTROLLER));
            Utils.getInvokedClassMethod(controllerClass, "reAssignTable", new Class[]{boolean.class}, outerController, false);
            selectOneAgain(selected);
        }
        else {
            filterStage.requestFocus();
        }
        refresh.setSelected(false);
    }
    
    /**
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }
    
    public Button getExitButton(){
        return exit;
    }
    
    public void setTableDataList(TableView<EditorPanelable> table, ObservableList<EditorPanelable> list){
        tableData = list;
        FilteredList<EditorPanelable> filteredData = new FilteredList<>(tableData, p -> true);
        search.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(object -> {
                return (newValue == null || newValue.isEmpty()|| object.toStringForSearch().contains(newValue.toLowerCase()));
            });
        });
        SortedList<EditorPanelable> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedData);
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
        if (selected == null) return;
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
