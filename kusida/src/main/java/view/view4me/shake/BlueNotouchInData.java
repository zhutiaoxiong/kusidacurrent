package view.view4me.shake;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.Objects;

public class BlueNotouchInData {
    public int openDistance;//靠近开距离
    public int lockAgile;//离开锁灵敏度
    public int openQty;//靠近开次数
    public int lockQty;//离开锁次数
    public int openNear;//靠近开开关
    public int lockNear;//离开锁开关

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BlueNotouchInData that = (BlueNotouchInData) o;
        return openDistance == that.openDistance &&
                lockAgile == that.lockAgile &&
                openQty == that.openQty &&
                lockQty == that.lockQty &&
                openNear == that.openNear &&
                lockNear == that.lockNear;
    }

    @Override
    public int hashCode() {
        return Objects.hash(openDistance, lockAgile, openQty, lockQty, openNear, lockNear);
    }
    public static JsonObject toJsonObject(BlueNotouchInData info) {
        Gson gson = new Gson();
        String     json = gson.toJson(info);
        JsonObject obj  = gson.fromJson(json, JsonObject.class);
        return obj;
    }
}
