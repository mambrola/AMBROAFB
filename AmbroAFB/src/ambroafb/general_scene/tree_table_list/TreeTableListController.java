/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general_scene.tree_table_list;

import ambro.AFilterableTreeItem;
import ambro.AFilterableTreeTableView;
import ambroafb.general.editor_panel.EditorPanelActionObserver;
import ambroafb.general.interfaces.DataFetchProvider;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.general.interfaces.FilterModel;
import ambroafb.general.interfaces.ListingController;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.function.Predicate;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import org.controlsfx.control.MaskerPane;

/**
 * FXML Controller class
 *
 * @author dato
 */
public class TreeTableListController extends ListingController implements EditorPanelActionObserver {

    @FXML
    private AFilterableTreeTableView<EditorPanelable> treeTableView;
    
    @FXML
    private MaskerPane masker;
    
    private final ObservableList<EditorPanelable> roots = FXCollections.observableArrayList();
    private Function<List<EditorPanelable>, ObservableList<EditorPanelable>> treeMakerFn;
    private int expandDepth = 1;
    
    private IntegerProperty expand;
    
    @Override
    protected void componentsInitialize(URL url, ResourceBundle rb) {
        treeTableView.setBundle(rb);
        treeTableView.getColumns().stream().forEach((column) -> {
            column.setSortable(false);
        });
        
        expand = new SimpleIntegerProperty();
        expand.addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            if (newValue != null) treeTableView.expand(newValue.intValue());
        });
    }
    
    @Override
    public void reAssignTable(FilterModel model){
        masker.setVisible(true);
        treeTableView.removeAll();
        new Thread(() -> {
            try {
                List<EditorPanelable> data = (model == null) ? dataFetchProvider.getFilteredBy(DataFetchProvider.PARAM_FOR_ALL) 
                                                             : dataFetchProvider.getFilteredBy(model);
                roots.setAll(treeMakerFn.apply(data));
                roots.stream().forEach((elem) -> {
                    treeTableView.append(elem);
                });
                treeTableView.expand(expand.get());
            } catch (Exception ex) {
                System.err.println("TreeTableController! Exception message: " + ex.getMessage());
            }
            
            Platform.runLater(() -> {
                masker.setVisible(false);
            });
        }).start();
    }
    
    @Override
    public void addListWith(Class content) {
        treeTableView.initialize(content);
        editorPanel.setTreeTable(treeTableView); // **
        editorPanel.registerObserver(this);
        
        treeTableView.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends TreeItem<EditorPanelable>> observable, TreeItem<EditorPanelable> oldValue, TreeItem<EditorPanelable> newValue) -> {
            if (newValue != null && newValue.getValue() != null){
                observers.stream().forEach((observer) -> observer.notify(newValue.getValue()));
            }
        });
    }
    
    @Override
    public void setListFilterConditions(Predicate<EditorPanelable> predicate, Observable... dependencies) {
        treeTableView.predicateProperty().bind(Bindings.createObjectBinding(() -> {
            return AFilterableTreeItem.TreeItemPredicate.create(predicate);
        }, dependencies));
    }
    
    @Deprecated
    public void setTreeFeatures(Function<List<EditorPanelable>, ObservableList<EditorPanelable>> treeMakerFn){
        this.treeMakerFn = treeMakerFn;
    }
    
    public IntegerProperty expandProperty(){
        return expand;
    }
    
    public void setExpand(int newExpand){
        expand.set(newExpand);
    }
    
    public int getExpand(){
        return expand.get();
    }
    
    
    @Override
    public void notifyDelete(EditorPanelable deleted) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void notifyEdit(EditorPanelable edited) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void notifyAdd(EditorPanelable added) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void notifyAddBySample(EditorPanelable addedBySample) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
