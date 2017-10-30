/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general.editor_panel;

import ambro.AFilterableTableView;
import ambro.AFilterableTreeTableView;
import ambro.ATableView;
import ambro.AView;
import ambroafb.general.GeneralConfig;
import ambroafb.general.Names;
import ambroafb.general.Names.EDITOR_BUTTON_TYPE;
import ambroafb.general.StageUtils;
import ambroafb.general.StagesContainer;
import ambroafb.general.Utils;
import ambroafb.general.interfaces.Dialogable;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.general.interfaces.EditorPanelableManager;
import ambroafb.general.interfaces.FilterModel;
import ambroafb.general.interfaces.Filterable;
import ambroafb.general.interfaces.ListingStage;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.function.Supplier;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
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
    private SplitMenuButton add;
    
    @FXML
    private ToggleButton refresh;
    
    @FXML
    private TextField search;


    @FXML
    private MenuItem addBySample;
    
    @FXML
    private Initializable outerController;
    
    @FXML
    private Region region;

    @FXML
    private HBox formNode;
    
    private enum CLASS_TYPE {OBJECT, DIALOG, FILTER, CONTROLLER};
    
    private ObservableList<EditorPanelable> tableData;
    
//    @FXML
    private void deleteOld(ActionEvent e) {
        Stage editorPanelSceneStage = (Stage) exit.getScene().getWindow();
        Stage dialogStage = StagesContainer.getStageFor(editorPanelSceneStage, Names.LEVEL_FOR_PATH);
        if (dialogStage == null || !dialogStage.isShowing()){
            EditorPanelable selected = (EditorPanelable)((AView)exit.getScene().lookup("#aview")).getCustomSelectedItem();
            Class objectClass = Utils.getClassByName(getClassName(CLASS_TYPE.OBJECT));
            EditorPanelable real = (EditorPanelable)Utils.getInvokedClassMethod(objectClass, "getOneFromDB", new Class[]{int.class}, null, selected.getRecId());
            if (real != null) {
                selected.copyFrom(real);
            }
            Class dialogClass = Utils.getClassByName(getClassName(CLASS_TYPE.DIALOG));
            Stage ownerStage = (Stage) exit.getScene().getWindow();
            Dialogable dialog = (Dialogable) Utils.getInstanceOfClass(dialogClass, new Class[]{EditorPanelable.class, EDITOR_BUTTON_TYPE.class, Stage.class}, selected, EDITOR_BUTTON_TYPE.DELETE, ownerStage);

            EditorPanelable result = dialog.getResult();
            if (result != null){
                boolean isDeleted = (boolean) Utils.getInvokedClassMethod(objectClass, "deleteOneFromDB", new Class[]{int.class}, null, selected.getRecId());
                if(isDeleted)
                    tableData.remove(selected);
            }
        }
        else {
            dialogStage.requestFocus();
            StageUtils.centerChildOf(editorPanelSceneStage, dialogStage);
        }
    }
    
    @FXML
    private void delete(ActionEvent e){
        ListingStage editorPanelSceneStage = (ListingStage) exit.getScene().getWindow();
        Stage dialogStage = StagesContainer.getStageFor(editorPanelSceneStage, Names.LEVEL_FOR_PATH);
        if (dialogStage == null || !dialogStage.isShowing()){
            EditorPanelable selected = (EditorPanelable)((AView)exit.getScene().lookup("#aview")).getCustomSelectedItem();
            EditorPanelableManager manager = editorPanelSceneStage.getEPManager();
            Consumer<Object> successAction = (objFromDB) -> {
                if (objFromDB != null){
                    selected.copyFrom((EditorPanelable)objFromDB);
                }
                Dialogable dialog = manager.getDialogFor(editorPanelSceneStage, EDITOR_BUTTON_TYPE.DELETE, selected);
                EditorPanelable result = dialog.getResult();
                if (result != null){
                    tableData.remove(selected);
                }
            };
            manager.getDataFetchProvider().getOneFromDB(selected.getRecId(), successAction, null);
                
        } else {
            dialogStage.requestFocus();
            StageUtils.centerChildOf(editorPanelSceneStage, dialogStage);
        }
    }
    
