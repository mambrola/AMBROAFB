/*
 * To setLanguage this license header, choose License Headers in Project Properties.
 * To setLanguage this template file, choose Tools | Templates
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
     * @param message  - შიდა, კონტექსტის დასახელება
     * @param title The title addition on stage up-left corner. It will show with message type inscription.
     */
    public AlertMessage(AlertType alertType, Exception ex, String message, String title) {
        super(alertType);
        String titleForAlertType; //added by Murman 
        switch (alertType){
            case CONFIRMATION: titleForAlertType = Names.ALERT_CONFIRMATION_WINDOW_TITLE; break;
            case ERROR: titleForAlertType = Names.ALERT_ERROR_WINDOW_TITLE; break;
            case INFORMATION: titleForAlertType = Names.ALERT_INFORMATION_WINDOW_TITLE; break;
            case WARNING: titleForAlertType = Names.ALERT_WARNING_WINDOW_TITLE; break;
            default: titleForAlertType = "?";
        }
        setTitle(GeneralConfig.getInstance().getTitleFor(title + " " + titleForAlertType));
        setHeaderText(GeneralConfig.getInstance().getTitleFor(message));
        
        getDialogPane().getStylesheets().add(
            getClass().getResource("/styles/css/core.css").toExternalForm());
        
        ((Stage)getDialogPane().getScene().getWindow()).initOwner(AmbroAFB.mainStage);
        if (titleForAlertType.equals(Names.ALERT_ERROR_WINDOW_TITLE))
            Utils.log(message, ex);
    }
    
    public void setOwner(Stage owner){
        this.initOwner(owner);
        Utils.centerStageOfParent((Stage)this.getDialogPane().getScene().getWindow(), owner);
    }
    
    /**
     * მეთოდი უზრუნველყოფს შეცდომის ტექსტის გამოჩენას ეკრანზე. 
     * Alert-ი ჩანს მანამ სანამ მომხმარებელი არ დააჭერს OK ღილაკს. 
     */
    public void showAlert(){
        showAndWait();
    }
    
}
