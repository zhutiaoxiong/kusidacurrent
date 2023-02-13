package model.answer;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by qq522414074 on 2016/8/15.
 */
public class DataFindway {
    public long   ide; // 标记哪个方式找回
    public String name;//哪种方式找回的名称
    public int    isSelect;//是否选中
    public static DataFindway fromJsonObject(JsonObject obj) {
        Gson gson    = new Gson();
        DataFindway thisobj = gson.fromJson(obj, DataFindway.class);
        return thisobj;
    }
    public static List<DataFindway> fromJsonArray(JsonArray brands) {
        if (brands == null || brands.size() == 0)
            return null;
        List<DataFindway> data = new ArrayList<DataFindway>();
        for (int i = 0; i < brands.size(); i++) {
            JsonObject objjj = brands.get(i).getAsJsonObject();
            DataFindway info = DataFindway.fromJsonObject(objjj);
            data.add(info);
        }
        return data;
    }
}
