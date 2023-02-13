package model.gps;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qq522414074 on 2017/3/27.
 */

public class SearchHistory {
    public String searchtxt;
    public static JsonArray toJsonArray(List<SearchHistory> list) {
        Gson gson = new Gson();
        String    json = gson.toJson(list);
        JsonArray arr  = gson.fromJson(json, JsonArray.class);
        return arr;
    }
    public static List<SearchHistory> fromJsonArray(JsonArray arr) {
        Log.i("msg", "Gson");
        List<SearchHistory> list = new ArrayList<>();
        Gson gson = new Gson();
        for(int i=0;i<arr.size();i++){
            JsonObject object = arr.get(i).getAsJsonObject();
            Log.i("msg", "Gson object"+object.toString());
            SearchHistory data = gson.fromJson(object, SearchHistory.class);
            data.fromJsonObject(object);
            list.add(data);
        }
        Log.i("msg", "Gson end");
        return list;
    }
    public static SearchHistory fromJsonObject(JsonObject obj) {
        Gson         gson    = new Gson();
        SearchHistory thisobj = gson.fromJson(obj, SearchHistory.class);
        return thisobj;
    }
}
