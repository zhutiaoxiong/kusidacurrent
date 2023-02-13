package model.common;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import com.kulala.staticsfunc.static_system.OArrayConvent;
import com.kulala.staticsfunc.static_system.OJsonGet;

import java.util.ArrayList;
import java.util.List;

public class DataBrands {

    public int ide;	//品牌id
    public String name = "";    // 品牌名
    public String logo = "";    // 图片地址
    public JsonArray series;


    public static DataBrands fromJsonObject(JsonObject obj) {
        Gson gson = new Gson();
        DataBrands thisobj = gson.fromJson(obj, DataBrands.class);
        return thisobj;
    }

    public static List<DataBrands> fromJsonArray(JsonArray brands) {
        if (brands == null || brands.size() == 0)
            return null;
        List<DataBrands> data = new ArrayList<DataBrands>();
        for (int i = 0; i < brands.size(); i++) {
            JsonObject brandsWithLetter = brands.get(i).getAsJsonObject();
            String letter = OJsonGet.getString(brandsWithLetter, "letter");
            JsonArray brandsArr = OJsonGet.getJsonArray(brandsWithLetter, "carBrands");
            for (int j = 0; j < brandsArr.size(); j++) {
                JsonObject object = brandsArr.get(j).getAsJsonObject();
                DataBrands info = DataBrands.fromJsonObject(object);
                data.add(info);
            }
        }
        return data;
    }

    // ========================get=======================
    // 品牌列表
    public static String[] getBrandsArr(List<DataBrands> brands) {
        String[] names = OArrayConvent.ListGetStringArr(brands, "name");
        return names;
    }

    // 系列列表
    public String[] getSeriesArr() {
        if (series == null)
            return null;
        String[] names = OArrayConvent.convent(series, "name");
        return names;
    }

    // 型号列表
    public String[] getModelsArr(String seriesName) {
        if (series == null)
            return null;
        for (int i = 0; i < series.size(); i++) {
            JsonObject object = series.get(i).getAsJsonObject();
            String name = OJsonGet.getString(object, "name");
            if (name.equals(seriesName)) {
                JsonArray models = OJsonGet.getJsonArray(object, "models");
                String[] names = OArrayConvent.convent(models);
                return names;
            }
        }
        return null;
    }
    // ========================set=======================
}