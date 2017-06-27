/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.params_general;

import ambro.AView;
import ambroafb.clients.Client;
import ambroafb.general.DBUtils;
import ambroafb.general.Utils;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.minitables.buysells.BuySell;
import ambroafb.minitables.subjects.Subject;
import ambroafb.params_general.helper.ParamGeneralDBResponse;
import authclient.db.ConditionBuilder;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import org.json.JSONObject;

/**
 *
 * @author dato
 */
public class ParamGeneral extends EditorPanelable {
    
    public static final String DB_SELECT_PROC_NAME = "process_general_param_select";
    public static final String DB_INSERT_UPDATE_PROC_NAME = "process_general_param_insert_update";
                                                
    @AView.Column(title = "%client", width = "150", styleClass = "textCenter")
    private final StringProperty clientDescrip;
    private final ObjectProperty<Client> clientObj;
    
    @AView.Column(title = "%buysell", width = "100", styleClass = "textCenter")
    private final StringProperty buySellDescrip;
    private final ObjectProperty<BuySell> buySellObj;
    
    @AView.Column(title = "%subject", width = "100", styleClass = "textCenter")
    private final StringProperty subjectDescrip;
    private final ObjectProperty<Subject> subjectObj;
    
    @AView.Column(title = "%param_type", width = "150")
    private final StringProperty paramType;
    
    @AView.Column(title = "%param", width = "100")
    private final StringProperty param;
    
    private static final String ALL = "ALL";
    
    public ParamGeneral(){
        clientDescrip = new SimpleStringProperty(ALL);
        clientObj = new SimpleObjectProperty<>(new Client());
        clientObj.get().setFirstName(ALL);
        clientObj.get().setRecId(0);
        
        buySellDescrip = new SimpleStringProperty(ALL);
        buySellObj = new SimpleObjectProperty<>(new BuySell());
        buySellObj.get().setDescrip(ALL);
        buySellObj.get().setRecId(0);
        
        subjectDescrip = new SimpleStringProperty(ALL);
        subjectObj = new SimpleObjectProperty<>(new Subject());
        subjectObj.get().setDescrip(ALL);
        subjectObj.get().setRecId(0);
        
        paramType = new SimpleStringProperty("");
        param = new SimpleStringProperty("");
        
        clientObj.addListener((ObservableValue<? extends Client> observable, Client oldValue, Client newValue) -> {
            resetClientDescrip();
        });
        
        buySellObj.addListener((ObservableValue<? extends BuySell> observable, BuySell oldValue, BuySell newValue) -> {
            rebindBuySellDescrip();
        });
        rebindBuySellDescrip();
        
        subjectObj.addListener((ObservableValue<? extends Subject> observable, Subject oldValue, Subject newValue) -> {
            rebindSubjectDescrip();
        });
        rebindSubjectDescrip();
    }
    
    private void resetClientDescrip(){
        Client newValue = clientObj.get();
        if (newValue != null){
            clientDescrip.set(newValue.getShortDescrip(",").get());
        }
    }
    
    private void rebindBuySellDescrip(){
        buySellDescrip.unbind();
        if (buySellObj.get() != null){
            buySellDescrip.bind(buySellObj.get().descripProperty());
        }
    }
    
    private void rebindSubjectDescrip(){
        subjectDescrip.unbind();
        if (subjectObj.get() != null){
            subjectDescrip.bind(subjectObj.get().descripProperty());
        }
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
    }
    
    public static boolean deleteOneFromDB(int id) {
        System.out.println("delete general_params ??");
        return false;
    }
    
    
    // Propertis:
    public ObjectProperty<Client> clientProperty(){
        return clientObj;
    }
    
    public ObjectProperty<BuySell> buySellProperty(){
        return buySellObj;
    }
    
    public StringProperty buySellDescripProperty(){
        return buySellDescrip;
    }
    
