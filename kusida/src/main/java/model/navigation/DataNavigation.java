package model.navigation;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import model.maintain.DataMaintain;

/**
 * 导航对象
 */

public class DataNavigation {
/**
 * home String 家地址名称
 company String 公司地址名称
 homeLatitude String 家经地址纬度
 companyLatitude String 公司地址经纬度

 */
    public String home;
    public String company;
    public String homeLatitude;
    public String companyLatitude;
    public static DataNavigation fromJsonObject(JsonObject obj) {
        if(obj == null)return null;
        Gson gson    = new Gson();
        DataNavigation thisobj = gson.fromJson(obj, DataNavigation.class);
        return thisobj;
    }
}
