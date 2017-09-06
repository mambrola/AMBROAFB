/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.docs;

import ambro.AView;
import ambroafb.currencies.Currency;
import ambroafb.docs.types.DocComponent;
import ambroafb.general.DBUtils;
import ambroafb.general.DateConverter;
import ambroafb.general.interfaces.EditorPanelable;
import authclient.db.ConditionBuilder;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDate;
import java.util.ArrayList;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;
import org.json.JSONObject;

/**
 *
 * @author dkobuladze
 */
public class Doc extends EditorPanelable {
    
    @AView.Column(width = "24", cellFactory = MarkerCellFactory.class)
    private final IntegerProperty marker;
    
    private final IntegerProperty parentRecId;
    private final IntegerProperty processId;
    
    @AView.Column(title = "%doc_date", width = "130", styleClass = "textCenter")
    private final StringProperty docDate;
    private final ObjectProperty<LocalDate> docDateObj;

    @AView.Column(title = "%doc_in_doc_date", width = "130", styleClass = "textCenter")
    private final StringProperty docInDocDate;
    private final ObjectProperty<LocalDate> docInDocDateObj;
    
    @AView.Column(title = "%debit", width = "230")
    private final StringProperty debitDescrip;
    private final DocSide debitObj;
    
    @AView.Column(title = "%credit", width = "230")
    private final StringProperty creditDescrip;
    private final DocSide creditObj;
    
    @AView.Column(title = "%iso", width = "50", styleClass = "textCenter")
    private final StringProperty iso;
    
    @AView.Column(title = "%amount", width = "80", styleClass = "textRight")
    private final StringProperty amount;
    
    @AView.Column(title = "%docCode", width = "80")
    private final StringProperty docCode;
    
    @AView.Column(title = "%descrip", width = "270")
    private final StringProperty descrip;
    private final IntegerProperty ownerId;
    
    private int docType;
    
    private static final String DB_VIEW_NAME = "docs_whole";
    private DocComponent dialogAbstraction;
    
    public Doc(){
        marker = new SimpleIntegerProperty(0);
        parentRecId = new SimpleIntegerProperty();
        processId = new SimpleIntegerProperty();
        docDate = new SimpleStringProperty("");
        docDateObj = new SimpleObjectProperty<>();
        docInDocDate = new SimpleStringProperty("");
        docInDocDateObj = new SimpleObjectProperty<>();
        
        debitDescrip = new SimpleStringProperty("");
        debitObj = new DocSide(true);
        
        creditDescrip = new SimpleStringProperty("");
        creditObj = new DocSide(false);
        
        iso = new SimpleStringProperty();
        amount = new SimpleStringProperty();
        docCode = new SimpleStringProperty();
        descrip = new SimpleStringProperty();
        ownerId = new SimpleIntegerProperty();
        
//        docDateObj.addListener((ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) -> {
//            docDate.set(convertDateToString(newValue));
//        });
//        
//        docInDocDateObj.addListener((ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) -> {
//            docInDocDate.set(convertDateToString(newValue));
//        });
    }
    
    private String convertDateToString(LocalDate date){
        String dateStr = "";
        if (date != null){
            dateStr = DateConverter.getInstance().getDayMonthnameYearBySpace(date);
        }
        return dateStr;
    }
    
    // DB methods:
    public static ArrayList<Doc> getAllFromDB() {
        JSONObject params = new ConditionBuilder().build();
        ArrayList<Doc> docs = DBUtils.getObjectsListFromDB(Doc.class, DB_VIEW_NAME, params);
        return docs;
    }
    
    public static Doc getOneFromDB (int recId){
        JSONObject params = new ConditionBuilder().where().and("rec_id", "=", recId).condition().build();
        return DBUtils.getObjectFromDB(Doc.class, DB_VIEW_NAME, params);
    }
    
    public static Currency saveOneToDB(Doc doc){
        if (doc == null) return null;
//        return DBUtils.saveObjectToDBSimple(doc, DB_TABLE_NAME);
        return null;
    }
    
    public static boolean deleteOneFromDB(int productId){
        System.out.println("delete from db...??");
        return false;
    }
    
    // Properties:
    public IntegerProperty parentRecIdProperty(){
        return parentRecId;
    }
    public IntegerProperty processIdProperty(){
        return processId;
    }
    
    public ObjectProperty<LocalDate> docDateProperty(){
        return docDateObj;
    }
    
    public ObjectProperty<LocalDate> docInDocDateProperty(){
        return docInDocDateObj;
    }
    
    public StringProperty isoProperty(){
        return iso;
    }
    
    public StringProperty amountProperty(){
        return amount;
    }
    
    public IntegerProperty ownerIdProperty(){
        return ownerId;
    }
            
    
    
    // Getters:
    public int getDocType(){
        return docType;
    }
    
    public int getParentRecId(){
        return parentRecId.get();
    }
    
    public int getProcessId(){
        return processId.get();
    }
    
    public String getDocDate(){
        return docDate.get();
    }
    
    public String getDocInDocDate(){
        return docInDocDate.get();
    }
    
    public DocSide getCredit(){
        return creditObj;
    }
    
    public DocSide getDebit(){
        return debitObj;
    }
    
    public String getIso(){
        return iso.get();
    }
    
    public Float getAmount(){
        return Float.valueOf(amount.get());
    }
    
    public String getDocCode(){
        return docCode.get();
    }
    
    public String getDescrip(){
        return descrip.get();
    }
    
    public int getOwnerId(){
        return ownerId.get();
    }
    
    public int getMarker(){
        return marker.get();
    }
    
    @JsonIgnore
    public boolean isParentDoc(){
        return marker.get() == -1;
    }
    
    
    // Setters:
    public void setDocType(int docType){
        this.docType = docType;
    }
    
