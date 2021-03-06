/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general.editor_panel;

import ambro.AFilterableTableView;
import ambro.AFilterableTreeTableView;
import ambroafb.AmbroAFB;
import ambroafb.docs.types.doc_in_order.DocOrderComponent;
import ambroafb.general.GeneralConfig;
import ambroafb.general.Names;
import ambroafb.general.StageUtils;
import ambroafb.general.StagesContainer;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.general.interfaces.FilterModel;
import ambroafb.general.interfaces.Filterable;
import ambroafb.general.interfaces.ListingStage;
import ambroafb.general_scene.SelectionObserver;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

/**
 *
 * @author dkobuladze
 */
public abstract class EditorPanel extends HBox implements Initializable, SelectionObserver {

    @FXML
    protected Button exit, delete, edit, view;
    
    @FXML
    protected SplitMenuButton add;
    
    @FXML
    protected MenuItem addBySample;
    
    @FXML
    protected ToggleButton refresh;
    
    @FXML
    protected TextField filter;

    @FXML
    protected Region region;
    
    protected final List<EditorPanelActionObserver> observers = new ArrayList<>();
    protected EditorPanelable selectedItem;
    
    
    public static final String DELETE_FXID = "#delete";
    public static final String EDIT_FXID = "#edit";
    public static final String VIEW_FXID = "#view";
    public static final String ADD_FXID = "#add";
    public static final String ADD_BY_SAMPLE_FXID = "#addBySample";
    public static final String REFRESH_FXID = "#refresh";
    public static final String SEARCH_FXID = "#filter";
    
    public static enum EDITOR_BUTTON_TYPE {
        DELETE, EDIT, VIEW, ADD, ADD_BY_SAMPLE
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        componentsInitialize(location, resources);
    }
    
