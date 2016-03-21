/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.scene.control.ComboBox;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author tabramishvili
 * @param <T>
 */
public class PhoneEditor<T> extends ComboBox<T> {

    private final IntegerProperty itemsSize = new SimpleIntegerProperty();

    public PhoneEditor() {
        getItems().addListener((ListChangeListener.Change<? extends T> c) -> {
            itemsSize.set(getItems().size());
            if (!isFocused()) {
                getSelectionModel().selectFirst();
            }
        });

        ChangeListener<Number> unselectable = (ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            getSelectionModel().selectFirst();
        };
        
        getSelectionModel().selectedIndexProperty().addListener(unselectable);
        editableProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (newValue){
                getSelectionModel().selectedIndexProperty().removeListener(unselectable);
            }else{
                getSelectionModel().selectedIndexProperty().addListener(unselectable);
            }
        });

        disableProperty().bind(editableProperty().not().and(itemsSize.lessThanOrEqualTo(1)));

        focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (!newValue) {
                getSelectionModel().selectFirst();
            }
        });

        addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
            getSelectionModel().select(-1);
        });

        addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                int index = getSelectionModel().getSelectedIndex();
                if (index >= 0) {
                    String text = getEditor().getText();
                    T val = getSelectionModel().getSelectedItem();
                    T newVal = getConverter().fromString(text);
                    if (!val.equals(newVal)) {
                        getItems().remove(index);
                        if (text != null && !text.isEmpty()) {
                            getItems().add(index, newVal);
                        }
                    }
                } else {
                    String text = getEditor().getText();
                    if (text != null && !text.isEmpty()) {
                        getItems().add(getConverter().fromString(text));
                        getSelectionModel().select(-1);
                        getEditor().setText("");
                    }
                }
            } else if (event.getCode().equals(KeyCode.DOWN)) {
                getSelectionModel().select(-1);
                show();
            }
        });
        
    }

}
