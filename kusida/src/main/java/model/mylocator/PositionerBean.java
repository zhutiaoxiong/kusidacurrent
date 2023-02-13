package model.mylocator;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;


/**
 * 定位器对象
 * */
public class PositionerBean {
    //编号
    private long id;
    //设备号
    private String deviceNum;
    //用户编号
    private String userId;
    //用户是否绑定
    private String isUserBind;
    //目标编号
    private long targetId;
    //目标是否绑定
    private String isTargetBind;
    //设备型号
    private String model;
    //设备版本
    private String deviceVersion;
    //主机版本
    private String hostVersion;
    //imei号
    private String iMei;
    //卡一iccId
    private String iccIdOne;
    //卡二iccId
    private String iccIdTow;
    //备注名称
    private String comments;
    //录音剩余数
    private int recording;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDeviceNum() {
        return deviceNum;
    }

    public void setDeviceNum(String deviceNum) {
        this.deviceNum = deviceNum;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getIsUserBind() {
        return isUserBind;
    }

    public void setIsUserBind(String isUserBind) {
        this.isUserBind = isUserBind;
    }

    public long getTargetId() {
        return targetId;
    }

    public void setTargetId(long targetId) {
        this.targetId = targetId;
    }

    public String getIsTargetBind() {
        return isTargetBind;
    }

    public void setIsTargetBind(String isTargetBind) {
        this.isTargetBind = isTargetBind;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getDeviceVersion() {
        return deviceVersion;
    }

    public void setDeviceVersion(String deviceVersion) {
        this.deviceVersion = deviceVersion;
    }

    public String getHostVersion() {
        return hostVersion;
    }

    public void setHostVersion(String hostVersion) {
        this.hostVersion = hostVersion;
    }

    public String getiMei() {
        return iMei;
    }

    public void setiMei(String iMei) {
        this.iMei = iMei;
    }

    public String getIccIdOne() {
        return iccIdOne;
    }

    public void setIccIdOne(String iccIdOne) {
        this.iccIdOne = iccIdOne;
    }

    public String getIccIdTow() {
        return iccIdTow;
    }

    public void setIccIdTow(String iccIdTow) {
        this.iccIdTow = iccIdTow;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public int getRecording() {
        return recording;
    }

    public void setRecording(int recording) {
        this.recording = recording;
    }
    
    public static PositionerBean fromJsonObject(JsonObject obj) {
        Gson gson    = new Gson();
        PositionerBean thisobj = gson.fromJson(obj, PositionerBean.class);
        return thisobj;
    }
    public static List<PositionerBean> fromJsonArray(JsonArray list) {
        if (list == null || list.size() == 0)return new ArrayList<PositionerBean>();
        List<PositionerBean> data = new ArrayList<PositionerBean>();
        for (int i = 0; i < list.size(); i++) {
            JsonObject  object = list.get(i).getAsJsonObject();
            PositionerBean info   = PositionerBean.fromJsonObject(object);
            data.add(info);
        }
        return data;
    }
}
