/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general.interfaces;

import ambroafb.general.GeneralConfig;
import ambroafb.general.Names;
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
    
    public UserInteractiveStage(Stage owner, String stageTitleBundleKey, String iconPath){
        this(owner, "", stageTitleBundleKey, iconPath);
    }
    
    /**
     * The constructor creates stage for filter or dialog scene.
     * @param owner The owner stage or this.
     * @param stageTitleBundleKey The bindle key for stage title.
     * @param isFilter The flag for stage will use filter or dialog. True - for filter stage, False - for dialog stage.
     */
    public UserInteractiveStage(Stage owner, String stageTitleBundleKey, boolean isFilter) {
        this(owner, Names.LEVEL_FOR_PATH, stageTitleBundleKey, isFilter ? "/images/filter.png" : "/images/dialog.png");
    }
}
