/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.params_general;

import ambro.AView;
import ambroafb.general.DBUtils;
import ambroafb.general.interfaces.EditorPanelable;
import authclient.db.ConditionBuilder;
import java.util.ArrayList;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;
import org.json.JSONObject;

/**
 *
 * @author dato
 */
public class ParamGeneral extends EditorPanelable {
    
    private static final String DB_VIEW_NAME = "process_general_params";
    
    @AView.Column(title = "%clients", width = "100", cellFactory = ParamsCellFactory.class)
    private int clientId;
    
    @AView.Column(title = "%buysells", width = "100", cellFactory = ParamsCellFactory.class)
    private int buysellId;
    
    @AView.Column(title = "%subjects", width = "100", cellFactory = ParamsCellFactory.class)
    private int subjectId;
    
    @AView.Column(title = "%param_type", width = "100")
    private String paramType;
    
    @AView.Column(title = "%param", width = "100")
    private String param;
    
    public ParamGeneral(){
    }
    
    // DB methods:
    public static ArrayList<ParamGeneral> getAllFromDB(){
        JSONObject params = new ConditionBuilder().build();
        ArrayList<ParamGeneral> paramsGeneral = DBUtils.getObjectsListFromDB(ParamGeneral.class, DB_VIEW_NAME, params);
        paramsGeneral.sort((ParamGeneral p1, ParamGeneral p2) -> p2.getRecId() - p1.getRecId());
        return paramsGeneral;
    }
    
    
    // Getters:
    public int getClientId() {
        return clientId;
    }

    public int getBuysell() {
        return buysellId;
    }

    public int getSubject() {
        return subjectId;
    }

    public String getParamType() {
        return paramType;
    }

    public String getParam() {
        return param;
    }
    
    
    // Setters:
    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public void setBuysell(int bysellId) {
        this.buysellId = bysellId;
    }

    public void setSubject(int subjectId) {
        this.subjectId = subjectId;
    }

    public void setParamType(String paramType) {
        this.paramType = paramType;
    }

    public void setParam(String param) {
        this.param = param;
    }
    
    

    // Overrides:
    @Override
    public ParamGeneral cloneWithoutID() {
        ParamGeneral clone = new ParamGeneral();
        clone.copyFrom(this);
        return clone;
    }

    @Override
    public ParamGeneral cloneWithID() {
        ParamGeneral clone = cloneWithoutID();
        clone.setRecId(this.getRecId());
        return clone;
    }

    @Override
    public void copyFrom(EditorPanelable other) {
        ParamGeneral otherParamGeneral = (ParamGeneral)other;
        setClientId(otherParamGeneral.getClientId());
        setBuysell(otherParamGeneral.getBuysell());
        setSubject(otherParamGeneral.getSubject());
        setParamType(otherParamGeneral.getParamType());
        setParam(otherParamGeneral.getParam());
    }

    @Override
    public boolean compares(EditorPanelable backup) {
        ParamGeneral other = (ParamGeneral)backup;
        return  getClientId() == other.getClientId() &&
                getBuysell() == other.getBuysell() &&
                getSubject() == other.getSubject() &&
                getParamType().equals(other.getParamType()) &&
                getParam().equals(other.getParam());
    }

    @Override
    public String toStringForSearch() {
        return  getClientId() + " " + getBuysell() + " " + getSubject() + " " +
                    getParamType() + " " + getParam();
    }
    
    
    public static class ParamsCellFactory implements Callback<TableColumn<ParamGeneral, Integer>, TableCell<ParamGeneral, Integer>> {

        @Override
        public TableCell<ParamGeneral, Integer> call(TableColumn<ParamGeneral, Integer> param) {
            return new TableCell<ParamGeneral, Integer>() {
                @Override
                public void updateItem(Integer id, boolean empty) {
                    setText(empty ? null : (id == 0 ? "ALL" : "" + id));
                }
            };
        }
    } 
}
