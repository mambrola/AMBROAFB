/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;

/**
 *
 * @author tabramishvili
 * @param <T>
 */
public class ListEditor<T extends Editable<String>> extends ComboBox<T> {

    private final Object FIRST_ITEM_KEY = new Object();
    private final Object SELECTED_ITEM_KEY = new Object();

    private final IntegerProperty itemsSize = new SimpleIntegerProperty();

    public ListEditor() {
        getItems().addListener((ListChangeListener.Change<? extends T> c) -> {
            itemsSize.set(getItems().size());
            if (itemsSize.get() > 0) {
                getSelectionModel().selectFirst();
                getProperties().put(SELECTED_ITEM_KEY, getSelectionModel().getSelectedItem());
            }
        });
        SingleSelectionModel<T> currentSelection = getSelectionModel();

        editableProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (newValue) {
                setSelectionModel(currentSelection);
                Object val = getProperties().get(FIRST_ITEM_KEY);
                if (val != null) {
                    getItems().add(0, (T) val);
                }
            } else {
                T val = getItems().size() > 0 ? getItems().get(0) : null;
                if (val != null) {
                    getProperties().put(FIRST_ITEM_KEY, val);
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
                setValue(val);
                getItems().remove(0);
            }
        });

        BooleanBinding forDisable = editableProperty().not().and(itemsSize.lessThanOrEqualTo(0));
        disableProperty().bind(forDisable);

        focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (!newValue) {
                getSelectionModel().selectFirst();
                getProperties().put(SELECTED_ITEM_KEY, getSelectionModel().getSelectedItem());
            }
        });

        setCellFactory(new Callback<ListView<T>, ListCell<T>>() {

            @Override
            public ListCell<T> call(ListView<T> param) {
                return new ListCell<T>() {

                    @Override
                    protected void updateItem(T item, boolean empty) {
                        super.updateItem(item, empty);
                        textProperty().unbind();
                        if (item != null && !empty) {
                            textProperty().bind(item.getObservableString());
                        }
                    }

                };
            }
        });

        addEventHandler(ActionEvent.ACTION, (ActionEvent event) -> {
            String text = getEditor().getText();
            boolean existing = false;
            for (int i = 0; i < getItems().size(); i++) {
                if (getConverter().toString(getItems().get(i)).equals(text)) {
                    existing = true;
                    break;
                }
            }

            T selectedItem = (T) getProperties().get(SELECTED_ITEM_KEY);
            if (existing && text.isEmpty()) {
                hide();
                T removingItem = getValue();
                getItems().remove(removingItem);
                getSelectionModel().selectFirst();
            } else if (!existing && selectedItem == null && !text.isEmpty()) {
                T newItem = getConverter().fromString(text);
                getItems().add(newItem);
                getSelectionModel().select(newItem);
            }

            getProperties().put(SELECTED_ITEM_KEY, getSelectionModel().getSelectedItem());

        });

        addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
            show();
            if (event.getCode().equals(KeyCode.N)) {
                if (event.isControlDown()) {
                    getProperties().remove(SELECTED_ITEM_KEY);
                    getEditor().setText("");
                }
            }
        });
        addEventHandler(KeyEvent.KEY_RELEASED, (KeyEvent event) -> {
            T selectedItem = (T) getProperties().get(SELECTED_ITEM_KEY);
            if (selectedItem != null) {
                selectedItem.edit(getEditor().getText());
            }
        });

    }

}
