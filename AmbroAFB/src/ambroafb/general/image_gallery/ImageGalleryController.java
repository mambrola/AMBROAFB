/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general.image_gallery;

import ambroafb.general.GeneralConfig;
import ambroafb.general.KFZClient;
import ambroafb.general.PDFHelper;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javax.imageio.ImageIO;
import jfxtras.scene.control.ListSpinner;
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
    private HBox galleryImageFrame;

    @FXML
    private ListSpinner<String> datesSlider;

    private ObservableList<String> datesSliderElems;
    private Map<String, DocumentViewer> viewersMap;
    private String undoOrDeleteImagePath;
    private Calendar calendar;
    private DateFormat formatter;
    private FileChooser fileChooser;
    private String serviceURLPrefix;
    private String parameter;

    private static final String GALLERY_DELETE_BUTTON_IMAGE_NAME = "/images/delete_128.png";
    private static final String GALLERY_UNDO_BUTTON_IMAGE_NAME = "/images/undo_128.png";

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        galleryImageView.setPreserveRatio(true);
        undoOrDeleteImagePath = GALLERY_DELETE_BUTTON_IMAGE_NAME;
        viewersMap = new HashMap<>();
        datesSliderElems = FXCollections.observableArrayList();
        calendar = Calendar.getInstance();
        formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss.SSS");
        fileChooser = new FileChooser();
        ExtensionFilter filter = new ExtensionFilter("Images files (*.png, *.jpg, *.pdf)", "*.png", "*.jpg", "*.pdf");
        fileChooser.getExtensionFilters().add(filter);
        datesSlider.setStringConverter(new StringConverter<String>() {
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
        });
    }
    
    public void sendingURLs(String prefix, String parameter) {
        serviceURLPrefix = prefix;
        this.parameter = parameter;
    }

    public void dowloadDatesOfImagesFrom(String urlPrefix, String parameter) {
        processAndSaveDatesFrom(urlPrefix + parameter);
        Platform.runLater(()->{
            datesSlider.indexProperty().addListener((ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) -> {
                showImage(urlPrefix, newValue);
            });
            if (datesSlider.getItems() != null && !datesSlider.getItems().isEmpty()) {
                datesSlider.setIndex(0);
            }
        });
        
    }

    private void processAndSaveDatesFrom(String url) {
        try {
            String imagesNames = GeneralConfig.getInstance().getServerClient().get(url);
            JSONArray namesJson = new JSONArray(imagesNames);
            for (int i = 0; i < namesJson.length(); i++) {
                String fullName = namesJson.getString(i);
                datesSliderElems.add(fullName);
                viewersMap.put(fullName, null);
            }
            Platform.runLater(()->{
                datesSlider.setItems(datesSliderElems);
            });
        } catch (KFZClient.KFZServerException ex) {
            System.out.println("ex code: " + ex.getStatusCode());
            if (ex.getStatusCode() == 404) {
            }
        } catch (JSONException | IOException ex) {
            Logger.getLogger(ImageGalleryController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void showImage(String urlPrefix, int index) {
        if (index >= 0 && index < datesSliderElems.size()) {
            String fullName = datesSliderElems.get(index);
            DocumentViewer viewer = viewersMap.get(fullName);
            if (viewer == null) {
                try {
                    HttpURLConnection con = GeneralConfig.getInstance().getServerClient().createConnection(urlPrefix + fullName);
                    viewer = DocumentViewer.Factory.getAppropriateViewer(con.getInputStream(), fullName);
                    viewersMap.put(fullName, viewer);
                } catch (IOException ex) {
                    Logger.getLogger(ImageGalleryController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (viewer != null) {
                final DocumentViewer dViewer = viewer;
                galleryImageFrame.getChildren().setAll(viewer.getComponent());
                deletedImageView.visibleProperty().unbind();
                deletedImageView.visibleProperty().bind(viewer.deletedProperty());
                ImageView icon = (ImageView) deleteOrUndo.getGraphic();
                icon.imageProperty().unbind();
                icon.imageProperty().bind(Bindings.createObjectBinding(() -> {
                    String url = dViewer.deletedProperty().get() ? GALLERY_UNDO_BUTTON_IMAGE_NAME : GALLERY_DELETE_BUTTON_IMAGE_NAME;
                    return new Image(getClass().getResourceAsStream(url));
                }, viewer.deletedProperty()));

            }
        }
    }

    @FXML
    private void deleteImage(ActionEvent event) {
        String fullName = datesSlider.getValue();
        DocumentViewer viewer = viewersMap.get(fullName);
        if(viewer != null){
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
        List<File> files = fileChooser.showOpenMultipleDialog(owner);
        if (files == null) {
            return;
        }
        files.stream().map((file) -> {
            fileChooser.setInitialDirectory(file.getParentFile());
            return file;
        }).forEach((file) -> {
            InputStream stream = null;
            try {
                String fileName = file.getName();
                stream = new FileInputStream(file);
                DocumentViewer viewer = (fileName.endsWith(".pdf")) ? new PDFViewer(stream, fileName)
                                                                    : new ImageViewer(stream, fileName);
                viewer.setIsNew(true);
                galleryImageFrame.getChildren().setAll(viewer.getComponent());
                galleryImageView.setPreserveRatio(true);
                
                Long currTime = new Date().getTime();
                String fileFullName = "_" + currTime + fileName.substring(fileName.lastIndexOf("."));
                viewersMap.put(fileFullName, viewer);
                if (datesSlider.getItems() == null){
                    datesSliderElems.add(fileFullName);
                    datesSlider.setItems(datesSliderElems);
                    datesSlider.setIndex(0);
                }
                else {
                    datesSliderElems.add(fileFullName);
                    datesSlider.setIndex(datesSliderElems.size() - 1);
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(ImageGalleryController.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    stream.close();
                } catch (IOException ex) {
                    Logger.getLogger(ImageGalleryController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    @FXML
    private void rotate(ActionEvent event) {
        String fullName = datesSlider.getValue();
        DocumentViewer viewer = viewersMap.get(fullName);
        viewer.rotate();
    }
    
    @FXML
    private void zoom(ActionEvent event){
        
    }

    public void sendDataToServer() {
        viewersMap.keySet().stream().forEach((key) -> {
            DocumentViewer viewer = viewersMap.get(key);
            if (viewer != null) {
                byte[] data = viewer.getContent();
                try {
                    if (viewer.isNew()) {
                        GeneralConfig.getInstance().getServerClient().post(
                                serviceURLPrefix + parameter,
                                Base64.getEncoder().encodeToString(data)
                        );
                    } else if (viewer.deletedProperty().get()) {
                        GeneralConfig.getInstance().getServerClient().call(serviceURLPrefix + key, "DELETE", null);
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
    }

}
