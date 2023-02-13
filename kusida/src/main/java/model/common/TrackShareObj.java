package model.common;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * Created by qq522414074 on 2017/3/30.
 */

public class TrackShareObj {
    public String shareTitle ;// 分享标题
    public String shareComment;  //分享内容
    public String sharePic ;// 分享图片地址
    public String shareUrl  ;//分享地址
    public static TrackShareObj fromJsonObject(JsonObject obj) {
        Gson gson = new Gson();
        TrackShareObj thisobj = gson.fromJson(obj, TrackShareObj.class);
        return thisobj;
    }

}
