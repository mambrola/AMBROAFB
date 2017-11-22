/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general;

import ambro.ADatePicker;
import ambroafb.clients.ClientComboBox;
import static ambroafb.general.Utils.getInvokedClassMethod;
import ambroafb.general.countcombobox.CountComboBox;
import ambroafb.general.editor_panel.EditorPanel;
import ambroafb.general.image_gallery.ImageGalleryController;
import ambroafb.general.interfaces.Annotations;
import ambroafb.general.interfaces.EditorPanelable;
import ambroafb.general.mapeditor.MapEditor;
import ambroafb.general.scene_components.number_fields.NumberField;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author dkobuladze
 */
public class AnnotiationUtils {
    
    private static Tooltip toolTip = new Tooltip();
    
    public static boolean everyFieldContentIsValidFor(Object currentClassObject, EditorPanel.EDITOR_BUTTON_TYPE type) {
        boolean result = true;
        Field[] fields = currentClassObject.getClass().getDeclaredFields();

        for (Field field : fields) {
            if (field.isAnnotationPresent(Annotations.ContentNotEmpty.class)) {
                result = result && checkValidationForIsNotEmptyAnnotation(field, currentClassObject);
            }
            if (field.isAnnotationPresent(Annotations.ContentMail.class)) {
                result = result && checkValidationForContentMailAnnotation(field, currentClassObject);
            }
            if (field.isAnnotationPresent(Annotations.ContentTreeItem.class)){
                result = result && checkValidationForContentTreeItemAnnotation(field, currentClassObject, type);
            }
            if (field.isAnnotationPresent(Annotations.ContentRate.class)){
                result = result && checkValidationForContentRateAnnotation(field, currentClassObject);
            }
            if (field.isAnnotationPresent(Annotations.ContentAmount.class)){
                result = result && checkValidationForContentAmountAnnotation(field, currentClassObject);
            }
            if (field.isAnnotationPresent(Annotations.ContentISO.class)){
                result = result && checkValidationForContentISOAnnotation(field, currentClassObject);
            }
            if (field.isAnnotationPresent(Annotations.ContentPattern.class)){
                result = result && checkValidationForContentPatternAnnotation(field, currentClassObject);
            }
            if (field.isAnnotationPresent(Annotations.ContentMapEditor.class)){
                result = result && checkValidationForContentMapEditorAnnotation(field, currentClassObject);
            }
        }
        return result;
    }

    private static boolean checkValidationForIsNotEmptyAnnotation(Field field, Object classObject) {
        boolean contentIsCorrect = false;
        Annotations.ContentNotEmpty annotation = field.getAnnotation(Annotations.ContentNotEmpty.class);

        Object[] typeAndContent = getNodesTypeAndContent(field, classObject);
        Object value = typeAndContent[1];
        
        boolean predicateValue = getPredicateValue(annotation.predicate(), field.getName());
        
        // case of comboBoxes value may contains tab or spaces so we need to trim the value.
        if (predicateValue && (value == null || value.toString().trim().isEmpty())) {
            changeNodeTitleLabelVisual((Node) typeAndContent[0], annotation.explain());
        } else {
            changeNodeTitleLabelVisual((Node) typeAndContent[0], "");
            contentIsCorrect = true;
        }
        return contentIsCorrect;
    }
    
    private static boolean checkValidationForContentMailAnnotation(Field field, Object currSceneController) {
        boolean contentIsCorrect = false;
        Annotations.ContentMail annotation = field.getAnnotation(Annotations.ContentMail.class);

        Object[] typeAndContent = getNodesTypeAndContent(field, currSceneController);

        boolean validSyntax = Pattern.matches(annotation.valueForSyntax(), (String) typeAndContent[1]);
        boolean validAlphabet = Pattern.matches(annotation.valueForAlphabet(), (String) typeAndContent[1]);
        boolean predicateValue = getPredicateValue(annotation.predicate(), field.getName());
        if (predicateValue && !validSyntax) { // otherwise email check label may stay a red color
            changeNodeTitleLabelVisual((Node) typeAndContent[0], annotation.explainForSyntax());
        } else if (predicateValue && !validAlphabet) { // otherwise email check label may stay a red color
            changeNodeTitleLabelVisual((Node) typeAndContent[0], annotation.explainForAlphabet());
        } else {
            changeNodeTitleLabelVisual((Node) typeAndContent[0], "");
            contentIsCorrect = true;
        }
        return contentIsCorrect;
    }
    
