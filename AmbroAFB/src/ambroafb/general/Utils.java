/*
 * To setLanguage this license header, choose License Headers in Project Properties.
 * To setLanguage this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general;
    
import ambro.ADatePicker;
import ambroafb.AmbroAFB;
import ambroafb.currencies.CurrencyComboBox;
import ambroafb.general.Names.EDITOR_BUTTON_TYPE;
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
import ambroafb.general.interfaces.Annotations.*;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.general.mapeditor.MapEditorComboBox;
import java.lang.reflect.Field;
import javafx.scene.control.TextField;
import java.util.regex.Pattern;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import org.apache.commons.lang3.StringUtils;

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
    
    /**
     * The method converts string to Integer. If String is incorrect, it returns  -1 (minus 1).
     * @param str String which must be converted to Integer.
     * @return -1 if parameter is incorrect, otherwise - appropriate Integer value.
     */
    public static int getIntValueFor(String str){
        int result = -1;
        try {
            result = Integer.parseInt(str);
        } catch (Exception ex){ }
        return result;
    }
    
    /**
     * The method converts string to Double. If String is incorrect, it returns  -1 (minus 1).
     * @param str String which must be converted to Double.
     * @return -1 if parameter is incorrect, otherwise - appropriate Double value.
     */
    public static double getDoubleValueFor(String str){
        double result = -1;
        try {
            result = Double.parseDouble(str);
        } catch(Exception ex){ }
        return result;
    }
    
    /**
     * The method compares two lists.
     * Note: If they are equal, then they must have same order of elements.
     * @param first The first list
     * @param second The second lists.
     * @return If they have another sizes or i-th element from first does not equal 
     *          i-th element from second, then method return false. True - otherwise.
     */
    public static boolean compareLists(List<?> first, List<?> second) {
        if (first.size() != second.size()) return false;
        for (int i = 0; i < first.size(); i++) {
            if (!first.get(i).equals(second.get(i))) return false;
        }
        return true;
    }
    
//
//    private static final BidiMap bidmap = new DualHashBidiMap();
//
//    public static int getSize() {
//        return bidmap.size();
//    }
//
//    /**
//     * The function saves child stage with path  ( path(owner).concat(childName) ). 
//     * @param owner owner stage
//     * @param childName child stage associated name
//     * @param child child
//     */
//    public static void registerStageByOwner(Stage owner, String childName, Stage child){
//        String path = childName;
//        if (owner != null && bidmap.containsValue(owner)){
//            path = getPathForStage(owner) + "/" + childName;
//        }
//        bidmap.put(path, child);
//    }
//
//    /**
//     * The function returns path for the given stage.
//     *
//     * @param stage - current stage
//     * @return
//     */
//    public static String getPathForStage(Stage stage) {
//        return (String) bidmap.getKey(stage);
//    }
//
//    /**
//     * The function returns stage for the given path
//     *
//     * @param path - full path for stage (ex: main/Clients/Dialog).
//     * @return
//     */
//    public static Stage getStageForPath(String path) {
//        return (Stage) bidmap.get(path);
//    }
//    
//    
//    /**
//     * The function removes stage from bidirectional map 
//     * and also removes its children stages if they exists.
//     * @param stage which must remove
//     */
//    public static void removeByStage(Stage stage){
//        if (bidmap.containsValue(stage)){
//            Utils.removeOnlySubstagesFor(stage);
//            String path = (String) bidmap.getKey(stage);
//            bidmap.remove(path);
//        }
//    }
//    
//    /**
//     * The function removes only children stages of given stage from bidirectional map if they exists.
//     * @param stage which children must be remove.
//     */
//    public static void removeOnlySubstagesFor(Stage stage) {
//        String path = (String) bidmap.getKey(stage);
//        List<String> pathes = new ArrayList<>();
//        bidmap.keySet().stream().forEach((key) -> {
//            if (((String) key).startsWith(path)) {
//                pathes.add((String) key);
//            }
//        });
//        pathes.stream().forEach((currPath) -> {
//            bidmap.remove((String) currPath);
//            System.out.println("amoishala: " + currPath);
//        });
//    }
//    
////    public static void iconifiedChildrenStagesFor(Stage currStage, boolean isMinimize){
////        List<String> directChildrenPathes = getDirectChildrenStagesPathesOf(currStage);
////        directChildrenPathes.stream().map((childPath) -> getStageForPath(childPath)).forEach((directChildStage) -> {
////            System.out.println(getPathForStage(currStage) + " gaushva recursia " + getPathForStage(directChildStage) + "-ze" );
////            iconifiedDirectChild(directChildStage, isMinimize);
////        });
////        System.out.println("daamtavra recursia " + getPathForStage(currStage) + "-ma tavis shvilebze");
////    }
////    
////    private static void iconifiedDirectChild(Stage currStage, boolean isMinimize){
////        List<String> directChildrenPathes = getDirectChildrenStagesPathesOf(currStage);
////        directChildrenPathes.stream().map((childPath) -> getStageForPath(childPath)).forEach((directChildStage) -> {
////            System.out.println(getPathForStage(currStage) + " gaushva recursia " + getPathForStage(directChildStage) + "-ze" );
////            iconifiedDirectChild(directChildStage, isMinimize);
////        });
////        currStage.setIconified(isMinimize);
////    }
////    
////    public static List<String> getDirectChildrenStagesPathesOf(Stage currStage){
////        String currentStagePath = getPathForStage(currStage);
////        List<String> directChildrenPathes = (List<String>) bidmap.keySet().stream()
////                                                                            .filter((key) -> isDirectChildPath((String)key, currentStagePath, "/"))
////                                                                            .collect(Collectors.toList());
////        return directChildrenPathes;
////    }
//    
//    private static boolean isDirectChildPath(String childPath, String ownerPath, String delimiter){
//        return  childPath.startsWith(ownerPath) &&
//                !childPath.equals(ownerPath)   &&
//                StringUtils.countMatches(childPath, delimiter) - 1 == StringUtils.countMatches(ownerPath, delimiter);
//    }
//    
//    /**
//     * The function returns stage which associated for the given local name.
//     *
//     * @param owner - owner of finding stage
//     * @param substageLocalName - local name of finding stage
//     * @return
//     */
//    public static Stage getStageFor(Stage owner, String substageLocalName) {
//        String ownerPath = getPathForStage(owner);
//        String substagePath = ownerPath + substageLocalName;
//        Stage substage = getStageForPath(substagePath);
//        return substage;
//    }
    
