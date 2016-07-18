/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general.image_gallery;

import ambroafb.general.KFZClient;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javafx.beans.property.BooleanProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Node;
import javafx.scene.image.Image;

/**
 *
 * @author dato
 */
public interface DocumentViewer {
    
    public Image getComponent();
    public void rotate();
    public void deleteOrUndo();
    public byte[] getContent();
    public void setIsNew(boolean isNew);
    public boolean isNew();
    public BooleanProperty deletedProperty();
    public boolean isEdit();
    public String getFullName();
    public boolean isNotValidDocument();
    public String getInvalidationMessage();
    
    public static final double FIT_WIDTH = 380;
    public static final double FIT_HEIGHT = 270;
    
    default public Image rotateImage(Image img) throws IOException, KFZClient.KFZServerException {
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
    
    static class  Factory {
        
        public static DocumentViewer getAppropriateViewer(InputStream stream, String fullName, int validPagesNumber){
            DocumentViewer viewer;
            if(fullName.endsWith("pdf")){
                viewer = new PDFViewer(stream, fullName);
            }
            else {
                viewer = new ImageViewer(stream, fullName);
            }
            return viewer;
        }
        
    }
}
