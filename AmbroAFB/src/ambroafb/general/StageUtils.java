/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general;

import java.util.function.Supplier;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 *
 * @author dato
 */
public class StageUtils {
    
    // The stage frame offset (area out of scene):
    private static final double stageFrameOffset = 20;
    
    /**
     * The function provides to follow child stage to owner stage when owner is moving.
     * Also if child center is out of owner bounds, then its center will move into owners bounds.
     * @param owner 
     * @param child
     */
    public static void followChildTo(Stage owner, Stage child){
        owner.xProperty().addListener(new LocationListener(owner, child, true));
        owner.yProperty().addListener(new LocationListener(owner, child, false));
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
        
        private final Stage owner;
        private final Stage child;
        private final boolean isListenerForX;
        
        public LocationListener(Stage owner, Stage child, boolean isListenerForX){
            this.owner = owner;
            this.child = child;
            this.isListenerForX = isListenerForX;
        }

        @Override
        public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
            Coordinate ownerCenter = getCenterOf(owner);
            if (ownerCenter != null) {
                double ownerDelta = oldValue.doubleValue() - newValue.doubleValue();
                setChildCenter();
                if (isListenerForX) {
                    child.setX(child.getX() - ownerDelta);
                } else {
                    child.setY(child.getY() - ownerDelta);
                }
            }
        }
        
        private Coordinate getCenterOf(Stage currStage){
            Coordinate center = new Coordinate();
            double halftWidth = currStage.getWidth() / 2;
            double halfHeight = currStage.getHeight() / 2;
            center.x = currStage.getX() + halftWidth;
            center.y = currStage.getY() + halfHeight;
            Rectangle2D screen = Screen.getPrimary().getVisualBounds();
            return (center.x > screen.getMinX() && center.x < screen.getMaxX() &&
                    center.y > screen.getMinY() && center.y < screen.getMaxY())
                    ? center : null;
        }
        
        private void setChildCenter(){
            Coordinate childCenter = getCenterOf(child);
            if (childCenter == null) return;
            if (childCenter.x < owner.getX()){
                child.setX(owner.getX() - child.getWidth() / 2);
            }
            else if(childCenter.x > owner.getX() + owner.getWidth()){
                child.setX(owner.getX() + owner.getWidth() - child.getWidth() / 2);
            }
            
            if (childCenter.y < owner.getY()){
                child.setY(owner.getY() - child.getHeight() / 2);
            }
            else if(childCenter.y > owner.getY() + owner.getHeight()){
                child.setY(owner.getY() + owner.getHeight() - child.getHeight() / 2);
            }
        }
        
        private class Coordinate {
            public double x, y;
        }
    }
}
