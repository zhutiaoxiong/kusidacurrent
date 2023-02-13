package model.store;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class ShopCarExamples {
    String terminalNum;
    String shopName;
    String personLiable;
    String agentPhone;
    String address;
    String shopLicense;
    String frontDoorPicture;
    String carPicture;
    String region;

    public ShopCarExamples(String terminalNum, String shopName, String personLiable, String agentPhone, String address, String shopLicense, String frontDoorPicture, String carPicture,String region) {
        this.terminalNum = terminalNum;
        this.shopName = shopName;
        this.personLiable = personLiable;
        this.agentPhone = agentPhone;
        this.address = address;
        this.shopLicense = shopLicense;
        this.frontDoorPicture = frontDoorPicture;
        this.carPicture = carPicture;
        this.region = region;
    }

    public static JsonObject toJsonObject(ShopCarExamples info) {
        Gson gson = new Gson();
        String     json = gson.toJson(info);
        JsonObject obj  = gson.fromJson(json, JsonObject.class);
        return obj;
    }
}
