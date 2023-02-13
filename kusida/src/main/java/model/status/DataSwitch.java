package model.status;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class DataSwitch {

    public int ide;//72靠近开，73离开锁
    public String name        = "";
    public int    isOpen      = 1;//0：未开启，1：开启
    public double signalValue = 0;//信号值,无匙开关信号值，开启时下发

    public int    isShow      = 1;//0：不显不，1：显示,本地判断下发NUMM,默认显示
    public static DataSwitch fromJsonObject(JsonObject obj) {
        if(obj == null)return null;
        Gson       gson    = new Gson();
        DataSwitch thisobj = gson.fromJson(obj, DataSwitch.class);
        return thisobj;
    }

    public static List<DataSwitch> fromJsonArray(JsonArray jsonarray) {
        if (jsonarray == null || jsonarray.size() == 0)
            return null;
        List<DataSwitch> data = new ArrayList<DataSwitch>();
        for (int i = 0; i < jsonarray.size(); i++) {
            JsonObject object = jsonarray.get(i).getAsJsonObject();
            DataSwitch info   = DataSwitch.fromJsonObject(object);
            data.add(info);
        }
        return data;
    }

    public static JsonArray toJsonArray(List<DataSwitch> list) {
        Gson      gson = new Gson();
        String    json = gson.toJson(list);
        JsonArray arr  = gson.fromJson(json, JsonArray.class);
        return arr;
    }
    public static String toJsonArrayString(List<DataSwitch> list) {
        Gson   gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }
}