//    public static void callGallerySendMethod(String sendingURLParameter, Object currSceneController) {
//        try {
//            Field[] fields = currSceneController.getClass().getDeclaredFields();
//            for (Field field : fields) {
//                boolean oldValue = field.isAccessible();
//                field.setAccessible(true);
//                if (field.isAnnotationPresent(FXML.class)) {
//                    if (field.get(currSceneController) instanceof ImageGalleryController) {
//                        ImageGalleryController controller = (ImageGalleryController)field.get(currSceneController);
//                        controller.sendDataToServer(sendingURLParameter);
//                    }
//                }
//                field.setAccessible(oldValue);
//            }
//        } catch (IllegalArgumentException | IllegalAccessException ex) {
//            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }

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
        } catch (ClassNotFoundException ex) { }
        return result;
    }

    /**
     * This class invokes a specific method ("methodName" parameter) for the
     * "owner" class.
     *
     * @param owner Class object which owned the method
     * @param methodName Name of method in its class
     * @param argsTypes Arguments types
     * @param object Object, witch (non!) static method will be invoke
     * @param argsValues Arguments value for method
     * @return object Will be null if we invokes a void type method, otherwise
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
    
    /**
     * The method provides valid content into given TextField. 
     * If content new value does not match to pattern, then content become old value.
     * @param textField Field that must validation.
     * @param pattern The correct syntax for textField.
     */
    public static void validateTextFieldContentListener(TextField textField, String pattern){
        textField.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (newValue != null && !newValue.isEmpty()){
                if (!Pattern.matches(pattern, newValue))
                    textField.setText(oldValue);
            }
        });
    }

    public static boolean everyFieldContentIsValidFor(Object currentClassObject, EDITOR_BUTTON_TYPE type) {
        boolean result = true;
        Field[] fields = currentClassObject.getClass().getDeclaredFields();

        for (Field field : fields) {
            if (field.isAnnotationPresent(ContentNotEmpty.class)) {
                result = result && checkValidationForIsNotEmptyAnnotation(field, currentClassObject);
            }
            if (field.isAnnotationPresent(ContentMail.class)) {
                result = result && checkValidationForContentMailAnnotation(field, currentClassObject);
            }
            if (field.isAnnotationPresent(ContentTreeItem.class)){
                result = result && checkValidationForContentTreeItemAnnotation(field, currentClassObject, type);
            }
            if (field.isAnnotationPresent(ContentPattern.class)){
                result = result && checkValidationForContentPatternAnnotation(field, currentClassObject);
            }
            if (field.isAnnotationPresent(ContentMapEditor.class)){
                result = result && checkValidationForContentMapEditorAnnotation(field, currentClassObject);
            }
        }
        return result;
    }

    private static boolean checkValidationForIsNotEmptyAnnotation(Field field, Object classObject) {
        boolean result = true;
        ContentNotEmpty annotation = field.getAnnotation(ContentNotEmpty.class);

        Object[] typeAndContent = getNodesTypeAndContent(field, classObject);
        String value = (String) typeAndContent[1];
        if (annotation.value() && (value == null || value.isEmpty())) {
            changeNodeTitleLabelVisual((Node) typeAndContent[0], annotation.explain());
            result = false;
        } else {
            changeNodeTitleLabelVisual((Node) typeAndContent[0], "");
        }
        return result;
    }

    private static boolean checkValidationForContentMailAnnotation(Field field, Object currSceneController) {
        boolean result = true;
        ContentMail annotation = field.getAnnotation(ContentMail.class);

        Object[] typeAndContent = getNodesTypeAndContent(field, currSceneController);

        boolean validSyntax = Pattern.matches(annotation.valueForSyntax(), (String) typeAndContent[1]);
        boolean validAlphabet = Pattern.matches(annotation.valueForAlphabet(), (String) typeAndContent[1]);
        if (!validSyntax) {
            changeNodeTitleLabelVisual((Node) typeAndContent[0], annotation.explainForSyntax());
            result = false;
        } else if (!validAlphabet) {
            changeNodeTitleLabelVisual((Node) typeAndContent[0], annotation.explainForAlphabet());
            result = false;
        } else {
            changeNodeTitleLabelVisual((Node) typeAndContent[0], "");
        }
        return result;
    }
    
    private static boolean checkValidationForContentTreeItemAnnotation(Field field, Object currSceneController, EDITOR_BUTTON_TYPE  type){
        boolean result = true;
        ContentTreeItem annotation = field.getAnnotation(ContentTreeItem.class);
        Object[] typeAndContent = getNodesTypeAndContent(field, currSceneController);
        String content = (String)typeAndContent[1];
        
        if (content.length() != Integer.parseInt(annotation.valueForLength())){
            changeNodeTitleLabelVisual((Node)typeAndContent[0], annotation.explainForLength() + annotation.valueForLength());
            result = false;
        }
        else if (!Pattern.matches(annotation.valueForSyntax(), content)){
            changeNodeTitleLabelVisual((Node)typeAndContent[0], annotation.explainForSyntax());
            result = false;
        }
        else {
            EditorPanelable newPanelableObject = (EditorPanelable) getInvokedClassMethod(currSceneController.getClass(), "getNewEditorPanelable", null, currSceneController);
            Object contr = getInvokedClassMethod(currSceneController.getClass(), "getOwnerController", null, currSceneController);
            // already exist this item for this code:
            if ((Boolean)getInvokedClassMethod(contr.getClass(), "accountAlreadyExistForCode", new Class[]{EditorPanelable.class, EDITOR_BUTTON_TYPE.class}, contr, newPanelableObject, type)){
                changeNodeTitleLabelVisual((Node)typeAndContent[0], annotation.explainForExists());
                result = false;
            }
            // item has not a parent:
            else if (!(Boolean)getInvokedClassMethod(contr.getClass(), "accountHasParent", new Class[]{String.class}, contr, content)){
                changeNodeTitleLabelVisual((Node)typeAndContent[0], annotation.explainForHasNotParent());
                result = false;
            }
            else {
                changeNodeTitleLabelVisual((Node) typeAndContent[0], "");
            }
        }
        return result;
    }
    
    private static boolean checkValidationForContentPatternAnnotation(Field field, Object currSceneController){
        boolean result = true;
        ContentPattern annotation = field.getAnnotation(ContentPattern.class);
        
        Object[] typeAndContent = getNodesTypeAndContent(field, currSceneController);
        
        if (!Pattern.matches(annotation.value(), (String)typeAndContent[1])){
            changeNodeTitleLabelVisual((Node) typeAndContent[0], annotation.explain());
            result = false;
        } else {
            changeNodeTitleLabelVisual((Node) typeAndContent[0], "");
        }
        return result;
    }
    
    private static boolean checkValidationForContentMapEditorAnnotation(Field field, Object currSceneController){
        boolean result = true;
        ContentMapEditor annotation = field.getAnnotation(ContentMapEditor.class);
        Object[] typeAndContent = getNodesTypeAndContent(field, currSceneController);
        MapEditorComboBox mapEditorComboBox = (MapEditorComboBox) typeAndContent[0];
        String editorContent = (String) typeAndContent[1];
        String keyPart = StringUtils.substringBefore(editorContent, mapEditorComboBox.getDelimiter()).trim();
        String valuePart = StringUtils.substringAfter(editorContent, mapEditorComboBox.getDelimiter()).trim();

        boolean keyMatch = Pattern.matches(mapEditorComboBox.getKeyPattern(), keyPart);
        boolean valueMatch = Pattern.matches(mapEditorComboBox.getValuePattern(), valuePart);
        String explain;
        if (!keyMatch || !valueMatch){
            explain = (!keyMatch) ? annotation.explainKey() : annotation.explainValue();
            changeNodeTitleLabelVisual(mapEditorComboBox, explain);
            result = false;
        }
        else if ((keyPart.isEmpty() && !valuePart.isEmpty()) || (!keyPart.isEmpty() && valuePart.isEmpty())){
            explain = annotation.explainEmpty();
            changeNodeTitleLabelVisual(mapEditorComboBox, explain);
            result = false;
        } else {
            changeNodeTitleLabelVisual(mapEditorComboBox, "");
        }
        return result;
    }

    private static Object[] getNodesTypeAndContent(Field field, Object classObject) {
        Object[] results = new Object[2];
        try {
            boolean accessible = field.isAccessible();
            field.setAccessible(true);

            if (field.getType().equals(TextField.class)) {
                TextField textField = (TextField) field.get(classObject);
                results[0] = textField;
                results[1] = textField.getText();
            }
            else if (field.getType().equals(ADatePicker.class)){
                ADatePicker datePicker = (ADatePicker) field.get(classObject);
                results[0] = datePicker;
                results[1] = datePicker.getEditor().getText();
            }
            else if (field.getType().equals(MapEditorComboBox.class)){
                MapEditorComboBox mapEditor = (MapEditorComboBox)field.get(classObject);
                results[0] = mapEditor;
                results[1] = mapEditor.getEditor().getText();
            }
            else if (field.getType().equals(ComboBox.class) || field.getType().equals(CurrencyComboBox.class)){
                ComboBox comboBox = (ComboBox) field.get(classObject);
                results[0] = comboBox;
                results[1] = comboBox.getValue().toString();
            }
            field.setAccessible(accessible);
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return results;
    }

    private static final Map<Label, Paint> default_colors_map = new HashMap<>();

    private static void changeNodeTitleLabelVisual(Node node, String text) {
        Parent parent = node.getParent();
        Label nodeTitleLabel = (Label) parent.lookup(".validationMessage");

        if (text.isEmpty()) {
            if (default_colors_map.containsKey(nodeTitleLabel)) {// This order of 'if' statements is correct!
                nodeTitleLabel.setTextFill(default_colors_map.get(nodeTitleLabel));
                default_colors_map.remove(nodeTitleLabel);
                Tooltip.uninstall(nodeTitleLabel, toolTip);
            }
        } else {
            node.requestFocus();
            toolTip.setText(text);
            toolTip.setStyle("-fx-background-color: gray; -fx-font-size: 8pt;");
            Tooltip.install(nodeTitleLabel, toolTip);
            default_colors_map.putIfAbsent(nodeTitleLabel, nodeTitleLabel.getTextFill());
            nodeTitleLabel.setTextFill(Color.RED);
        }
    }
    
}
