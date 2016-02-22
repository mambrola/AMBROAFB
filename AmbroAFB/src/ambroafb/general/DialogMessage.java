/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general;

import java.util.Optional;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;

/**
 *
 * @author Administrator
 */
public class DialogMessage extends Dialog<String> {
    
    
    private ButtonType okButton;
    private TextField textField;
    
    /**
     * ამზადებს Dialog კლასის ობიექტს. აკეთებს მონაცემების შეყვანისთვის textField-ს და
     * ok button-ს Dialog-ს დამთავრებისთვის. 
     * გამოძახებისას შედეგის დაჭერა მოხდება შემდგენაირად: 
     *          DialogMessage msg = new DialogMessage("Some header title");
     *          Optional<String> result = msg.showAndWait();
     *          String str = result.get();
     * @param headerTitle - window-ს შიგა, სათაური.
     */
    public DialogMessage(String headerTitle){
        setTitle(Names.DIALOG_WINDOW_TITLE);
        setHeaderText(headerTitle);
        
        Label fieldLabel = new Label("Enter data:");
        textField = new TextField();
        
        GridPane pane = new GridPane();
        pane.add(fieldLabel, 1, 1);
        pane.add(textField, 2, 1);
        getDialogPane().setContent(pane);
        
        okButton = new ButtonType("Ok", ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().add(okButton);
        setResultConverter(new DialogCallBack());
        
    }

    /**
     * Callback ინტერფეისის კლასი, რომელიც უზრუნველყოფს showAndWait() მეთოდის გამოძახების შემდეგ დაბრუნდეს 
     * Dialog კლასის პარამეტრის მნიშვნელობა. ჩვენს შემთხვევაში პარამეტრის ტიპია String.
     */
    private class DialogCallBack implements Callback<ButtonType, String> {

        @Override
        public String call(ButtonType param) {
            String result = "";
            if(param == okButton){
                return textField.getText();
            }
            return result;
        }
        
    }
}