    private static boolean getPredicateValue(Class predicateClass, String fieldName){
        try {
            if (predicateClass != Annotations.ContentNotEmpty.DEFAULT.class){
                Predicate<String> p = (Predicate<String>) predicateClass.getConstructor().newInstance();
                return p.test(fieldName);
            }
        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }

    private static boolean checkValidationForContentTreeItemAnnotation(Field field, Object currSceneController, EditorPanel.EDITOR_BUTTON_TYPE  type){
        boolean contentIsCorrect = false;
        Annotations.ContentTreeItem annotation = field.getAnnotation(Annotations.ContentTreeItem.class);
        Object[] typeAndContent = getNodesTypeAndContent(field, currSceneController);
        String content = (String)typeAndContent[1];
        
        if (content.length() != Integer.parseInt(annotation.valueForLength())){
            changeNodeTitleLabelVisual((Node)typeAndContent[0], annotation.explainForLength() + annotation.valueForLength());
        }
        else if (!Pattern.matches(annotation.valueForSyntax(), content)){
            changeNodeTitleLabelVisual((Node)typeAndContent[0], annotation.explainForSyntax());
        }
        else {
            EditorPanelable newPanelableObject = (EditorPanelable) getInvokedClassMethod(currSceneController.getClass(), "getNewEditorPanelable", null, currSceneController);
            Object contr = getInvokedClassMethod(currSceneController.getClass(), "getOwnerController", null, currSceneController);
            // already exist this item for this code:
            if ((Boolean)getInvokedClassMethod(contr.getClass(), "accountAlreadyExistForCode", new Class[]{EditorPanelable.class, EditorPanel.EDITOR_BUTTON_TYPE.class}, contr, newPanelableObject, type)){
                changeNodeTitleLabelVisual((Node)typeAndContent[0], annotation.explainForExists());
            }
            // item has not a parent:
            else if (!(Boolean)getInvokedClassMethod(contr.getClass(), "accountHasParent", new Class[]{String.class}, contr, content)){
                changeNodeTitleLabelVisual((Node)typeAndContent[0], annotation.explainForHasNotParent());
            }
            else {
                changeNodeTitleLabelVisual((Node) typeAndContent[0], "");
                contentIsCorrect = true;
            }
        }
        return contentIsCorrect;
    }
    
    private static boolean checkValidationForContentPatternAnnotation(Field field, Object currSceneController){
        boolean contentIsCorrect = false;
        Annotations.ContentPattern annotation = field.getAnnotation(Annotations.ContentPattern.class);
        
        Object[] typeAndContent = getNodesTypeAndContent(field, currSceneController);
        
        if (!Pattern.matches(annotation.value(), (String)typeAndContent[1])){
            changeNodeTitleLabelVisual((Node) typeAndContent[0], annotation.explain());
        } else {
            changeNodeTitleLabelVisual((Node) typeAndContent[0], "");
            contentIsCorrect = true;
        }
        return contentIsCorrect;
    }
    
    private static boolean checkValidationForContentMapEditorAnnotation(Field field, Object currSceneController){
        boolean contentIsCorrect = false;
        Annotations.ContentMapEditor annotation = field.getAnnotation(Annotations.ContentMapEditor.class);
        Object[] typeAndContent = getNodesTypeAndContent(field, currSceneController);
        MapEditor mapEditorComboBox = (MapEditor) typeAndContent[0];
        String editorContent = (String) typeAndContent[1];
        String keyPart = StringUtils.substringBefore(editorContent, mapEditorComboBox.getDelimiter()).trim();
        String valuePart = StringUtils.substringAfter(editorContent, mapEditorComboBox.getDelimiter()).trim();

        boolean keyMatch = Pattern.matches(mapEditorComboBox.getKeyPattern(), keyPart);
        boolean valueMatch = Pattern.matches(mapEditorComboBox.getValuePattern(), valuePart);
        String explain;
        if (!keyMatch || !valueMatch){
            explain = (!keyMatch) ? annotation.explainKey() : annotation.explainValue();
            changeNodeTitleLabelVisual(mapEditorComboBox, explain);
        }
        else if ((keyPart.isEmpty() && !valuePart.isEmpty()) || (!keyPart.isEmpty() && valuePart.isEmpty())){
            explain = annotation.explainEmpty();
            changeNodeTitleLabelVisual(mapEditorComboBox, explain);
        } else {
            changeNodeTitleLabelVisual(mapEditorComboBox, "");
            contentIsCorrect = true;
        }
        return contentIsCorrect;
    }
    
    private static boolean checkValidationForContentRateAnnotation(Field field, Object currSceneController){
        boolean contentIsCorrect = false;
        Annotations.ContentRate annotation = field.getAnnotation(Annotations.ContentRate.class);
        Object[] typeAndContent = getNodesTypeAndContent(field, currSceneController);

        if (!Pattern.matches(annotation.valueForIntegerPart(), (String) typeAndContent[1])){
            String explainFromBundle = GeneralConfig.getInstance().getTitleFor(annotation.explainForIntegerPart());
            String integerPartExp = explainFromBundle.replaceFirst("#", "" + Annotations.ContentRate.integerPartLen);
            changeNodeTitleLabelVisual((Node)typeAndContent[0], integerPartExp);
        }
        else if (!Pattern.matches(annotation.valueForFractionalPart(), (String) typeAndContent[1])){
            String explainFromBundle = GeneralConfig.getInstance().getTitleFor(annotation.explainForFractionalPart());
            String fractionalPartExp = explainFromBundle.replaceFirst("#", "" + Annotations.ContentRate.fractionalPartLen);
            changeNodeTitleLabelVisual((Node)typeAndContent[0], fractionalPartExp);
        }
        else if (!Pattern.matches(annotation.valueForWhole(), (String) typeAndContent[1])){
            changeNodeTitleLabelVisual((Node)typeAndContent[0], annotation.explainForWhole());
        }
        else {
            changeNodeTitleLabelVisual((Node) typeAndContent[0], "");
            contentIsCorrect = true;
        }
        return contentIsCorrect;
    }
    
    private static boolean checkValidationForContentAmountAnnotation (Field field, Object currSceneController){
        boolean contentIsCorrect = false;
        Annotations.ContentAmount annotation = field.getAnnotation(Annotations.ContentAmount.class);
        Object[] typeAndContent = getNodesTypeAndContent(field, currSceneController);

        String integerPartPattern = annotation.valueForIntegerPart();
        int outerIntegerPartMaxLen = annotation.integerPartMaxLen();
        if (outerIntegerPartMaxLen > 0){
            integerPartPattern = annotation.valueForIntegerPart().replaceFirst("" + (Annotations.ContentAmount.integerPartMaxLen-1), "" + annotation.integerPartMaxLen());
        }
        
        if (!Pattern.matches(integerPartPattern, (String) typeAndContent[1])){
            String integerPartExplain = (outerIntegerPartMaxLen > 0) ? "" + outerIntegerPartMaxLen : "" + Annotations.ContentAmount.integerPartMaxLen;
            String explainFromBundle = GeneralConfig.getInstance().getTitleFor(annotation.explainForIntegerPart());
            String integerPartExp = explainFromBundle.replaceFirst("#", integerPartExplain);
            changeNodeTitleLabelVisual((Node)typeAndContent[0], integerPartExp);
        }
        else if (!Pattern.matches(annotation.valueForFractionalPart(), (String) typeAndContent[1])){
            String explainFromBundle = GeneralConfig.getInstance().getTitleFor(annotation.explainForFractionalPart());
            String fractionalPartExp = explainFromBundle.replaceFirst("#", "" + Annotations.ContentAmount.fractionalPartMaxLen);
            changeNodeTitleLabelVisual((Node)typeAndContent[0], fractionalPartExp);
        }
        else if (!Pattern.matches(annotation.valueForWhole(), (String) typeAndContent[1])){
            changeNodeTitleLabelVisual((Node)typeAndContent[0], annotation.explainForWhole());
        }
        else {
            changeNodeTitleLabelVisual((Node) typeAndContent[0], "");
            contentIsCorrect = true;
        }
        return contentIsCorrect;
    }
    
    private static boolean checkValidationForContentISOAnnotation(Field field, Object currSceneController){
        boolean contentIsCorrect = false;
        Annotations.ContentISO annotation = field.getAnnotation(Annotations.ContentISO.class);
        Object[] typeAndContent = getNodesTypeAndContent(field, currSceneController);
        
        if (!Pattern.matches(annotation.value(), (String)typeAndContent[1])){
            String explainFromBundle = GeneralConfig.getInstance().getTitleFor(annotation.explain());
            String explain = explainFromBundle.replaceFirst("#", "" + Annotations.ContentISO.isoLen);
            changeNodeTitleLabelVisual((Node)typeAndContent[0], explain);
        }
        else {
            changeNodeTitleLabelVisual((Node)typeAndContent[0], "");
            contentIsCorrect = true;
        }
        return contentIsCorrect;
    }
    
    private static Object[] getNodesTypeAndContent(Field field, Object ownerClassObject) {
        Object[] results = new Object[2];
        try {
            boolean accessible = field.isAccessible();
            field.setAccessible(true);

            if (field.getType().equals(TextField.class) || field.getType().getSuperclass().equals(NumberField.class)) {
                TextField textField = (TextField) field.get(ownerClassObject);
                results[0] = textField;
                results[1] = textField.getText();
            }
            else if (field.getType().equals(ADatePicker.class)){
                ADatePicker datePicker = (ADatePicker) field.get(ownerClassObject);
                results[0] = datePicker;
                results[1] = datePicker.getEditor().getText();
            }
            else if (field.getType().equals(MapEditor.class)){
                MapEditor mapEditor = (MapEditor)field.get(ownerClassObject);
                results[0] = mapEditor;
                results[1] = mapEditor.getEditor().getText();
            }
            else if (field.getType().equals(ImageGalleryController.class)){
                ImageGalleryController gallery = (ImageGalleryController) field.get(ownerClassObject);
                results[0] = gallery.getRoot();
                results[1] = (gallery.isEmpty()) ? null : "not empty";
            }
            else if (field.getType().equals(CountComboBox.class)){
                CountComboBox countComboBox = (CountComboBox) field.get(ownerClassObject);
                results[0] = countComboBox;
                results[1] = (countComboBox.getBasket().isEmpty()) ? null : countComboBox.getValue();
            }
            // Note: ClientComboBox is not ComboBox extened, so this case specific case:
            else if (field.getType().equals(ClientComboBox.class)){
                ClientComboBox clientComboBox = (ClientComboBox) field.get(ownerClassObject);
                results[0] = clientComboBox;
                results[1] = (clientComboBox.getValue() == null) ? null : clientComboBox.getValue();
            }
            else if (field.getType().toString().contains("ComboBox")) {
                ComboBox comboBox = (ComboBox) field.get(ownerClassObject);
                results[0] = comboBox;
                // Note: comboBox.getValue() may be null but some class may provides to make some action that avoid nullable and return empty string for example. So we check selection index.
//                int selectedIndex = comboBox.getSelectionModel().getSelectedIndex();
//                results[1] = (comboBox.getValue() == null || selectedIndex < 0) ? null : comboBox.getValue();
                
                results[1] = (comboBox.getValue() == null ) ? null : comboBox.getValue();
            }
            field.setAccessible(accessible);
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return results;
    }

    private static final Map<Label, Paint> default_colors_map = new HashMap<>();

    private static void changeNodeTitleLabelVisual(Node node, String explain) {
        Parent parent = node.getParent();
        Label nodeTitleLabel = (Label) parent.lookup(".validationMessage");

        if (explain.isEmpty()) {
            if (default_colors_map.containsKey(nodeTitleLabel)) {// This order of 'if' statements is correct!
                nodeTitleLabel.setTextFill(default_colors_map.get(nodeTitleLabel));
                default_colors_map.remove(nodeTitleLabel);
                Tooltip.uninstall(nodeTitleLabel, toolTip);
            }
        } else {
            node.requestFocus();
            toolTip.setText(GeneralConfig.getInstance().getTitleFor(explain));
            toolTip.setStyle("-fx-background-color: gray; -fx-font-size: 8pt;");
            Tooltip.install(nodeTitleLabel, toolTip);
            default_colors_map.putIfAbsent(nodeTitleLabel, nodeTitleLabel.getTextFill());
            nodeTitleLabel.setTextFill(Color.RED);
        }
    }
    
}
