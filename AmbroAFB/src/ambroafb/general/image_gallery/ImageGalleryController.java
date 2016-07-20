/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general.image_gallery;

import ambroafb.general.GeneralConfig;
import ambroafb.general.KFZClient;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.controlsfx.control.MaskerPane;
import org.json.JSONArray;
import org.json.JSONException;

/**
 * FXML Controller class
 *
 * @author dato
 */
public class ImageGalleryController implements Initializable {

    @FXML
    private VBox imagesGalleryRoot;
//    @FXML
//    private Image galleryImage;
    
    @FXML
    private Button deleteOrUndo, rotateToRight, upload;

    @FXML
    private ImageView galleryImageView;
    @FXML
    private ImageView deletedImageView;

    @FXML
    private HBox imageButtonsHBox, galleryImageFrame;

    @FXML
    private MaskerPane masker;
    @FXML
    private MagnifierPane magnifier;
    
    @FXML
    private VBox pdfPagingPane;
    @FXML
    private Region pagingRegion;
    @FXML
    private Button up, down;
    @FXML
    private Label page;
    
    private ObservableList<String> datesSliderElems;
    private Map<String, DocumentViewer> viewersMap;
    private Calendar calendar;
    private DateFormat formatter;
    private FileChooser fileChooser;
    private String serviceURLPrefix;
    private String parameterUpload;
    private String parameterDownload;
    private String defaultFileChooserPath;
    private MessageSlider msgSlider;

    private static final String GALLERY_DELETE_BUTTON_IMAGE_NAME = "/images/delete_128.png";
    private static final String GALLERY_UNDO_BUTTON_IMAGE_NAME = "/images/undo_128.png";
    private static final String UPLOAD_DIRECTORY_PATH = "upload_directory";
    private StringConverter<String> converter;
    
    
    private Map<String, Viewer> viewers;
    
    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        defaultFileChooserPath = GeneralConfig.prefs.get(UPLOAD_DIRECTORY_PATH, null);
        viewersMap = new HashMap<>();
        viewers = new HashMap<>();
        datesSliderElems = FXCollections.observableArrayList();
        calendar = Calendar.getInstance();
        formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss.SSS");
        fileChooser = new FileChooser();
        ExtensionFilter filter = new ExtensionFilter("Images files (*.png, *.jpg, *.pdf)", "*.png", "*.jpg", "*.pdf");
        fileChooser.getExtensionFilters().add(filter);
        converter = new ImageGalleryStringConverter();
        msgSlider = new MessageSlider(datesSliderElems, converter, rb);
        imageButtonsHBox.getChildren().add(msgSlider);
        
        pdfPagingPane.setPickOnBounds(false);
        pagingRegion.setPickOnBounds(false);
        
