/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambroafb.general.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import org.json.JSONObject;

/**
 *
 * @author tabramishvili
 */
public class JSONObjectSerializer extends JsonSerializer<JSONObject> {

    @Override
    public void serialize(JSONObject t, JsonGenerator jg, SerializerProvider sp) throws IOException {
        jg.writeRawValue(t.toString());
    }
    
}
