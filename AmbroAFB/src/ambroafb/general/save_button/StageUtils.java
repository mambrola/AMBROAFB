/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general.save_button;

import java.util.function.Supplier;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.stage.Stage;

/**
 *
 * @author dato
 */
public class StageUtils {
    
    private static final double stageFrameOffset = 20;
    
    /**
     * The function provides to follow child stage to owner stage when owner is moving.
     * Also if child center is out of owner bounds, then its center will move into owners bounds.
     * @param owner 
     * @param child
     */
    public static void followChildToOwner(Stage owner, Stage child){
        owner.xProperty().addListener(new LocationListener(child, owner, true));
        owner.yProperty().addListener(new LocationListener(child, owner, false));
    }
    
    /**
     * The function sets child stage location on center of owner stage.
     * @param owner
     * @param child 
     */
    public static void centerChildOf(Stage owner, Stage child){
        child.widthProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            if (oldValue.intValue() == 0){
                child.setX(owner.getX() + owner.getWidth() / 2 - child.getWidth() / 2);
            }
        });
        child.heightProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            if (oldValue.intValue() == 0){
                child.setY(owner.getY() + owner.getHeight()/ 2 - child.getHeight() / 2);
            }
        });
    }
    
    /**
     * The function stops current stage width decreasing process and calls supplier method for
     * access min width parameter.
     * @param currStage Stage that don't decrease.
     * @param supplier The function which gets width, where is min width for current stage.
     */
    public static void stopStageWidthDecrease(Stage currStage, Supplier<Double> supplier){
        currStage.widthProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            double edPanMinWidth = supplier.get();
            double stageMinWidth = edPanMinWidth + stageFrameOffset;
            if (newValue.doubleValue() < stageMinWidth) {
                currStage.setMinWidth(stageMinWidth);
            }
        });
    }
    

    /**
     * The inner class provides to change child stage location by owners moving.
     * 1. If child center is out of owner bounds, it will be inside the owner.
     * 2. If owner stage moving on screen, the child stage will follow the owner.
     */
    private static class LocationListener implements ChangeListener<Number> {
        
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
                child.setY(owner.getY() - child.getHeight() / 2);
            }
            else if(childCenterY > owner.getY() + owner.getHeight()){
                child.setY(owner.getY() + owner.getHeight() - child.getHeight() / 2);
            }
        }
        
    }
}
