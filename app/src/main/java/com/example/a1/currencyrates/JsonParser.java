package com.example.a1.currencyrates;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by 1 on 23.09.2017.
 */

public class JsonParser {
    public static String getJsonField(String json, String jsonObject, String fieldName){
        String field = null;
        try {
            JSONObject obj = new JSONObject(json);
            field = obj.getJSONObject(jsonObject).getString(fieldName);
        } catch (JSONException exc) {}

        return field;
    }
}