//    @FXML
    private void editOld(ActionEvent e) {
        Stage editorPanelSceneStage = (Stage) exit.getScene().getWindow();
        Stage dialogStage = StagesContainer.getStageFor(editorPanelSceneStage, Names.LEVEL_FOR_PATH);
        if (dialogStage == null || !dialogStage.isShowing()){
            EditorPanelable selected = (EditorPanelable)((AView)exit.getScene().lookup("#aview")).getCustomSelectedItem();
            Class objectClass = Utils.getClassByName(getClassName(CLASS_TYPE.OBJECT));
            EditorPanelable real = (EditorPanelable) Utils.getInvokedClassMethod(objectClass, "getOneFromDB", new Class[]{int.class}, null, selected.getRecId());
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
                Object resultFromDB = Utils.getInvokedClassMethod(objectClass, "saveOneToDB", new Class[]{objectClass}, null, result);
                if (resultFromDB == null){
                    selected.copyFrom(backup);
                }
            }
        }
        else {
            dialogStage.requestFocus();
            StageUtils.centerChildOf(editorPanelSceneStage, dialogStage);
        }
    }
    
    @FXML
    private void edit(ActionEvent e) {
        ListingStage editorPanelSceneStage = (ListingStage) exit.getScene().getWindow();
        Stage dialogStage = StagesContainer.getStageFor(editorPanelSceneStage, Names.LEVEL_FOR_PATH);
        if (dialogStage == null || !dialogStage.isShowing()){
            EditorPanelable selected = (EditorPanelable)((AView)exit.getScene().lookup("#aview")).getCustomSelectedItem();
            EditorPanelableManager manager = editorPanelSceneStage.getEPManager(); // EPManagerFactory.getEPManager(selected);
            Consumer<Object> successAction = (ObjFromDB) -> {
                if (ObjFromDB != null) {
                    selected.copyFrom((EditorPanelable)ObjFromDB);
                }
                EditorPanelable backup = selected.cloneWithID();
                Dialogable dialog = manager.getDialogFor(editorPanelSceneStage, EDITOR_BUTTON_TYPE.EDIT, selected);
                EditorPanelable result = dialog.getResult();
                if (result == null){
                    selected.copyFrom(backup);
                }
            };
            manager.getDataFetchProvider().getOneFromDB(selected.getRecId(), successAction, null);
            
        }
        else {
            dialogStage.requestFocus();
            StageUtils.centerChildOf(editorPanelSceneStage, dialogStage);
        }
    }
    
//    @FXML
    private void viewOld(ActionEvent e) {
        Stage editorPanelSceneStage = (Stage) exit.getScene().getWindow();
        Stage dialogStage = StagesContainer.getStageFor(editorPanelSceneStage, Names.LEVEL_FOR_PATH);
        if(dialogStage == null || !dialogStage.isShowing()){
            EditorPanelable selected = (EditorPanelable)((AView)exit.getScene().lookup("#aview")).getCustomSelectedItem();
            EditorPanelable real = (EditorPanelable)Utils.getInvokedClassMethod(Utils.getClassByName(getClassName(CLASS_TYPE.OBJECT)), "getOneFromDB", new Class[]{int.class}, null, selected.getRecId());
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
            StageUtils.centerChildOf(editorPanelSceneStage, dialogStage);
        }
    }
    
    @FXML
    private void view(ActionEvent e) {
        ListingStage editorPanelSceneStage = (ListingStage) exit.getScene().getWindow();
        Stage dialogStage = StagesContainer.getStageFor(editorPanelSceneStage, Names.LEVEL_FOR_PATH);
        if(dialogStage == null || !dialogStage.isShowing()){
            EditorPanelable selected = (EditorPanelable)((AView)exit.getScene().lookup("#aview")).getCustomSelectedItem();
            EditorPanelableManager manager = editorPanelSceneStage.getEPManager();
            Consumer<Object> successAction = (ObjFromDB) -> {
                if (ObjFromDB != null) {
                    selected.copyFrom((EditorPanelable)ObjFromDB);
                }
                Dialogable dialog = manager.getDialogFor(editorPanelSceneStage, EDITOR_BUTTON_TYPE.VIEW, selected);
                dialog.showAndWait();
            };
            manager.getDataFetchProvider().getOneFromDB(selected.getRecId(), successAction, null);

        }
        else {
            dialogStage.requestFocus();
            StageUtils.centerChildOf(editorPanelSceneStage, dialogStage);
        }
    }
    
