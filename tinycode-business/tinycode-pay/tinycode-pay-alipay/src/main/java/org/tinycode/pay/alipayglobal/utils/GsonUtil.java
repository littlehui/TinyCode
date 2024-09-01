package org.tinycode.pay.alipayglobal.utils;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GsonUtil {

    private static Gson gson = new Gson().newBuilder()
            .registerTypeAdapter(Double.class, new JsonSerializer<Double>() {
                @Override
                public JsonElement serialize(Double src, Type typeOfSrc, JsonSerializationContext context) {
                    if (src == src.longValue()) {
                        return new JsonPrimitive(src.longValue());
                    }
                    return new JsonPrimitive(src);
                }
            })
            //日期格式转换 yyyy-MM-dd HH:mm:ss  TO yyyy-MM-dd'T'HH:mm:ss.SSSXXX  (yyyy-MM-dd'T'HH:mm:ss.SSSZ)
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX")
            .registerTypeAdapterFactory(new RetainFieldMapFactory())
            .create();

    public static String toJson(Object o) {
        return gson.toJson(o);
    }

    public static Map<String, Object> jsonToMap(String strJson) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(new TypeToken<Map<String,Object>>(){}.getType(),new MapTypeAdapter()).create();
        return gson.fromJson(strJson, new TypeToken<Map<String, Object>>() {
        }.getType());
    }

    public static Map<String, Object> objectToMap(Object o) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(new TypeToken<Map<String,Object>>(){}.getType(),new MapTypeAdapter()).create();
        return gson.fromJson(gson.toJson(o), new TypeToken<Map<String, Object>>() {
        }.getType());
    }

    public static <T> T fromJson(String json, Class<T> c) {
        return gson.fromJson(json, c);
    }

    public static <T> T fromJson(String json, TypeToken<T> typeToken) {
        return gson.fromJson(json, typeToken.getType());
    }

    public static JSONObject fromJson(String json) {
        Map<String, ?> data = gson.fromJson(json, Map.class);
        return new JSONObject(data);
    }

    public static <T> List<T> fromJsonArray(String json, Class<T> c) {
        List<T> list = new ArrayList<>();
        JsonArray array = JsonParser.parseString(json).getAsJsonArray();
        for (final JsonElement elem : array) {
            list.add(new Gson().fromJson(elem, c));
        }
        return list;
    }

    public static JSONArray fromJsonArray(String json) {
        return gson.fromJson(json, JSONArray.class);
    }
}
