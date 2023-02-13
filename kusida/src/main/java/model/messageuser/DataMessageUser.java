package model.messageuser;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * Created by Administrator on 2016/8/1.
 */
public class DataMessageUser {
    public long   ide;
    public String content="";//消息内容
    public long   createTime;//消息时间

    public static DataMessageUser fromJsonObject(JsonObject obj) {
        if(obj == null)return null;
        Gson            gson    = new Gson();
        DataMessageUser thisobj = gson.fromJson(obj, DataMessageUser.class);
        return thisobj;
    }
}