//    @FXML
    private void addOld(ActionEvent e) {
        Stage editorPanelSceneStage = (Stage) exit.getScene().getWindow();
        Stage dialogStage = StagesContainer.getStageFor(editorPanelSceneStage, Names.LEVEL_FOR_PATH);
        if(dialogStage == null || !dialogStage.isShowing()){
            Class dialogClass = Utils.getClassByName(getClassName(CLASS_TYPE.DIALOG));
            Dialogable dialog = (Dialogable)Utils.getInstanceOfClass(dialogClass, new Class[]{EditorPanelable.class, EDITOR_BUTTON_TYPE.class, Stage.class}, null, EDITOR_BUTTON_TYPE.ADD, (Stage) exit.getScene().getWindow());
            
            EditorPanelable result = dialog.getResult();
            if (result != null) {
                Class objectClass = Utils.getClassByName(getClassName(CLASS_TYPE.OBJECT));
                EditorPanelable newInstanceWithId = (EditorPanelable) Utils.getInvokedClassMethod(objectClass, "saveOneToDB", new Class[]{objectClass}, null, result);
                if (newInstanceWithId != null){
                    tableData.add(newInstanceWithId);
                }
            }
        }
        else {
            dialogStage.requestFocus();
            StageUtils.centerChildOf(editorPanelSceneStage, dialogStage);
        }
    }
    
    @FXML
    private void add(ActionEvent e) {
        ListingStage editorPanelSceneStage = (ListingStage) exit.getScene().getWindow();
        Stage dialogStage = StagesContainer.getStageFor(editorPanelSceneStage, Names.LEVEL_FOR_PATH);
        if(dialogStage == null || !dialogStage.isShowing()){
            EditorPanelableManager manager = editorPanelSceneStage.getEPManager();
            Dialogable dialog = manager.getDialogFor(editorPanelSceneStage, EDITOR_BUTTON_TYPE.ADD, null);
            
            EditorPanelable result = dialog.getResult();
            if (result != null) {
                tableData.add(result);
            }
        }
        else {
            dialogStage.requestFocus();
            StageUtils.centerChildOf(editorPanelSceneStage, dialogStage);
        }
    }
    
