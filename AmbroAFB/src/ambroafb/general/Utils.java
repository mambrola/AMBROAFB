/*
 * To setLanguage this license header, choose License Headers in Project Properties.
 * To setLanguage this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general;
    
import ambroafb.AmbroAFB;
import ambroafb.accounts.Account;
import ambroafb.docs.Doc;
import ambroafb.general.countcombobox.CountComboBoxItem;
import ambroafb.invoices.Invoice;
import ambroafb.products.Product;
import authclient.AuthServerException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.regex.Pattern;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.binding.StringExpression;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author tabramishvili
 */
public class Utils {

    private static Logger logger;
    private static ObjectMapper mapper = new ObjectMapper();

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
    
    
    public static JSONObject getJSONFromClass(Object classObject){
        JSONObject result = null;
        try {
            ObjectWriter writer = mapper.writer().withDefaultPrettyPrinter();
            result = new JSONObject(writer.writeValueAsString(classObject));
        } catch (JSONException | JsonProcessingException ex) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
    
    
    
    public static <T> T getClassFromJSON(Class targetClass, JSONObject json){
        try {
            if (targetClass != null && json != null){
                return (T) (mapper.readValue(json.toString(), targetClass));
            }
        } catch (IOException ex) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public static <T> ArrayList<T> getListFromJSONArray(Class targetClass, JSONArray array){
        try {
            return mapper.readValue(array.toString(), mapper.getTypeFactory().constructCollectionType(ArrayList.class, targetClass));
        } catch (IOException ex) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<>();
    }
    
    
    public static boolean dateEquals(LocalDate date1, LocalDate date2){
        if (date1 == null && date2 == null) return true;
        else if (date1 == null && date2 != null) return false;
        else if (date1 != null && date2 == null) return false;
        return date1.equals(date2);
    }
    
    
    public static ArrayList<Node> getFocusTraversableBottomChildren(Parent root) {
        ArrayList<Node> arrayList = new ArrayList<>();
        root.getChildrenUnmodifiable().stream().forEach((n) -> {
            boolean allow = !n.getStyleClass().contains("alwaysEnable") &&
                            !n.getClass().toString().contains("ImageView");
//            boolean accessToChildren = !n.getStyleClass().contains("blockAccessToChildrenFocus");
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
    
    public static StringExpression getDelimiterAfter(StringProperty strProp, String delimiter){
        return Bindings.when(strProp.isNull().or(strProp.isEmpty())).
                        then("").
                        otherwise(delimiter);
    }
    
    public static StringExpression getDelimiterBetween(StringProperty strPropLeft, StringProperty strPropRight, BooleanProperty rightIsLastElem, String delimiter){
        return Bindings.when(strPropLeft.isNull().or(strPropLeft.isEmpty())).
                        then("").
                        otherwise(Bindings.when(rightIsLastElem).
                                            then(Bindings.when(strPropRight.isNull().or(strPropRight.isEmpty())).
                                                          then(".").
                                                          otherwise(delimiter)).
                                            otherwise(Bindings.when(strPropRight.isNull().or(strPropRight.isEmpty())).then("").otherwise(delimiter))
                                           
                        );
//        Bindings.when(strPropRight.isNull().or(strPropRight.isEmpty())).
//                                            then(".").
//                                            otherwise(delimiter)
    }
    
    public static Account avoidNullAndReturnEmpty(Account acc){
        return acc == null ? new Account() : acc;
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
     * The method checks date value on null.
     * @param date The interested date.
     * @return LocalDate.now() if date is null. Otherwise, returns  'date'  again.
     */
    public static LocalDate avoidNull(LocalDate date){
        return (date == null) ? LocalDate.now() : date;
    }
    
    /** Returns JSON object for given key and value. 
     * If jsonIbject parameter is null creates new object and returns it, otherwise uses old itself
     * for put new pair (key -> value).
     * @param jsonObj
     * @param key
     * @param value
     * @return  
     */
    public static JSONObject getJsonFrom(JSONObject jsonObj, String key, Object value){
        JSONObject json = (jsonObj == null) ? new JSONObject() : jsonObj;
        try {
            json.put(key, value);
        } catch (JSONException ex) {
            Logger.getLogger(Invoice.class.getName()).log(Level.SEVERE, null, ex);
        }
        return json;
    }
    
    /**
     * The function returns JSON object from string.
     * Note: string must be JSON syntax.
     * @param strAsJson JSON syntax string
     * @return Null if can not create JSON from parameter, otherwise returns JSON object.
     */
    public static JSONObject getJsonFrom(String strAsJson){
        try {
            JSONObject json = new JSONObject(strAsJson);
            return json;
        } catch (JSONException ex) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    /**
     * The method converts string to Integer. If String is incorrect, it returns  -1 (minus 1).
     * @param str String which must be converted to Integer.
     * @return -1 if parameter is incorrect, otherwise - appropriate Integer value.
     */
    public static int getIntValueFor(String str){
        int result = -1;
        try {
            if (str.isEmpty()) result = 0;
            else result = Integer.parseInt(str);
        } catch (Exception ex){ }
        return result;
    }
    
    /**
     * The method converts string to Integer. If String is incorrect, it returns  null.
     * @param str String which must be converted to Integer.
     * @return null if parameter is incorrect, otherwise - appropriate Integer value.
     */
    public static Integer getIntegerFrom(String str){
        Integer result = null;
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
            if (str.isEmpty()) result = 0;
            result = Double.parseDouble(str);
        } catch(Exception ex){ } // This exception is need for it. NOT change to NumberFormatException !
        return result;
    }
    
    public static BigDecimal getBigDecimalFor(String money){
        BigDecimal result = null;
        try {
            NumberFormat localeFormatter = NumberFormat.getNumberInstance(Locale.GERMANY);
            ((DecimalFormat) localeFormatter).setParseBigDecimal(true);
            result = (BigDecimal)localeFormatter.parse(money);
        } catch (ParseException ex) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
    
    /**
     * The function return appropriate integer from boolean.
     * @param value True or False
     * @return 1 if value is True, 0 if value is False.
     */
    public static int getIntFromBoolean(boolean value){
        return value ? 1 : 0;
    }
    
    /**
     * The function process AuthServerException and returns array of its content data;
     * @param ex
     * @return String[] where error code is first and error message is second.
     */
    public static String[] processAuthServerError(AuthServerException ex){
        String[] codeAndMsg = new String[2];
        try {
            JSONObject  errorJson = new JSONObject(ex.getLocalizedMessage());
            codeAndMsg[0] = "" + errorJson.getInt("code");
            codeAndMsg[1] = errorJson.getString("message");
        } catch (JSONException ex1) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex1);
        }
        return codeAndMsg;
    }
    
    public static boolean compareDocs(List<Doc> docs1, List<Doc> docs2){
        if (docs1.size() != docs2.size()){
            return false;
        }
        docs1.sort((Doc d1, Doc d2) -> d1.compareById(d2));
        docs2.sort((Doc d1, Doc d2) -> d1.compareById(d2));
        for (int i = 0; i < docs1.size(); i++) {
            if (!docs1.get(i).compares(docs2.get(i))){
                return false;
            }
        }
        return true;
    }
    
    /**
     * The method compares two lists.
     * Note: If they are equal, then they must have same order of elements.
     * @param first The first list
     * @param second The second lists.
     * @return If they have another sizes or i-th element from first does not equal 
     *          i-th element from second, then method return false. True - otherwise.
     */
    public static boolean compareListsByElemOrder(List<?> first, List<?> second) {
        if (first.size() != second.size()) return false;
        for (int i = 0; i < first.size(); i++) {
            if (!first.get(i).equals(second.get(i))) return false;
        }
        return true;
    }
    
    
    public static boolean compareProductsCounter(Map<CountComboBoxItem, Integer> first, Map<CountComboBoxItem, Integer> second){
        boolean result = true;
        if (first.keySet().size() != second.keySet().size()) {
            result = false;
        }
        else {
            for(CountComboBoxItem p : first.keySet()){
                Product pFromSecondMap = getAppropriateProductFrom(second, p.getUniqueIdentifier());

                if (pFromSecondMap == null || first.get(p).intValue() != second.get(pFromSecondMap).intValue()){
                    result = false;
                    break;
                }
            }
        }
        return result;
    }
    
    private static Product getAppropriateProductFrom(Map<CountComboBoxItem, Integer> map, String identifier){
        Product result = null;
        for (CountComboBoxItem p : map.keySet()){
            if (p.getUniqueIdentifier().equals(identifier)){
                result = (Product)p;
                break;
            }
        }
        return result;
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
        if (name == null) return null;
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
    
    public static boolean existsMethodFor(Class owner, String methodName) {
        boolean result = false;
        try {
            Method  m = owner.getMethod(methodName);
            result = true;
        } catch (NoSuchMethodException | SecurityException ex) {
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
}
