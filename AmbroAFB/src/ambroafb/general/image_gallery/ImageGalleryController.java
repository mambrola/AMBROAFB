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
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    private Map<String, ImageOfGallery> images;
    private String undoDeleteImagePath;
    private Calendar calendar;
    private DateFormat formatter;
    private FileChooser fileChooser;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        galleryImageView.setPreserveRatio(true);
        galleryImageView.fitWidthProperty().bind(galleryImageFrame.widthProperty());
        undoDeleteImagePath = "/images/delete2.png";
        images = new HashMap<>();
        datesSliderElems = FXCollections.observableArrayList();
        calendar = Calendar.getInstance();
        formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        fileChooser = new FileChooser();
        ExtensionFilter filter = new ExtensionFilter("Images files (*.png, *.jpg, *.pdf)", "*.png", "*.jpg", "*.pdf");
        fileChooser.getExtensionFilters().add(filter);

    }

    public void dowloadDatesOfImagesFrom(String urlPrefix, String parameter) {
        processAndSaveDatesFrom(urlPrefix + parameter);
        datesSlider.indexProperty().addListener((ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) -> {
            showImage(urlPrefix, newValue);
        });
        datesSlider.setIndex(0);
    }

    private void processAndSaveDatesFrom(String url) {
        try {
            String imagesNames = GeneralConfig.getInstance().getServerClient().get(url);
            JSONArray namesJson = new JSONArray(imagesNames);
            for (int i = 0; i < namesJson.length(); i++) {
                String fullName = namesJson.getString(i);
                int start = fullName.indexOf("_") + 1;
                int end = fullName.lastIndexOf(".");
                String miliseconds = fullName.substring(start, end);
                calendar.setTimeInMillis(Long.parseLong(miliseconds));
                String onlyDateAndTime = formatter.format(calendar.getTime());

                datesSliderElems.add(onlyDateAndTime);
                images.put(onlyDateAndTime, new ImageOfGallery(fullName, null));
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
            try {
                String date = datesSliderElems.get(index);
                Image img = images.get(date).getImage();
                if (img != null) {
                    galleryImageView.setImage(img);
                    return;
                }
                if (images.get(date).getPDFasImages() != null){
                    galleryImageView.setImage(images.get(date).getPDFImageByPage(0));
                }
                String imageFullName = images.get(date).getImageFullName();
                HttpURLConnection con = GeneralConfig.getInstance().getServerClient().createConnection(urlPrefix + imageFullName);
                if (imageFullName.contains(".pdf")) {
                    try (PDFHelper pdfHelper = new PDFHelper(con.getInputStream())) {
                        images.get(date).setPDFasImages(pdfHelper.getImages());
                        galleryImageView.setImage(images.get(date).getPDFImageByPage(0));
                    }
                } else {
                    Image image = new Image(con.getInputStream());
                    images.get(date).setImage(image);
                    galleryImageView.setImage(image);
                }
            } catch (IOException ex) {
                Logger.getLogger(ImageGalleryController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @FXML
    private void deleteImage(ActionEvent event) {
        System.out.println("delete");
        boolean imageDeleteNow = undoDeleteImagePath.equals("/images/delete2.png");
        if (imageDeleteNow) {
            undoDeleteImagePath = "/images/undo.png";
        } else {
            undoDeleteImagePath = "/images/delete2.png";
        }
        String date = datesSlider.getValue();
        images.get(date).setIsDeleted(imageDeleteNow);

        setImageToButton(deleteOrUndo, undoDeleteImagePath);
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
        for (String key : images.keySet()) {
            ImageOfGallery image = images.get(key);
            System.out.println(key + " " + image.getImageFullName()
                    + " del: " + image.isDeleted
                    + " add: " + image.isAdded
                    + " rot: " + image.isRotate
                    + " image: " + image.getImage());
        }
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

    private class ImageOfGallery {

        private String imageName;
        private Image image;
        private List<Image> pdfImages;
        private boolean isDeleted;
        private boolean isAdded;
        private boolean isRotate;

        public ImageOfGallery(String url, Image image) {
            this.image = image;
            this.imageName = url;
            pdfImages = new ArrayList<Image>();
        }

        private Image getImage() {
            return image;
        }

        public String getImageFullName() {
            return imageName;
        }

        public void setImagePath(String newImageURL) {
            this.imageName = newImageURL;
        }

        public boolean isDeleted() {
            return isDeleted;
        }

        public boolean isIsAdded() {
            return isAdded;
        }

        public boolean isIsRotate() {
            return isRotate;
        }

        public List<Image> getPDFasImages() {
            return pdfImages;
        }

        public Image getPDFImageByPage(int page) {
            Image result = null;
            if (page < pdfImages.size() && page >= 0) {
                result = pdfImages.get(page);
            }
            return result;
        }

        public void setIsDeleted(boolean isDeleted) {
            this.isDeleted = isDeleted;
        }

        public void setIsAdded(boolean isAdded) {
            this.isAdded = isAdded;
        }

        public void setIsRotate(boolean isRotate) {
            this.isRotate = isRotate;
        }

        private void setImage(Image image) {
            this.image = image;
        }

        private void setPDFasImages(List<Image> images) {
            pdfImages = images;
        }

    }

}
