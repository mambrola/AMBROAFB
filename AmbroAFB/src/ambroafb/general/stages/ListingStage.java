/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general.stages;

import ambroafb.general.StageUtils;
import ambroafb.general.StagesContainer;
import java.util.function.Supplier;
import javafx.stage.Stage;

/**
 *
 * @author dato
 */
public abstract class ListingStage extends UserInteractiveStage {
    
    /**
     * 
     * @param owner The owner stage.
     * @param nameForPath Path for save the opened stage.
     * @param localizableTitle The key which will convert an appropriate language by bundle.
     */
    public ListingStage(Stage owner, String nameForPath, String localizableTitle){
        super(owner, nameForPath, localizableTitle, "/images/list.png");
        this.setResizable(true);
    }
    
    /**
     * The method sets min width to current stage.
     * @param supplier The function that gives a min width for stage in runtime.
     */
    public final void setFeatures(Supplier<Double> supplier) {
        StageUtils.stopStageWidthDecrease((Stage)this, supplier);
        StagesContainer.setSizeFor((Stage)this);
    }
}
