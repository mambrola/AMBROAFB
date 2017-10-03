/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general.scene_components.number_fields;

import ambroafb.AmbroAFB;
import ambroafb.docs.types.doc_in_order.DocOrderComponent;
import ambroafb.general.GeneralConfig;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;

/**
 *
 * @author dkobuladze
 */
public class NumberField extends TextField {
    
    private final FieldContentListener fcl = new FieldContentListener("");
    
    public NumberField(){
        super();
        assignLoader();
        textProperty().addListener(fcl);
    }
    
    /**
     *  The method provides to load scene.
     */
    private void assignLoader(){
        FXMLLoader loader = new FXMLLoader(AmbroAFB.class.getResource("/ambroafb/general/scene_components/number_fields/NumberField.fxml"));
        loader.setResources(GeneralConfig.getInstance().getBundle());
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException ex) {
            Logger.getLogger(DocOrderComponent.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     *  The method sets content pattern on field for valid text.
     * @param pattern The allowed text pattern.
     */
    protected final void contentRuntimePatternListener(String pattern){
        fcl.setPattern(pattern);
    }
    
    /**
     *  The class provides to sets pattern, listen text change and match it to pattern. If text is valid for pattern, field sets the text.
     */
    private class FieldContentListener implements ChangeListener<String> {

        private String pattern;
        
        public FieldContentListener(String pattern){
            this.pattern = pattern;
        }
        
        private void setPattern(String newPattern){
            this.pattern = newPattern;
        }
        
        @Override
        public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
            if (newValue != null && !newValue.isEmpty()){
                if (!Pattern.matches(pattern, newValue))
                    setText(oldValue);
            }
        }
        
    }
}
