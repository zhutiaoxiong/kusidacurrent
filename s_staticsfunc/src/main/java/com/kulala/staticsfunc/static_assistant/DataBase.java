package com.kulala.staticsfunc.static_assistant;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class DataBase {
    public static<V> V fromJsonObject(JsonObject obj,Class<V> tClass) {
        Gson         gson    = new Gson();
        V thisobj = gson.fromJson(obj, tClass);
        return thisobj;
    }
    public static<V> ArrayList<V> fromJsonArray(JsonArray array, Class<V> tClass) {
        if (array == null || array.size() == 0)return null;
        ArrayList<V> data = new ArrayList<V>();
        for (int i = 0; i < array.size(); i++) {
            JsonObject jsonObject = array.get(i).getAsJsonObject();
            V   obj       = fromJsonObject(jsonObject,tClass);
            data.add(obj);
        }
        return data;
    }

    public static<V> JsonObject toJsonObject(V dataObj) {
        Gson       gson = new Gson();
        String     json = gson.toJson(dataObj);
        JsonObject obj  = gson.fromJson(json, JsonObject.class);
        return obj;
    }

    public static<V> JsonArray toJsonArray(List<V> list) {
        Gson      gson = new Gson();
        String    json = gson.toJson(list);
        JsonArray arr  = gson.fromJson(json, JsonArray.class);
        return arr;
    }

}
