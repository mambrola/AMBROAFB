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
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
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
    private VBox imagesGalleryRoot;
//    @FXML
//    private Image galleryImage;
    
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
    private int validPDFPagesForClientDialog;
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
        validPDFPagesForClientDialog = 10;
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
        
        
        
        
        //Murman - bind-, რაღაც ამდაგვარი უნდა გამოვიყენოთ
//        galleryImageView.fitWidthProperty().bind(imagesGalleryRoot.widthProperty());
//        galleryImageView.fitHeightProperty().bind(imagesGalleryRoot.heightProperty());

        System.out.println( "in - galleryImageFrame.size: " + " : " + 
                            galleryImageFrame.getWidth()+ " : " +
                            galleryImageFrame.getHeight());
        
        galleryImageView.setFitWidth(imagesGalleryRoot.getWidth());
        msgSlider.indexProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            showImage(serviceURLPrefix, newValue.intValue());
        });
        Platform.runLater(()->{
            doAfterInicialize(new Image("/images/logo.png"));
        });
    }
    
    private void doAfterInicialize(Image image){
        System.out.println( "after - imagesGalleryRoot.size: " + " : " + 
                            imagesGalleryRoot.getWidth()+ " : " +
//                imagesGalleryRoot.getPrefWidth() + " : " +
//                imagesGalleryRoot.getMaxWidth() + " : " +
//                imagesGalleryRoot.getMinWidth()+ " : " +
                            imagesGalleryRoot.getHeight());
        
        System.out.println(String.format("image sizes -> width: %f       height: %f", 
                                                        image.getWidth(), image.getHeight())); 
       
        if(image.getWidth() > imagesGalleryRoot.getWidth()){
            galleryImageView.setFitWidth(imagesGalleryRoot.getWidth());
            System.out.println("axla imageView-s fitWidth: " + galleryImageView.getFitWidth());
        }
        if(image.getHeight() > imagesGalleryRoot.getHeight()){
            galleryImageView.setFitHeight(imagesGalleryRoot.getHeight());
            System.out.println("axla imageView-s fitHeight: " + galleryImageView.getFitHeight());
        }
        
        galleryImageView.setImage(image);
        
    }
    
    public void tm(){
        galleryImageView.setFitWidth(0.7*imagesGalleryRoot.getWidth());
        //galleryImageView.setFitWidth(imagesGalleryRoot.getWidth());
        System.out.println( "imagesGalleryRoot.size: " + " : " + 
                            imagesGalleryRoot.getWidth()+ " : " +
                            imagesGalleryRoot.getHeight());
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
        
    private void showImage(String urlPrefix, int index) {
        if (index >= 0 && index < datesSliderElems.size()) {
            String fullName = datesSliderElems.get(index);
            DocumentViewer viewer = viewersMap.get(fullName);
            if (viewer == null) {
                new Thread(()->{
                    try {
                        Platform.runLater(() -> {
                            masker.setVisible(true);
                        });
                        HttpURLConnection con = GeneralConfig.getInstance().getServerClient().createConnection(urlPrefix + fullName);
                        DocumentViewer newViewer = DocumentViewer.Factory.getAppropriateViewer(con.getInputStream(), fullName, validPDFPagesForClientDialog);
                        Platform.runLater(() -> {
                            viewersMap.put(fullName, newViewer);
                            showViewerComponentOnScene(newViewer);
                            masker.setVisible(false);
                        });
                    } catch (IOException ex) {
                        Logger.getLogger(ImageGalleryController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }).start();
            }
            else {
                showViewerComponentOnScene(viewer);
            }
        }
    }
    
    private void showViewerComponentOnScene(DocumentViewer viewer){
        final DocumentViewer dViewer = viewer;
        
//        int size = galleryImageFrame.getChildren().size();
//        System.out.println("galleryImageFrame.getChildren().size(): " + size);
//        for (int i = 0; i < size; i++){
//            System.out.println("galleryImageFrame.getChildren().size(): " + galleryImageFrame.getChildren().get(i));
//        }
//        galleryImageFrame.getChildren().setAll(dViewer.getComponent());
//        galleryImageView
        doAfterInicialize(viewer.getComponent());
        deletedImageView.visibleProperty().unbind();
        deletedImageView.visibleProperty().bind(dViewer.deletedProperty());
        ImageView icon = (ImageView) deleteOrUndo.getGraphic();
        icon.imageProperty().unbind();
        icon.imageProperty().bind(Bindings.createObjectBinding(() -> {
            String url = dViewer.deletedProperty().get() ? GALLERY_UNDO_BUTTON_IMAGE_NAME : GALLERY_DELETE_BUTTON_IMAGE_NAME;
            return new Image(getClass().getResourceAsStream(url));
        }, dViewer.deletedProperty()));
    }
    
    public void setUploadDataURL(String serviceURLPrefix, String parameterUpload, String parameterDownload) {
        this.serviceURLPrefix = serviceURLPrefix;
        this.parameterUpload = parameterUpload;
        this.parameterDownload = parameterDownload;
    }

    public void downloadData() {
        try {
            String imagesNames = GeneralConfig.getInstance().getServerClient().get(serviceURLPrefix + parameterDownload);
            JSONArray namesJson = new JSONArray(imagesNames);
            for (int i = 0; i < namesJson.length(); i++) {
                String fullName = namesJson.getString(i);
                datesSliderElems.add(fullName);
                viewersMap.put(fullName, null);
            }
//            if (datesSliderElems != null && !datesSliderElems.isEmpty()) {
//                msgSlider.setValueOn(0); // +++
//            }
        } catch (KFZClient.KFZServerException ex) {
            System.out.println("ex code: " + ex.getStatusCode() + "  User has not images.");
            
        } catch (JSONException | IOException ex) {
            Logger.getLogger(ImageGalleryController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void deleteImage(ActionEvent event) {
        String fullName = msgSlider.getValue();
        DocumentViewer viewer = viewersMap.get(fullName);
        if (viewer != null) {
            viewer.deleteOrUndo();
        }
    }

    @FXML
    private void uploadFile(ActionEvent event) {
        Stage owner = (Stage) galleryImageFrame.getScene().getWindow();
        if (defaultFileChooserPath != null){
            fileChooser.setInitialDirectory(new File(defaultFileChooserPath));
        }
        List<File> files = fileChooser.showOpenMultipleDialog(owner);
        if (files == null) return;
        
        defaultFileChooserPath = files.get(files.size() - 1).getParentFile().getPath();
        GeneralConfig.prefs.put(UPLOAD_DIRECTORY_PATH, defaultFileChooserPath);
        
        List<String> invalidViewersMessages = new ArrayList<>();
        new Thread(() -> {
            Platform.runLater(()->{
                masker.setVisible(true);
            });
            files.stream().forEach((file) -> {
                try {
                    String fileName = file.getName();
                    InputStream stream = new FileInputStream(file);
                    DocumentViewer viewer;
                    if (fileName.substring(fileName.lastIndexOf(".")).toLowerCase().equals(".pdf")) {
                        viewer = new PDFViewer(stream, fileName);
                    } else {
                        viewer = new ImageViewer(stream, fileName);
                    }
                    
                    viewer.setIsNew(true);
                    Platform.runLater(() -> {
                        proccessViewer(viewer, fileName);
                    });
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(ImageGalleryController.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            Platform.runLater(()->{
                masker.setVisible(false);
                showLargePDFWarning(invalidViewersMessages);
            });
        }).start();
    }
    
    /** 
     * The method count date and set index of images slider. This action cause to show new uploading file.
     * @param viewer
     * @param fileName 
     */
    private void proccessViewer(DocumentViewer viewer, String fileName){
        Long currTime = new Date().getTime();
        String fileFullName = "_" + currTime + fileName.substring(fileName.lastIndexOf("."));
        viewersMap.put(fileFullName, viewer);
        datesSliderElems.add(fileFullName);
        msgSlider.setValueOn(datesSliderElems.size() - 1);
    }
    
    private void showLargePDFWarning(List<String> invalidViewersMessages) {
        String warningMsg = "";
        warningMsg = invalidViewersMessages.stream().map((msg) -> msg + "\n").reduce(warningMsg, String::concat);
        if (!warningMsg.isEmpty()){
            new AlertMessage(Alert.AlertType.WARNING, null, warningMsg).showAlert();
        }
    }

    @FXML
    private void rotate(ActionEvent event) {
        String fullName = msgSlider.getValue();
        DocumentViewer viewer = viewersMap.get(fullName);
        if (viewer != null) {
            viewer.rotate();
            doAfterInicialize(viewer.getComponent());
//            galleryImageView.setImage(viewer.getComponent());
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
