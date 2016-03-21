/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.scene.control.ComboBox;
import javafx.scene.control.SelectionModel;
import javafx.scene.control.SingleSelectionModel;
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
            getSelectionModel().selectFirst();
        });
        SingleSelectionModel<T> currentSelection = getSelectionModel();

        editableProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (newValue) {
                setSelectionModel(currentSelection);
                Object val = getProperties().get("_firstItem");
                if (val != null) {
                    getItems().add(0, (T) val);
                }
            } else {
                T val = getItems().size() > 0 ? getItems().get(0) : null;
                if (val != null) {
                    getProperties().put("_firstItem", val);
                }
                setSelectionModel(new SingleSelectionModel<T>() {
                    @Override
                    protected T getModelItem(int index) {
                        return val;
                    }

                    @Override
                    protected int getItemCount() {
                        return 1;
                    }
                });
                getItems().remove(0);
            }
        });

        BooleanBinding forDisable = editableProperty().not().and(itemsSize.lessThanOrEqualTo(0));
        disableProperty().bind(forDisable);

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
                        event.consume();
                    }
                } else {
                    String text = getEditor().getText();
                    if (text != null && !text.isEmpty()) {
                        getItems().add(getConverter().fromString(text));
                        getSelectionModel().select(-1);
                        getEditor().setText("");
                        event.consume();
                    }
                }
            } else if (event.getCode().equals(KeyCode.DOWN)) {
                getSelectionModel().select(-1);
                show();
            }
        });

    }

}
