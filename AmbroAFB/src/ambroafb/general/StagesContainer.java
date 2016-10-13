/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general;

import static ambroafb.general.Utils.getInvokedClassMethod;
import ambroafb.general.interfaces.Dialogable;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.stage.Stage;
import org.apache.commons.collections.BidiMap;
import org.apache.commons.collections.bidimap.DualHashBidiMap;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author dato
 */
public class StagesContainer {
    
    private static final BidiMap bidmap = new DualHashBidiMap();

    public static int getSize() {
        return bidmap.size();
    }

    /**
     * The function saves child stage with path  ( path(owner).concat(childName) ). 
     * @param owner owner stage
     * @param childName child stage associated name
     * @param child child
     */
    public static void registerStageByOwner(Stage owner, String childName, Stage child){
        String path = childName;
        if (owner != null && bidmap.containsValue(owner)){
            path = getPathForStage(owner) + "/" + childName;
        }
        bidmap.put(path, child);
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
     * and also removes its children stages if they exists.
     * @param stage which must remove
     */
    public static void removeByStage(Stage stage){
        if (bidmap.containsValue(stage)){
            removeOnlySubstagesFor(stage);
            String path = (String) bidmap.getKey(stage);
            bidmap.remove(path);
        }
    }
    
    /**
     * The function removes only children stages of given stage from bidirectional map if they exists.
     * @param stage which children must be remove.
     */
    public static void removeOnlySubstagesFor(Stage stage) {
        String path = (String) bidmap.getKey(stage);
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
    
//    public static void iconifiedChildrenStagesFor(Stage currStage, boolean isMinimize){
//        List<String> directChildrenPathes = getDirectChildrenStagesPathesOf(currStage);
//        directChildrenPathes.stream().map((childPath) -> getStageForPath(childPath)).forEach((directChildStage) -> {
//            System.out.println(getPathForStage(currStage) + " gaushva recursia " + getPathForStage(directChildStage) + "-ze" );
//            iconifiedDirectChild(directChildStage, isMinimize);
//        });
//        System.out.println("daamtavra recursia " + getPathForStage(currStage) + "-ma tavis shvilebze");
//    }
//    
//    private static void iconifiedDirectChild(Stage currStage, boolean isMinimize){
//        List<String> directChildrenPathes = getDirectChildrenStagesPathesOf(currStage);
//        directChildrenPathes.stream().map((childPath) -> getStageForPath(childPath)).forEach((directChildStage) -> {
//            System.out.println(getPathForStage(currStage) + " gaushva recursia " + getPathForStage(directChildStage) + "-ze" );
//            iconifiedDirectChild(directChildStage, isMinimize);
//        });
//        currStage.setIconified(isMinimize);
//    }
//    
    public static List<String> getDirectChildrenStagesPathesOf(Stage currStage){
        String currentStagePath = getPathForStage(currStage);
        List<String> directChildrenPathes = (List<String>) bidmap.keySet().stream()
                                                                            .filter((key) -> isDirectChildPath((String)key, currentStagePath, "/"))
                                                                            .collect(Collectors.toList());
        return directChildrenPathes;
    }
    
    private static boolean isDirectChildPath(String childPath, String ownerPath, String delimiter){
        return  childPath.startsWith(ownerPath) &&
                !childPath.equals(ownerPath)   &&
                StringUtils.countMatches(childPath, delimiter) - 1 == StringUtils.countMatches(ownerPath, delimiter);
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
        String substagePath = ownerPath + "/" + substageLocalName;
        Stage substage = getStageForPath(substagePath);
        return substage;
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

    
    
    public static void closeStageAndItsChildrenStages(Stage currStage){
        closeStageWithChildren(currStage);
        if (currStage.isShowing()){
            currStage.close();
        }
    }

    /**
     * The function closes children stages and after that it close the given stage.
     * @param currStage Current stage.
     * @return True if current stage must close, false otherwise.
     */
    public static boolean closeStageWithChildren(Stage currStage) {
        boolean closePermission = true;
        String currStagePath = (String) bidmap.getKey(currStage);
        List<String> childrenPath = getFirstLevelChildrenFor(currStagePath);
        if (childrenPath.isEmpty()) {
            if (currStage instanceof Dialogable) {
                if (currStage.getOnCloseRequest() == null) {
                    currStage.close();
                } else {
                    currStage.getOnCloseRequest().handle(null);
                }
                Object controller = currStage.getScene().getProperties().get("controller");
                closePermission = (Boolean) getInvokedClassMethod(controller.getClass(), "getPermissionToClose", null, controller);
            } else {
                currStage.close();
            }
        }
        else {
            for (String childPath : childrenPath) {
                closePermission = closeStageWithChildren((Stage) bidmap.get(childPath)) && closePermission;
            }
            if (currStage.isShowing() && closePermission && !((String)bidmap.getKey(currStage)).equals("main")){
                currStage.close();
            }
        }
        return closePermission;
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
