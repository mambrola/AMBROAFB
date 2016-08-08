/*
 * To setLanguage this license header, choose License Headers in Project Properties.
 * To setLanguage this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general;

import ambroafb.AmbroAFB;
import ambroafb.general.image_gallery.ImageGalleryController;
import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.apache.commons.collections.BidiMap;
import org.apache.commons.collections.bidimap.DualHashBidiMap;
import ambroafb.general.interfaces.Annotations.*;
import java.lang.reflect.Field;
import java.util.SortedSet;
import java.util.TreeSet;
import javafx.scene.control.TextField;
import java.util.regex.Pattern;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author tabramishvili
 */
public class Utils {

    private static Logger logger;
    private static Tooltip toolTip = new Tooltip();

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
        Scene scene = createScene(name, null);
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
        Scene scene = createScene(name, null);
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
     * ქმნის სცენას გადმოცემული პარამეთრების მიხედვით
     *
     * @param name - fxml დოკუმენტის მისამართი
     * @param controller
     * @return
     */
    public static Scene createScene(String name, Object controller) {
        Scene scene = null;
        FXMLLoader loader = new FXMLLoader();
        loader.setResources(GeneralConfig.getInstance().getBundle());
        if (controller != null) {
            loader.setController(controller);
        }
        try {
            Parent root = loader.load(AmbroAFB.class.getResource(name).openStream());
            scene = new Scene(root);
            scene.getProperties().put("controller", loader.getController());
        } catch (IOException ex) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        }
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

    /** a
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
        GeneralConfig.getInstance().dumpIntoPrefs();
    }

    public static ArrayList<Node> getFocusTraversableBottomChildren(Parent root) {
        ArrayList<Node> arrayList = new ArrayList<>();
        root.getChildrenUnmodifiable().stream().forEach((n) -> {
            boolean allow = !n.getStyleClass().contains("alwaysEnable") &&
                            !n.getClass().toString().contains("ImageView");
            if (allow){
                if (((Parent) n).getChildrenUnmodifiable().isEmpty()) {
                    if (n.isFocusTraversable()) {
                        arrayList.add(n);
                    }
                } else {
                    arrayList.addAll(getFocusTraversableBottomChildren((Parent) n));
                }
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

    public static StringBinding avoidNull(StringProperty prop) {
        return Bindings.when(prop.isNull()).then("").otherwise(prop);
    }

    private static final BidiMap bidmap = new DualHashBidiMap();

    public static int getSize() {
        return bidmap.size();
    }

    /**
     * The function saves stage and its path into bidirectional map
     *
     * @param path - owner path plus current stage local name.
     * @param stage - current stage
     */
    public static void saveShowingStageByPath(String path, Stage stage) {
        bidmap.put(path, stage);
    }

    /**
     * The function returns path for the given stage.
     *
     * @param stage - current stage
     * @return
     */
    public static String getPathForStage(Stage stage) {
        return (String) bidmap.getKey(stage);
    }

    /**
     * The function returns stage for the given path
     *
     * @param path - full path for stage (ex: main/Clients/Dialog).
     * @return
     */
    public static Stage getStageForPath(String path) {
        return (Stage) bidmap.get(path);
    }
    
    
    /**
     * The function removes stage from bidirectional map 
     * and use "removeAlsoSubstagesByPath" function for it.
     * @param stage - which must remove
     */
    public static void removeByStage(Stage stage){
        if (bidmap.containsKey(stage)){
            String path = (String) bidmap.getKey(stage);
            Utils.removeAlsoSubstagesByPath(path);
        }
    }
    
    /**
     * The function removes stage for the given path and also removes its
     * subStages. The function needs a helper collection to save removable
     * object in it, because of don't mess an iterator of map.
     *
     * @param path - full path for stage (ex: main/Clients/DialogOrFilter).
     */
    public static void removeAlsoSubstagesByPath(String path) {
        List<String> pathes = new ArrayList<>();
        bidmap.keySet().stream().forEach((key) -> {
            if (((String) key).startsWith(path)) {
                pathes.add((String) key);
            }
        });
        pathes.stream().forEach((currPath) -> {
            bidmap.remove((String) currPath);
        });
    }
    
    public static void hideChildrenStagesFor(Stage currStage, boolean minimized){
        bidmap.keySet().stream().forEach((key) -> {
            String currPath = (String)key;
            if (currPath.equals(getPathForStage(currStage))) return;
            String ownerPath = currPath.substring(0, currPath.lastIndexOf("/"));
            if (!ownerPath.equals(getPathForStage(AmbroAFB.mainStage))){
                if (bidmap.containsKey(ownerPath)){
                    Stage owner = getStageForPath(currPath);
                    owner.setIconified(minimized);
                }
            }
        });
    }
    
