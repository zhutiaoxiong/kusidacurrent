package model;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import model.maintain.DataMaintain;


public class DataDisPlay {
    public long id;
    public String name;
    public String url;
    public String content;
    public String type;
    public String status;
    public String showRegion;
    public String showEndTime;
    public String showStartTime;
    public String platform;
    public String isDisplay;
    public static DataDisPlay fromJsonObject(JsonObject obj) {
        if(obj == null)return null;
        Gson gson    = new Gson();
        DataDisPlay thisobj = gson.fromJson(obj, DataDisPlay.class);
        return thisobj;
    }
}
