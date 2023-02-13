package model.remotecontrol;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import model.store.ShopCarExamplesPid;

public class BeanRemoteControl {
    long id;
    String terminalNum;
    long productId;
    String oneName;
    String towName;
    String slotOne;
    String slotTwo;
    String isAutomatic;
    String isOpen;

    public BeanRemoteControl(long id, String terminalNum, long productId, String oneName, String towName, String slotOne, String slotTwo) {
        this.id = id;
        this.terminalNum = terminalNum;
        this.productId = productId;
        this.oneName = oneName;
        this.towName = towName;
        this.slotOne = slotOne;
        this.slotTwo = slotTwo;
    }

    public static JsonObject toJsonObject(BeanRemoteControl info) {
        Gson gson = new Gson();
        String     json = gson.toJson(info);
        JsonObject obj  = gson.fromJson(json, JsonObject.class);
        return obj;
    }
    public static BeanRemoteControl fromJsonObject(JsonObject obj) {
        Gson gson    = new Gson();
        BeanRemoteControl thisobj = gson.fromJson(obj, BeanRemoteControl.class);
        return thisobj;
    }
}
