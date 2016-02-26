/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general;

import ambroafb.AmbroAFB;
import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author tabramishvili
 */
public class Utils {

    private static Logger logger;

    /**
     * აკეთებს exception-ის ლოგირებას კონსოლში და ფაილში სახელად 'error.log' 
     * რომელიც იქმნება პროექტის დირექტორიაში.
     * @param title
     * @param e 
     */
    public static void log(String title, Exception e) {
        if (logger == null) {
            logger = Logger.getLogger(AmbroAFB.class.getName());
            try {
                FileHandler file = new FileHandler("errors.log", true);
                logger.addHandler(file);
                SimpleFormatter simpleFormatter = new SimpleFormatter();
                file.setFormatter(simpleFormatter);

            } catch (IOException | SecurityException ex) {
                Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        logger.log(Level.SEVERE, title, e);
    }

    private static final Map<String, Stage> stages = new HashMap<>();

    /**
     * ქმნის stage-ს რომელზეც შემდგომში მარტივი იქნება სცენების შეცვლა
     *
     * @param name - fxml დოკუმენტის მისამართი
     * @param title - წარწერა, რომელიც stage-ს ექნება გაკეთებული
     * @param logo - icon სურათის მისამართი
     * @param owner - stage, რომლის შვილობილადაც შეიქმნება ეს ახალი stage
     * @return
     * @throws IOException
     */
    public static MultiSceneStage createMultiSceneStage(String name, String title, String logo, Stage owner) throws IOException {
        MultiSceneStage controller = null;

        if (stages.containsKey(name)) {
            controller = (MultiSceneStage) stages.get(name);
            controller.centerOnScreen();
            controller.toFront();
            return controller;
        }

        controller = new MultiSceneStage();
        Scene scene = createScene(name);
        controller.addScene(scene);
        addsFeaturesToStage(controller, name, title, logo);
        stages.put(name, controller);
        if (controller.getOwner() == null) {
            controller.initOwner(owner);
        }
        return controller;
    }

    /**
     * ქმნის stage-ს გადმოცემული პარამეტრების მიხედვით
     *
     * @param name - fxml დოკუმენტის მისამართი
     * @param title - წარწერა, რომელიც stage-ს ექნება გაკეთებული
     * @param logo - icon სურათის მისამართი
     * @return
     * @throws IOException
     */
    public static Stage createStage(String name, String title, String logo) throws IOException {
        if (stages.containsKey(name)) {
            Stage stage = stages.get(name);
            stage.centerOnScreen();
            stage.toFront();
            return stage;
        }

        Stage stage = new Stage();
        Scene scene = createScene(name);
        stage.setScene(scene);
        addsFeaturesToStage(stage, name, title, logo);
        stages.put(name, stage);

        return stage;
    }

    private static void addsFeaturesToStage(Stage stage, String name, String title, String logo) throws IOException {
        stage.setTitle(title);
        if (logo != null) {
            Image logoImage = new Image(Utils.class.getResourceAsStream(logo));
            stage.getIcons().add(logoImage);
        }

        GeneralConfig conf = GeneralConfig.getInstance();
        GeneralConfig.Sizes size = conf.getSizeFor(name);
        size = null; // მე ჩავსვი, დავადიზეიბლე კლიენტის მონაცემის გამოყენება
        if (size != null) {
            if (size.width > 0) {
                stage.setWidth(size.width);
            }
            if (size.height > 0) {
                stage.setHeight(size.height);
            }

            stage.setMaximized(size.maximized);
        }

        stage.widthProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            if (!stage.isMaximized()) {
                GeneralConfig.getInstance().setSizeFor(name, newValue.doubleValue(), stage.getHeight());
            }
        });

        stage.heightProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            if (!stage.isMaximized()) {
                GeneralConfig.getInstance().setSizeFor(name, stage.getWidth(), newValue.doubleValue());
            }
        });

        stage.maximizedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            GeneralConfig.getInstance().setSizeFor(name, newValue);
        });

        stage.setOnCloseRequest((WindowEvent event) -> {
            stages.remove(name);
            stage.close();
        });
    }

    /**
     * ქმნის stage-ს გადმოცემული პარამეტრების მიხედვით
     *
     * @param name - fxml დოკუმენტის მისამართი
     * @param title - წარწერა, რომელიც stage-ს ექნება გაკეთებული
     * @param logo - icon სურათის მისამართი
     * @param ownerStage- stage, რომლის შვილობილადაც შეიქმნება ეს ახალი stage
     * @return
     * @throws IOException
     */
    public static Stage createStage(String name, String title, String logo, Stage ownerStage) throws IOException {
        Stage stage = createStage(name, title, logo);
        if (stage.getOwner() == null) {
            stage.initOwner(ownerStage);
        }
        return stage;
    }

    /**
     * ქმნის სცენას გადმოცემული პარამეთრების მიხედვით
     *
     * @param name - fxml დოკუმენტის მისამართი
     * @return
     * @throws IOException
     */
    public static Scene createScene(String name) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setResources(GeneralConfig.getInstance().getBundle());
        
        System.out.println(name + " name: " + AmbroAFB.class.getResource(name));
        
        Parent root = loader.load(AmbroAFB.class.getResource(name).openStream());
        return new Scene(root);
    }

    /**
     * ინახავს მიმდინარე კონფიგურაციებს, თიშავს მიმდინარე აპლიკაციას და უშვებს
     * ახლიდან
     */
    public static void restart() {
        saveConfigChanges();

        StringBuilder cmd = new StringBuilder();
        cmd.append("\"").append(System.getProperty("java.home")).append(File.separator).append("bin").append(File.separator).append("java ").append("\" ");
        ManagementFactory.getRuntimeMXBean().getInputArguments().stream().forEach((jvmArg) -> {
            cmd.append(jvmArg).append(" ");
        });
        cmd.append("-cp \"").append(ManagementFactory.getRuntimeMXBean().getClassPath()).append("\" ");
        cmd.append(AmbroAFB.class.getName()).append(" ");

        System.out.println("restart: " + cmd);
        try {
            Runtime.getRuntime().exec(cmd.toString());
        } catch (IOException e) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, e);
        }

        exitApplication();
    }

    /**
     * ინახავს მიმდინარე კონფიგურაციებს და თიშავს აპლიკაციას
     */
    public static void exit() {
        saveConfigChanges();
        exitApplication();
    }

    /**
     * თიშავს აპლიკაციას კონფიგურაციების შენახვის გარეშე
     */
    public static void exitApplication() {
        try {
            if (AmbroAFB.socket != null)
                AmbroAFB.socket.close();
        } catch (IOException ex) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        }
        Platform.exit();
        System.exit(0);
    }

    private static void saveConfigChanges() {
        GeneralConfig.getInstance().dump();
    }
}
