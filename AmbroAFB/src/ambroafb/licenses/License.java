/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.licenses;

import ambro.AView;
import ambroafb.general.DateConverter;
import ambroafb.general.interfaces.EditorPanelable;
import java.time.LocalDate;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

/**
 *
 * @author Dato
 */
public class License extends EditorPanelable {

    @AView.Column(width = "20", cellFactory = CheckedCellFactory.class)
    private final BooleanProperty checked;
    
    @AView.Column(title = "%date", width = "80")
    private final StringProperty date;
    private final ObjectProperty<LocalDate> dateObjProperty;
    
    @AView.Column(title = "%license N")
    private final StringProperty licenseNumber;
    
    public License(){
        checked = new SimpleBooleanProperty();
        date = new SimpleStringProperty("");
        dateObjProperty = new SimpleObjectProperty<>();
        licenseNumber = new SimpleStringProperty("");
    }
    
    // DB methods:
    
    
    // Properties:
    public BooleanProperty checkedProperty(){
        return checked;
    }
    
    public ObjectProperty<LocalDate> dateProperty(){
        return dateObjProperty;
    }
    
    
    // Getters:
    public boolean getChecked(){
        return checked.get();
    }
    
    public String getDate(){
        return date.get();
    }
    
    
    // Setters:
    public void setChecked(boolean newValue){
        this.checked.set(newValue);
    }
    
    public void setDate(String date){
        String localDateStr;
        try {
            dateObjProperty.set(DateConverter.parseDateWithTimeWithoutMilisecond(date));
            localDateStr = DateConverter.getDayMonthnameYearBySpace(dateObjProperty.get());
        } catch(Exception ex) {
            localDateStr = date;
        }
        this.date.set(localDateStr);
    }
    
    
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
    public String toStringForSearch() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    public static class CheckedCellFactory implements Callback<TableColumn<License, Boolean>, TableCell<License, Boolean>> {

        @Override
        public TableCell<License, Boolean> call(TableColumn<License, Boolean> param) {
            return new TableCell<License, Boolean>() {
                @Override
                public void updateItem(Boolean isChecked, boolean empty) {
                    CheckBox checker = new CheckBox();
                    checker.setSelected(isChecked);
                    setGraphic(empty ? null : checker);
                }
            };
        }
    }
}
