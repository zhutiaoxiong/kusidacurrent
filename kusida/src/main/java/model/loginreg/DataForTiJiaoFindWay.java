package model.loginreg;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * Created by qq522414074 on 2016/8/17.
 */
public class DataForTiJiaoFindWay  {
    public Integer[] secretTypes;
    public JsonObject toJsonObject(DataForTiJiaoFindWay info) {
        Gson gson = new Gson();
        String     json = gson.toJson(info);
        JsonObject obj  = gson.fromJson(json, JsonObject.class);
        return obj;
    }
}
