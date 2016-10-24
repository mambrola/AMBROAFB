/*
 * To setLanguage this license header, choose License Headers in Project Properties.
 * To setLanguage this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.clients.filter;

import ambro.ADatePicker;
import ambroafb.clients.Client;
import ambroafb.clients.helper.ClientStatus;
import ambroafb.countries.CountryComboBox;
import ambroafb.general.FilterModel;
import ambroafb.general.GeneralConfig;
import ambroafb.general.Names;
import ambroafb.general.SceneUtils;
import ambroafb.general.interfaces.Filterable;
import ambroafb.general.okay_cancel.FilterOkayCancelController;
import ambroafb.general.StageUtils;
import ambroafb.general.StagesContainer;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import org.controlsfx.control.CheckComboBox;
import org.json.JSONObject;

/**
 *
 * @author mambroladze
 */
public class ClientFilter  extends Stage implements Filterable, Initializable {
    @FXML
    private ADatePicker dateBigger, dateLess;
    @FXML
    private CheckBox juridical, rezident;
    @FXML
    private CountryComboBox countries;
    @FXML
    private CheckComboBox<ClientStatus> statuses;
    @FXML
    private FilterOkayCancelController okayCancelController;
    
    private final ClientFilterModel clientFilterModel = new ClientFilterModel();
    
    private JSONObject jSonResult;
    public static final String DATE_BIGGER = "1970-01-01";
    public static final String DATE_LESS = "9999-01-01";
    
    public ClientFilter(Stage owner) {
        StagesContainer.registerStageByOwner(owner, Names.LEVEL_FOR_PATH, (Stage)this);
        
        this.initStyle(StageStyle.UNIFIED);
        this.setTitle(GeneralConfig.getInstance().getTitleFor("clients_filter"));
        Scene scene = SceneUtils.createScene("/ambroafb/clients/filter/ClientFilter.fxml", (ClientFilter)this);
        this.setScene(scene);
        this.initOwner(owner);
        this.setResizable(false);

        onCloseRequestProperty().set((EventHandler<WindowEvent>) (WindowEvent event) -> {
            okayCancelController.cancel(null);
            if(event != null) event.consume();
        });
        
        StageUtils.centerChildOf(owner, (Stage)this);
        StageUtils.followChildTo(owner, (Stage)this);
    }

    @Override
    public JSONObject getResult() {
        showAndWait();
        return jSonResult;
    }

    @Override
    public FilterModel getFilterResult() {
        showAndWait();
        return clientFilterModel;
    }

