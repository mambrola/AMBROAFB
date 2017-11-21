/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general.jackson;

import com.fasterxml.jackson.databind.module.SimpleModule;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author tabramishvili
 */
public class JSONModule extends SimpleModule {

    public JSONModule() {
        addDeserializer(JSONObject.class, new JSONObjectDeserializer());
        addDeserializer(JSONArray.class, new JSONArrayDeserializer());
        addSerializer(JSONObject.class, new JSONObjectSerializer());
        addSerializer(JSONArray.class, new JSONArraySerializer());
    }
}
