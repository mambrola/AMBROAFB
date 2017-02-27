/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.params_general;

import ambro.AView;
import ambroafb.general.DBUtils;
import ambroafb.general.Utils;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.params_general.helper.ParamGeneralDBResponse;
import authclient.db.ConditionBuilder;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.ArrayList;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.json.JSONObject;

/**
 *
 * @author dato
 */
public class ParamGeneral extends EditorPanelable {
    
    private static final String DB_SELECT_PROC_NAME = "process_general_param_select";
    private static final String DB_INSERT_UPDATE_PROC_NAME = "process_general_param_insert_update";
                                                
    @AView.Column(title = "%client", width = "100", styleClass = "textCenter")
    private final StringProperty clientId;
    
    @AView.Column(title = "%buysell", width = "100", styleClass = "textCenter")
//    private final ObjectProperty<BuySell> buySellObj;
    private final StringProperty buysell;
    private final StringProperty buySellDescrip;
    
    @AView.Column(title = "%subject", width = "100", styleClass = "textCenter")
//    private final ObjectProperty<Subject> subjectObj;
    private final StringProperty subject;
    private final StringProperty subjectDescrip;
    
    @AView.Column(title = "%param_type", width = "150")
    private final StringProperty paramType;
    
    @AView.Column(title = "%param", width = "100")
    private final StringProperty param;
    
    
    private static final String ALL = "ALL";
    
    public ParamGeneral(){
        clientId = new SimpleStringProperty(ALL);
        buysell = new SimpleStringProperty(ALL);
        buySellDescrip = new SimpleStringProperty("");
        subject = new SimpleStringProperty(ALL);
        subjectDescrip = new SimpleStringProperty("");
        paramType = new SimpleStringProperty("");
        param = new SimpleStringProperty("");
        
    }
    
    // DB methods:
    public static ArrayList<ParamGeneral> getAllFromDB(){
        JSONObject json = new ConditionBuilder().build();
        ParamGeneralDBResponse paramsGeneralResponse = DBUtils.getParamsGeneral(DB_SELECT_PROC_NAME, json);
//        DBUtils.getObjectsListFromDBProcedure(ParamGeneral.class, DB_SELECT_PROC_NAME, lang, json) ;// 
        ArrayList<ParamGeneral> paramsGeneral = paramsGeneralResponse.getParamGenerals();
        paramsGeneral.sort((ParamGeneral p1, ParamGeneral p2) -> p1.getRecId() - p2.getRecId());
        return paramsGeneral;
    }
    
    public static ParamGeneral getOneFromDB(int id) {
        JSONObject json = new ConditionBuilder().where().and("rec_id", "=", id).condition().build();
        return DBUtils.getObjectFromDBProcedure(ParamGeneral.class, DB_SELECT_PROC_NAME, json);
    }
    
    public static ParamGeneral saveOneToDB(ParamGeneral genParam) {
        if (genParam == null) return null;
        return DBUtils.saveParamGeneral(genParam, DB_INSERT_UPDATE_PROC_NAME);
//        return DBUtils.saveObjectToDBByProcedure(genParam, DB_INSERT_UPDATE_PROC_NAME);
    }
    
    public static boolean deleteOneFromDB(int id) {
        System.out.println("delete general_params ??");
        return false;
    }
    
    
    // Propertis:
    public StringProperty clientProperty(){
        return clientId;
    }
    
    public StringProperty buySellProperty(){
        return buysell;
    }
    
    public StringProperty buySellDescripProperty(){
        return buySellDescrip;
    }
    
    public StringProperty subjectProperty(){
        return subject;
    }
    
    public StringProperty subjectDescripProperty(){
        return subjectDescrip;
    }
    
    public StringProperty paramTypeProperty() {
        return paramType;
    }

    public StringProperty paramProperty() {
        return param;
    }
    
    
    // Getters:
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    public Integer getClientId() {
        return Utils.getIntegerFrom(clientId.get()); // For DB we need that method returns null;
    }
    
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    public Integer getBuysell() {
        return Utils.getIntegerFrom(buysell.get()); // For DB we need that method returns null;
    }

    @JsonIgnore
    public String getBuysellDescrip() {
        return buySellDescrip.get();
    }

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    public Integer getSubject() {
        return Utils.getIntegerFrom(subject.get()); // For DB we need that method returns null;
    }

    @JsonIgnore
    public String getSubjectDescrip() {
        return subjectDescrip.get();
    }

    public String getParamType() {
        return paramType.get();
    }

