/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general.interfaces;

import ambroafb.general.SceneUtils;
import ambroafb.general.StageUtils;
import ambroafb.general.StagesContainer;
import ambroafb.general.editor_panel.EditorPanel;
import java.util.function.Supplier;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author dato
 */
public abstract class ListingStage extends UserInteractiveStage {
    
    private EditorPanelableManager editorPanelableManager;
    private ListingController controller;
    
    /**
     * @param owner The owner stage.
     * @param nameForPath Path for save the opened stage.
     * @param localizableTitle The key which will convert an appropriate language by bundle.
     */
    public ListingStage(Stage owner, String nameForPath, String localizableTitle){
        super(owner, nameForPath, localizableTitle, "/images/list.png");
        this.setResizable(true);
    }
    
    /**
     *  The constructor of listingStage provides to create base features stage for list elements.
     * @param owner The owner of list stage.
     * @param fxmlPath The path of FXML file.
     * @param contentClass The table content class.
     * @param localizableTitle The bundle key.
     * @param editorPanel The editor panel type.
     */
    public ListingStage(Stage owner, String fxmlPath, Class contentClass, String localizableTitle, EditorPanel editorPanel){
        super(owner, contentClass.getSimpleName(), localizableTitle, "/images/list.png");
        this.setResizable(true);
        
        Scene scene = SceneUtils.createScene(fxmlPath, null);
        controller = (ListingController) scene.getProperties().get("controller");
        controller.setEditorPanel(editorPanel);
        controller.addListWith(contentClass);
        controller.registerObserver(editorPanel);
        this.setScene(scene);
        
        onCloseRequestProperty().set((EventHandler<WindowEvent>) (WindowEvent event) -> {
            controller.getEditorPanel().getExitButton().getOnAction().handle(null);
            if(event != null) event.consume();
        });
        
        StageUtils.stopStageWidthDecrease((Stage)this, () -> controller.getEditorPanel().getPanelMinWidth());
        StagesContainer.setSizeFor((Stage)this);
    }
    
    
    
    /**
     * The method sets min width to current stage.
     * @param supplier The function that gives a min width for stage in runtime.
     */
    public final void setFeatures(Supplier<Double> supplier) {
        StageUtils.stopStageWidthDecrease((Stage)this, supplier);
        StagesContainer.setSizeFor((Stage)this);
    }
    
    public void setEPManager(EditorPanelableManager manager){
        if (manager != null){
            this.editorPanelableManager = manager;
            controller.dataFetchProvider = manager.getDataFetchProvider();
        }
    }
    
    public EditorPanelableManager getEPManager(){
        return editorPanelableManager;
    }
    
    public ListingController getController(){
        return controller;
    }
}
