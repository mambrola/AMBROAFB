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
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.json.JSONObject;

/**
 *
 * @author dato
 */
public class ParamGeneral extends EditorPanelable {
    
    private static final String DB_VIEW_NAME = "process_general_params";
    
    @AView.Column(title = "%clients", width = "100")
//    private int clientId;
    private final StringProperty clientId;
    
    @AView.Column(title = "%buysells", width = "100")
//    private int buysellId;
    private final StringProperty buysellId;
    
    @AView.Column(title = "%subjects", width = "100")
//    private int subjectId;
    private final StringProperty subjectId;
    
    @AView.Column(title = "%param_type", width = "100")
    private final StringProperty paramType;
    
    @AView.Column(title = "%param", width = "100")
    private final StringProperty param;
    
    public ParamGeneral(){
        clientId = new SimpleStringProperty("ALL");
        buysellId = new SimpleStringProperty("ALL");
        subjectId = new SimpleStringProperty("ALL");
        paramType = new SimpleStringProperty("ALL");
        param = new SimpleStringProperty("ALL");
    }
    
    // DB methods:
    public static ArrayList<ParamGeneral> getAllFromDB(){
        JSONObject params = new ConditionBuilder().build();
        ArrayList<ParamGeneral> paramsGeneral = DBUtils.getObjectsListFromDB(ParamGeneral.class, DB_VIEW_NAME, params);
        paramsGeneral.sort((ParamGeneral p1, ParamGeneral p2) -> p2.getRecId() - p1.getRecId());
        return paramsGeneral;
    }
    
    public static ParamGeneral getOneFromDB(int id) {
        JSONObject params = new ConditionBuilder().where().and("rec_id", "=", id).condition().build();
        return DBUtils.getObjectFromDB(ParamGeneral.class, DB_VIEW_NAME, params);
    }
    
    
    // Propertis:
    public StringProperty clientProperty(){
        return clientId;
    }
    
    public StringProperty buysellProperty(){
        return buysellId;
    }
    
    public StringProperty subjectProperty(){
        return subjectId;
    }

    public StringProperty paramTypeProperty() {
        return paramType;
    }

    public StringProperty paramProperty() {
        return param;
    }
    
    
    // Getters:
    public String getClientId() {
        return clientId.get();
    }

    public String getBuysell() {
        return buysellId.get();
    }

    public String getSubject() {
        return subjectId.get();
    }

    public String getParamType() {
        return paramType.get();
    }

    public String getParam() {
        return param.get();
    }
    
    
    // Setters:
    public void setClientId(String clientId) {
        this.clientId.set(clientId);
    }

    public void setBuysell(String buysellId) {
        this.buysellId.set(buysellId);
    }

    public void setSubject(String subjectId) {
        this.subjectId.set(subjectId);
    }

    public void setParamType(String paramType) {
        this.paramType.set(paramType);
    }

    public void setParam(String param) {
        this.param.set(param);
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
        return  getClientId().equals(other.getClientId()) &&
                getBuysell().equals(other.getBuysell()) &&
                getSubject().equals(other.getSubject()) &&
                getParamType().equals(other.getParamType()) &&
                getParam().equals(other.getParam());
    }

    @Override
    public String toStringForSearch() {
        return  getClientId() + " " + getBuysell() + " " + getSubject() + " " +
                    getParamType() + " " + getParam();
    }
    
    
//    public static class ParamsCellFactory implements Callback<TableColumn<ParamGeneral, Integer>, TableCell<ParamGeneral, Integer>> {
//
//        @Override
//        public TableCell<ParamGeneral, Integer> call(TableColumn<ParamGeneral, Integer> param) {
//            return new TableCell<ParamGeneral, Integer>() {
//                @Override
//                public void updateItem(Integer id, boolean empty) {
//                    setText(empty ? null : (id == 0 ? "ALL" : "" + id));
//                }
//            };
//        }
//    } 
}
