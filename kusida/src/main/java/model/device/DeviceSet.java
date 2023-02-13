package model.device;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class DeviceSet {
    public long id;//车辆设置编号
    public String terminalNum;//设备号
    public int openDistance;//靠近开距离
    public int lockAgile;//离开锁灵敏度
    public int openQty;//靠近开次数
    public int lockQty;//离开锁次数
    public int openNear;//靠近开开关
    public int lockNear;//离开锁开关
    public int windowRiseInterval;//升窗间隔时间
    public int windowRiseTime;//升窗时间
    public int hornVolume;//喇叭音量
    public int trunkOpenWith;//尾箱开启方式
    public int electrifyBeforehandTime;//提前通电时间
    public int switchesOffDelayTime;//延迟断电时间
    public int pressKayTime;//按键短按时间
    public int avoidanceDeviceTechnique;//回避器控制方
    public int avoidanceDeviceOperation;//回避器控制
    public int unlockWay;//开锁方法
    public int lockWay;//关锁方法

    public static JsonObject toJsonObject(DeviceSet info) {
        Gson gson = new Gson();
        String     json = gson.toJson(info);
        JsonObject obj  = gson.fromJson(json, JsonObject.class);
        return obj;
    }

}
