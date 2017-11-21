/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author tabramishvili
 */
public class JSONObjectDeserializer extends JsonDeserializer<JSONObject> {

    @Override
    public JSONObject deserialize(JsonParser jp, DeserializationContext arg1) throws IOException, JsonProcessingException {
        try {
            JsonNode node = jp.getCodec().readTree(jp);
            return new JSONObject(node.toString());
        } catch (JSONException ex) {
            Logger.getLogger(JSONObjectDeserializer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}
