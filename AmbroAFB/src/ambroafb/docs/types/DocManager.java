/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.docs.types;

import ambroafb.general.Names;
import ambroafb.general.interfaces.Dialogable;
import ambroafb.general.interfaces.EditorPanelable;
import javafx.stage.Stage;

/**
 *
 * @author dkobuladze
 */
public interface DocManager {
    
    /**
        * The method gets specific doc component from DB.
        * @param id The id of interesting doc.
        * @return 
        */
    public EditorPanelable getOneFromDB(int id);
    
    /**
        * The method saves doc component to DB.
        * @param newDocComponent New doc component abstraction that must be save.
        * @return 
        */
    public EditorPanelable saveOneToDB(EditorPanelable newDocComponent);
    
    /**
        * The method removes specific doc component from DB.
        * @param id The id of doc, that must be deleted.
        * @return 
        */
    public boolean deleteOneFromDB(int id);
    
    /**
     *  The method discards changing of selected component from list.
     */
    public void undo();
    
    /**
        * The method returns doc component specific dialog according to editor_button_type.
        *  @param owner The owner for dialog stage.
        * @param type One from: Delete, View, Edit, Add
        * @param object Object that must be show on scene.
        * @return DocDialogable abstraction.
        */
    public Dialogable getDocDialogFor(Stage owner, Names.EDITOR_BUTTON_TYPE type, EditorPanelable object);
}
