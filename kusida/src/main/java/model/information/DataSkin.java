package model.information;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import com.kulala.staticsfunc.static_system.OJsonGet;

import java.util.ArrayList;
import java.util.List;

public class DataSkin {
    public boolean isType = false;//是分类名
    public int ide;//车型装扮id
    public String name   = "";//分类名
    public String title  = "";//皮肤名
    public String size   = "";//包大小
    public float fee    = 0;//价格
    public String feeStr = "";//用于显示价格
    public int status;//1：点击直接下载，2：需要购买，点击跳转支付
    public String smallPic = "";//缩略图
    public String[] pics;//略览图列表
    public int type;//1：车辆皮肤类型，2：车辆背景类型


    public static DataSkin fromJsonObject(JsonObject obj) {
        Gson     gson    = new Gson();
        DataSkin thisobj = gson.fromJson(obj, DataSkin.class);
        return thisobj;
    }

    public static List<DataSkin> fromJsonArrayNoTitle(JsonArray informInfos) {
        if (informInfos == null || informInfos.size() == 0) return null;
        List<DataSkin> data = new ArrayList<DataSkin>();
        for (int i = 0; i < informInfos.size(); i++) {
            JsonObject obj = informInfos.get(i).getAsJsonObject();
            DataSkin   info   = DataSkin.fromJsonObject(obj);
            data.add(info);
        }
        return data;
    }
    public static List<DataSkin> fromJsonArray(JsonArray informInfos) {
        if (informInfos == null || informInfos.size() == 0) return null;
        List<DataSkin> data = new ArrayList<DataSkin>();
        for (int i = 0; i < informInfos.size(); i++) {
            JsonObject obj = informInfos.get(i).getAsJsonObject();
            String name = OJsonGet.getString(obj, "name");
            JsonArray infos = OJsonGet.getJsonArray(obj, "infos");
            //加个标题
            DataSkin   title   = new DataSkin();
            title.name = name;
            title.isType = true;
            data.add(title);
            if(infos!=null){
                for (int j = 0; j < infos.size(); j++) {
                    JsonObject object = infos.get(j).getAsJsonObject();
                    DataSkin   info   = DataSkin.fromJsonObject(object);
                    info.name = name;
                    data.add(info);
                }
            }
        }
        return data;
    }



    public static JsonObject toJsonObject(DataSkin info) {
        Gson       gson = new Gson();
        String     json = gson.toJson(info);
        JsonObject obj  = gson.fromJson(json, JsonObject.class);
        return obj;
    }

    public static JsonArray toJsonArray(List<DataSkin> list) {
        Gson      gson = new Gson();
        String    json = gson.toJson(list);
        JsonArray arr  = gson.fromJson(json, JsonArray.class);
        return arr;
    }
}
