package view.view4me.shake;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class BlueNotouchInDataAndTerminum {
    public String terminalNum;
    public int openDistance;//靠近开距离
    public int lockAgile;//离开锁灵敏度
    public int openQty;//靠近开次数
    public int lockQty;//离开锁次数
    public int openNear;//靠近开开关
    public int lockNear;//离开锁开关


    public static JsonObject toJsonObject(BlueNotouchInDataAndTerminum info) {
        Gson gson = new Gson();
        String     json = gson.toJson(info);
        JsonObject obj  = gson.fromJson(json, JsonObject.class);
        return obj;
    }
}
