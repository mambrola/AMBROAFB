/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general;

import java.util.function.Consumer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;

/**
 *
 * @author tabramishvili
 */
public class Panel extends Pane {

    public final Object MARKED_NODE_KEY = new Object();
    public final Object BACKUP_VALUE_KEY = new Object();

    public Panel() {
        getChildren().addListener((ListChangeListener.Change<? extends Node> c) -> {
            customizeChildren();
        });
    }

    private void customizeChildren() {
        Utils.getFocusTraversableBottomChildren(this)
                .stream().parallel()
                .filter((t) -> !t.getProperties().containsKey(MARKED_NODE_KEY))
                .forEach(this::customize);
    }

    private void customize(Node node) {
        node.getProperties().put(MARKED_NODE_KEY, null);
        if (node.getClass().equals(TextField.class)) {
            TextField field = (TextField) node;
            field.focusedProperty().addListener((observable, oldValue, newValue) -> {
                if (!oldValue && newValue) {
                    node.getProperties().put(BACKUP_VALUE_KEY, field.getText());
                }
            });
            field.addEventHandler(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
                if (event.getCode().equals(KeyCode.ESCAPE)) {
                    field.setText((String) node.getProperties().get(BACKUP_VALUE_KEY));
                    field.positionCaret(field.getText().length() - 1);
                    field.selectAll();
                }
            });
        } else if (node.getClass().equals(Button.class)) {
            node.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
                if (event.getCode().equals(KeyCode.SPACE)) {
                    event.consume();
                } else if (event.getCode().equals(KeyCode.ENTER)) {
                    ((Button) node).fire();
                    event.consume();
                }
            });
        }
        node.addEventHandler(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                node.fireEvent(new KeyEvent(null, null, KeyEvent.KEY_PRESSED, "", "\t", KeyCode.TAB, false, false, false, false));
            }
        });
    }

}
