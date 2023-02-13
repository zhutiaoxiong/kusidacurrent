package model.mylocator;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.Date;

/**
 * 定位器定位信息对象
 * */
public class PositionerInfoBean {
    //编号
    private float id;
    //定位模式 定位方式 0 实时定位 1定时定位 2深度休眠 3定时开机
    private String way;
    //设备号
    private String deviceNum;
    //gps状态 0离线 1在线
    private String gpsOn;
    //坐标
    private String latlng;
    //高度
    private String height;
    //速度 Km/h
    private String speed;
    //方向 0-360
    private String direction;
    //是否在线 0否 1是
    private String isOnline;
    //电池电压 0-100
    private String voltage;
    //运动状态 0静止 1运动
    private String motional;
    //定位时间 最后定位时间
    private Date locationTime;
    //修改时间 最后通讯时间
    private Date updateTime;

    public float getId() {
        return id;
    }

    public void setId(float id) {
        this.id = id;
    }

    public String getWay() {
        return way;
    }

    public void setWay(String way) {
        this.way = way;
    }

    public String getDeviceNum() {
        return deviceNum;
    }

    public void setDeviceNum(String deviceNum) {
        this.deviceNum = deviceNum;
    }

    public String getGpsOn() {
        return gpsOn;
    }

    public void setGpsOn(String gpsOn) {
        this.gpsOn = gpsOn;
    }

    public String getLatlng() {
        return latlng;
    }

    public void setLatlng(String latlng) {
        this.latlng = latlng;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(String isOnline) {
        this.isOnline = isOnline;
    }

    public String getVoltage() {
        return voltage;
    }

    public void setVoltage(String voltage) {
        this.voltage = voltage;
    }

    public String getMotional() {
        return motional;
    }

    public void setMotional(String motional) {
        this.motional = motional;
    }

    public Date getLocationTime() {
        return locationTime;
    }

    public void setLocationTime(Date locationTime) {
        this.locationTime = locationTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public static PositionerInfoBean fromJsonObject(JsonObject obj) {
        Gson gson    = new Gson();
        PositionerInfoBean thisobj = gson.fromJson(obj, PositionerInfoBean.class);
        return thisobj;
    }
}
