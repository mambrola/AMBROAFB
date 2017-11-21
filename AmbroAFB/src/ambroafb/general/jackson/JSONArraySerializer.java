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
import org.json.JSONArray;

/**
 *
 * @author tabramishvili
 */
public class JSONArraySerializer extends JsonSerializer<JSONArray> {

    @Override
    public void serialize(JSONArray t, JsonGenerator jg, SerializerProvider sp) throws IOException {
        jg.writeRaw(t.toString());
    }

}
