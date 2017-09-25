/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general;

import javafx.collections.ListChangeListener;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;

/**
 *
 * @author tabramishvili
 */
public class DialogPanel extends StackPane {

    public final Object MARKED_NODE_KEY = new Object();
    public final Object BACKUP_VALUE_KEY = new Object();

    public DialogPanel() {
        // გადასაკეთებელია login Controller_ისთვის
//        this.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
//            if (event.getCode().equals(KeyCode.ENTER)) {
//                Node focusedNode = this.getScene().getFocusOwner();
////                if (nodeParentNotEqual(focusedNode, "okayCancel")){
//                    focusedNode.fireEvent(new KeyEvent(null, null, KeyEvent.KEY_PRESSED, "", "\t", KeyCode.TAB, false, false, false, false));
//                    event.consume();
////                }
//            }
//        });
        
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
    
    private boolean nodeParentNotEqual(Node node, String parentId){
        return node.getParent().getId() == null || !node.getParent().getId().equals(parentId);
    }

    private void customize(Node node) {
        if (nodeParentNotEqual(node, "okayCancel")){
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
                        String backupValue = (String) node.getProperties().get(BACKUP_VALUE_KEY);
                        if (backupValue != null) {
                            field.setText(backupValue);
                            int index = field.getText().length() - 1;
                            if (index >= 0) {
                                field.positionCaret(index);
                                field.selectAll();
                            }
                        }
                    }
                });
            } else if (node.getClass().equals(Button.class)) {
                node.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
                    if (event.getCode().equals(KeyCode.SPACE)) {
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
        else {
            node.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
                if (event.getCode().equals(KeyCode.ENTER)) {
                    ((Button)node).getOnAction().handle(null);
                }
            });
        }
    }

}
