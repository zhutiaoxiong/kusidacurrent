package model.find;

import com.google.gson.Gson;
import com.google.gson.JsonObject;


/**
 * Created by qq522414074 on 2016/9/13.
 */
public class CardInfo{
    /**
     * 卡片对象
     */

    /**
     * ide int 卡片id
     * name String 卡片名称
     * typeStr String 卡片类型详细描述 例如“中级卡片”
     * pic String 卡片图片
     * count int 卡片个数
     */
    public int ide;
    public String name;
    public String typeStr;//卡片类型详细描述 例如“中级卡片
    public String pic;
    public int type;//卡片类型1—6
    public CardInfo copy() {
        JsonObject object = toJsonObject(this);
        CardInfo info   = CardInfo.fromJsonObject(object);
        return info;
    }
    public static CardInfo fromJsonObject(JsonObject obj) {
        Gson gson    = new Gson();
        CardInfo thisobj = gson.fromJson(obj, CardInfo.class);
        return thisobj;
    }

    public static JsonObject toJsonObject(CardInfo info) {
        Gson       gson = new Gson();
        String     json = gson.toJson(info);
        JsonObject obj  = gson.fromJson(json, JsonObject.class);
        return obj;
    }
}
