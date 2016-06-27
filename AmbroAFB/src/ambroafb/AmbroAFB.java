/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb;

import ambroafb.general.GeneralConfig;
import ambroafb.general.GeneralConfig.Sizes;
import ambroafb.general.Names;
import ambroafb.general.Utils;
import ambroafb.general.UtilsDB;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author tabramishvili
 */
public class AmbroAFB extends Application {

    public static Stage mainStage;
    public static ServerSocket socket;

    @Override
    public void start(Stage stage) {
        mainStage = stage;
        UtilsDB.getInstance().createLocalUsageTables();
        Utils.saveShowingStageByPath("main", mainStage);
        Scene scene = Utils.createScene(Names.MAIN_FXML, null);
        stage.setScene(scene);
        stage.setTitle(GeneralConfig.getInstance().getTitleFor(Names.MAIN_TITLE));
        if (Names.MAIN_LOGO != null) {
            try {
                stage.getIcons().add(new Image(Utils.class.getResource(Names.MAIN_LOGO).openStream()));
            } catch (IOException ex) { Logger.getLogger(AmbroAFB.class.getName()).log(Level.SEVERE, null, ex); }
        }

        GeneralConfig conf = GeneralConfig.getInstance();
        Sizes size = conf.getSizeFor(Names.MAIN_FXML);
        if (size != null) {
            if (size.width > 0) {
                stage.setWidth(size.width);
            }
            if (size.height > 0) {
                stage.setHeight(size.height);
            }

            stage.setMaximized(size.maximized);
        }

        stage.setOnCloseRequest((WindowEvent we) -> {
            Utils.saveSizeFor(mainStage);
            Utils.exit();
        });
        
        Utils.setSizeFor(mainStage);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            socket = new ServerSocket(20556, 0, InetAddress.getLoopbackAddress());
            new Thread(() -> {
                while (true) {
                    try (Socket peer = socket.accept()) {
                        int signal = peer.getInputStream().read();
                        if (signal == 1) {
                            Platform.runLater(() -> {
                                if (mainStage != null) {
                                    mainStage.toFront();
                                }
                            });
                        }
                    } catch (IOException ex) {
                        //Logger.getLogger(AmbroAFB.class.getName()).log(Level.SEVERE, null, ex);
                        break;
                    }
                }
            }).start();
            launch(args);
        } catch (IOException ex) {
            try (Socket peer = new Socket(InetAddress.getLoopbackAddress(), 20556)) {
                peer.getOutputStream().write(1);
            } catch (UnknownHostException ex1) {
                Logger.getLogger(AmbroAFB.class.getName()).log(Level.SEVERE, null, ex1);
            } catch (IOException ex1) {
                Logger.getLogger(AmbroAFB.class.getName()).log(Level.SEVERE, null, ex1);
            }
            Logger.getLogger(AmbroAFB.class.getName()).log(Level.SEVERE, null, ex);
            Utils.exitApplication();
        }
    }

}
