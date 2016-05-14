/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general;

import ambroafb.AmbroAFB;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

/**
 *
 * @author Administrator
 */
public class AlertMessage extends Alert {
    
    /**
     * კონსტრუქტორი უზრუნველყოფს შექმნას შესაბამისი AlertType alert-ი.
     * დაუსეტავს ფანჯარას დასახელებას და ასევე დასასელებას, თუ რის შესახებაა დიალოგი. , 
     * @param alertType    - მესიჯის ტიპი, (AlertType.ERROR, AlertType.WARNING, AlertType.CONFIRMATION ...)
     * @param ex
     * @param messageName  - შიდა, კონტექსტის დასახელება
     */
    public AlertMessage(AlertType alertType, Exception ex, String messageName) {
        super(alertType);
        String titleAlert; //added by Murman 
        switch (alertType){
            case CONFIRMATION: titleAlert = Names.ALERT_CONFIRMATION_WINDOW_TITLE; break;
            case ERROR: titleAlert = Names.ALERT_ERROR_WINDOW_TITLE; break;
            case INFORMATION: titleAlert = Names.ALERT_INFORMATION_WINDOW_TITLE; break;
            case WARNING: titleAlert = Names.ALERT_WARNING_WINDOW_TITLE; break;
            default: titleAlert = "?";
        }
        setTitle(GeneralConfig.getInstance().getTitleFor(titleAlert));
        setHeaderText(GeneralConfig.getInstance().getTitleFor(messageName));
        
        getDialogPane().getStylesheets().add(
            getClass().getResource("/styles/css/core.css").toExternalForm());
        
//        getDialogPane().getScene().getStylesheets().add("/ambroafb/general/core.css");
        ((Stage)getDialogPane().getScene().getWindow()).initOwner(AmbroAFB.mainStage);

        Utils.log(messageName, ex);
    }
    
    /**
     * მეთოდი უზრუნველყოფს შეცდომის ტექსტის გამოჩენას ეკრანზე. 
     * Alert-ი ჩანს მანამ სანამ მომხმარებელი არ დააჭერს OK ღილაკს. 
     */
    public void showAlert(){
        showAndWait();
    }
    
}
