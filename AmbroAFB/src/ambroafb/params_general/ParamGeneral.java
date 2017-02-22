/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.params_general;

import ambro.AView;
import ambroafb.general.DBUtils;
import ambroafb.general.GeneralConfig;
import ambroafb.general.Utils;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.minitables.buysells.BuySell;
import ambroafb.minitables.subjects.Subject;
import authclient.db.ConditionBuilder;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;
import org.json.JSONObject;

/**
 *
 * @author dato
 */
public class ParamGeneral extends EditorPanelable {
    
    private static final String DB_SELECT_PROC_NAME = "process_general_param_select";
    private static final String DB_INSERT_UPDATE_PROC_NAME = "process_general_param_insert_update";
                                                
    @AView.Column(title = "%client", width = "100", cellFactory = ParamsCellFactory.class, styleClass = "textCenter")
    private final StringProperty clientIdStr;
    
    @AView.Column(title = "%buysell", width = "100", styleClass = "textCenter")
    private final ObjectProperty<BuySell> buySellObj;
    
    @AView.Column(title = "%subject", width = "100", styleClass = "textCenter")
    private final ObjectProperty<Subject> subjectObj;
    
    @AView.Column(title = "%param_type", width = "150")
    private final StringProperty paramType;
    
    @AView.Column(title = "%param", width = "100")
    private final StringProperty param;
    
    
    
    public ParamGeneral(){
//        clientIdInt = new SimpleIntegerProperty(0);
        clientIdStr = new SimpleStringProperty("");
        
        buySellObj = new SimpleObjectProperty<>(new BuySell());
        buySellObj.get().setDescrip("ALL");
        
        subjectObj = new SimpleObjectProperty<>(new Subject());
        subjectObj.get().setDescrip("ALL");
        
        paramType = new SimpleStringProperty("");
        param = new SimpleStringProperty("");
    }
    
    // DB methods:
    public static ArrayList<ParamGeneral> getAllFromDB(){
        String lang = GeneralConfig.getInstance().getDBClient().getLang();
        JSONObject json = new ConditionBuilder().build();
        ArrayList<ParamGeneral> paramsGeneral = DBUtils.getObjectsListFromDBProcess(ParamGeneral.class, DB_SELECT_PROC_NAME, lang, json);
        paramsGeneral.sort((ParamGeneral p1, ParamGeneral p2) -> p1.getRecId() - p2.getRecId());
        return paramsGeneral;
    }
    
    public static ParamGeneral getOneFromDB(int id) {
//        JSONObject params = new ConditionBuilder().where().and("rec_id", "=", id).condition().build();
//        return DBUtils.getObjectFromDB(ParamGeneral.class, "general_select", params);

        List<ParamGeneral> list = getAllFromDB().stream().filter((ParamGeneral genParam) -> {
            return genParam.getRecId() == id;
        }).collect(Collectors.toList());
        if (list != null && !list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }
    
    public static ParamGeneral saveOneToDB(ParamGeneral genParam) {
        if (genParam == null) return null;
        return DBUtils.saveObjectToDBByProcess(genParam, DB_INSERT_UPDATE_PROC_NAME);
    }
    
    public static boolean deleteOneFromDB(int id) {
        System.out.println("delete general_params ??");
        return false;
    }
    
    
    // Propertis:
    public StringProperty clientProperty(){
        return clientIdStr;
    }
    
    public StringProperty buysellProperty(){
        return buySellObj.get().descripProperty();
    }
    
    public StringProperty subjectProperty(){
        return subjectObj.get().descripProperty();
    }

    public StringProperty paramTypeProperty() {
        return paramType;
    }

    public StringProperty paramProperty() {
        return param;
    }
    
    public ObjectProperty<BuySell> buySellObjProperty(){
        return buySellObj;
    }
    
    public ObjectProperty<Subject> subjectObjProperty(){
        return subjectObj;
    }
    
    
    // Getters:
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    public int getClient_id() {
        return Utils.getIntValueFor(clientIdStr.get());
    }

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    public int getBuysell() {
        return buySellObj.get().getRecId();
    }
    
    @JsonIgnore
    public String getBuysell_descrip(){
        return buySellObj.get().getDescrip();
    }

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    public int getSubject() {
        return subjectObj.get().getRecId();
    }
    
    @JsonIgnore
    public String getSubject_descrip(){
        return subjectObj.get().getDescrip();
    }

    public String getParam_type() {
        return paramType.get();
    }

    public String getParam() {
        return param.get();
    }
    
    
    // Setters:
    public void setClientId(int clientId) {
        if (clientId > 0){
            this.clientIdStr.set("" + clientId);
        }
    }

    public void setBuysell(int buysellId) {
        this.buySellObj.get().setRecId(buysellId);
    }
    
    public void setBuysellDescrip(String descrip) {
        this.buySellObj.get().setDescrip(descrip);
    }

    public void setSubject(int subjectId) {
        this.subjectObj.get().setRecId(subjectId);
    }
    
    public void setSubjectDescrip(String descrip) {
        this.subjectObj.get().setDescrip(descrip);
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
        
        setClientId(otherParamGeneral.getClient_id());
        buySellObjProperty().get().copyFrom(otherParamGeneral.buySellObjProperty().get());
        subjectObjProperty().get().copyFrom(otherParamGeneral.subjectObjProperty().get());

        setParamType(otherParamGeneral.getParam_type());
        setParam(otherParamGeneral.getParam());
    }

    @Override
    public boolean compares(EditorPanelable backup) {
        ParamGeneral other = (ParamGeneral)backup;
        return  getClient_id() == other.getClient_id() &&
                buySellObjProperty().get().compares(other.buySellObjProperty().get()) &&
                subjectObjProperty().get().compares(other.subjectObjProperty().get()) &&
                getParam_type().equals(other.getParam_type()) &&
                getParam().equals(other.getParam());
    }

    @Override
    public String toStringForSearch() {
        return  getClient_id() + " " + getBuysell() + " " + getSubject() + " " +
                    getParam_type() + " " + getParam();
    }
    
    
    
    public static class ParamsCellFactory implements Callback<TableColumn<ParamGeneral, String>, TableCell<ParamGeneral, String>> {

        @Override
        public TableCell<ParamGeneral, String> call(TableColumn<ParamGeneral, String> param) {
            return new TableCell<ParamGeneral, String>() {
                @Override
                public void updateItem(String str, boolean empty) {
                    setText(empty ? null : (str.isEmpty() ? "ALL" : str));
                }
            };
        }
    } 
    
    public static class BuySellCellFactory implements Callback<TableColumn<ParamGeneral, BuySell>, TableCell<ParamGeneral, BuySell>> {

        @Override
        public TableCell<ParamGeneral, BuySell> call(TableColumn<ParamGeneral, BuySell> param) {
            return new TableCell<ParamGeneral, BuySell>() {
                @Override
                public void updateItem(BuySell buysell, boolean empty) {
                    setText(empty ? null : (buysell.getDescrip().isEmpty() ? "ALL" : buysell.getDescrip()));
                }
            };
        }
    } 
    
    public static class SubjectCellFactory implements Callback<TableColumn<ParamGeneral, Subject>, TableCell<ParamGeneral, Subject>> {

        @Override
        public TableCell<ParamGeneral, Subject> call(TableColumn<ParamGeneral, Subject> param) {
            return new TableCell<ParamGeneral, Subject>() {
                @Override
                public void updateItem(Subject buysell, boolean empty) {
                    setText(empty ? null : (buysell.getDescrip().isEmpty() ? "ALL" : buysell.getDescrip()));
                }
            };
        }
    } 
}