    public void setParentRecId(int parentId){
        this.parentRecId.set(parentId);
    }
    
    public void setProcessId(int processID){
        this.processId.set(processID);
    }
    
    public void setDocDate(String docDate){
        this.docDate.set(docDate);
    }
    
    public void setDocInDocDate(String docInDocDate){
        this.docInDocDate.set(docInDocDate);
    }
    
    public void setDebitId(int debitId){
        debitObj.setId(debitId);
    }
    
    public void setDebitAccount(int accountNumber){
        debitObj.setAccount(accountNumber);
    }
    
    public void setDebitDescrip(String descrip){
        debitObj.setDescrip(descrip);
        debitDescrip.set(descrip);
    }
    
    public void setCreditId(int creditId){
        creditObj.setId(recId);
    }
    
    public void setCreditAccount(int accountNumber){
        creditObj.setAccount(accountNumber);
    }
    
    public void setCreditDescrip(String descrip){
        creditObj.setDescrip(descrip);
        creditDescrip.set(descrip);
    }
    
    public void setIso(String iso){
        this.iso.set(iso);
    }
    
    public void setAmount(Float amount){
        this.amount.set("" + amount);
    }
    
    public void setDocCode(String docCode){
        this.docCode.set(docCode);
    }
    
    public void setDescrip(String descrip){
        this.descrip.set(descrip);
    }
    
    public void setOwnerId(int ownerId){
        this.ownerId.set(ownerId);
    }
    
    public void setMarker(int marker){
        this.marker.set(marker);
    }
    
    
    @Override
    public Doc cloneWithoutID() {
        Doc clone = new Doc();
        clone.copyFrom(this);
        return clone;
    }

    @Override
    public Doc cloneWithID() {
        Doc clone = cloneWithoutID();
        clone.setRecId(getRecId());
        return clone;
    }

    @Override
    public void copyFrom(EditorPanelable other) {
        Doc otherDoc = (Doc) other;
        setParentRecId(otherDoc.getParentRecId());
        setProcessId(otherDoc.getProcessId());
        setDocDate(otherDoc.getDocDate());
        setDocInDocDate(otherDoc.getDocInDocDate());
        setIso(otherDoc.getIso());
        debitObj.copyFrom(otherDoc.getDebit());
        creditObj.copyFrom(otherDoc.getCredit());
        setAmount(otherDoc.getAmount());
        setDocCode(otherDoc.getDocCode());
        setDescrip(otherDoc.getDescrip());
        setOwnerId(otherDoc.getOwnerId());
        setDocType(otherDoc.getDocType());
        setMarker(otherDoc.getMarker());
    }

    @Override
    public boolean compares(EditorPanelable backup) {
        Doc docBackup = (Doc) backup;
        return  getParentRecId() == docBackup.getParentRecId() &&
                getProcessId() == docBackup.getProcessId() &&
                getDocDate().equals(docBackup.getDocDate()) &&
                getDocInDocDate().equals(docBackup.getDocInDocDate()) &&
                getIso().equals(docBackup.getIso()) &&
                creditObj.equals(docBackup.getCredit()) &&
                debitObj.equals(docBackup.getDebit()) &&
                getAmount().equals(docBackup.getAmount()) &&
                getDocCode().equals(docBackup.getDocCode()) &&
                getDescrip().equals(docBackup.getDescrip()) &&
                getOwnerId() == docBackup.getOwnerId() &&
                getDocType() == docBackup.getDocType() &&
                getMarker() == docBackup.getMarker();
    }

    @Override
    public String toStringForSearch() {
        return  getDocDate() + " " + getDocInDocDate() + " " + getDebit().getDescrip() + " "  + 
                getCredit().getDescrip() + " " + getIso() + " " + getDocCode() + " " + getDescrip();
    }

    @Override
    public String toString() {
        return "Doc{" + "parentRecId=" + parentRecId + ", processId=" + processId + ", docDate=" + docDate + ", docDateObj=" + docDateObj + ", docInDocDate=" + docInDocDate + ", docInDocDateObj=" + docInDocDateObj + ", debitDescrip=" + debitDescrip + ", debitObj=" + debitObj + ", creditDescrip=" + creditDescrip + ", creditObj=" + creditObj + ", iso=" + iso + ", amount=" + amount + ", docCode=" + docCode + ", descrip=" + descrip + ", ownerId=" + ownerId + ", docType=" + docType + ", dialogAbstraction=" + dialogAbstraction + '}';
    }

    private static final String DOC_NO_CHILD_IMG_URL = "/images/doc_hasnot_child.png", DOC_PARENT_IMG_URL = "/images/doc_parent.png", DOC_CHILD_IMG_URL = "/images/doc_child.png";
    
    public static class MarkerCellFactory implements Callback<TableColumn<Doc, Integer>, TableCell<Doc, Integer>> {

        @Override
        public TableCell<Doc, Integer> call(TableColumn<Doc, Integer> param) {
            TableCell<Doc, Integer> cell = new TableCell<Doc, Integer>(){
                private final ImageView view = new ImageView();

                @Override
                public void updateItem(Integer marker, boolean empty) {
                    if (empty) {
                        setGraphic(null);
                    } else {
                        String imgPath = DOC_NO_CHILD_IMG_URL;
                        switch(marker){
                            case -1:
                                imgPath = DOC_PARENT_IMG_URL;
                                break;
                            case 1:
                                imgPath = DOC_CHILD_IMG_URL;
                                break;
                            default:
                                break;
                        }
                        view.setImage(new Image(imgPath));
                        setGraphic(view);
                    }
                }
            };
            return cell;
        }
        
    }
}
