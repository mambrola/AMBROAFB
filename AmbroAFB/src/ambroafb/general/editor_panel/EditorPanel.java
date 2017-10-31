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
import ambroafb.AmbroAFB;
import ambroafb.docs.types.doc_in_order.DocOrderComponent;
import ambroafb.general.GeneralConfig;
import ambroafb.general.interfaces.EditorPanelable;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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

/**
 *
 * @author dkobuladze
 */
public abstract class EditorPanel extends HBox implements Initializable {

    @FXML
    protected Button exit, delete, edit, view;
    
    @FXML
    protected SplitMenuButton add;
    
    @FXML
    protected ToggleButton refresh;
    
    @FXML
    protected TextField search;


    @FXML
    protected MenuItem addBySample;
    
    @FXML
    protected Region region;
    
    protected ObservableList<EditorPanelable> tableData;
    
    protected Initializable outerController; // ----
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        componentsInitialize(location, resources);
    }
    
    protected final void assignLoader(){
        FXMLLoader loader = new FXMLLoader(AmbroAFB.class.getResource("/ambroafb/general/editor_panel/EditorPanelTest.fxml"));
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
        delete.setOnAction(this::delete);
        edit.setOnAction(this::edit);
        view.setOnAction(this::view);
        add.setOnAction(this::add);
        addBySample.setOnAction(this::addBySample);
        refresh.setOnAction(this::refresh);
    }
    
        /**
     *  Removes Buttons from editor panel.
     * @param fxIds fx:ids array which must be removed.
     */
    public void removeButtonsByFxIDs(String... fxIds){
        for (String fxId : fxIds) {
            this.getChildren().remove(this.lookup(fxId));
        }
    }
    
    /**
     * The method turns off addBySample functionality.
     */
    public void turnOffAddBySample(){
        int addBySimpleIndex = this.getChildren().indexOf(add);
        Button addButton = buildAddWithoutSample();
        this.getChildren().set(addBySimpleIndex, addButton);
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
        return this.getWidth() - region.getWidth();
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
    
    
    protected abstract void componentsInitialize(URL location, ResourceBundle resources);
    public abstract void delete(ActionEvent event);
    public abstract void edit(ActionEvent event);
    public abstract void view(ActionEvent event);
    public abstract void add(ActionEvent event);
    public abstract void addBySample(ActionEvent event);
    public abstract void refresh(ActionEvent event);
    
}