        msgSlider.indexProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            showImage(serviceURLPrefix, newValue.intValue());
        });
        Platform.runLater(()->{
            doAfterInicialize(new Image(GeneralConfig.getInstance().getTitleFor("def_image_url")));
        });
    }
    
    private void doAfterInicialize(Image image){
        galleryImageView.setFitWidth(0);
        galleryImageView.setFitHeight(0);
        if(image.getWidth() > imagesGalleryRoot.getWidth()){
            galleryImageView.setFitWidth(imagesGalleryRoot.getWidth());
        }
        if(image.getHeight() > imagesGalleryRoot.getHeight() - imageButtonsHBox.getHeight()){
            galleryImageView.setFitHeight(imagesGalleryRoot.getHeight() - imageButtonsHBox.getHeight());
        }
        galleryImageView.setImage(image);
   }
    
    private class ImageGalleryStringConverter extends StringConverter<String> {
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
        
    }
        
    private Map<Integer, Object> threadMap = new ConcurrentHashMap<>();
    
    private void showImage(String urlPrefix, int index) {
        if (index >= 0 && index < datesSliderElems.size()) {
            String fullName = datesSliderElems.get(index);
            Viewer currViewer = viewers.get(fullName);
            if (currViewer == null){
                if (!threadMap.containsKey(index)) {
                    Thread t = new Thread(new DataDownloadRunnable(fullName, index));
//                    stopThreadFor(index - 1);
//                    stopThreadFor(index + 1);
                    Object lock = new Object();
                    //ThreadComplete compl = new ThreadComplete(t, lock);
                    threadMap.put(index, lock);
                    t.start();
                    System.out.println("daistarta " + index + " thread.");
                } else{
                    System.out.println("aq unda gavacocxlot " + index + " thread.");
                    threadMap.get(index).notify(); // .getLock().notify();
                }
            }
            else {
//                System.out.println("else-shia");
                showViewerComponentOnScene(currViewer);
            }
        }
    }
    
    private final Object ob = new Object();
    
    private void stopThreadFor(int index){
        if (threadMap.containsKey(index)) {
//            threadMap.get(index).getThread().interrupt();
//            try {
//                threadMap.get(index).getLock().wait();
//            } catch (InterruptedException ex) {
//                Logger.getLogger(ImageGalleryController.class.getName()).log(Level.SEVERE, null, ex);
//            }
        }
    }    
    
    private void showViewerComponentOnScene(Viewer viewer){
        doAfterInicialize(viewer.getImage());
        pdfPagingPane.visibleProperty().unbind();
        pdfPagingPane.visibleProperty().bind(viewer.pdfProperty());
        if (viewer.pdfProperty().get()){
            up.visibleProperty().unbind();
            up.visibleProperty().bind(Bindings.createBooleanBinding(() -> {
                return viewer.indexProperty().get() > 0;
            }, viewer.indexProperty()));

            down.visibleProperty().unbind();
            down.visibleProperty().bind(Bindings.createBooleanBinding(() -> {
                return viewer.indexProperty().get() + 1 < viewer.getPages();
            }, viewer.indexProperty()));
            
            page.textProperty().unbind();
            page.textProperty().bind(Bindings.createStringBinding(() -> {
                return "" + (viewer.indexProperty().get() + 1);
            }, viewer.indexProperty()));
        }
        
        
        deletedImageView.visibleProperty().unbind();
        deletedImageView.visibleProperty().bind(viewer.deletedProperty());
        ImageView icon = (ImageView) deleteOrUndo.getGraphic();
        icon.imageProperty().unbind();
        icon.imageProperty().bind(Bindings.createObjectBinding(() -> {
            String url = viewer.deletedProperty().get() ? GALLERY_UNDO_BUTTON_IMAGE_NAME : GALLERY_DELETE_BUTTON_IMAGE_NAME;
            return new Image(getClass().getResourceAsStream(url));
        }, viewer.deletedProperty()));
    }
    
    public void setUploadDataURL(String serviceURLPrefix, String parameterUpload, String parameterDownload) {
        this.serviceURLPrefix = serviceURLPrefix;
        this.parameterUpload = parameterUpload;
        this.parameterDownload = parameterDownload;
    }

    public void downloadData() {
        try {
            String imagesNames = GeneralConfig.getInstance().getServerClient().get(serviceURLPrefix + parameterDownload);
            JSONArray namesJson = new JSONArray(imagesNames);
            for (int i = 0; i < namesJson.length(); i++) {
                String fullName = namesJson.getString(i);
                datesSliderElems.add(fullName);
            }
            if (datesSliderElems != null && !datesSliderElems.isEmpty()) {
                SortedList<String> sorted = datesSliderElems.sorted(Comparator.<String>naturalOrder());
                FXCollections.copy(datesSliderElems, sorted);
                msgSlider.setValueOn(datesSliderElems.size() - 1);
            }
        } catch (KFZClient.KFZServerException ex) {
            System.out.println("ex code: " + ex.getStatusCode() + "  User has not images.");
        } catch (JSONException | IOException ex) {
            Logger.getLogger(ImageGalleryController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void deleteImage(ActionEvent event) {
        String fullName = msgSlider.getValue();
        Viewer viewer = viewers.get(fullName);
        if (viewer != null) {
            viewer.deleteOrUndo();
        }
    }

    @FXML
    private void uploadFile(ActionEvent event) {
        Stage owner = (Stage) galleryImageFrame.getScene().getWindow();
        if (defaultFileChooserPath != null){
            fileChooser.setInitialDirectory(new File(defaultFileChooserPath));
        }
        List<File> files = fileChooser.showOpenMultipleDialog(owner);
        if (files == null) return;
        
        defaultFileChooserPath = files.get(files.size() - 1).getParentFile().getPath();
        GeneralConfig.prefs.put(UPLOAD_DIRECTORY_PATH, defaultFileChooserPath);
        
        new Thread(() -> {
            Platform.runLater(()->{
                masker.setVisible(true);
            });
            files.stream().forEach((file) -> {
                try {
                    String fileName = file.getName();
                    InputStream stream = new FileInputStream(file);
                    Viewer viewer = new Viewer(stream, fileName.substring(fileName.lastIndexOf(".")).toLowerCase().equals(".pdf"));
                    viewer.setIsNew(true);
                    Platform.runLater(() -> {
                        proccessViewer(viewer, fileName);
                    });
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(ImageGalleryController.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            Platform.runLater(()->{
                masker.setVisible(false);
            });
        }).start();
    }
    
    /** 
     * The method count date and set index of images slider. This action cause to show new uploading file.
     * @param viewer
     * @param fileName 
     */
    private void proccessViewer(DocumentViewer viewer, String fileName){
        Long currTime = new Date().getTime();
        String fileFullName = "_" + currTime + fileName.substring(fileName.lastIndexOf("."));
        viewersMap.put(fileFullName, viewer);
        datesSliderElems.add(fileFullName);
        msgSlider.setValueOn(datesSliderElems.size() - 1);
    }
    
    private void proccessViewer(Viewer viewer, String fileName){
        Long currTime = new Date().getTime();
        String fileFullName = "_" + currTime + fileName.substring(fileName.lastIndexOf("."));
        viewers.put(fileFullName, viewer);
        datesSliderElems.add(fileFullName);
        msgSlider.setValueOn(datesSliderElems.size() - 1);
    }
    
    @FXML
    private void rotate(ActionEvent event) {
        String fullName = msgSlider.getValue();
        Viewer viewer = viewers.get(fullName);
        if (viewer != null) {
            viewer.rotate();
            doAfterInicialize(viewer.getImage());
        }
    }
    
    @FXML
    private void up(ActionEvent event){
        String value = msgSlider.getValue();
        Viewer viewer = viewers.get(value);
        viewer.indexProperty().set(viewer.indexProperty().get() - 1);
        doAfterInicialize(viewer.getImage());
    }
    
    @FXML
    private void down(ActionEvent event){
        String value = msgSlider.getValue();
        Viewer viewer = viewers.get(value);
        viewer.indexProperty().set(viewer.indexProperty().get() + 1);
        doAfterInicialize(viewer.getImage());
    }
    
    @FXML
    private void enterMouse(MouseEvent event){
        magnifier.hide();
    }

    /**
     * The method sends image gallery data to server in thread.
     * So before this method, image gallery controller must known service URL prefix 
     * and parameter by setUploadDataURL method.
     */
    public void sendDataToServer() {
        new Thread(() -> {
            viewers.keySet().stream().forEach((key) -> {
                Viewer viewer = viewers.get(key);
                if (viewer != null) {
                    byte[] data = viewer.getContent();
                    try {
                        if (viewer.deletedProperty().get()) {
                            GeneralConfig.getInstance().getServerClient().call(serviceURLPrefix + key, "DELETE", null);
                        } else if (viewer.isNew()) {
                            GeneralConfig.getInstance().getServerClient().post(
                                    serviceURLPrefix + parameterUpload + key.substring(key.lastIndexOf(".") + 1),
                                    Base64.getEncoder().encodeToString(data)
                            );
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
        }).start();
    }

    /**
     * The method returns true if user upload new file, rotate or delete existed. 
     * False if state does not change.
     * @return 
     */
    public boolean anyViewerChanged(){
        boolean result = false;
        for (DocumentViewer viewer : viewersMap.values()) {
            if (viewer == null) continue;
            result = viewer.isNew() || viewer.isEdit()|| viewer.deletedProperty().get();
            if (result) break;
        }
        return result;
    }
    
    private class DataDownloadRunnable implements Runnable{

        private final String fullName;
        private final int index;
        
        public DataDownloadRunnable(String fullName, int index){
            this.fullName = fullName;
            this.index = index;
        }
        
        @Override
        public void run() {
            Platform.runLater(() -> {
                masker.setVisible(true);
            });

            try {
                HttpURLConnection con = GeneralConfig.getInstance().getServerClient().createConnection(serviceURLPrefix + fullName);
                Viewer newViewer = new Viewer(con.getInputStream(), fullName.endsWith(".pdf"));
                
                Object lock = threadMap.get(index); // .getLock();
                synchronized(lock){
                    try {
                        System.out.println("daiwyebs mocdas cotaxani " + index + " thread....");
                        lock.wait(1000);
//                        while(threadMap.get(index).getThread().isInterrupted()){
                        if(msgSlider.indexProperty().get() != index){
                            System.out.println("kai xani icdis " + index + " thread.");
                            lock.wait();
                        }
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ImageGalleryController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                
                Platform.runLater(() -> {
                    viewers.put(fullName, newViewer);
                    showViewerComponentOnScene(newViewer);
                    masker.setVisible(false);
                });
            } catch (IOException ex) {
                Logger.getLogger(ImageGalleryController.class.getName()).log(Level.SEVERE, null, ex);
            }
            threadMap.remove(index);
        }
    
    }
    
    private class ThreadComplete {
        
        private final Thread thread;
        private final Object lock;
        
        public ThreadComplete(Thread thread, Object lock){
            this.thread = thread;
            this.lock = lock;
        }
        
        public Object getLock(){
            return lock;
        }
        
        public Thread getThread(){
            return thread;
        }
    }
}
