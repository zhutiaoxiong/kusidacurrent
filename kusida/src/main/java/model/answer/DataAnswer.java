package model.answer;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;


import java.util.ArrayList;
import java.util.List;

public class DataAnswer {
    public int   ide;                                    // 问题id
    public String title;//问题标题
    public int    type;//问题分类 1：地区类型，由2个列表框组成，2：普通列表，由一个列表框组成，3： 人数选择类型，弹框显示

    public JsonArray answerInfos;//答案选择列表
    public JsonArray multipleBoxInfos;//多层列表框列表,type1 ios
    public List<DataPlace> androidMultipleBoxInfos;//多层列表框列表,type1 android
    public List<DataSingleTxt> singleBoxInfos;//普通列表,type2
    public List<DataSingleTxt> toastBoxInfos;//人数选择类型,type3


    public static DataAnswer fromJsonObject(JsonObject obj) {
        Gson       gson    = new Gson();
        DataAnswer thisobj = gson.fromJson(obj, DataAnswer.class);
        return thisobj;
    }
    public static List<DataAnswer> fromJsonArray(JsonArray brands) {
        if (brands == null || brands.size() == 0)
            return null;
        List<DataAnswer> data = new ArrayList<DataAnswer>();
        for (int i = 0; i < brands.size(); i++) {
            JsonObject objjj = brands.get(i).getAsJsonObject();
            DataAnswer info = DataAnswer.fromJsonObject(objjj);
            data.add(info);
        }
        return data;
    }

    //单层列表
    public class DataSingleTxt{
        public int ide;
        public String name;
    }
    //省份
    public class DataPlace{
        public String name;
        public List<DataSingleTxt> boxInfos;//,城市
    }

}
