package model.loginreg;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * Created by qq522414074 on 2016/8/16.
 */
public class DataResetPassword {
    public Integer[] problemId;
    public String[] answerId;
    public int entrance;
    public String phoneNum;
    public int type;
    public String verifyStr;
    public  JsonObject toJsonObject(DataResetPassword info) {
        Gson       gson = new Gson();
        String     json = gson.toJson(info);
        JsonObject obj  = gson.fromJson(json, JsonObject.class);
        return obj;
    }

}
