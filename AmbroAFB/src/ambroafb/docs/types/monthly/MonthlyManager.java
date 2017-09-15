/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.docs.types.monthly;

import ambroafb.docs.Doc;
import ambroafb.docs.types.DocManager;
import ambroafb.docs.types.monthly.dialog.MonthlyDialog;
import ambroafb.general.Names;
import ambroafb.general.interfaces.Dialogable;
import ambroafb.general.interfaces.EditorPanelable;
import java.util.ArrayList;
import javafx.stage.Stage;

/**
 *
 * @author dkobuladze
 */
public class MonthlyManager implements DocManager {
    
    private final String DB_VIEW_NAME = "docs_whole";

    @Override
    public EditorPanelable getOneFromDB(int id) {
        return null;
    }
    

    @Override
    public ArrayList<Doc> saveOneToDB(EditorPanelable newDocComponent) {
        ArrayList<Doc> monthlies = new ArrayList();
        return monthlies;
    }

    @Override
    public boolean deleteOneFromDB(int id) {
        return false;
    }

    @Override
    public void undo() {

    }

    @Override
    public Dialogable getDocDialogFor(Stage owner, Names.EDITOR_BUTTON_TYPE type, EditorPanelable object) {
        MonthlyDialog dialog = new MonthlyDialog(object, type, owner);
        return dialog;
    }
    
}
