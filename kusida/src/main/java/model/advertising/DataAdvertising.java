package model.advertising;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * Created by qq522414074 on 2017/10/21.
 */

public class DataAdvertising {
    public String jumpUrl;    //跳转地址;
    public String url;//图片地址
    public int    type;//0：不跳转，1：webView跳转，2：其他跳转
    public static DataAdvertising fromJsonObject(JsonObject obj) {
        Gson gson    = new Gson();
        DataAdvertising thisobj = gson.fromJson(obj, DataAdvertising.class);
        return thisobj;
    }

}
