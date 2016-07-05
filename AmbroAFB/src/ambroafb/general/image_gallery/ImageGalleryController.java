/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general.image_gallery;

import ambro.ANodeSlider;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import ambroafb.general.AlertMessage;
import ambroafb.general.GeneralConfig;
import ambroafb.general.KFZClient;
import ambroafb.general.PDFHelper;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.embed.swing.SwingFXUtils;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
    private Map<String, DocumentViewer> images;
    private String undoOrDeleteImagePath;
    private Calendar calendar;
    private DateFormat formatter;
    private FileChooser fileChooser;
    private String serviceURLPrefix;
    private String parameter;

    
    private static final String GALLERY_DELETE_BUTTON_IMAGE_NAME = "/images/deleteImg.png";
    private static final String GALLERY_UNDO_BUTTON_IMAGE_NAME = "/images/undo.png";
    
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
        images = new HashMap<>();
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
//        showImage(urlPrefix, 0);
        processAndSaveDatesFrom(urlPrefix + parameter);
        if(datesSlider.getItems() != null && !datesSlider.getItems().isEmpty()){
            datesSlider.indexProperty().addListener((ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) -> {
                showImage(urlPrefix, newValue);
            });
            datesSlider.setIndex(0);
        }
    }

    private void processAndSaveDatesFrom(String url) {
        try {
            String imagesNames = GeneralConfig.getInstance().getServerClient().get(url);
            JSONArray namesJson = new JSONArray(imagesNames);
            for (int i = 0; i < namesJson.length(); i++) {
                String fullName = namesJson.getString(i);
//                int start = fullName.indexOf("_") + 1;
//                int end = fullName.lastIndexOf(".");
//                String miliseconds = fullName.substring(start, end);
//                calendar.setTimeInMillis(Long.parseLong(miliseconds));
//                String onlyDateAndTime = formatter.format(calendar.getTime());                
                datesSliderElems.add(fullName);
                images.put(fullName, null);
            }
            datesSlider.setItems(datesSliderElems);
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
            DocumentViewer viewer = images.get(fullName);
            if (viewer == null){
                try {
                    HttpURLConnection con = GeneralConfig.getInstance().getServerClient().createConnection(urlPrefix+fullName);
                    viewer = DocumentViewer.Factory.getAppropriateViewer(con.getInputStream(), fullName);
                    images.put(fullName, viewer);
                } catch (IOException ex) {
                    Logger.getLogger(ImageGalleryController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (viewer != null){
                galleryImageFrame.getChildren().setAll(viewer.getComponent());
                deletedImageView.visibleProperty().unbind();
                deletedImageView.visibleProperty().bind(viewer.deletedProperty());
            }
//            try {
//                String date = datesSliderElems.get(index);
//                Image img = images.get(date).getImage();
//                if (img != null) {
//                    galleryImageView.setImage(img);
//                    return;
//                }
//                if (images.get(date).getPDFasImages() != null){
//                    galleryImageView.setImage(images.get(date).getPDFImageByPage(0));
//                }
//                String imageFullName = images.get(date).getImageFullName();
//                HttpURLConnection con = GeneralConfig.getInstance().getServerClient().createConnection(urlPrefix + imageFullName);
//                if (imageFullName.contains(".pdf")) {
////                    try (PDFHelper pdfHelper = new PDFHelper(con.getInputStream())) {
////                        images.get(date).setPDFasImages(pdfHelper.getImages());
////                        images.get(date).savePDFHelper(pdfHelper);
////                        
////                        galleryImageView.setImage(images.get(date).getPDFImageByPage(0));
////                    }
//                    DocumentViewer viewer = new PDFViewer(con.getInputStream(), "/images/default_profile_image.png");
//                    galleryImageFrame.getChildren().add(viewer.getComponent());
//                } else {
//                    Image image = new Image(con.getInputStream());
//                    images.get(date).setImage(image);
//                    galleryImageView.setImage(image);
//                }
//            } catch (IOException ex) {
//                Logger.getLogger(ImageGalleryController.class.getName()).log(Level.SEVERE, null, ex);
//            }
        }
    }

    @FXML
    private void deleteImage(ActionEvent event) {
        System.out.println("delete");
        boolean imageDeleteNow = undoOrDeleteImagePath.equals(GALLERY_DELETE_BUTTON_IMAGE_NAME);
        if (imageDeleteNow) {
            undoOrDeleteImagePath = GALLERY_UNDO_BUTTON_IMAGE_NAME;
        } else {
            undoOrDeleteImagePath = GALLERY_DELETE_BUTTON_IMAGE_NAME;
        }
        String fullName = datesSlider.getValue();
        System.out.println("delete method fullname: " + fullName);
        
        setImageToButton(deleteOrUndo, undoOrDeleteImagePath);
        deletedImageView.setVisible(imageDeleteNow);
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
        System.out.println("upload");
        Stage owner = (Stage) galleryImageView.getScene().getWindow();
        List<File> files = fileChooser.showOpenMultipleDialog(owner);
        if (files == null) {
            return;
        }
        for (File file : files) {
            fileChooser.setInitialDirectory(file.getParentFile());
            String fileName = file.getName();
            int lastPointIndex = fileName.lastIndexOf(".");
            String ext = fileName.substring(lastPointIndex + 1);
            if (ext.equals("pdf")) {

            } else {
                try {
                    BufferedImage bImage = ImageIO.read(file);
                    Image image = SwingFXUtils.toFXImage(bImage, null);
                    galleryImageView.setImage(image);
                    galleryImageView.setPreserveRatio(true);
                } catch (IOException ex) {
                    Logger.getLogger(ImageGalleryController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    @FXML
    private void rotate(ActionEvent event) {
        try {
            Image newImage = rotateImage(galleryImageView.getImage());
            galleryImageView.setImage(newImage);
        } catch (IOException | KFZClient.KFZServerException ex) {
            Logger.getLogger(ImageGalleryController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // -----------------------------------
    private void printMap() {
//        for (String key : images.keySet()) {
//            ImageOfGallery image = images.get(key);
//            System.out.println(key + " " + image.getImageFullName()
//                    + " del: " + image.isDeleted
//                    + " add: " + image.isAdded
//                    + " rot: " + image.isRotate
//                    + " image: " + image.getImage());
//        }
    }

    private Image rotateImage(Image img) throws IOException, KFZClient.KFZServerException {
        BufferedImage bImage = SwingFXUtils.fromFXImage(img, null);

        AffineTransform tx = new AffineTransform();
        tx.translate(bImage.getHeight() / 2, bImage.getWidth() / 2);
        tx.rotate(Math.PI / 2);
        tx.translate(-bImage.getWidth() / 2, -bImage.getHeight() / 2);

        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);

        BufferedImage rotImage = new BufferedImage(bImage.getHeight(), bImage.getWidth(), bImage.getType());
        op.filter(bImage, rotImage);

        return SwingFXUtils.toFXImage(rotImage, null);
    }


    public void sendDataToServer() {
//        for (String key : images.keySet()) {
//            ImageOfGallery imageData = images.get(key);
//            PDFHelper pdfHelper = imageData.getPDFHelper();
//            if (pdfHelper != null){
//                if (imageData.isIsAdded()){
//                    try {
//                        String url = serviceURLPrefix + parameter + "pdf";
//                        GeneralConfig.getInstance().getServerClient().post(url, Base64.getEncoder().encodeToString(pdfHelper.getContent()));
//                    } catch (IOException | KFZClient.KFZServerException ex) {
//                        Logger.getLogger(ImageGalleryController.class.getName()).log(Level.SEVERE, null, ex);
//                    }
//                }
////                else if (){
////                    
////                }
//            }
//            else{
//                
//            }
//        }
    }

}
