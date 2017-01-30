/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general.stages;

import ambroafb.general.GeneralConfig;
import ambroafb.general.StageUtils;
import ambroafb.general.StagesContainer;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 *
 * @author dato
 */
public abstract class UserInteractiveStage extends Stage {
    
    /**
     * 
     * @param owner The owner stage.
     * @param nameForPath Path for save the opened stage.
     * @param stageTitleBundleKey The key which will convert an appropriate language by bundle.
     * @param iconPath The path for stage upper left icon.
     */
    public UserInteractiveStage(Stage owner, String nameForPath, String stageTitleBundleKey, String iconPath) {
        super();

        this.initOwner(owner);
        this.setResizable(false);
        
        if (nameForPath != null && !nameForPath.isEmpty()){
            StagesContainer.registerStageByOwner(owner, nameForPath, (Stage)this);
        }
        if (stageTitleBundleKey != null && !stageTitleBundleKey.isEmpty()){
            this.setTitle(GeneralConfig.getInstance().getTitleFor(stageTitleBundleKey));
        }
        if (iconPath != null && !iconPath.isEmpty()) {
            this.getIcons().add(new Image(iconPath));
        }
        
        StageUtils.centerChildOf(owner, (Stage)this);
        StageUtils.followChildTo(owner, (Stage)this);
    }
    
}
