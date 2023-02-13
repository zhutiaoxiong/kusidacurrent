package model.carlist;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class DataCarTypeInfo{
    /**
     *
     int   ide    车型装扮id
     title String 名称
     size String 包大小 例如：1.3M
     fee double 价格
     feeStr String 价格串 用于显示
     status int 状态 1：点击直接下载，2：需要购买，点击跳转支付
     smallPic String 缩略图 用于显示再在列表页的图片
     pics String[] 略览图列表 装扮列表中点开可以直接看到图片列表
     */
    public  int    ide;
    public String title;
    public String size;
    public double fee;
    public String feeStr;
    public int status;
    public String smallPic;
    public String[] pics;

    public static DataCarTypeInfo fromJsonObject(JsonObject obj) {
        Gson            gson    = new Gson();
        DataCarTypeInfo thisobj = gson.fromJson(obj, DataCarTypeInfo.class);
        return thisobj;
    }

    public static JsonObject toJsonObject(DataCarTypeInfo info) {
        Gson       gson = new Gson();
        String     json = gson.toJson(info);
        JsonObject obj  = gson.fromJson(json, JsonObject.class);
        return obj;
    }
    public static List<DataCarTypeInfo> fromJsonArray(JsonArray carBackgroundInfos) {
        if (carBackgroundInfos == null || carBackgroundInfos.size() == 0)
            return null;
        List<DataCarTypeInfo> data = new ArrayList<DataCarTypeInfo>();
        for (int i = 0; i < carBackgroundInfos.size(); i++) {
            JsonObject objjj = carBackgroundInfos.get(i).getAsJsonObject();
            DataCarTypeInfo info = DataCarTypeInfo.fromJsonObject(objjj);
            data.add(info);
        }
        return data;
    }
    public static JsonArray toJsonArray(List<DataCarTypeInfo> list) {
        Gson      gson = new Gson();
        String    json = gson.toJson(list);
        JsonArray arr  = gson.fromJson(json, JsonArray.class);
        return arr;
    }
}
