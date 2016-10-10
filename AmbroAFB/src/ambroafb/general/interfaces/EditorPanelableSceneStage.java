/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general.interfaces;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author dato
 */
public abstract class EditorPanelableSceneStage extends Stage {
    
    private static final double stageFrameOffset = 20;
    private EditorPanelableSceneController controller;
    
    public EditorPanelableSceneStage(Stage owner){
        this.initOwner(owner);
        
        // Close stage:
        this.onCloseRequestProperty().set((EventHandler<WindowEvent>) (WindowEvent event) -> {
            if (controller != null){
                controller.getEditorPanelController().getExitButton().getOnAction().handle(null);
                if(event != null) event.consume();
            }
        });
        
        // Stop stage width decreasing, when width is less then editorPanel minWidth:
        this.widthProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            if (controller != null) {
                double edPanMinWidth = controller.getEditorPanelController().getPanelMinWidth();
                double stageMinWidth = edPanMinWidth + stageFrameOffset;
                if (newValue.doubleValue() < stageMinWidth) {
                    this.setMinWidth(stageMinWidth);
                }
            }
        });
        
        // Center stage of his owner:
        this.widthProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            if (oldValue.intValue() == 0){
                this.setX(owner.getX() + owner.getWidth() / 2 - this.getWidth() / 2);
            }
        });
        this.heightProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            if (oldValue.intValue() == 0){
                this.setY(owner.getY() + owner.getHeight()/ 2 - this.getHeight() / 2);
            }
        });
        
        // change stage location via owner:
        owner.xProperty().addListener(new LocationListener(this, owner, true));
        owner.yProperty().addListener(new LocationListener(this, owner, false));
    }
    
    public void setController(EditorPanelableSceneController controller){
        this.controller = controller;
    }
    
    
    /**
     * The inner class provides to change child stage location by owners moving.
     * 1. If child center is out of owner bounds, it will be inside the owner.
     * 2. If owner stage moving on screen, the child stage will follow the owner.
     */
    private class LocationListener implements ChangeListener<Number> {
        
        private final Stage child;
        private final Stage owner;
        private final boolean isListenerForX;
        
        public LocationListener(Stage child, Stage owner, boolean isListenerForX){
            this.child = child;
            this.owner = owner;
            this.isListenerForX = isListenerForX;
        }

        @Override
        public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
            double ownerDiff = oldValue.doubleValue() - newValue.doubleValue();
            setCenter();
            if (isListenerForX){
                child.setX(child.getX() - ownerDiff);
            }
            else {
                child.setY(child.getY() - ownerDiff);
            }
        }
        
        private void setCenter(){
            double childCenterX = child.getX() + child.getWidth() / 2;
            double childCenterY = child.getY() + child.getHeight() / 2;
            if (childCenterX < owner.getX()){
                child.setX(owner.getX() - child.getWidth() / 2);
            }
            else if(childCenterX > owner.getX() + owner.getWidth()){
                child.setX(owner.getX() + owner.getWidth() - child.getWidth() / 2);
            }
            
            if (childCenterY < owner.getY()){
                child.setY(owner.getY() - child.getHeight() / 2 + 4);
            }
            else if(childCenterY > owner.getY() + owner.getWidth()){
                child.setY(owner.getY() + owner.getHeight() - child.getHeight() / 2);
            }
        }
        
    }
}
