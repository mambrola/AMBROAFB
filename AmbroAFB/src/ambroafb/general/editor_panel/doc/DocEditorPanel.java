/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general.editor_panel.doc;

import ambro.AView;
import ambroafb.docs.Doc;
import ambroafb.docs.filter.DocFilter;
import ambroafb.docs.types.DocManagersFactory;
import ambroafb.general.Names;
import ambroafb.general.StageUtils;
import ambroafb.general.StagesContainer;
import ambroafb.general.editor_panel.EditorPanel;
import ambroafb.general.interfaces.Dialogable;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.general.interfaces.EditorPanelableManager;
import ambroafb.general.interfaces.FilterModel;
import ambroafb.general.interfaces.Filterable;
import ambroafb.general.interfaces.ListingStage;
import ambroafb.general_scene.doc_table_list.DocTableListController;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.stage.Stage;

/**
 *
 * @author dkobuladze
 */
public class DocEditorPanel extends EditorPanel implements Initializable {

    public DocEditorPanel() {
        assignLoader();
    }
    
    @Override
    protected void componentsInitialize(URL location, ResourceBundle resources){
    }

    @Override
    public void delete(ActionEvent event) {
        Stage docEditorPanelSceneStage = (Stage) exit.getScene().getWindow();
        Stage dialogStage = StagesContainer.getStageFor(docEditorPanelSceneStage, Names.LEVEL_FOR_PATH);
        if(dialogStage == null || !dialogStage.isShowing()){
            Doc selected = (Doc)((AView)exit.getScene().lookup("#aview")).getCustomSelectedItem();
            EditorPanelableManager manager = DocManagersFactory.getEPManager(selected);
            Consumer<EditorPanelable> successAction = (obj) -> {
                Dialogable dialog = manager.getDialogFor(docEditorPanelSceneStage, Names.EDITOR_BUTTON_TYPE.DELETE, obj);
                List<Doc> result = dialog.getResult();
                if (!result.isEmpty()){
                    int selectedIndex = tableData.indexOf(selected);
                    tableData.remove(selected);
                    if (selected.isParentDoc()){
                        List<Doc> childrenDocs = tableData.stream().map((elem) -> (Doc)elem).
                                                            filter((doc) -> doc.getParentRecId() == selected.getRecId()).
                                                        collect(Collectors.toList());
                        tableData.removeAll(childrenDocs);
                    }
                    if (selectedIndex > tableData.size() - 1){
                        selectedIndex = tableData.size() - 1;
                    }
                    ((DocTableListController)outerController).setSelected(selectedIndex);
                }
            };
            manager.getDataFetchProvider().getOneFromDB(selected.getRecId(), successAction, null);
            
        }
        else {
            dialogStage.requestFocus();
            StageUtils.centerChildOf(docEditorPanelSceneStage, dialogStage);
        }
    }

    @Override
    public void edit(ActionEvent event) {
        Stage docEditorPanelSceneStage = (Stage) exit.getScene().getWindow();
        Stage dialogStage = StagesContainer.getStageFor(docEditorPanelSceneStage, Names.LEVEL_FOR_PATH);
        if(dialogStage == null || !dialogStage.isShowing()){
            Doc selected = (Doc)((AView)exit.getScene().lookup("#aview")).getCustomSelectedItem();
            EditorPanelableManager manager = DocManagersFactory.getEPManager(selected);
            Consumer<EditorPanelable> successAction = (obj) -> {
                Dialogable dialog = manager.getDialogFor(docEditorPanelSceneStage, Names.EDITOR_BUTTON_TYPE.EDIT, obj);
                List<Doc> result = dialog.getResult();
                result.stream().forEach((newDoc) -> {
                                    Optional<Doc> docFromAFBTable = tableData.stream().map((elem) -> (Doc)elem).
                                                                        filter((Doc doc) -> doc.getRecId() == newDoc.getRecId()).findFirst();
                                    if (docFromAFBTable.isPresent()){
                                        docFromAFBTable.get().copyFrom(newDoc);
                                    }
                });
            };
            manager.getDataFetchProvider().getOneFromDB(selected.getRecId(), successAction, null);
            
        }
        else {
            dialogStage.requestFocus();
            StageUtils.centerChildOf(docEditorPanelSceneStage, dialogStage);
        }
    }

    @Override
    public void view(ActionEvent event) {
        Stage docEditorPanelSceneStage = (Stage) exit.getScene().getWindow();
        Stage dialogStage = StagesContainer.getStageFor(docEditorPanelSceneStage, Names.LEVEL_FOR_PATH);
        if(dialogStage == null || !dialogStage.isShowing()){
            Doc selected = (Doc)((AView)exit.getScene().lookup("#aview")).getCustomSelectedItem();
            EditorPanelableManager dm = DocManagersFactory.getEPManager(selected);
            Consumer<EditorPanelable> consumer = (obj) -> {
                Dialogable dialog = dm.getDialogFor(docEditorPanelSceneStage, Names.EDITOR_BUTTON_TYPE.VIEW, obj);
                dialog.showAndWait();
            };
            dm.getDataFetchProvider().getOneFromDB(selected.recId, consumer, null);
        }
        else {
            dialogStage.requestFocus();
            StageUtils.centerChildOf(docEditorPanelSceneStage, dialogStage);
        }
    }

    @Override
    public void add(ActionEvent event) {
        System.out.println("DocEditorPanel add");
    }

    @Override
    public void addBySample(ActionEvent event) {
        System.out.println("DocEditorPanel addBySample");
    }

    @Override
    public void refresh(ActionEvent event) {
        ListingStage editorPanelSceneStage = (ListingStage) exit.getScene().getWindow();
        Stage filterStage = StagesContainer.getStageFor(editorPanelSceneStage, Names.LEVEL_FOR_PATH);
        if (filterStage == null || !filterStage.isShowing()){
            Filterable filter = new DocFilter(editorPanelSceneStage);
            FilterModel model = filter.getResult();
            if (model != null && !model.isCanceled()){
                editorPanelSceneStage.getController().reAssignTable(model);
            }
        }
        else {
            filterStage.requestFocus();
            StageUtils.centerChildOf(editorPanelSceneStage, filterStage);
        }
        refresh.setSelected(false);
    }
    
}
