package model.mylocator;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.Date;

/**
 * 定位器设置对象
 * */
public class PositionerSetBean {
    //编号
    private float id;
    //设备号
    private String deviceNum;
    //定位方式 0实时1定时2休眠3定时开机
    private String way;
    //定位时长
    private String duration;
    //录音方式 1手动 2自动 3停止
    private long recordingWay;
    //录音时长 单位ms
    private String recordingDuration;
    //震动报警 0关闭 1
    private String warnShock;
    //震动灵敏度 1 2 3(灵敏度)
    private String sensitiveShock;
    //超速报警 0关闭(单位KM)
    private String warnSpeed;
    //拆机报警 0关闭 1打开
    private String warnDismantle;
    //围栏坐标
    private String radiusLatlng;
    //半径
    private String radius;
    //围栏 0关闭 1开启
    private String fence;
    //创建时间
    private Date createTime;
    //修改时间
    private Date updateTime;

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

    public String getWay() {
        return way;
    }

    public void setWay(String way) {
        this.way = way;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public long getRecordingWay() {
        return recordingWay;
    }

    public void setRecordingWay(long recordingWay) {
        this.recordingWay = recordingWay;
    }

    public String getRecordingDuration() {
        return recordingDuration;
    }

    public void setRecordingDuration(String recordingDuration) {
        this.recordingDuration = recordingDuration;
    }

    public String getWarnShock() {
        return warnShock;
    }

    public void setWarnShock(String warnShock) {
        this.warnShock = warnShock;
    }

    public String getSensitiveShock() {
        return sensitiveShock;
    }

    public void setSensitiveShock(String sensitiveShock) {
        this.sensitiveShock = sensitiveShock;
    }

    public String getWarnSpeed() {
        return warnSpeed;
    }

    public void setWarnSpeed(String warnSpeed) {
        this.warnSpeed = warnSpeed;
    }

    public String getWarnDismantle() {
        return warnDismantle;
    }

    public void setWarnDismantle(String warnDismantle) {
        this.warnDismantle = warnDismantle;
    }

    public String getRadiusLatlng() {
        return radiusLatlng;
    }

    public void setRadiusLatlng(String radiusLatlng) {
        this.radiusLatlng = radiusLatlng;
    }

    public String getRadius() {
        return radius;
    }

    public void setRadius(String radius) {
        this.radius = radius;
    }

    public String getFence() {
        return fence;
    }

    public void setFence(String fence) {
        this.fence = fence;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
    public static PositionerSetBean fromJsonObject(JsonObject obj) {
        Gson gson    = new Gson();
        PositionerSetBean thisobj = gson.fromJson(obj, PositionerSetBean.class);
        return thisobj;
    }
}
