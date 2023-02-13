package model.find;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kulala.staticsfunc.static_system.OJsonGet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qq522414074 on 2016/9/23.
 */
public class CardDetails {
    /**
     * 卡片详情对象
     */

    public  int count; //卡片个数
    public  CardInfo card;//卡片对象
//    public int ide;
//    public String name;
//    public String typeStr;
//    public String pic;
    public static List<CardDetails> fromJsonArray(JsonArray cardList) {
        if (cardList == null || cardList.size() == 0)
            return null;
        List<CardDetails> data = new ArrayList<CardDetails>();
        for (int i = 0; i < cardList.size(); i++) {
            JsonObject object = cardList.get(i).getAsJsonObject();
            CardDetails details=CardDetails.fromJsonObject(object);
            details.count=OJsonGet.getInteger(object,"count");
            JsonObject cardinfo= OJsonGet.getJsonObject(object,"card");
            details.card=CardInfo.fromJsonObject(cardinfo);
            data.add(details);
        }
        return data;
    }
    public static CardDetails fromJsonObject(JsonObject obj) {
        Gson gson    = new Gson();
        CardDetails thisobj = gson.fromJson(obj, CardDetails.class);
        return thisobj;
    }
}
