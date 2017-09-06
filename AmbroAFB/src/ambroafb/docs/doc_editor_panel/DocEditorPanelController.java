/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.docs.doc_editor_panel;

import ambro.AFilterableTableView;
import ambro.AFilterableTreeTableView;
import ambro.ATableView;
import ambro.AView;
import ambroafb.docs.Doc;
import ambroafb.docs.types.DocManager;
import ambroafb.docs.types.DocManagersFactory;
import ambroafb.docs.types.utilities.charge.ChargeUtilityManager;
import ambroafb.docs.types.utilities.payment.PaymentUtilityManager;
import ambroafb.general.Names;
import ambroafb.general.StageUtils;
import ambroafb.general.StagesContainer;
import ambroafb.general.Utils;
import ambroafb.general.interfaces.Dialogable;
import ambroafb.general.interfaces.EditorPanelable;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author dkobuladze
 */
public class DocEditorPanelController implements Initializable {

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
    
    private ObservableList<Doc> tableData;
    private final DocEditorPanelModel editorPanelModel = new DocEditorPanelModel();
    
    @FXML
    private void delete(ActionEvent e) {
        Stage docEditorPanelSceneStage = (Stage) exit.getScene().getWindow();
        Stage dialogStage = StagesContainer.getStageFor(docEditorPanelSceneStage, Names.LEVEL_FOR_PATH);
        if(dialogStage == null || !dialogStage.isShowing()){
            Doc selected = (Doc)((AView)exit.getScene().lookup("#aview")).getCustomSelectedItem();
            if (selected.isParentDoc()){
                DocManager dm = DocManagersFactory.getDocManager(selected.getDocType());
                EditorPanelable docFromDB = dm.getOneFromDB(selected.getRecId());
                Dialogable dialog = dm.getDocDialogFor(docEditorPanelSceneStage, Names.EDITOR_BUTTON_TYPE.DELETE, docFromDB);
                EditorPanelable result = dialog.getResult();
                if (result != null){
                    boolean isDeleted = dm.deleteOneFromDB(docFromDB.getRecId());
                    if (isDeleted){
                        tableData.remove(selected);
                    }
                }
            }
            else {
                System.out.println("must show custom dialog");
            }
        }
        else {
            dialogStage.requestFocus();
            StageUtils.centerChildOf(docEditorPanelSceneStage, dialogStage);
        }
    }
    
    @FXML
    private void edit(ActionEvent e) {
        Stage docEditorPanelSceneStage = (Stage) exit.getScene().getWindow();
        Stage dialogStage = StagesContainer.getStageFor(docEditorPanelSceneStage, Names.LEVEL_FOR_PATH);
        if(dialogStage == null || !dialogStage.isShowing()){
            Doc selected = (Doc)((AView)exit.getScene().lookup("#aview")).getCustomSelectedItem();
            if (selected.isParentDoc()){
                DocManager dm = DocManagersFactory.getDocManager(selected.getDocType());
                EditorPanelable docFromDB = dm.getOneFromDB(selected.getRecId());
                Dialogable dialog = dm.getDocDialogFor(docEditorPanelSceneStage, Names.EDITOR_BUTTON_TYPE.EDIT, docFromDB);
                EditorPanelable result = dialog.getResult();
                if (result != null){ // If result is null, "selected" Doc object stay in table. The edit dialog changed "docFromDB" object.
                    ArrayList<Doc> newDocsFromDB = dm.saveOneToDB(docFromDB);
                    if (!newDocsFromDB.isEmpty()){ // "selected" object in list does not change, if newFromDB is null. Otherwise,  EditorPanelable abstraction must convert to Doc
                        newDocsFromDB.stream().forEach((newDoc) -> {
                            Optional<Doc> docFromAFBTable = tableData.stream().filter((Doc doc) -> doc.getRecId() == newDoc.getRecId()).findFirst();
                            if (docFromAFBTable.isPresent()){
                                docFromAFBTable.get().copyFrom(newDoc);
                            }
                        });
                    }
                }
            }
            else {
                System.out.println("must show custom dialog");
            }
        }
        else {
            dialogStage.requestFocus();
            StageUtils.centerChildOf(docEditorPanelSceneStage, dialogStage);
        }
    }
    
    @FXML
    private void view(ActionEvent e) {
        Stage docEditorPanelSceneStage = (Stage) exit.getScene().getWindow();
        Stage dialogStage = StagesContainer.getStageFor(docEditorPanelSceneStage, Names.LEVEL_FOR_PATH);
        if(dialogStage == null || !dialogStage.isShowing()){
            Doc selected = (Doc)((AView)exit.getScene().lookup("#aview")).getCustomSelectedItem();
            if (selected.isParentDoc()){
                DocManager dm = DocManagersFactory.getDocManager(selected.getDocType());
                EditorPanelable docFromDB = dm.getOneFromDB(selected.getRecId());
                Dialogable dialog = dm.getDocDialogFor(docEditorPanelSceneStage, Names.EDITOR_BUTTON_TYPE.VIEW, docFromDB);
                dialog.showAndWait();
            }
            else {
                System.out.println("must show custom dialog");
            }
        }
        else {
            dialogStage.requestFocus();
            StageUtils.centerChildOf(docEditorPanelSceneStage, dialogStage);
        }
    }
    
    @FXML
    private void addCustom(ActionEvent e) {
        System.out.println("addCustom method. Must show custom dialog.");
    }
    
