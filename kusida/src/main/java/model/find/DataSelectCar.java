package model.find;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * Created by qq522414074 on 2016/9/7.
 */
public class DataSelectCar {
    public Long[] carIds;
    public int carTypeId;
    public int type;
    public JsonObject toJsonObject(DataSelectCar info) {
        Gson gson = new Gson();
        String     json = gson.toJson(info);
        JsonObject obj  = gson.fromJson(json, JsonObject.class);
        return obj;
    }
}