    public ObjectProperty<Subject> subjectProperty(){
        return subjectObj;
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
    private Integer getIdFrom(EditorPanelable object){
        Integer result = null;
        if (object != null){
            int id = object.getRecId();
            result = (id == 0) ? null : id;  // For DB we need that method returns null;
        }
        return result;
    }
    
//    public Integer getClientId() {
//        return getIdFrom(clientObj.get());
//    }
    
    public Integer getAttitude() {
        return getIdFrom(buySellObj.get());
    }

    @JsonIgnore
    public String getAttitudeDescrip() {
        return (buySellObj.get() != null) ? buySellObj.get().getDescrip() : buySellDescrip.get();
    }

    public Integer getMerchandise() {
        return getIdFrom(subjectObj.get());
    }

    @JsonIgnore
    public String getMerchandiseDescrip() {
        return subjectDescrip.get();
    }

    public String getParamType() {
        return paramType.get();
    }

    public String getParam() {
        return param.get();
    }
    
    
    // Setters:
//    public void setClientId(Integer clientId) { // It is needed in "copyFrom" method.
//        if (clientObj.get() != null){
//            clientObj.get().setRecId((clientId == null) ? 0 : clientId);
//        }
//    }

    public void setAttitude(Integer buysellId) {
        if (buySellObj.get() != null){
            buySellObj.get().setRecId((buysellId == null) ? 0 : buysellId);
        }
    }
    
    @JsonProperty
    public void setAttitudeDescrip(String descrip) {
        if (buySellObj.get() != null){
            buySellObj.get().setDescrip(descrip);
        }
    }

    public void setMerchandise(Integer subjectId) {
        if (subjectObj.get() != null){
            subjectObj.get().setRecId((subjectId == null) ? 0 : subjectId);
        }
    }
    
    @JsonProperty
    public void setMerchandiseDescrip(String descrip) {
        if (subjectObj.get() != null){
            subjectObj.get().setDescrip(descrip);
        }
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
        
        clientObj.set(otherParamGeneral.clientProperty().get());
//        setClientId(Utils.avoidNullAndReturnInt(otherParamGeneral.getClientId()));
        
        buySellObj.get().copyFrom(otherParamGeneral.buySellProperty().get());
//        setBuysell(Utils.avoidNullAndReturnInt(otherParamGeneral.getBuysell()));
//        setBuysellDescrip(otherParamGeneral.getBuysellDescrip());
        subjectObj.get().copyFrom(otherParamGeneral.subjectProperty().get());
//        setSubject(Utils.avoidNullAndReturnInt(otherParamGeneral.getSubject()));
//        setSubjectDescrip(otherParamGeneral.getSubjectDescrip());
        
        setParamType(otherParamGeneral.getParamType());
        setParam(otherParamGeneral.getParam());
    }

    @Override
    public boolean compares(EditorPanelable backup) {
        ParamGeneral other = (ParamGeneral)backup;
//        Utils.avoidNullAndReturnInt(getClientId()) == Utils.avoidNullAndReturnInt(other.getClientId()) &&
        return  
                buySellObj.get().compares(other.buySellProperty().get()) &&
                subjectObj.get().compares(other.subjectProperty().get()) &&
                getParamType().equals(other.getParamType()) &&
                getParam().equals(other.getParam());
    }

    @Override
    public String toStringForSearch() {
//        int clientID = Utils.avoidNullAndReturnInt(getClientId());
//        String clientDescr = ""; // (clientID <= 0) ? ALL : "" + clientID;
        
//        int buySellID = Utils.avoidNullAndReturnInt(getBuysell());
//        String buySellStr = (buySellID <= 0) ? ALL : "" + buySellID;

        int subjectID =  Utils.avoidNullAndReturnInt(getMerchandise());
        String subjectStr = (subjectID <= 0) ? ALL : "" + subjectID;
        return  clientDescrip.get() + " " + buySellDescrip.get() + " " + subjectDescrip.get() + " " +
                    getParamType() + " " + getParam();
    }
    
    @Override
    public String toString(){
        String delimiter = "; ";
        return  clientDescrip.get() + delimiter + buySellDescrip.get() + delimiter + 
                subjectDescrip.get() + delimiter + getParamType() + delimiter + getParam();
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
