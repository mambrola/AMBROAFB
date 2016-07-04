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
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.embed.swing.SwingFXUtils;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
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
    private ANodeSlider<Label> datesSlider;

    private ObservableList<Label> datesSliderElems;
    private Map<String, ImageOfGallery> images;
    private String undoDeleteImagePath;
    private Calendar calendar;
    private DateFormat formatter;
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        galleryImageView.setPreserveRatio(true);
        galleryImageView.fitWidthProperty().bind(galleryImageFrame.widthProperty());
        undoDeleteImagePath = "/images/delete2.png";
        images = new HashMap<>();
        datesSliderElems = datesSlider.getItems();
        calendar = Calendar.getInstance();
        formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        
    }
    
    public void dowloadDatesOfImagesFrom(String url){
        processAndSaveDatesFrom(url);
        if (!datesSliderElems.isEmpty()){
            try {
                String date = datesSliderElems.get(0).getText();
                String imageFullName = images.get(date).getImageFullName();
                if (imageFullName.contains(".pdf")){
                    System.out.println("pdf-ia da ara surati");
                }
                else {
                    HttpURLConnection con = GeneralConfig.getInstance().getServerClient().createConnection("/clients/passport/" + imageFullName);
                    Image image = new Image(con.getInputStream());
                    images.get(date).setImage(image);
                    galleryImageView.setImage(image);
                }
            } catch (IOException ex) {
                Logger.getLogger(ImageGalleryController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private void processAndSaveDatesFrom(String url){
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
                System.out.println("only: " + onlyDateAndTime);
                
                datesSliderElems.add(new Label(onlyDateAndTime));
                images.put(onlyDateAndTime, new ImageOfGallery(fullName, null));
            }
        } catch (KFZClient.KFZServerException ex) {
            if (ex.getStatusCode() == 404){
                new AlertMessage(Alert.AlertType.ERROR, ex, "%image_not_found").showAlert();
                Logger.getLogger(ImageGalleryController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (JSONException | IOException ex) {
            Logger.getLogger(ImageGalleryController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    private void deleteImage(ActionEvent event){
        System.out.println("delete");
        boolean imageDeleteNow = undoDeleteImagePath.equals("/images/delete2.png");
        if (imageDeleteNow){
            undoDeleteImagePath = "/images/undo.png";
        }
        else {
            undoDeleteImagePath = "/images/delete2.png";
        }
        String date = datesSlider.getValue().getText();
        images.get(date).setIsDeleted(imageDeleteNow);
        
        setImageToButton(deleteOrUndo, undoDeleteImagePath);
        deletedImageView.setVisible(imageDeleteNow);
    }
    
    private void setImageToButton(Button button, String imageURL){
        Image image = new Image(getClass().getResourceAsStream(imageURL));
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(((ImageView)button.getGraphic()).getFitWidth());
        imageView.setFitHeight(((ImageView)button.getGraphic()).getFitHeight());
        button.setGraphic(imageView);
    }
    
    @FXML
    private void uploadImage(ActionEvent event){
        System.out.println("upload");
        // sadacaa im qveynios dro saitidan....
    }
    
    private Image oldImage;
    
    @FXML
    private void rotate(ActionEvent event){
        galleryImageView.setRotate(galleryImageView.getRotate() + 90);
//        if (galleryImageView.getRotate()){
//            
//        }
        
        double imgW = galleryImageView.getImage().getWidth();
        double imgH = galleryImageView.getImage().getHeight();
        System.out.println("img width: " + imgW + " img height: " + imgH);
        
//        printMap();
    }
    
    private void setImagePos(Image image, ImageView imgView){
        
    }
    
    // -----------------------------------
    private void printMap(){
        for (String key : images.keySet()) {
            ImageOfGallery image = images.get(key);
            System.out.println(key + " " + image.getImageFullName() + 
                                " del: " + image.isDeleted + 
                                " add: " + image.isAdded + 
                                " rot: " + image.isRotate);
        }
    }
    
    public Image rotateImage(Image img) throws IOException, KFZClient.KFZServerException {
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
        private boolean isDeleted;
        private boolean isAdded;
        private boolean isRotate;
        
        public ImageOfGallery(String url, Image image) {
            this.image = image;
            this.imageName = url;
        }
        
        private Image getImage(){
            return image;
        }
        
        public String getImageFullName(){
            return imageName;
        }
        
        public void setImagePath(String  newImageURL){
            this.imageName = newImageURL;
        }
        
        public boolean isDeleted(){
            return isDeleted;
        }

        public boolean isIsAdded() {
            return isAdded;
        }

        public boolean isIsRotate() {
            return isRotate;
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
        
    }
    
}