    public String getParam() {
        return param.get();
    }
    
    
    // Setters:
    public void setClientId(int clientId) {
        this.clientId.set((clientId <= 0) ? ALL : "" + clientId);
    }

    public void setBuysell(int buysellId) {
        this.buysell.set((buysellId <= 0) ? ALL : "" + buysellId);
    }
    
    public void setBuysellDescrip(String descrip) {
        this.buySellDescrip.set(descrip);
    }

    public void setSubject(int subjectId) {
        this.subject.set((subjectId <= 0) ? ALL : "" + subjectId);
    }
    
    public void setSubjectDescrip(String descrip) {
        this.subjectDescrip.set(descrip);
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
        
        setClientId(Utils.avoidNullAndReturnInt(otherParamGeneral.getClientId()));
        setBuysell(Utils.avoidNullAndReturnInt(otherParamGeneral.getBuysell()));
        setBuysellDescrip(otherParamGeneral.getBuysellDescrip());
        
        setSubject(Utils.avoidNullAndReturnInt(otherParamGeneral.getSubject()));
        setSubjectDescrip(otherParamGeneral.getSubjectDescrip());
        
        setParamType(otherParamGeneral.getParamType());
        setParam(otherParamGeneral.getParam());
    }

    @Override
    public boolean compares(EditorPanelable backup) {
        ParamGeneral other = (ParamGeneral)backup;
        return  Utils.avoidNullAndReturnInt(getClientId()) == Utils.avoidNullAndReturnInt(other.getClientId()) &&
                Utils.avoidNullAndReturnInt(getBuysell()) == Utils.avoidNullAndReturnInt(other.getBuysell()) &&
                getBuysellDescrip().equals(other.getBuysellDescrip()) &&
                Utils.avoidNullAndReturnInt(getSubject()) == Utils.avoidNullAndReturnInt(other.getSubject()) &&
                getSubjectDescrip().equals(other.getSubjectDescrip()) &&
//                buySellObjProperty().get().compares(other.buySellObjProperty().get()) &&
//                subjectObjProperty().get().compares(other.subjectObjProperty().get()) &&
getParamType().equals(other.getParamType()) &&
                getParam().equals(other.getParam());
    }

    @Override
    public String toStringForSearch() {
        int clientID = Utils.avoidNullAndReturnInt(getClientId());
        int buySellID = Utils.avoidNullAndReturnInt(getBuysell());
        int subjectID =  Utils.avoidNullAndReturnInt(getSubject());
        String clientStr = (clientID <= 0) ? ALL : "" + clientID;
        String buySellStr = (buySellID <= 0) ? ALL : "" + buySellID;
        String subjectStr = (subjectID <= 0) ? ALL : "" + subjectID;
        return  clientStr + " " + buySellStr + " " + subjectStr + " " +
                    getParamType() + " " + getParam();
    }
    
    
    
//    public static class ParamsCellFactory implements Callback<TableColumn<ParamGeneral, String>, TableCell<ParamGeneral, String>> {
//
//        @Override
//        public TableCell<ParamGeneral, String> call(TableColumn<ParamGeneral, String> param) {
//            return new TableCell<ParamGeneral, String>() {
//                @Override
//                public void updateItem(String str, boolean empty) {
//                    setText(empty ? null : (str.isEmpty() ? "ALL" : str));
//                }
//            };
//        }
//    } 
//    
//    public static class BuySellCellFactory implements Callback<TableColumn<ParamGeneral, BuySell>, TableCell<ParamGeneral, BuySell>> {
//
//        @Override
//        public TableCell<ParamGeneral, BuySell> call(TableColumn<ParamGeneral, BuySell> param) {
//            return new TableCell<ParamGeneral, BuySell>() {
//                @Override
//                public void updateItem(BuySell buysell, boolean empty) {
//                    setText(empty ? null : (buysell.getDescrip().isEmpty() ? "ALL" : buysell.getDescrip()));
//                }
//            };
//        }
//    } 
//    
//    public static class SubjectCellFactory implements Callback<TableColumn<ParamGeneral, Subject>, TableCell<ParamGeneral, Subject>> {
//
//        @Override
//        public TableCell<ParamGeneral, Subject> call(TableColumn<ParamGeneral, Subject> param) {
//            return new TableCell<ParamGeneral, Subject>() {
//                @Override
//                public void updateItem(Subject buysell, boolean empty) {
//                    setText(empty ? null : (buysell.getDescrip().isEmpty() ? "ALL" : buysell.getDescrip()));
//                }
//            };
//        }
//    } 
}