//    @FXML
    private void addBySampleOld(ActionEvent e) {
        Stage editorPanelSceneStage = (Stage) exit.getScene().getWindow();
        Stage dialogStage = StagesContainer.getStageFor(editorPanelSceneStage, Names.LEVEL_FOR_PATH);
        if(dialogStage == null || !dialogStage.isShowing()){
            EditorPanelable selected = (EditorPanelable)((AView)exit.getScene().lookup("#aview")).getCustomSelectedItem();
            Class objectClass = Utils.getClassByName(getClassName(CLASS_TYPE.OBJECT));
            EditorPanelable real = (EditorPanelable) Utils.getInvokedClassMethod(objectClass, "getOneFromDB", new Class[]{int.class}, null, selected.getRecId());
            EditorPanelable cloneOfSelected;
            if (real != null) {
                cloneOfSelected = real.cloneWithoutID();
            }
            else {
                cloneOfSelected = selected.cloneWithoutID();
            }
            
            Class dialogClass = Utils.getClassByName(getClassName(CLASS_TYPE.DIALOG));
            Dialogable dialog = (Dialogable) Utils.getInstanceOfClass(dialogClass, new Class[]{EditorPanelable.class, EDITOR_BUTTON_TYPE.class, Stage.class}, cloneOfSelected, EDITOR_BUTTON_TYPE.ADD_BY_SAMPLE, (Stage) exit.getScene().getWindow());
            
            EditorPanelable result = dialog.getResult();
            if (result != null) {
                EditorPanelable newInstanceWithId = (EditorPanelable) Utils.getInvokedClassMethod(objectClass, "saveOneToDB", new Class[]{objectClass}, null, result); 
                if (newInstanceWithId != null){ // result != null
                    tableData.add(newInstanceWithId);
                }
            }
        }
        else {
            dialogStage.requestFocus();
            StageUtils.centerChildOf(editorPanelSceneStage, dialogStage);
        }
    }
    
    @FXML
    private void addBySample(ActionEvent e) {
        ListingStage editorPanelSceneStage = (ListingStage) exit.getScene().getWindow();
        Stage dialogStage = StagesContainer.getStageFor(editorPanelSceneStage, Names.LEVEL_FOR_PATH);
        if(dialogStage == null || !dialogStage.isShowing()){
            EditorPanelable selected = (EditorPanelable)((AView)exit.getScene().lookup("#aview")).getCustomSelectedItem();
            EditorPanelableManager manager = editorPanelSceneStage.getEPManager();
            Consumer<Object> successAction = (objFromDB) -> {
                EditorPanelable cloneOfSelected;
                if (objFromDB != null) {
                    cloneOfSelected = ((EditorPanelable)objFromDB).cloneWithoutID();
                }
                else {
                    cloneOfSelected = selected.cloneWithoutID();
                }

                Dialogable dialog = manager.getDialogFor(editorPanelSceneStage, EDITOR_BUTTON_TYPE.ADD_BY_SAMPLE, cloneOfSelected);
                EditorPanelable result = dialog.getResult();
                if (result != null) {
                    tableData.add(result);
                }
            };
            manager.getDataFetchProvider().getOneFromDB(selected.getRecId(), successAction, null);
            
        }
        else {
            dialogStage.requestFocus();
            StageUtils.centerChildOf(editorPanelSceneStage, dialogStage);
        }
    }
    