    @Override
    public void setResult(boolean isOk){
//        jSonResult = new JSONObject();
        if(!isOk){
            clientFilterModel.changeModelAsEmpty();
        }
        else {
            
//        try {
            dateBigger.setEditingValue();
            dateLess.setEditingValue();
            
            clientFilterModel.setFromDate(dateBigger.getValue());
            clientFilterModel.setToDate(dateLess.getValue());
            clientFilterModel.setJuridicalIndeterminate(juridical.isIndeterminate());
            clientFilterModel.setJuridicalSelected(juridical.isSelected());
            clientFilterModel.setSelectedCountryIndex(countries.getSelectionModel().getSelectedIndex());
            clientFilterModel.setSelectedCountry(countries.getValue());
            clientFilterModel.setRezidentIndeterminate(rezident.isIndeterminate());
            clientFilterModel.setRezidentSelected(rezident.isSelected());
            clientFilterModel.setSelectedStatusesIndexes(statuses.getCheckModel().getCheckedIndices());
            clientFilterModel.setSelectedStatuses(statuses.getCheckModel().getCheckedItems());
            
//            jSonResult.put("dateBigger", (dateBigger.getValue() == null ? DATE_BIGGER : dateBigger.getValue()).toString());
//            jSonResult.put(  "dateLess", (  dateLess.getValue() == null ? DATE_LESS   :   dateLess.getValue()).toString());
//            jSonResult.put( "juridical", (  juridical.indeterminateProperty().get() ? 2 : juridical.isSelected() ? 1 : 0 ));
//            jSonResult.put(   "country",  countries.getValue());
//            jSonResult.put(  "rezident", (  rezident.indeterminateProperty().get() ? 2 : rezident.isSelected() ? 1 : 0 ));
//            ObservableList<ClientStatus> statusList = statuses.getCheckModel().getCheckedItems();
//            jSonResult.putOpt("statuses", statusList);
//            
//            GeneralConfig.prefs.put("clients/filter/dateBigger", (dateBigger.getValue() == null) ? "" : dateBigger.getValue().toString());
//            GeneralConfig.prefs.put(  "clients/filter/dateLess", (  dateLess.getValue() == null) ? "" :   dateLess.getValue().toString());
//            GeneralConfig.prefs.putInt( "clients/filter/juridical", jSonResult.getInt("juridical"));
//            Country country = countries.getValue();
//            GeneralConfig.prefs.putInt(   "clients/filter/country/rec_id", (country == null) ? 0 : country.getRecId());
//            GeneralConfig.prefs.put(   "clients/filter/country/code", (country == null) ? "" : country.getCode());
//            GeneralConfig.prefs.put(   "clients/filter/country/descrip", (country == null) ? "" : country.getDescrip());
//            GeneralConfig.prefs.putInt(   "clients/filter/rezident", jSonResult.getInt("rezident"));
//            
//            final ObjectMapper mapper = new ObjectMapper();
//            final ObjectWriter writer = mapper.writer().withDefaultPrettyPrinter();
//            final JSONArray statusesArray = new JSONArray();
//            statusList.stream().forEach((clientStatus) -> {
//                try {
//                    JSONObject statusesJson = new JSONObject(writer.writeValueAsString(clientStatus));
//                    statusesArray.put(statusesJson);
//                } catch (JsonProcessingException | JSONException ex) {
//                    Logger.getLogger(ClientFilter.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            });
//            GeneralConfig.prefs.put("clients/filter/statuses", statuses.getCheckModel().getCheckedIndices().toString()); // statusesArray.toString()
//        }  catch (JSONException ex) { 
//              Logger.getLogger(ClientFilter.class.getName()).log(Level.SEVERE, null, ex); 
//         }
        }
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Client.getAllStatusFromDB().forEach((clientStatus) -> {
            statuses.getItems().add(clientStatus);
        });
        
        dateBigger.setValue(clientFilterModel.getFromDate());
        dateLess.setValue(clientFilterModel.getToDate());
        juridical.setSelected(clientFilterModel.isJuridicalSelected());
        juridical.setIndeterminate(clientFilterModel.isJuridicalIndeterminate());
        countries.getSelectionModel().select(clientFilterModel.getSelectedCountryIndex());
        rezident.setSelected(clientFilterModel.isRezidentSelected());
        rezident.setIndeterminate(clientFilterModel.isRezidentIndeterminate());
        clientFilterModel.getSelectedStatusesIndexes().stream().forEach((index) -> {
            statuses.getCheckModel().check(index);
        });
        
//        String dateB = GeneralConfig.prefs.get("clients/filter/dateBigger", "");
//        String dateL = GeneralConfig.prefs.get("clients/filter/dateLess", "");
//        int jurid = GeneralConfig.prefs.getInt("clients/filter/juridical", 0);
//        int countryId = GeneralConfig.prefs.getInt("clients/filter/country/rec_id", -1);
//        String code = GeneralConfig.prefs.get("clients/filter/country/code", "");
//        String countryDescrip = GeneralConfig.prefs.get("clients/filter/country/descrip", "");
//        Country country = new Country(code, countryDescrip);
//        country.setRecId(countryId);
        
//        String status = GeneralConfig.prefs.get("clients/filter/status", null);
//        int rez = GeneralConfig.prefs.getInt("clients/filter/rezident", 0);
//
//        LocalDate bigger = (dateB.isEmpty()) ? null : LocalDate.parse(dateB);
//        LocalDate less = (dateL.isEmpty()) ? null : LocalDate.parse(dateL);
//
//        System.out.println("client statuses: " + status);
//        
//        dateBigger.setValue(bigger);
//        dateLess.setValue(less);
//        juridical.setSelected(jurid == 1);
//        juridical.setIndeterminate(jurid == 2);
//        if (countryId != -1)
//            countries.setValue(country);
//        rezident.setSelected(rez == 1);
//        rezident.setIndeterminate(rez == 2);
        
        
//        String statusesPref = GeneralConfig.prefs.get("clients/filter/statuses", "");
//        if (!statusesPref.isEmpty()){
//            ObjectMapper mapper = new ObjectMapper();
//            try {
//                ArrayList<Integer> checkedStatusList = mapper.readValue(statusesPref, new TypeReference<ArrayList<Integer>>(){});
//                checkedStatusList.stream().forEach((checkedIndex) -> {
//                    statuses.getCheckModel().check(checkedIndex);
//                });
//            } catch (IOException ex) {
//                Logger.getLogger(ClientFilter.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
        
    }
    
}
