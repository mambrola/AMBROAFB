/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general;

/**
 *
 * @author tabramishvili
 */
public class Names {
    
    public static final String DB_SERVICE_URL_ON_SERVER = "http://db.kfz-soft.com/DBService/api";
    public static final String DB_SERVICE_ALTERNATIVE_URL_ON_SERVER = "http://db.kfz-soft.de/DBService/api";
    public static final String DB_SERVICE_URL_FOR_TEST = "http://192.168.0.30:8080/DBService/api";
    
    public static final String MONITORING_URL_ON_SERVER = "http://kfz-soft.com/Monitoring/webapi";
    public static final String MONITORING_URL_FOR_TEST = "http://192.168.0.30:8080/Monitoring/webapi";
    
    public static String APP_NAME = "AmbroAFB";
    public static String LOGIN_STAGE = "login_stage";
    
    public static final String MAIN_FXML = "/ambroafb/Main.fxml"; // "/ambroafb/clients/Clients.fxml"; //"ambroafb/clients/filter/ClientFilter.fxml"; //
    public static final String MAIN_TITLE = "title_main";
    public static final String MAIN_LOGO = "/images/mainabfor2a.png";
    
    public static final String CONFIGURATION_FXML = "/ambroafb/configuration/Configuration.fxml";
    public static final String CONFIGURATION = "configuration";
    public static final String CONFIGURATION_LOGO = "/images/innerLogo.png";
    
    public static final String CAR_FINES_FXML = "/ambroafb/car_fines/CarFines.fxml";
    public static final String CAR_FINES_TITLE = "title_car_fines";
    public static final String CAR_FINES_LOGO = "/images/innerLogo.png";
    public static final String CAR_FINS_SECOND_FXML = "/ambroafb/car_fines/CarFinesSecond.fxml";
    
    public static final String IN_OUT_FXML = "/ambroafb/in_out/InOut.fxml";
    public static final String IN_OUT_TITLE = "title_in_out_usd";
    public static final String IN_OUT_LOGO = "/images/innerLogo.png";
    
    
    public static final String IMAGE_REFRESH = "/images/refresh.png";
    public static final String IMAGE_ENTER = "/images/enter.png";
    public static final String IMAGE_LOADER = "/images/loader.gif";
    
    public static final String TOOLTIP_REFRESH = "refresh";
    
    public static final String BUNDLE_TITLES_NAME = "bundles.titles";
    
    public static final String GENERAL_CONFIGURATION_FILE_NAME = "config.ser";
    public static final String LOCKER_FILE_NAME = "locker.lck";
    
    public static final String ALERT_CONFIRMATION_WINDOW_TITLE = "alert_confirmation";
    public static final String ALERT_ERROR_WINDOW_TITLE = "alert_error";
    public static final String ALERT_INFORMATION_WINDOW_TITLE = "alert_information";
    public static final String ALERT_WARNING_WINDOW_TITLE = "alert_warning";
    
    public static final String ERROR_BUTTON_CLICK = "Car_fines_second_view";
    public static final String CONFIGURATION_FILE_OR_CLASS_NOT_FOUND = "File_or_class_not_found";
    public static final String ERROR_CONFIGURATION = "Error_in_save_configurations";
    public static final String ERROR_DUMP = "Error_in_dump";
    public static final String SQL_ERROR = "Sql_Error";
    public static final String ERROR_MAIN_CONFIGURATION = "Configuration_Error";
    public static final String ERROR_CAR_FINES_SCENE_START = "Error_to_start_car_fines_scene";
    public static final String ERROR_IN_OUT_START_SCENE = "Error_to_start_inOut_scene";
    public static final String ERROR_INOUT = "Error_in_in_out_scene";
    public static final String ERROR_CAR_FINES = "Error_car_fines";
    public static final String ERROR_CONFIG = "Error_in_configuration";
    
    public static final String FONT = "Times New Roman GEO";
    
    public static final String DIALOG_WINDOW_TITLE = "Dialog";
    
    public static final String LEVEL_FOR_PATH = "DialogOrFilter";
    
    
    public static final String BAL_ACCOUNT_ACT_PAS = "Act_Pas";
    public static final String BAL_ACCOUNT_ACT = "Act";
    public static final String BAL_ACCOUNT_PAS = "Pas";
    
    public static enum EDITOR_BUTTON_TYPE {
        DELETE, EDIT, VIEW, ADD, ADD_BY_SAMPLE
    } 
    
    public static enum DOC_EDITOR_BUTTON_TYPE {
        DELETE, EDIT, VIEW, ADD, ADD_BY_SAMPLE;
    } 
    
}
