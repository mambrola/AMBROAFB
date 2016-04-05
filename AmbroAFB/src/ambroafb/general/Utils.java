/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general;

import ambro.AMySQLChanel;
import ambroafb.AmbroAFB;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
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
     *
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
     * ქმნის stage-ს გადმოცემული პარამეტრების მიხედვით Murman:ჩავამატე
     * parameters
     *
     * @param name - fxml დოკუმენტის მისამართი
     * @param parameters
     * @param title - წარწერა, რომელიც stage-ს ექნება გაკეთებული
     * @param logo - icon სურათის მისამართი
     * @return
     * @throws IOException
     */
    public static Stage createStage(String name, HashMap<String, Object> parameters, String title, String logo) throws IOException {
        if (stages.containsKey(name)) {
            Stage stage = stages.get(name);
            stage.centerOnScreen();
            stage.toFront();
            return stage;
        }
        Stage stage = new Stage();
        Scene scene = createScene(name, parameters);
        stage.setScene(scene);
        addsFeaturesToStage(stage, name, title, logo);
        stages.put(name, stage);

        return stage;
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
     * ქმნის stage-ს გადმოცემული პარამეტრების მიხედვით Murman:ჩავამატე
     * parameters
     *
     * @param name - fxml დოკუმენტის მისამართი
     * @param parameters
     * @param title - წარწერა, რომელიც stage-ს ექნება გაკეთებული
     * @param logo - icon სურათის მისამართი
     * @param ownerStage- stage, რომლის შვილობილადაც შეიქმნება ეს ახალი stage
     * @return
     * @throws IOException
     */
    public static Stage createStage(String name, HashMap<String, Object> parameters, String title, String logo, Stage ownerStage) throws IOException {
        Stage stage = createStage(name, parameters, title, logo);
        if (stage.getOwner() == null) {
            stage.initOwner(ownerStage);
        }
        return stage;
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
     * ქმნის სცენას გადმოცემული პარამეთრების მიხედვით Murman:ჩავამატე parameters
     *
     * @param name - fxml დოკუმენტის მისამართი
     * @param parameters
     * @return
     * @throws IOException
     */
    public static Scene createScene(String name, HashMap<String, Object> parameters) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setResources(GeneralConfig.getInstance().getBundle());
        Parent root = loader.load(AmbroAFB.class.getResource(name).openStream());
        return new Scene(root);
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
        Parent root = loader.load(AmbroAFB.class.getResource(name).openStream());
        Scene scene = new Scene(root);
        scene.getProperties().put("controller", loader.getController());
        return scene;
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
        GeneralConfig.getInstance().logoutServerClient();
        try {
            if (AmbroAFB.socket != null) {
                AmbroAFB.socket.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        }
        Platform.exit();
        System.exit(0);
    }

    private static void saveConfigChanges() {
        GeneralConfig.getInstance().dump();
    }

    // ბაზასთან ურთიორთობის მეთოდები:
    // შეიძლება ღირდეს მათი ახალ ფაილში, მაგ. UtilsDB გატანა
    public static ArrayList<Object[]> getArrayListsByQueryFromDB(String query, String[] requestedColumnNames) {
        ArrayList<Object[]> arrayList = new ArrayList<>();
        try (Connection conn = GeneralConfig.getInstance().getConnectionToDB(); Statement statement = conn.createStatement()) {
            ResultSet resultSet = statement.executeQuery(query);
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            int columnCount = resultSetMetaData.getColumnCount();
            ArrayList<String> columnNames = new ArrayList<>();
            for (int i = 0; i < columnCount; i++) {
                columnNames.add(i, resultSetMetaData.getColumnName(i + 1));
            }
            while (resultSet.next()) {
                Object[] objectArray = new Object[columnCount];
                for (int c = 0; c < requestedColumnNames.length; c++) {
                    int appropriateIndex = columnNames.indexOf(requestedColumnNames[c]) + 1;
                    objectArray[c] = AMySQLChanel.extractFronResultSet(resultSet, appropriateIndex, resultSetMetaData.getColumnTypeName(appropriateIndex));
                }
                arrayList.add(objectArray);
            }
        } catch (SQLException | NullPointerException ex) {
            Platform.runLater(() -> {
                new AlertMessage(Alert.AlertType.ERROR, ex, Names.SQL_ERROR).showAlert();
            });
        }
        return arrayList;
    }

    public static ArrayList<Node> getFocusTraversableBottomChildren(Parent root) {
        ArrayList<Node> arrayList = new ArrayList<>();
        root.getChildrenUnmodifiable().stream().forEach((n) -> {
            if (((Parent) n).getChildrenUnmodifiable().isEmpty()) {
                if (n.isFocusTraversable()) {
                    arrayList.add(n);
                }
            } else {
                arrayList.addAll(getFocusTraversableBottomChildren((Parent) n));
            }
        });
        return arrayList;
    }

    public static String avoidNullAndReturnString(Object object) {
        return object == null ? "" : (String) object;
    }

    public static int avoidNullAndReturnInt(Object object) {
        return object == null ? 0 : (int) object;
    }

    public static boolean avoidNullAndReturnBoolean(Object object) {
        return object == null ? false : (boolean) object;
    }

    public static String readStream(InputStream stream) throws IOException {
        StringBuilder response = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(stream))) {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
        }

        return response.toString();
    }
}
