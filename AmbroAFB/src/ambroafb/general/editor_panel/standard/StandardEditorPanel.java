/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general.editor_panel.standard;

import ambroafb.general.editor_panel.EditorPanel;
import ambroafb.general.interfaces.Dialogable;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.general.interfaces.EditorPanelableManager;
import ambroafb.general.interfaces.ListingStage;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import javafx.event.ActionEvent;

/**
 *
 * @author dkobuladze
 */
public class StandardEditorPanel extends EditorPanel {

    public StandardEditorPanel() {
        assignLoader();
    }
    
    @Override
    protected void componentsInitialize(URL location, ResourceBundle resources){
    }
    
    @Override
    public void delete(ActionEvent event) {
        ListingStage editorPanelSceneStage = (ListingStage) exit.getScene().getWindow();
        EditorPanelable selected = selectedItem;
        EditorPanelableManager manager = editorPanelSceneStage.getEPManager();
        Consumer<Object> successAction = (objFromDB) -> {
            if (objFromDB != null){
                selected.copyFrom((EditorPanelable)objFromDB);
            }
            Dialogable dialog = manager.getDialogFor(editorPanelSceneStage, EditorPanel.EDITOR_BUTTON_TYPE.DELETE, selected);
            EditorPanelable result = dialog.getResult();
            if (result != null){
                observers.forEach((observer) -> observer.notifyDelete(result));
            }
        };
        manager.getDataFetchProvider().getOneFromDB(selected.getRecId(), successAction, null);
    }

    @Override
    public void edit(ActionEvent event) {
        ListingStage editorPanelSceneStage = (ListingStage) exit.getScene().getWindow();
        EditorPanelable selected = selectedItem;
        EditorPanelableManager manager = editorPanelSceneStage.getEPManager(); // EPManagerFactory.getEPManager(selected);
        Consumer<Object> successAction = (ObjFromDB) -> {
            if (ObjFromDB != null) {
                selected.copyFrom((EditorPanelable)ObjFromDB);
            }
            EditorPanelable backup = selected.cloneWithID();
            Dialogable dialog = manager.getDialogFor(editorPanelSceneStage, EditorPanel.EDITOR_BUTTON_TYPE.EDIT, selected);
            EditorPanelable result = dialog.getResult();
            if (result == null){
                selected.copyFrom(backup);
            }
            else {
                observers.forEach((observer) -> observer.notifyEdit(result));
            }
        };
        manager.getDataFetchProvider().getOneFromDB(selected.getRecId(), successAction, null);
    }

    @Override
    public void view(ActionEvent event) {
        ListingStage editorPanelSceneStage = (ListingStage) exit.getScene().getWindow();
        EditorPanelable selected = selectedItem;
        EditorPanelableManager manager = editorPanelSceneStage.getEPManager();
        Consumer<Object> successAction = (ObjFromDB) -> {
            if (ObjFromDB != null) {
                selected.copyFrom((EditorPanelable)ObjFromDB);
            }
            Dialogable dialog = manager.getDialogFor(editorPanelSceneStage, EditorPanel.EDITOR_BUTTON_TYPE.VIEW, selected);
            dialog.showAndWait();
        };
        manager.getDataFetchProvider().getOneFromDB(selected.getRecId(), successAction, null);
    }

    @Override
    public void add(ActionEvent event) {
        ListingStage editorPanelSceneStage = (ListingStage) exit.getScene().getWindow();
        EditorPanelableManager manager = editorPanelSceneStage.getEPManager();
        Dialogable dialog = manager.getDialogFor(editorPanelSceneStage, EditorPanel.EDITOR_BUTTON_TYPE.ADD, null);

        EditorPanelable result = dialog.getResult();
        if (result != null) {
            observers.forEach((observer) -> observer.notifyAdd(result));
        }
    }

    @Override
    public void addBySample(ActionEvent event) {
        ListingStage editorPanelSceneStage = (ListingStage) exit.getScene().getWindow();
        EditorPanelable selected = selectedItem;
        EditorPanelableManager manager = editorPanelSceneStage.getEPManager();
        Consumer<Object> successAction = (objFromDB) -> {
            EditorPanelable cloneOfSelected;
            if (objFromDB != null) {
                cloneOfSelected = ((EditorPanelable)objFromDB).cloneWithoutID();
            }
            else {
                cloneOfSelected = selected.cloneWithoutID();
            }

            Dialogable dialog = manager.getDialogFor(editorPanelSceneStage, EditorPanel.EDITOR_BUTTON_TYPE.ADD_BY_SAMPLE, cloneOfSelected);
            EditorPanelable result = dialog.getResult();
            if (result != null) {
                observers.forEach((observer) -> observer.notifyAddBySample(result));
            }
        };
        manager.getDataFetchProvider().getOneFromDB(selected.getRecId(), successAction, null);
    }

}
