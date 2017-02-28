/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.minitables.subjects;

import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;

/**
 *
 * @author dkobuladze
 */
public class SubjectComboBox extends ComboBox<Subject> {
    
    private final ObservableList<Subject> items = FXCollections.observableArrayList();
    private final Subject all = new Subject();
    
    public SubjectComboBox(){
        this.setItems(items);
        all.setRecId(0);
        all.setDescrip("ALL");
        items.add(all);

        this.setConverter(new StringConverter<Subject>() {
            @Override
            public String toString(Subject subject) {
                return subject.toString();
            }
            @Override
            public Subject fromString(String input) {
                return null;
            }
        });
        ArrayList<Subject> subjects = Subject.getAllFromDB();
        subjects.sort((Subject b1, Subject b2) -> b1.getRecId() - b2.getRecId());
        items.addAll(subjects);
        this.setValue(all);
    }
    
    public void selectItem(Subject subject){
        this.getSelectionModel().select(subject);
    }

    public void showCategoryAll(boolean show){
        if (!show){
            if (getItems().contains(all)){
                getItems().remove(0);
            }
        }
        else {
            if (!getItems().contains(all)){
                getItems().add(all);
            }
        }
    }
}
