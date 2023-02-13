package model.store;

import com.google.gson.Gson;
import com.google.gson.JsonObject;


public class ShopCarExamplesPid {
    public String getAgentPhone() {
        return agentPhone;
    }

    String terminalNum;
    String shopName;
    String personLiable;
    String agentPhone;
    String address;
    String shopLicense;
    String frontDoorPicture;
    String carPicture;
    String region;
    int id;
    public static ShopCarExamplesPid fromJsonObject(JsonObject obj) {
        Gson gson    = new Gson();
        ShopCarExamplesPid thisobj = gson.fromJson(obj, ShopCarExamplesPid.class);
        return thisobj;
    }

    public long getpId() {
        return id ;
    }
}
