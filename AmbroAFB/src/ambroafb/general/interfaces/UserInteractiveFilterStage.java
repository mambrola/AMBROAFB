/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general.interfaces;

import ambroafb.general.Names;
import javafx.stage.Stage;

/**
 *
 * @author dkobuladze
 */
public class UserInteractiveFilterStage extends UserInteractiveStage {
    
    public UserInteractiveFilterStage(Stage owner, String stageTitleBundleKey){
        super(owner, Names.LEVEL_FOR_PATH, stageTitleBundleKey, "/images/filter.png");
    }
    
}