//    @FXML
    private void refreshOld(ActionEvent e) {
        Stage editorPanelSceneStage = (Stage) exit.getScene().getWindow();
        Stage filterStage = StagesContainer.getStageFor(editorPanelSceneStage, Names.LEVEL_FOR_PATH);
        if (filterStage == null || !filterStage.isShowing()){
            Class className = Utils.getClassByName(getClassName(CLASS_TYPE.FILTER));
            Filterable filter = (className != null) ? (Filterable)Utils.getInstanceOfClass(className, new Class[]{Stage.class}, (Stage) exit.getScene().getWindow()) : null;
            
            FilterModel model = (filter != null) ? filter.getResult() : null;
            if (model != null && !model.isCanceled()){
                Class objectClass = Utils.getClassByName(getClassName(CLASS_TYPE.OBJECT));
                Supplier<List<EditorPanelable>> fetchData = makeAppropSupplier(objectClass, model);

                Class controllerClass = Utils.getClassByName(getClassName(CLASS_TYPE.CONTROLLER));
                Utils.getInvokedClassMethod(controllerClass, "reAssignTable", new Class[]{Supplier.class}, outerController, fetchData);
            }
        }
        else {
            filterStage.requestFocus();
            StageUtils.centerChildOf(editorPanelSceneStage, filterStage);
        }
        refresh.setSelected(false);
    }
    
    @FXML
    private void refresh(ActionEvent e) {
        ListingStage editorPanelSceneStage = (ListingStage) exit.getScene().getWindow();
        Stage filterStage = StagesContainer.getStageFor(editorPanelSceneStage, Names.LEVEL_FOR_PATH);
        if (filterStage == null || !filterStage.isShowing()){
            Filterable filter = editorPanelSceneStage.getEPManager().getFilterFor(editorPanelSceneStage);
            FilterModel model = (filter != null) ? filter.getResult() : null;
            if (model != null && !model.isCanceled()){
//                Class objectClass = Utils.getClassByName(getClassName(CLASS_TYPE.OBJECT));
//                Supplier<List<EditorPanelable>> fetchData = makeAppropSupplier(objectClass, model);
                
                editorPanelSceneStage.getController().reAssignTable(model);
            }
        }
        else {
            filterStage.requestFocus();
            StageUtils.centerChildOf(editorPanelSceneStage, filterStage);
        }
        refresh.setSelected(false);
    }
    
    private Supplier<List<EditorPanelable>> makeAppropSupplier(Class targetClass, FilterModel model){
        Supplier<List<EditorPanelable>> result;
        if (model == null){
            result = () -> (List<EditorPanelable>) Utils.getInvokedClassMethod(targetClass, "getAllFromDB", null, null);
        }
        else {
            result = () -> (List<EditorPanelable>) Utils.getInvokedClassMethod(targetClass, "getFilteredFromDB", new Class[]{FilterModel.class}, null, model);
        }
        return result;
    }
    
    /**
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }
    
    /**
     * The method creates  add button without addBySample functionality.
     */
    private Button buildAddWithoutSample(){
        Button addB = new Button();
        addB.setTooltip(new Tooltip(GeneralConfig.getInstance().getTitleFor("new")));
        ImageView imgView = new ImageView(new Image("/images/new.png"));
        imgView.setFitHeight(18);
        imgView.setFitWidth(18);
        addB.setGraphic(imgView);
        addB.setOnAction(this::add);
        return addB;
    }
    
    public double getPanelMinWidth(){
        return formNode.getWidth() - region.getWidth();
    }
    
    public Button getExitButton(){
        return exit;
    }
    
    /**
     * The method saves table data list and also provides to search element in table.
     * @param table Table component on scene.
     * @param list  Data list of given table (At the beginning, it may be empty).
     */
    public void setTableDataList(ATableView<EditorPanelable> table, ObservableList<EditorPanelable> list){
        tableData = list;
        table.setItems(list);
        if (table instanceof AFilterableTableView){
            AFilterableTableView<EditorPanelable> filterableTable = (AFilterableTableView) table;
            filterableTable.makeBindingsForFilterOn(search, (EditorPanelable panelable) -> panelable.toStringForSearch().toLowerCase().contains(search.getText().toLowerCase()));
        }
    }
    
    public void setTreeTable(AFilterableTreeTableView<EditorPanelable> treeTable){
        treeTable.makeBindingsForFilterOn(search, (EditorPanelable panelable) -> panelable.toStringForSearch().toLowerCase().contains(search.getText().toLowerCase()));
    }
    
    public void buttonsMainPropertysBinder (AView<EditorPanelable> aView){
        BooleanBinding allowModify = Bindings.createBooleanBinding(() -> {
                                                                    if (aView.getCustomSelectedItem() == null){
                                                                        return true;
                                                                    }
                                                                    return aView.getCustomSelectedItem().isAllowToModify().not().get();
                                                                }, aView.getCustomSelectionModel().selectedItemProperty());
        if (aView instanceof AFilterableTreeTableView){
            AFilterableTreeTableView<EditorPanelable> treeTable = (AFilterableTreeTableView)aView;
            delete.disableProperty().bind(Bindings.createBooleanBinding(() -> {
                if (aView.getCustomSelectionModel().selectedItemProperty().isNull().get()) {
                    return true;
                }
                return !treeTable.getSelectionModel().getSelectedItem().isLeaf();
            }, aView.getCustomSelectionModel().selectedItemProperty()));
        }
        else if (aView instanceof ATableView){
            delete.disableProperty().bind(allowModify);
        }
        edit.disableProperty().bind(allowModify);
        view.disableProperty().bind(aView.getCustomSelectionModel().selectedItemProperty().isNull());
        addBySample.disableProperty().bind(aView.getCustomSelectionModel().selectedItemProperty().isNull());
    }
    
    public void setOuterController(Initializable controller){
        outerController = controller;
    }
    
    /**
     * Adds SimplePanelButton (like exit, add, edit, or delete) before refresh button.
     * @param fxIds
     */
