/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general.image_gallery;

import ambroafb.general.KFZClient;
import ambroafb.general.Utils;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javax.imageio.ImageIO;

/**
 *
 * @author dato
 */
public class ImageViewer implements DocumentViewer, Initializable {

    @FXML
    private HBox root;
    @FXML
    private ImageView imageView;

    private boolean isNew;
    private final BooleanProperty deletedProperty = new SimpleBooleanProperty();
    private final String fullName;
    private int degree;
    private final InputStream stream;

    public ImageViewer(InputStream stream, String imageFullName) {
        fullName = imageFullName;
        this.stream = stream;
        Utils.createScene("/ambroafb/general/image_gallery/ImageViewer.fxml", (Object) this);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        imageView.setFitWidth(DocumentViewer.FIT_WIDTH);
        imageView.setFitHeight(DocumentViewer.FIT_HEIGHT);
        
        Image image = new Image(stream);
        double reqW = image.getRequestedWidth();
        double reqH = image.getRequestedHeight();
        System.out.println("reqW: " + reqW + " reqH: " + reqH);
        
        if (reqW < DocumentViewer.FIT_WIDTH) {
            imageView.setFitWidth(reqW);
        }
        if (reqH < DocumentViewer.FIT_HEIGHT) {
            imageView.setFitHeight(reqH);
        }
        imageView.setImage(image);
    }
    
    @Override
    public Node getComponent() {
        return root;
    }

    @Override
    public void rotate() {
        try {
            degree += 90;
            Image image = rotateImage(imageView.getImage());
            
            double reqW = image.getRequestedWidth();
            double reqH = image.getRequestedHeight();
            System.out.println("reqW: " + reqW + " reqH: " + reqH);
            
            double propW = image.widthProperty().get();
            double propH = image.heightProperty().get();
            System.out.println("propW: " + propW + " propH: " + propH);
            
            if(reqH < DocumentViewer.FIT_WIDTH){
                imageView.setFitWidth(reqH);
            }
            if(reqW < DocumentViewer.FIT_HEIGHT){
                imageView.setFitHeight(reqW);
            }
            imageView.setImage(rotateImage(imageView.getImage()));
        } catch (IOException | KFZClient.KFZServerException ex) {
            Logger.getLogger(ImageViewer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void deleteOrUndo() {
        deletedProperty.set(!deletedProperty.get());
    }

    @Override
    public byte[] getContent() {
        byte[] result = null;
        try {
            BufferedImage bImage = SwingFXUtils.fromFXImage(imageView.getImage(), null);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ImageIO.write(bImage, "png", out);
            result = out.toByteArray();
        } catch (IOException ex) {
            Logger.getLogger(ImageViewer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
    
    @Override
    public void setIsNew(boolean isNew) {
        this.isNew = isNew;
    }

    @Override
    public boolean isNew() {
        return isNew;
    }

    @Override
    public BooleanProperty deletedProperty() {
        return deletedProperty;
    }

    @Override
    public boolean isEdit() {
        return degree % 360 != 0;
    }

    @Override
    public String getFullName() {
        return fullName;
    }

    @Override
    public boolean isNotValidDocument() {
        return false;
    }

    @Override
    public String getInvalidationMessage() {
        return "invalid image"; // ???????????
    }

}
