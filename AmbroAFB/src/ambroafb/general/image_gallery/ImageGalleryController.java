/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general.image_gallery;

import ambroafb.general.AlertMessage;
import ambroafb.general.GeneralConfig;
import ambroafb.general.KFZClient;
import ambroafb.general.PDFHelper;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.StringConverter;
import org.controlsfx.control.MaskerPane;
import org.json.JSONArray;
import org.json.JSONException;

/**
 * FXML Controller class
 *
 * @author dato
 */
public class ImageGalleryController implements Initializable {

    @FXML
    private Button deleteOrUndo, rotateToRight, upload;

    @FXML
    private ImageView galleryImageView, deletedImageView;

    @FXML
    private HBox imageButtonsHBox, galleryImageFrame;

    @FXML
    private MaskerPane masker;

    private ObservableList<String> datesSliderElems;
    private Map<String, DocumentViewer> viewersMap;
    private String undoOrDeleteImagePath;
    private Calendar calendar;
    private DateFormat formatter;
    private FileChooser fileChooser;
    private String serviceURLPrefix;
    private String parameterUpload;
    private String parameterDownload;
    private int defaultPages;
    private String defaultFileChooserPath;
    private MessageSlider msgSlider;

    private static final String GALLERY_DELETE_BUTTON_IMAGE_NAME = "/images/delete_128.png";
    private static final String GALLERY_UNDO_BUTTON_IMAGE_NAME = "/images/undo_128.png";
    private static final String UPLOAD_DIRECTORY_PATH = "upload_directory";
    private StringConverter<String> converter;
    
    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        defaultPages = 10;
        defaultFileChooserPath = GeneralConfig.prefs.get(UPLOAD_DIRECTORY_PATH, null);
        undoOrDeleteImagePath = GALLERY_DELETE_BUTTON_IMAGE_NAME;
        viewersMap = new HashMap<>();
        datesSliderElems = FXCollections.observableArrayList();
        calendar = Calendar.getInstance();
        formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss.SSS");
        fileChooser = new FileChooser();
        ExtensionFilter filter = new ExtensionFilter("Images files (*.png, *.jpg, *.pdf)", "*.png", "*.jpg", "*.pdf");
        fileChooser.getExtensionFilters().add(filter);
        converter = new ImageGalleryStringConverter();
        msgSlider = new MessageSlider(datesSliderElems, converter, rb);
        imageButtonsHBox.getChildren().add(msgSlider);
        masker.setVisible(false);
        msgSlider.indexProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            showImage(serviceURLPrefix, newValue.intValue());
        });
    }
    
    private class ImageGalleryStringConverter extends StringConverter<String> {
        @Override
            public String toString(String object) {
                String fullName = object;
                int start = fullName.indexOf("_") + 1;
                int end = fullName.lastIndexOf(".");
                String miliseconds = fullName.substring(start, end);
                calendar.setTimeInMillis(Long.parseLong(miliseconds));
                String onlyDateAndTime = formatter.format(calendar.getTime());
                return onlyDateAndTime;
            }

            @Override
            public String fromString(String string) {
                return string;
            }
        
    }
    
    public void setUploadDataURL(String serviceURLPrefix, String parameterUpload, String parameterDownload) {
        this.serviceURLPrefix = serviceURLPrefix;
        this.parameterUpload = parameterUpload;
        this.parameterDownload = parameterDownload;
    }

    public void downloadData() {
//        downloadAndSaveDataFrom(serviceURLPrefix + parameterDownload);
        try {
            String imagesNames = GeneralConfig.getInstance().getServerClient().get(serviceURLPrefix + parameterDownload);
            JSONArray namesJson = new JSONArray(imagesNames);
            for (int i = 0; i < namesJson.length(); i++) {
                String fullName = namesJson.getString(i);
                datesSliderElems.add(fullName);
                viewersMap.put(fullName, null);
            }
            if (datesSliderElems != null && !datesSliderElems.isEmpty()) {
                msgSlider.setValueOn(0);
            }
        } catch (KFZClient.KFZServerException ex) {
            System.out.println("ex code: " + ex.getStatusCode() + "  User has not images.");
            
        } catch (JSONException | IOException ex) {
            Logger.getLogger(ImageGalleryController.class.getName()).log(Level.SEVERE, null, ex);
        }
//        msgSlider.indexProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
//            showImage(serviceURLPrefix, newValue.intValue());
//        });
//        if (datesSliderElems != null && !datesSliderElems.isEmpty()) {
//            msgSlider.setValueOn(0);
//        }
    }

    private void downloadAndSaveDataFrom(String url) {
        try {
            String imagesNames = GeneralConfig.getInstance().getServerClient().get(url);
            JSONArray namesJson = new JSONArray(imagesNames);
            for (int i = 0; i < namesJson.length(); i++) {
                String fullName = namesJson.getString(i);
                datesSliderElems.add(fullName);
                viewersMap.put(fullName, null);
            }
            if (datesSliderElems != null && !datesSliderElems.isEmpty()) {
                msgSlider.setValueOn(0);
            }
        } catch (KFZClient.KFZServerException ex) {
            System.out.println("ex code: " + ex.getStatusCode() + "  User has not images.");
            
        } catch (JSONException | IOException ex) {
            Logger.getLogger(ImageGalleryController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void showImage(String urlPrefix, int index) {
        if (index >= 0 && index < datesSliderElems.size()) {
            System.out.println("bla  " + masker);
//            Platform.runLater(()->{
                masker.setVisible(true);
//            });
            System.out.println("blu");
            String fullName = datesSliderElems.get(index);
            DocumentViewer viewer = viewersMap.get(fullName);
            System.out.println("aq fullName aris: " + fullName + " viewer: " + viewer);

            if (viewer == null) {
                try {
                    HttpURLConnection con = GeneralConfig.getInstance().getServerClient().createConnection(urlPrefix + fullName);
                    viewer = DocumentViewer.Factory.getAppropriateViewer(con.getInputStream(), fullName);
                    viewersMap.put(fullName, viewer);
                    showViewerComponentOnScene(viewer);
                    System.out.println("aq movida... " + viewersMap.get(fullName));
                } catch (IOException ex) {
                    Logger.getLogger(ImageGalleryController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else {
                showViewerComponentOnScene(viewer);
            }
            System.out.println("bla2");
//            Platform.runLater(()-> {
                masker.setVisible(false);
//            });
            System.out.println("blu2");
        }
    }
    
    private void showViewerComponentOnScene(DocumentViewer viewer){
        final DocumentViewer dViewer = viewer;
        galleryImageFrame.getChildren().setAll(dViewer.getComponent());
        deletedImageView.visibleProperty().unbind();
        deletedImageView.visibleProperty().bind(dViewer.deletedProperty());
        ImageView icon = (ImageView) deleteOrUndo.getGraphic();
        icon.imageProperty().unbind();
        icon.imageProperty().bind(Bindings.createObjectBinding(() -> {
            String url = dViewer.deletedProperty().get() ? GALLERY_UNDO_BUTTON_IMAGE_NAME : GALLERY_DELETE_BUTTON_IMAGE_NAME;
            return new Image(getClass().getResourceAsStream(url));
        }, dViewer.deletedProperty()));
    }

    @FXML
    private void deleteImage(ActionEvent event) {
        String fullName = msgSlider.getValue();
        DocumentViewer viewer = viewersMap.get(fullName);
        if (viewer != null) {
            viewer.deleteOrUndo();
        }
    }

    private void setImageToButton(Button button, String imageURL) {
        Image image = new Image(getClass().getResourceAsStream(imageURL));
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(((ImageView) button.getGraphic()).getFitWidth());
        imageView.setFitHeight(((ImageView) button.getGraphic()).getFitHeight());
        button.setGraphic(imageView);
    }

    @FXML
    private void uploadImage(ActionEvent event) {
        Stage owner = (Stage) galleryImageFrame.getScene().getWindow();
        if (defaultFileChooserPath != null){
            fileChooser.setInitialDirectory(new File(defaultFileChooserPath));
        }
        List<File> files = fileChooser.showOpenMultipleDialog(owner);
        if (files == null) return;
        
        defaultFileChooserPath = files.get(files.size() - 1).getParentFile().getPath();
        GeneralConfig.prefs.put(UPLOAD_DIRECTORY_PATH, defaultFileChooserPath);
        
        List<String> largePDFsNames = new ArrayList<>();
        new Thread(() -> {
            for (File file : files) {
                try {
                    String fileName = file.getName();
                    InputStream stream = new FileInputStream(file);
                    DocumentViewer viewer;
                    if (fileName.substring(fileName.lastIndexOf(".")).toLowerCase().equals(".pdf")) {
                        PDFHelper pdfHelper = new PDFHelper(new FileInputStream(file));
                        if(pdfHelper.getPageCount() > defaultPages){
                            largePDFsNames.add(file.getName());
                            continue;
                        }
                        viewer = new PDFViewer(stream, fileName);
                    } else {
                        viewer = new ImageViewer(stream, fileName);
                    }
                    viewer.setIsNew(true);
                    Platform.runLater(() -> {
                        galleryImageFrame.getChildren().setAll(viewer.getComponent());
//                        galleryImageView.setPreserveRatio(true);
                    });
                    proccessViewer(viewer, fileName);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(ImageGalleryController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(ImageGalleryController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            showLargePDFWarning(largePDFsNames);
        }).start();
    }
    
    private void showLargePDFWarning(List<String> largePDFsNames) {
        String pdfs = GeneralConfig.getInstance().getTitleFor("large_pdfs") + defaultPages + ": \n";
        String separator = ",";
        pdfs = largePDFsNames.stream().map((largePDFName) -> largePDFName + separator).reduce(pdfs, String::concat);
        if (pdfs.contains(separator)){
            new AlertMessage(Alert.AlertType.WARNING, null, pdfs.substring(0, pdfs.length() - 1)).showAlert();
        }
    }
    
    private void proccessViewer(DocumentViewer viewer, String fileName){
        Long currTime = new Date().getTime();
        String fileFullName = "_" + currTime + fileName.substring(fileName.lastIndexOf("."));
        viewersMap.put(fileFullName, viewer);
        datesSliderElems.add(fileFullName);
    }
    
    @FXML
    private void rotate(ActionEvent event) {
        String fullName = msgSlider.getValue();
        DocumentViewer viewer = viewersMap.get(fullName);
        if (viewer != null) {
            viewer.rotate();
        }
    }

    /**
     * The method sends image gallery data to server in thread.
     * So before this method, image gallery controller must known service URL prefix 
     * and parameter by setUploadDataURL method.
     */
    public void sendDataToServer() {
        new Thread(() -> {
            viewersMap.keySet().stream().forEach((key) -> {
                DocumentViewer viewer = viewersMap.get(key);
                if (viewer != null) {
                    byte[] data = viewer.getContent();
                    try {
                        if (viewer.deletedProperty().get()) {
                            GeneralConfig.getInstance().getServerClient().call(serviceURLPrefix + key, "DELETE", null);
                        } else if (viewer.isNew()) {
                            GeneralConfig.getInstance().getServerClient().post(
                                    serviceURLPrefix + parameterUpload + key.substring(key.lastIndexOf(".") + 1),
                                    Base64.getEncoder().encodeToString(data)
                            );
                        } else if (viewer.isEdit()) {
                            GeneralConfig.getInstance().getServerClient().call(
                                    serviceURLPrefix + key,
                                    "PUT",
                                    Base64.getEncoder().encodeToString(data)
                            );
                        }
                    } catch (IOException | KFZClient.KFZServerException ex) {
                        Logger.getLogger(ImageGalleryController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
        }).start();
    }

    /**
     * The method returns true if user upload new file, rotate or delete existed. 
     * False if state does not change.
     * @return 
     */
    public boolean anyViewerChanged(){
        boolean result = false;
        for (DocumentViewer viewer : viewersMap.values()) {
            if (viewer == null) continue;
            result = viewer.isNew() || viewer.isEdit()|| viewer.deletedProperty().get();
            if (result) break;
        }
        return result;
    }
}