    /**
     * The function returns stage which associated for the given local name.
     *
     * @param owner - owner of finding stage
     * @param substageLocalName - local name of finding stage
     * @return
     */
    public static Stage getStageFor(Stage owner, String substageLocalName) {
        String ownerPath = getPathForStage(owner);
        String substagePath = ownerPath + substageLocalName;
        Stage substage = getStageForPath(substagePath);
        return substage;
    }

    /**
     * It can use instead of
     * '.getConstructor(EditorPanelable.class).newInstance(selected)'
     *
     * @param obj - we need this object instance.
     * @param constructorParams - 'Class' parameter for created specific
     * constructor
     * @param args - arguments for instance
     * @return
     */
    public static Object getInstanceOfClass(Class<?> obj, Class[] constructorParams, Object... args) {
        Object result = null;
        try {
            result = obj.getConstructor(constructorParams).newInstance(args);
        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    /**
     * It can use instead of 'Class.forName(getClassName("objectClass"))'
     *
     * @param name - name of class for example: Client, Country.
     * @return
     */
    public static Class getClassByName(String name) {
        Class result = null;
        try {
            result = Class.forName(name);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    /**
     * This class invokes a specific method ("methodName" parameter) for the
     * "owner" class.
     *
     * @param methodName - name of method in its class
     * @param argsTypes - arguments types
     * @param owner - class object which owned the method
     * @param object - object, witch (non!) static method will be invoke
     * @param argsValues - arguments value for method
     * @return - object will be null if we invokes a void type method, otherwise
     * will return a specific object of class.
     */
    public static Object getInvokedClassMethod(Class owner, String methodName, Class<?>[] argsTypes, Object object, Object... argsValues) {
        Object result = null;
        try {
            result = owner.getMethod(methodName, argsTypes).invoke(object, argsValues);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException ex) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public static boolean everyFieldContentIsValidFor(Object currentClassObject) {
        boolean result = true;
        Field[] fields = currentClassObject.getClass().getDeclaredFields();

        for (Field field : fields) {
            if (field.isAnnotationPresent(ContentNotEmpty.class)) {
                result = result && checkValidationForIsNotEmptyAnnotation(field, currentClassObject);
            }
            if (field.isAnnotationPresent(ContentMail.class)) {
                result = result && checkValidationForContentPatternAnnotation(field, currentClassObject);
            }
        }
        return result;
    }

    private static boolean checkValidationForIsNotEmptyAnnotation(Field field, Object classObject) {
        boolean result = true;
        ContentNotEmpty annotation = field.getAnnotation(ContentNotEmpty.class);

        Object[] typeAndContent = getNodesTypeAndContent(field, classObject);
        String text = (String) typeAndContent[1];
        if (annotation.value() && text.isEmpty()) {
            changeNodeVisualByEmpty((Node) typeAndContent[0], annotation.explain());
            result = false;
        } else {
            changeNodeVisualByEmpty((Node) typeAndContent[0], "");
        }
        return result;
    }

    private static boolean checkValidationForContentPatternAnnotation(Field field, Object classObject) {
        boolean result = true;
        ContentMail annotation = field.getAnnotation(ContentMail.class);

        Object[] typeAndContent = getNodesTypeAndContent(field, classObject);

        boolean validSyntax = Pattern.matches(annotation.valueForSyntax(), (String) typeAndContent[1]);
        boolean validAlphabet = Pattern.matches(annotation.valueForAlphabet(), (String) typeAndContent[1]);
        if (!validSyntax) {
            changeNodeVisualByEmpty((Node) typeAndContent[0], annotation.explainForSyntax());
            result = false;
        } else if (!validAlphabet) {
            changeNodeVisualByEmpty((Node) typeAndContent[0], annotation.explainForAlphabet());
            result = false;
        } else {
            changeNodeVisualByEmpty((Node) typeAndContent[0], "");
        }
        return result;
    }

    private static Object[] getNodesTypeAndContent(Field field, Object classObject) {
        Object[] results = new Object[2];
        try {
            boolean accessible = field.isAccessible();
            field.setAccessible(true);

            if (field.getType().toString().contains("TextField")) {
                results[0] = (TextField) field.get(classObject);
                results[1] = ((TextField) results[0]).getText();
            }

            field.setAccessible(accessible);
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return results;
    }

    private static final Map<Label, Paint> labels_colors_map = new HashMap<>();

    private static void changeNodeVisualByEmpty(Node node, String text) {
        Parent parent = node.getParent();
        Label nodeTitleLabel = (Label) parent.lookup(".validationMessage");

        if (text.isEmpty()) {
            if (labels_colors_map.containsKey(nodeTitleLabel)) {// This order of 'if' statements is correct!
                nodeTitleLabel.setTextFill(labels_colors_map.get(nodeTitleLabel));
                labels_colors_map.remove(nodeTitleLabel);
                Tooltip.uninstall(nodeTitleLabel, toolTip);
            }
        } else {
            node.requestFocus();
            toolTip.setText(text);
            toolTip.setStyle("-fx-background-color: gray; -fx-font-size: 8pt;");
            Tooltip.install(nodeTitleLabel, toolTip);
            labels_colors_map.putIfAbsent(nodeTitleLabel, nodeTitleLabel.getTextFill());
            nodeTitleLabel.setTextFill(Color.RED);
        }
    }

    public static void saveSizeFor(Stage stage) {
        try {
            String path = getPathForStage(stage);
            JSONObject jsonForStageSize = new JSONObject();
            jsonForStageSize.put("width", stage.getWidth());
            jsonForStageSize.put("height", stage.getHeight());
            jsonForStageSize.put("isMaximized", stage.isMaximized());
            GeneralConfig.prefs.put("stage_size_" + path, jsonForStageSize.toString());
        } catch (JSONException ex) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void setSizeFor(Stage stage) {
        String path = getPathForStage(stage);
        try {
            String json_str = GeneralConfig.prefs.get("stage_size_" + path, null);
            if (json_str == null) {
                return;
            }
            JSONObject json = new JSONObject(json_str);
            if (json.getBoolean("isMaximized")) {
                stage.setMaximized(true);
            } else {
                stage.setWidth(json.getDouble("width"));
                stage.setHeight(json.getDouble("height"));
            }
        } catch (JSONException ex) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void callGallerySendMethod(String sendingURLParameter, Object currSceneController) {
        try {
            Field[] fields = currSceneController.getClass().getDeclaredFields();
            for (Field field : fields) {
                boolean oldValue = field.isAccessible();
                field.setAccessible(true);
                if (field.isAnnotationPresent(FXML.class)) {
                    if (field.get(currSceneController) instanceof ImageGalleryController) {
                        ImageGalleryController controller = (ImageGalleryController)field.get(currSceneController);
                        controller.sendDataToServer(sendingURLParameter);
                    }
                }
                field.setAccessible(oldValue);
            }
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void closeStageAndItsChildrenStages(Stage currStage){
        closeOnlyChildrenStages(currStage);
        if (currStage.isShowing()){
            currStage.close();
        }
    }

    /**
     * The function closes children stages and after that it close the given stage.
     * @param currStage Current stage.
     * @return True if current stage must close, false otherwise.
     */
    public static boolean closeOnlyChildrenStages(Stage currStage) {
        boolean closePermissionForStage = true;
        String currStagePath = (String) bidmap.getKey(currStage);
        List<String> childrenPath = getFirstLevelChildrenFor(currStagePath);
        if (childrenPath.isEmpty()) {
            if (currStagePath.endsWith(Names.LEVEL_FOR_PATH)) {
                if (currStage.getOnCloseRequest() == null) {
                    currStage.close();
                } else {
                    currStage.getOnCloseRequest().handle(null);
                }
                Object controller = currStage.getScene().getProperties().get("controller");
                closePermissionForStage = (Boolean) getInvokedClassMethod(controller.getClass(), "getPermissionToClose", null, controller);
                System.out.println("permission: " + closePermissionForStage);
            }
        }
        else {
            for (String childPath : childrenPath) {
                closePermissionForStage = closePermissionForStage && closeOnlyChildrenStages((Stage) bidmap.get(childPath));
                Stage childStage = (Stage) getStageForPath(childPath);
                if (childStage.isShowing() && closePermissionForStage){
                    childStage.close();
                }
            }
        }
        return closePermissionForStage;
    }
    
    private static List<String> getFirstLevelChildrenFor(String ownerPath){
        List<String> children = new ArrayList<>();
        SortedSet<Object> sortedKeys = new TreeSet<>(bidmap.keySet());
        sortedKeys.stream().forEach((key) -> {
            String path = (String) key;
            String pathAfterFirstSlash = StringUtils.substringAfter(path, ownerPath + "/");
            if (!path.equals(ownerPath) && path.startsWith(ownerPath) &&
                !pathAfterFirstSlash.contains("/") && ((Stage) bidmap.get(path)).isShowing()){

                children.add(path);
            }
        });
        return children;
    }
}