//    public void addPanelButton(SimpleButton button){
//        int emptySpaceIndex = formNode.getChildren().size() - 3;
//        formNode.getChildren().add(emptySpaceIndex, button);
//    }
    
    
    /**
     *  Removes Buttons from editor panel.
     * @param fxIds fx:ids array which must be removed.
     */
    public void removeButtonsByFxIDs(String... fxIds){
        for (String fxId : fxIds) {
            formNode.getChildren().remove(formNode.lookup(fxId));
        }
    }
    
    /**
     * The method turns off addBySample functionality.
     */
    public void turnOffAddBySample(){
        int addBySimpleIndex = formNode.getChildren().indexOf(add);
        Button addButton = buildAddWithoutSample();
        formNode.getChildren().set(addBySimpleIndex, addButton);
    }
    
    private String getClassName(CLASS_TYPE type){
        String rtrn = exit.getScene().getProperties().get("controller").toString();
        if(type.equals(CLASS_TYPE.CONTROLLER))
            return rtrn.substring(0, rtrn.indexOf("@"));
        String contentClassPathOfAView = ((AView)exit.getScene().lookup("#aview")).getInitializer();
        Class contentClassOfAView = Utils.getClassByName(contentClassPathOfAView);
        
        String path = contentClassPathOfAView.substring(0, contentClassPathOfAView.lastIndexOf(".") + 1);
        String contentClassShortName = contentClassPathOfAView.substring(contentClassPathOfAView.lastIndexOf(".") + 1);
        
        
        
        switch (type){
            case DIALOG:
                if (Utils.existsMethodFor(contentClassOfAView, "getDialogStagePath")) {
                    rtrn = (String)Utils.getInvokedClassMethod(contentClassOfAView, "getDialogStagePath", null, null);
                }
                else {
                    rtrn = findCorrectPathFor("dialog", contentClassShortName + "Dialog", contentClassPathOfAView); // path + "dialog." + contentClassShortName + "Dialog";
                }
                break;
            case FILTER:
                if (Utils.existsMethodFor(contentClassOfAView, "getFilterStagePath")) {
                    rtrn = (String)Utils.getInvokedClassMethod(contentClassOfAView, "getFilterStagePath", null, null);
                }
                else {
                    rtrn = findCorrectPathFor("filter", contentClassShortName + "Filter", contentClassPathOfAView); // path + "dialog." + contentClassShortName + "Dialog";
                }
//                rtrn = path + "filter." + contentClassShortName + "Filter";
                break;
            default: // case OBJECT:
                rtrn = contentClassPathOfAView; // path + singularName;
                break;
        }
        return rtrn;
    }
    
    private String findCorrectPathFor(String targetPkg, String targetPkgClass, String currClassPath) {
        if (currClassPath == null || currClassPath.isEmpty() || !currClassPath.contains(".")) return null;
        
        String cuttingLastPart = currClassPath.substring(0, currClassPath.lastIndexOf(".") + 1);
        String result = cuttingLastPart + targetPkg + "." + targetPkgClass;
        
        if (Utils.getClassByName(result) == null){
            result = findCorrectPathFor(targetPkg, targetPkgClass, cuttingLastPart.substring(0, cuttingLastPart.length() - 1));
        }
        return result;
    }
    
}
