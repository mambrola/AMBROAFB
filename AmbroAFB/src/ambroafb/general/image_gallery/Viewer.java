/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general.image_gallery;

import ambroafb.general.KFZClient;
import ambroafb.general.PDFHelper;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

/**
 *
 * @author dato
 */
public class Viewer {
    
    private List<Image> images;
    private PDFHelper pdfHelper;
    private IntegerProperty indexProperty;
    private BooleanProperty deleteProperty;
    private String fileName;
    private boolean isNew;
    private boolean isPDF;
    private int degree;
    
    public Viewer(InputStream stream, boolean isPDF){
        this.isPDF = isPDF;
        images = new ArrayList<>();
        indexProperty = new SimpleIntegerProperty(1);
        deleteProperty = new SimpleBooleanProperty(false);
        if (isPDF){
            try {
                pdfHelper = new PDFHelper(stream);
                images.add(pdfHelper.getImage(0));
                System.out.println("pdf-i"); 
                indexProperty.addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
                    if (newValue.intValue() >= 0 && newValue.intValue() < images.size()) {
//                        Image img = images.get(newValue.intValue());
                        if (images.get(newValue.intValue()) == null) {
                            try {
                                Image img = pdfHelper.getImage(newValue.intValue());
                                images.add(img);
                            } catch (IOException ex) {
                                Logger.getLogger(PDFViewer.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                });
            } catch (IOException ex) {
                Logger.getLogger(Viewer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else {
            images.add(new Image(stream));
        }
        indexProperty.set(0);
    }
    
    
    public IntegerProperty indexProperty(){
        return indexProperty;
    }
    
    public Image getImage(){
        return images.get(indexProperty.get());
    }
    
    public String getFileName(){
        return fileName;
    }
    
    public boolean isPDFViewer(){
        return isPDF;
    }
    
    public boolean isNew(){
        return isNew;
    }
    
    public void setIsNew(boolean isNew){
        this.isNew = isNew;
    }
    
    public BooleanProperty deletedProperty(){
        return deleteProperty;
    }
    
    public boolean isEdit(){
        return degree % 360 != 0;
    }

    void deleteOrUndo() {
        deleteProperty.set(!deleteProperty.get());
    }

    void rotate() {
        try {
            images.set(indexProperty.get(), rotateImage(images.get(indexProperty.get())));
        } catch (IOException | KFZClient.KFZServerException ex) {
            Logger.getLogger(Viewer.class.getName()).log(Level.SEVERE, null, ex);
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
}
