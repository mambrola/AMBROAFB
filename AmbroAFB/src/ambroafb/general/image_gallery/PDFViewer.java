/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general.image_gallery;

import ambroafb.general.KFZClient;
import ambroafb.general.PDFHelper;
import ambroafb.general.Utils;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author dato
 */
public class PDFViewer implements Initializable, DocumentViewer {

    @FXML
    private ImageView imageView;
    @FXML
    private Button up, down;

    private PDFHelper pdfHelper;
    private List<Image> images;
    private Map<Integer, Integer> rotatedImages;
    private Node root;
    private IntegerProperty indexProperty = new SimpleIntegerProperty(1);
    private BooleanProperty deletedProperty = new SimpleBooleanProperty();
    private boolean isNew;
    private String fullName;
    private boolean isInvalid;

    public PDFViewer(InputStream stream, String pdfFullName, int validPageNamber) {
        try {
            pdfHelper = new PDFHelper(stream);
            if (pdfHelper.getPageCount() > validPageNamber) {
                isInvalid = true;
            } else {
                images = pdfHelper.getImages();
                rotatedImages = new HashMap<>();
                Scene scene = Utils.createScene("/ambroafb/general/image_gallery/PDFViewer.fxml", (Object) this);
                root = scene.getRoot();
                fullName = pdfFullName;

                up.setOnAction((ActionEvent event) -> {
                    indexProperty.set(indexProperty.get() - 1);
                });

                down.setOnAction((ActionEvent event) -> {
                    indexProperty.set(indexProperty.get() + 1);
                });
            }
        } catch (IOException ex) {
            System.out.println("pdf probleeeem. davichirot esec da davamushaot baregam.");
//            Logger.getLogger(PDFViewer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        indexProperty.addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            if (newValue.intValue() >= 0 && newValue.intValue() < images.size()) {
                imageView.setImage(images.get(newValue.intValue()));
            }
        });
        up.disableProperty().bind(Bindings.createBooleanBinding(() -> {
            return indexProperty.get() <= 0;
        }, indexProperty));

        down.disableProperty().bind(Bindings.createBooleanBinding(() -> {
            return indexProperty.get() >= images.size() - 1;
        }, indexProperty));
        indexProperty.set(0);
    }

    @Override
    public Node getComponent() {
        return root;
    }

    @Override
    public void rotate() {
        try {
            Image cur = imageView.getImage();
            cur = rotateImage(cur);
            images.set(indexProperty.get(), cur);
            imageView.setImage(cur);
            int deg = rotatedImages.getOrDefault(indexProperty.getValue(), 0);
            rotatedImages.put(indexProperty.getValue(), deg + 90);
        } catch (IOException | KFZClient.KFZServerException ex) {
            Logger.getLogger(PDFViewer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void deleteOrUndo() {
        deletedProperty.set(!deletedProperty.get());
    }

    @Override
    public byte[] getContent() {
        getRotated().stream().forEach((Integer t) -> {
            try {
                pdfHelper.replace(images.get(t), t);
            } catch (IOException ex) {
                Logger.getLogger(PDFViewer.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        byte[] result = null;
        try {
            result = pdfHelper.getContent();
        } catch (IOException ex) {
            Logger.getLogger(PDFViewer.class.getName()).log(Level.SEVERE, null, ex);
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
        return getRotated().size() > 0;
    }

    public List<Integer> getRotated() {
        return rotatedImages.keySet().stream().filter((Integer t) -> rotatedImages.get(t) % 360 != 0).collect(Collectors.toList());
    }

    @Override
    public String getFullName() {
        return fullName;
    }

    @Override
    public boolean isNotValidDocument() {
        return isInvalid;
    }

    @Override
    public String getInvalidationMessage() {
        String warningMSg = "";
        if (true){
            warningMSg += "";
        }
        return warningMSg;
    }
}
