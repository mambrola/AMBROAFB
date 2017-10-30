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
    
    private Exception exception;
    
    /**
     *  The constructor of AlertMessage class. The alert stage title generates from alert type.
     * @param owner The owner stage.
     * @param alertType The type of alert.
     * @param headerText The header text (left from alert icon).
     * @param contentText The content text (bottom of header text).
     */
    public AlertMessage(Stage owner, AlertType alertType, String headerText, String contentText) {
        super(alertType);
        setOwner(owner);
        setHeaderText(headerText);
        setContentText(contentText);
        
        String titleForAlertType; //added by Murman 
        switch (alertType){
            case CONFIRMATION: titleForAlertType = Names.ALERT_CONFIRMATION_WINDOW_TITLE; break;
            case ERROR: titleForAlertType = Names.ALERT_ERROR_WINDOW_TITLE; break;
            case INFORMATION: titleForAlertType = Names.ALERT_INFORMATION_WINDOW_TITLE; break;
            case WARNING: titleForAlertType = Names.ALERT_WARNING_WINDOW_TITLE; break;
            default: titleForAlertType = "?";
        }
        setTitle(GeneralConfig.getInstance().getTitleFor(titleForAlertType));
        setResizable(true);
        
        getDialogPane().getStylesheets().add(getClass().getResource("/styles/css/core.css").toExternalForm());
    }
    
    /**
     * 
     * @param ex The exception object.
     * @see ambroafb.general.AlertMessage#AlertMessage(javafx.stage.Stage, javafx.scene.control.Alert.AlertType, java.lang.String, java.lang.String)  AlertMessage Base Constructor
     */
    public AlertMessage(Stage owner, AlertType alertType, Exception ex, String headerText, String contentText) {
        this(owner, alertType, headerText, contentText);
        setException(ex);
    }
    
    @Deprecated
    public AlertMessage(AlertType alertType, Exception ex, String headerText, String contentText) {
        this(AmbroAFB.mainStage, alertType, headerText, contentText);
        setException(ex);
    }
    
    private void setException(Exception ex){
        exception = ex;
    }
    
    public final void setOwner(Stage owner){
        this.initOwner(owner);
        StageUtils.centerChildOf(owner, (Stage)this.getDialogPane().getScene().getWindow());
    }
    
}
