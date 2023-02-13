package model.mylocator;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 定位器录音信息对象
 * */
public class PositionerRecordingBean {
    //编号
    private float id;
    //设备号
    private String deviceNum;
    //文件名
    private String name;
    //Base64码
    private String code;

    public float getId() {
        return id;
    }

    public void setId(float id) {
        this.id = id;
    }

    public String getDeviceNum() {
        return deviceNum;
    }

    public void setDeviceNum(String deviceNum) {
        this.deviceNum = deviceNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
    public static PositionerRecordingBean fromJsonObject(JsonObject obj) {
        Gson gson    = new Gson();
        PositionerRecordingBean thisobj = gson.fromJson(obj, PositionerRecordingBean.class);
        return thisobj;
    }
    public static List<PositionerRecordingBean> fromJsonArray(JsonArray list) {
        if (list == null || list.size() == 0)return new ArrayList<PositionerRecordingBean>();
        List<PositionerRecordingBean> data = new ArrayList<PositionerRecordingBean>();
        for (int i = 0; i < list.size(); i++) {
            JsonObject  object = list.get(i).getAsJsonObject();
            PositionerRecordingBean info   = PositionerRecordingBean.fromJsonObject(object);
            data.add(info);
        }
        return data;
    }
}
