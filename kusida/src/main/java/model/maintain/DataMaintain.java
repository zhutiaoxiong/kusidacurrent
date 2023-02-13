package model.maintain;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by qq522414074 on 2016/10/31.
 */
public class DataMaintain{
    /**
     * ide long 保养id
     startTime long 开始时间
     endTime long 结束时间
     time int 保养周期 比如3个月
     miles int 保养公里数 单位km
     nowMiles int 当前公里数 当前已经行走的公里数
     status int 状态 0:正在保养，1：正常保养结束，2：提前保养结束
     msgType int 消息类型
     用于保养消息弹框，1：弹出框，左边为知道了，右边为查看
     2：弹出框，左边为稍后提醒，右边为知道了
     3：弹出框，左边为稍后提醒，右边为已保养
     message String 保养弹框消息 只有保养弹框消息时才下发，其他情况不下发
     */
    public long ide;
    public long startTime;
    public long endTime;
    public int time;
    public int miles;
    public int nowMiles;
    public int status;
    public String message;
    public int msgType;
    public String num;//从本地获取
    public static DataMaintain fromJsonObject(JsonObject obj) {
        if(obj == null)return null;
        Gson gson    = new Gson();
        DataMaintain thisobj = gson.fromJson(obj, DataMaintain.class);
        return thisobj;
    }
    public static List<DataMaintain> fromJsonArray(JsonArray brands) {
        if (brands == null || brands.size() == 0)
            return null;
        List<DataMaintain> data = new ArrayList<DataMaintain>();
        for (int i = 0; i < brands.size(); i++) {
            JsonObject objjj = brands.get(i).getAsJsonObject();
            DataMaintain info = DataMaintain.fromJsonObject(objjj);
            data.add(info);
        }
        return data;
    }
}
