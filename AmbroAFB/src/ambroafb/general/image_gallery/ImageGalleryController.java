/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general.image_gallery;

import ambro.ANodeSlider;
import ambroafb.general.KFZClient;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

/**
 * FXML Controller class
 *
 * @author dato
 */
public class ImageGalleryController implements Initializable {

    @FXML
    private Button deleteOrUndo, rotateToRight, upload;
//    
//    @FXML
//    private HBox profileImageFrame, imageButtonsHBox;
    
    @FXML
    private ImageView profImageView, deletedImageView;
    
    @FXML
    private ANodeSlider<Label> nodeSlider;
    
    private String undoDeletePath;
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        profImageView.setPreserveRatio(true);
        profImageView.setTranslateX((profImageView.getFitWidth() - profImageView.getFitHeight())/2);
        undoDeletePath = "/images/delete2.png";
        nodeSlider.getItems().add(new Label("DATE 1"));
        nodeSlider.getItems().add(new Label("DATE 2"));
    }    
    
    @FXML
    private void deleteImage(ActionEvent event){
        System.out.println("delete");
        boolean imageDeleteNow = undoDeletePath.equals("/images/delete2.png");
        if (imageDeleteNow){
            undoDeletePath = "/images/undo.png";
        }
        else {
            undoDeletePath = "/images/delete2.png";
        }
        setImageToButton(deleteOrUndo, undoDeletePath);
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
    }
    
    @FXML
    private void rotate(ActionEvent event){
        profImageView.setRotate(profImageView.getRotate() + 90);
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
}