    protected final void assignLoader(){
        FXMLLoader loader = new FXMLLoader(AmbroAFB.class.getResource("/ambroafb/general/editor_panel/EditorPanel.fxml"));
        loader.setResources(GeneralConfig.getInstance().getBundle());
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException ex) {
            Logger.getLogger(DocOrderComponent.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        buttonsActions();
    }
    
    private void buttonsActions(){
        delete.setOnAction(this::deleteAction);
        edit.setOnAction(this::editAction);
        view.setOnAction(this::viewAction);
        add.setOnAction(this::addAction);
        addBySample.setOnAction(this::addBySampleAction);
        refresh.setOnAction(this::refreshAction);
        
        // If buttons click more than one, call appropriate "onAction" method. Without this, dialogs stages for any single button, may show more than one.
        delete.setOnMouseClicked((event) -> {
            if (event.getClickCount() > 1) delete.getOnAction().handle(null);
        });
        view.setOnMouseClicked((event) -> {
            if (event.getClickCount() > 1) view.getOnAction().handle(null);
        });
        edit.setOnMouseClicked((event) -> {
            if (event.getClickCount() > 1) edit.getOnAction().handle(null);
        });
        add.setOnMouseClicked((event) -> {
            if (event.getClickCount() > 1) add.getOnAction().handle(null);
        });
        refresh.setOnMouseClicked((event) -> {
            if (event.getClickCount() > 1) refresh.getOnAction().handle(null);
        });
    }
    
    public void registerObserver(EditorPanelActionObserver observer){
        if (observer != null){
            observers.add(observer);
        }
    }
    
    public void removeObserver(EditorPanelActionObserver observer){
        if (observer != null){
            observers.remove(observer);
        }
    }
    
    /**
     *  Removes Buttons from editor panel.
     * @param fxIds fx:ids array which must be removed.
     */
    public void removeComponents(String... fxIds){
        for (String fxId : fxIds) {
            this.getChildren().remove(this.lookup(fxId));
        }
    }
    
    /**
     * The method adds new component to the editor panel on specific index from left side.
     * @param onIndex The index where new component will be placed (Numeration starts from 0).
     * @param node The new component.
     */
    public void addComponent(int onIndex, Node node){
        if (onIndex >= 0){
            this.getChildren().add(onIndex, node);
        }
    }
    
    /**
     * The method turns hide add button items list on right side of it.
     */
    public void hideMenuOnAddButton(){
        int addBySimpleIndex = this.getChildren().indexOf(add);
        Button addButton = buildAddWithoutMenu();
        this.getChildren().set(addBySimpleIndex, addButton);
    }
    
    private Button buildAddWithoutMenu(){
        Button addB = new Button();
        addB.setTooltip(new Tooltip(GeneralConfig.getInstance().getTitleFor("new")));
        ImageView imgView = new ImageView("/images/new.png");
        imgView.setFitHeight(18);
        imgView.setFitWidth(18);
        addB.setGraphic(imgView);
        addB.setOnAction(this::add);
        return addB;
    }
    
    public double getPanelMinWidth(){
        return this.getWidth() - region.getWidth();
    }
    
    public Button getExitButton(){
        return exit;
    }
    
    public TextField getFilterField(){
        return filter;
    }
    
    public void setTableDataList(AFilterableTableView<EditorPanelable> table){
        table.makeBindingsForFilterOn(filter, (EditorPanelable panelable) -> panelable.toStringForSearch().toLowerCase().contains(filter.getText().toLowerCase()));
    }
    
    public void setTreeTable(AFilterableTreeTableView<EditorPanelable> treeTable){
        treeTable.makeBindingsForFilterOn(filter, (EditorPanelable panelable) -> panelable.toStringForSearch().toLowerCase().contains(filter.getText().toLowerCase()));
    }
    
    @Override
    public void notify(EditorPanelable selected) {
        selectedItem = selected;
        boolean disable = (selected == null);
        delete.setDisable(disable);
        edit.setDisable(disable);
        view.setDisable(disable);
        addBySample.setDisable(disable);
    }

    @Override
    public void update(EditorPanelable selected) {
        selectedItem = selected;
    }

    
    
    
    /**
     *  The function checks any type of dialog or filter stage already show or not.
     * @return False - if filter or any type of dialog stage  is  showing. True - otherwise.
     */
    public final boolean allowToOpenDialogOrFilter(){
        boolean allow = true;
        Stage editorPanelSceneStage = (Stage) exit.getScene().getWindow();
        Stage dialogStage = StagesContainer.getStageFor(editorPanelSceneStage, Names.LEVEL_FOR_PATH);
        if(dialogStage != null && dialogStage.isShowing()){
            dialogStage.requestFocus();
            StageUtils.centerChildOf(editorPanelSceneStage, dialogStage);
            allow = false;
        }
        return allow;
    }
    
    private void deleteAction(ActionEvent event){
        if (allowToOpenDialogOrFilter()) delete(event);
    }
    
    private void editAction(ActionEvent event){
        if (allowToOpenDialogOrFilter()) edit(event);
    }
    
    private void viewAction(ActionEvent event){
        if (allowToOpenDialogOrFilter()) view(event);
    }
    
    private void addAction(ActionEvent event){
        if (allowToOpenDialogOrFilter()) add(event);
    }
    
    private void addBySampleAction(ActionEvent event){
        if (allowToOpenDialogOrFilter()) addBySample(event);
    }
    
    private void refreshAction(ActionEvent event){
        if (allowToOpenDialogOrFilter()) {
            ListingStage editorPanelSceneStage = (ListingStage) exit.getScene().getWindow();
            Filterable filterStage = editorPanelSceneStage.getEPManager().getFilterFor(editorPanelSceneStage);
            FilterModel model = (filterStage != null) ? filterStage.getResult() : null;
            if (model == null || !model.isCanceled()){
                editorPanelSceneStage.getController().reAssignTable(model);
            }
            refresh.setSelected(false);
        }
    }
    
    
    protected abstract void componentsInitialize(URL location, ResourceBundle resources);
    public abstract void delete(ActionEvent event);
    public abstract void edit(ActionEvent event);
    public abstract void view(ActionEvent event);
    public abstract void add(ActionEvent event);
    public abstract void addBySample(ActionEvent event);
    
}
