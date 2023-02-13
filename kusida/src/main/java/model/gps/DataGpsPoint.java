package model.gps;

import com.google.gson.Gson;
import com.google.gson.JsonObject;


public class DataGpsPoint {
    public long   createTime;        // 开始时间
    public String location;            // 开始位置
    public String latlng;            // 开始位置
    public long   ide;                // id
    public String note = "";//		备注

    public boolean isStart    = false;
    public boolean isSelected = false;

    public static DataGpsPoint fromJsonObject(JsonObject obj) {
        Gson         gson    = new Gson();
        DataGpsPoint thisobj = gson.fromJson(obj, DataGpsPoint.class);
        return thisobj;
    }
}
