/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.params_general;

import ambroafb.general.DBUtils;
import ambroafb.general.interfaces.EditorPanelable;
import authclient.db.ConditionBuilder;
import java.util.ArrayList;
import org.json.JSONObject;

/**
 *
 * @author dato
 */
public class ParamGeneral extends EditorPanelable {
    
    private static final String DB_VIEW_NAME = "process_general_param_whole";
    
    public ParamGeneral(){
        
    }
    
    // DB methods:
    public static ArrayList<ParamGeneral> getAllFromDB(){
        JSONObject params = new ConditionBuilder().build();
        ArrayList<ParamGeneral> paramsGeneral = DBUtils.getObjectsListFromDB(ParamGeneral.class, DB_VIEW_NAME, params);
        paramsGeneral.sort((ParamGeneral p1, ParamGeneral p2) -> p2.getRecId() - p1.getRecId());
        return paramsGeneral;
    }
    
    
    

    // Overrides:
    @Override
    public EditorPanelable cloneWithoutID() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public EditorPanelable cloneWithID() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void copyFrom(EditorPanelable other) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean compares(EditorPanelable backup) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String toStringForSearch() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
