package model.score;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/5.
 */
public class DataScore {
    public int     ide;//积分类型id
    public String     pic;//图标
    public String     title;//积分描述
    public int     score;//积分数
    public int     isDone;//是否已经完成	0：未完成，1：完成
    public int     jumpPage;//跳转页面 如果能找到则跳转，否则则不跳转，1：分享页2：车控制页3：皮肤页4：卡片页5：完善个人信息6：设置->安全页
    public long     createTime;//时间

    public DataScore copy() {
        JsonObject object = toJsonObject();
        return fromJsonObject(object);
    }
    public static DataScore fromJsonObject(JsonObject obj) {
        Gson     gson    = new Gson();
        DataScore thisobj = gson.fromJson(obj, DataScore.class);
        return thisobj;
    }
    public JsonObject toJsonObject() {
        Gson       gson = new Gson();
        String     json = gson.toJson(this);
        JsonObject obj  = gson.fromJson(json, JsonObject.class);
        return obj;
    }
    public static JsonArray toJsonArray(List<DataScore> list) {
        Gson      gson = new Gson();
        String    json = gson.toJson(list);
        JsonArray arr  = gson.fromJson(json, JsonArray.class);
        return arr;
    }
    public static List<DataScore> fromJsonArray(JsonArray list) {
        if (list == null || list.size() == 0)return new ArrayList<DataScore>();
        List<DataScore> data = new ArrayList<DataScore>();
        for (int i = 0; i < list.size(); i++) {
            JsonObject  object = list.get(i).getAsJsonObject();
            DataScore info   = DataScore.fromJsonObject(object);
            data.add(info);
        }
        return data;
    }
}
