package view.find;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class ProSetDataAndTerminum {
    public String terminalNum;
    public int windowRiseInterval;//升窗间隔时间
    public int windowRiseTime;//升窗时间
    public int hornVolume;//喇叭音量
    public int trunkOpenWith;//尾箱开启方式
    public int electrifyBeforehandTime;//提前通电时间
    public int switchesOffDelayTime;//延迟断电时间
    public int pressKayTime;//按键短按时间
    public int avoidanceDeviceTechnique=-1;//回避器控制方
    public int avoidanceDeviceOperation;//回避器控制
    public int unlockWay;//开锁方法
    public int lockWay;//关锁方法
    public int carLockWindowRise;
    public static JsonObject toJsonObject(ProSetDataAndTerminum info) {
        Gson gson = new Gson();
        String     json = gson.toJson(info);
        JsonObject obj  = gson.fromJson(json, JsonObject.class);
        return obj;
    }
}
