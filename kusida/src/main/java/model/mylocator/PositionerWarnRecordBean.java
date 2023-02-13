package model.mylocator;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 定位器预警消息对象
 * */
public class PositionerWarnRecordBean {
    //消息编号
    private long id;
    //用户编号
    private long userId;
    //设备号
    private String deviceNum;
    //预警编号
    private long alertId;
    //预警类型
    private String alertType;
    //预警内容
    private String content;
    //是否已读 (0否1 是)
    private String isRead;
    //发生预警时的坐标
    private String gps;
    //创建时间
    private Date createTime;
    //修改时间
    private Date updateTime;

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

    public long getAlertId() {
        return alertId;
    }

    public void setAlertId(long alertId) {
        this.alertId = alertId;
    }

    public String getAlertType() {
        return alertType;
    }

    public void setAlertType(String alertType) {
        this.alertType = alertType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getIsRead() {
        return isRead;
    }

    public void setIsRead(String isRead) {
        this.isRead = isRead;
    }

    public String getGps() {
        return gps;
    }

    public void setGps(String gps) {
        this.gps = gps;
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
    public static PositionerWarnRecordBean fromJsonObject(JsonObject obj) {
        Gson gson    = new Gson();
        PositionerWarnRecordBean thisobj = gson.fromJson(obj, PositionerWarnRecordBean.class);
        return thisobj;
    }
    public static List<PositionerWarnRecordBean> fromJsonArray(JsonArray list) {
        if (list == null || list.size() == 0)return new ArrayList<PositionerWarnRecordBean>();
        List<PositionerWarnRecordBean> data = new ArrayList<PositionerWarnRecordBean>();
        for (int i = 0; i < list.size(); i++) {
            JsonObject  object = list.get(i).getAsJsonObject();
            PositionerWarnRecordBean info   = PositionerWarnRecordBean.fromJsonObject(object);
            data.add(info);
        }
        return data;
    }
}
