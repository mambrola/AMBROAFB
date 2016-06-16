/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;

/**
 *
 * @author mambroladze
 */
public class ADatePicker extends DatePicker {
    private String pattern = "d MMM yyyy";
//    private TextField textField;
    public ADatePicker() {
//        textField = this.getEditor();
//        
//        textField.textProperty().addListener((observable, oldValue, newValue) -> {
//            this.setValue(this.getConverter().fromString(this.getEditor().getText()));
//        });
//        
        
        this.setPromptText(pattern.toLowerCase());
        this.setConverter(new StringConverter<LocalDate>() {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);
            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    try {
                        return LocalDate.parse(string, dateFormatter);
                    } catch (Exception ex) {
                        return null;
                    }
                } else {
                    return null;
                }
            }
        });
    }
    
    public void setEditingValue(){
        this.setValue(this.getConverter().fromString(this.getEditor().getText()));
    }
}