    @FXML
    private void addBySample(ActionEvent e) {
        Stage docEditorPanelSceneStage = (Stage) exit.getScene().getWindow();
        Stage dialogStage = StagesContainer.getStageFor(docEditorPanelSceneStage, Names.LEVEL_FOR_PATH);
        if(dialogStage == null || !dialogStage.isShowing()){
            Doc selected = (Doc)((AView)exit.getScene().lookup("#aview")).getCustomSelectedItem();
            DocManager dm = DocManagersFactory.getDocManager(selected.getDocType());
            EditorPanelable docFromDB = dm.getOneFromDB(selected.getRecId());
            EditorPanelable cloneFromReal = docFromDB.cloneWithoutID(); // Without this coping, program make "edit" action.
            Dialogable dialog = dm.getDocDialogFor(docEditorPanelSceneStage, Names.EDITOR_BUTTON_TYPE.ADD, cloneFromReal);
            EditorPanelable newFromDialog =  dialog.getResult();
            if (newFromDialog != null){
                ArrayList<Doc> newDocsFromDB = dm.saveOneToDB(newFromDialog);
                if (!newDocsFromDB.isEmpty()){
                    newDocsFromDB.stream().forEach((doc) -> {
                        tableData.add(doc);
                    });
                }
            }
        }
    }
    
    @FXML
    private void addPaymentUtility(ActionEvent e) {
        Stage docEditorPanelSceneStage = (Stage) exit.getScene().getWindow();
        Stage dialogStage = StagesContainer.getStageFor(docEditorPanelSceneStage, Names.LEVEL_FOR_PATH);
        if(dialogStage == null || !dialogStage.isShowing()){
            DocManager dm = new PaymentUtilityManager();
            Dialogable dd = dm.getDocDialogFor(docEditorPanelSceneStage, Names.EDITOR_BUTTON_TYPE.ADD, null);
            EditorPanelable newPaymentUtility = dd.getResult();
            if (newPaymentUtility != null){
                ArrayList<Doc> newDocsFromDB = dm.saveOneToDB(newPaymentUtility);
                if (!newDocsFromDB.isEmpty()){
                    newDocsFromDB.stream().forEach((doc) -> {
                        tableData.add(doc);
                    });
                }
            }
        }
        else {
            dialogStage.requestFocus();
            StageUtils.centerChildOf(docEditorPanelSceneStage, dialogStage);
        }
    }
    
    @FXML
    private void addChargeUtility(ActionEvent e) {
        Stage docEditorPanelSceneStage = (Stage) exit.getScene().getWindow();
        Stage dialogStage = StagesContainer.getStageFor(docEditorPanelSceneStage, Names.LEVEL_FOR_PATH);
        if(dialogStage == null || !dialogStage.isShowing()){
            DocManager dm = new ChargeUtilityManager();
            Dialogable dd = dm.getDocDialogFor(docEditorPanelSceneStage, Names.EDITOR_BUTTON_TYPE.ADD, null);
            EditorPanelable newChargeUtility = dd.getResult();
            if (newChargeUtility != null){
                ArrayList<Doc> newDocsFromDB = dm.saveOneToDB(newChargeUtility);
                if (!newDocsFromDB.isEmpty()){
                    newDocsFromDB.stream().forEach((doc) -> {
                        tableData.add(doc);
                    });
                }
            }
        }
        else {
            dialogStage.requestFocus();
            StageUtils.centerChildOf(docEditorPanelSceneStage, dialogStage);
        }
    }
    
    @FXML
    private void addRefund(ActionEvent e){
        System.out.println("addRefund method");
    }
    @FXML
    private void refresh(ActionEvent e) {
        Stage editorPanelSceneStage = (Stage) exit.getScene().getWindow();
        Stage filterStage = StagesContainer.getStageFor(editorPanelSceneStage, Names.LEVEL_FOR_PATH);
        if (filterStage == null || !filterStage.isShowing()){
            Class controllerClass = getClassByName("ambroafb.general_scene.doc_table_list.DocTableListController");
            Supplier<ArrayList<Doc>> fetchData = () -> {
                                                        return new ArrayList(Doc.getAllFromDB());
                                                    };
            Utils.getInvokedClassMethod(controllerClass, "reAssignTable", new Class[]{Supplier.class}, outerController, fetchData);
        }
        else {
            filterStage.requestFocus();
            StageUtils.centerChildOf(editorPanelSceneStage, filterStage);
        }
        refresh.setSelected(false);
    }
    
    private Class getClassByName(String name){
        if (name == null) return null;
        Class result = null;
        try {
            result = Class.forName(name);
        } catch (ClassNotFoundException ex) { }
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
     * The method saves table data list and also provides to search element in table.
     * @param table Table component on scene.
     * @param list  Data list of given table (At the beginning, it may be empty).
     */
    public void setTableDataList(ATableView<Doc> table, ObservableList<Doc> list){
        tableData = list;
        table.setItems(list);
        if (table instanceof AFilterableTableView){
            AFilterableTableView<EditorPanelable> filterableTable = (AFilterableTableView) table;
            filterableTable.makeBindingsForFilterOn(search, (EditorPanelable panelable) -> panelable.toStringForSearch().toLowerCase().contains(search.getText().toLowerCase()));
        }
    }
    
    public void buttonsMainPropertysBinder (AView<Doc> aView){
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
    
    /**
     * Returns panel width without space size before search field.
     * @return 
     */
    public double getPanelMinWidth(){
        return formNode.getWidth() - region.getWidth();
    }
    
    public Button getExitButton(){
        return exit;
    }
    
    public void setOuterController(Initializable controller){
        outerController = controller;
    }
    
}
