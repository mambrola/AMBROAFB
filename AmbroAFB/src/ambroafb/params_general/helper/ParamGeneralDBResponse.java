/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.params_general.helper;

import ambroafb.params_general.ParamGeneral;
import java.util.ArrayList;

/**
 *
 * @author dato
 */
public class ParamGeneralDBResponse {
    
    private ArrayList<ParamGeneral> params;
    private ArrayList<Integer> conflictParamsIDs;
    
    public ParamGeneralDBResponse(){}
    
    public void setParamGenerals(ArrayList<ParamGeneral> list){
        params = list;
    }
    
    public ArrayList<ParamGeneral> getParamGenerals(){
        return params;
    }
    
    public void setConflictParamsIDs(ArrayList<Integer> ids){
        conflictParamsIDs = ids;
    }

    public ArrayList<Integer> getConflictParamsIDs() {
        return conflictParamsIDs;
    }
    
}
