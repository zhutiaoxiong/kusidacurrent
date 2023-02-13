package model.mylocator;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 定位器轨迹对象
 * */
public class PositionerTrailBean {
    //消息编号
    private long id;
    //用户编号
    private long userId;
    //设备号
    private String deviceNum;
    //开始时间
    private long startTime;
    //开始位置
    private String startLocation;
    //结束时间
    private String endTime;
    //结束位置
    private String endLocation;
    //轨迹经纬度列表 经纬度用，分割
    private String[] latlng;
    //行驶距离(km)
    private double miles;
    //备注
    private String comment;
    //间隔时长
    private String intervalTime;
    //是否收藏
    private String isCollect;
    //创建时间
    private Date createTime;
    //开始位置 经纬度用，分割
    private String startLocationCoordinate;
    //结束位置 经纬度用，分割
    private String endLocationCoordinate;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getDeviceNum() {
        return deviceNum;
    }

    public void setDeviceNum(String deviceNum) {
        this.deviceNum = deviceNum;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public String getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(String startLocation) {
        this.startLocation = startLocation;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(String endLocation) {
        this.endLocation = endLocation;
    }

    public String[] getLatlng() {
        return latlng;
    }

    public void setLatlng(String[] latlng) {
        this.latlng = latlng;
    }

    public double getMiles() {
        return miles;
    }

    public void setMiles(double miles) {
        this.miles = miles;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getIntervalTime() {
        return intervalTime;
    }

    public void setIntervalTime(String intervalTime) {
        this.intervalTime = intervalTime;
    }

    public String getIsCollect() {
        return isCollect;
    }

    public void setIsCollect(String isCollect) {
        this.isCollect = isCollect;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getStartLocationCoordinate() {
        return startLocationCoordinate;
    }

    public void setStartLocationCoordinate(String startLocationCoordinate) {
        this.startLocationCoordinate = startLocationCoordinate;
    }

    public String getEndLocationCoordinate() {
        return endLocationCoordinate;
    }

    public void setEndLocationCoordinate(String endLocationCoordinate) {
        this.endLocationCoordinate = endLocationCoordinate;
    }
    public static PositionerTrailBean fromJsonObject(JsonObject obj) {
        Gson gson    = new Gson();
        PositionerTrailBean thisobj = gson.fromJson(obj, PositionerTrailBean.class);
        return thisobj;
    }
    public static List<PositionerTrailBean> fromJsonArray(JsonArray list) {
        if (list == null || list.size() == 0)return new ArrayList<PositionerTrailBean>();
        List<PositionerTrailBean> data = new ArrayList<PositionerTrailBean>();
        for (int i = 0; i < list.size(); i++) {
            JsonObject  object = list.get(i).getAsJsonObject();
            PositionerTrailBean info   = PositionerTrailBean.fromJsonObject(object);
            data.add(info);
        }
        return data;
    }
}
