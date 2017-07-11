/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.JPEGFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.rendering.PDFRenderer;

/**
 *
 * @author tabramishvili
 */
public class PDFHelper implements Closeable {

    private final int DPI = 100;
    private final PDDocument doc;
    private final PDFRenderer rend;
    
    public PDFHelper(InputStream in) throws IOException {
        doc = PDDocument.load(in);
        rend = new PDFRenderer(doc);
    }

    public int getPageCount() {
        return doc.getNumberOfPages();
    }

    public Image getImage(int i) throws IOException {
        return SwingFXUtils.toFXImage(render(i), null);
    }

    public BufferedImage render(int i) throws IOException {
        return rend.renderImageWithDPI(i, DPI);
    }

    public List<Image> getImages() throws IOException {
        ArrayList<Image> list = new ArrayList<>();
        for (int page = 0; page < getPageCount(); ++page) {
            list.add(getImage(page));
        }
        return list;
    }

    public void replace(Image img, int index) throws IOException {
        replace(SwingFXUtils.fromFXImage(img, null), index);
    }

    public void replace(BufferedImage img, int index) throws IOException {
        PDPage old = doc.getPage(index);
        float width, height;
        if (img.getWidth() > img.getHeight()) {
            width = PDRectangle.LETTER.getHeight();
            height = PDRectangle.LETTER.getWidth();
        } else {
            height = PDRectangle.LETTER.getHeight();
            width = PDRectangle.LETTER.getWidth();
        }
        PDPage newPage = new PDPage(new PDRectangle(width, height));
        doc.getPages().insertAfter(newPage, old);
        doc.removePage(old);
        PDImageXObject pdImage = JPEGFactory.createFromImage(doc, img);
        try (PDPageContentStream contents = new PDPageContentStream(doc, newPage)) {
            contents.drawImage(pdImage, 0, 0, width, height);
        }
    }

    public byte[] getContent() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        doc.save(out);
        return out.toByteArray();
    }

    public PDDocument getDocument() {
        return doc;
    }

    @Override
    public void close() throws IOException {
        doc.close();
    }

}
